package ca.pfv.spmf.algorithms.frequentpatterns.hauopm;

import ca.pfv.spmf.tools.MemoryLogger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of HAUOPM for mining high average utility occupancy patterns.
 * <br/><br/>
 * The implementation follows the AUOL, average transaction utility occupancy,
 * and AUOUB pruning ideas from:
 * Kumar, M. J. K., Rana, D. (2024). HAUOPM: High Average Utility Occupancy
 * Pattern Mining. Arabian Journal for Science and Engineering.
 * <br/><br/>
 * This version uses the standard SPMF utility transaction format:
 * items:transactionUtility:itemUtilities.
 *
 * @author OpenAI
 */
public class AlgoHAUOPM {

    private static final int DEFAULT_BUFFER_SIZE = 200;

    /** Start time of the last run. */
    public long startTimestamp = 0;

    /** End time of the last run. */
    public long endTimestamp = 0;

    /** Number of high average utility occupancy patterns found. */
    public int hauopCount = 0;

    /** Number of AUO-list joins performed. */
    public long joinCount = 0;

    /** Number of subtrees pruned by AUOUB. */
    public long upperBoundPrunedCount = 0;

    /** Number of itemsets pruned by support before recursive exploration. */
    public long supportPrunedCount = 0;

    private BufferedWriter writer;
    private Map<Integer, Integer> mapItemToSupport;
    private int[] itemsetBuffer;
    private int transactionCount;
    private int minSupportCount;
    private double minOccupancy;
    private double databaseUtility;
    private HAUOPMPruningStrategy pruningStrategy;

    /**
     * Run HAUOPM using all pruning strategies.
     *
     * @param input               input file in SPMF utility transaction format
     * @param output              output file
     * @param minSupportThreshold minimum support as a ratio in (0,1) or an absolute count if >= 1
     * @param minOccupancy        minimum average transaction utility occupancy
     * @throws IOException if an error occurs while reading or writing files
     */
    public void runAlgorithm(String input, String output, double minSupportThreshold,
                             double minOccupancy) throws IOException {
        runAlgorithm(input, output, minSupportThreshold, minOccupancy, HAUOPMPruningStrategy.ALL);
    }

    /**
     * Run HAUOPM.
     *
     * @param input               input file in SPMF utility transaction format
     * @param output              output file
     * @param minSupportThreshold minimum support as a ratio in (0,1) or an absolute count if >= 1
     * @param minOccupancy        minimum average transaction utility occupancy
     * @param pruningStrategy     pruning strategy configuration
     * @throws IOException if an error occurs while reading or writing files
     */
    public void runAlgorithm(String input, String output, double minSupportThreshold,
                             double minOccupancy, HAUOPMPruningStrategy pruningStrategy) throws IOException {
        MemoryLogger.getInstance().reset();
        reset();

        this.minOccupancy = minOccupancy;
        this.pruningStrategy = pruningStrategy == null ? HAUOPMPruningStrategy.ALL : pruningStrategy;
        this.itemsetBuffer = new int[DEFAULT_BUFFER_SIZE];
        this.writer = new BufferedWriter(new FileWriter(output));
        this.startTimestamp = System.currentTimeMillis();

        try {
            scanDatabaseToCalculateSupport(input);
            this.minSupportCount = convertMinSupportToAbsolute(minSupportThreshold, transactionCount);

            List<AUOList> initialLists = createInitialAUOLists();
            buildInitialAUOLists(input, initialLists);

            MemoryLogger.getInstance().checkMemory();
            search(0, null, initialLists);
            MemoryLogger.getInstance().checkMemory();
        } finally {
            if (writer != null) {
                writer.close();
            }
            endTimestamp = System.currentTimeMillis();
        }
    }

    /**
     * Reset statistics and per-run structures.
     */
    private void reset() {
        hauopCount = 0;
        joinCount = 0;
        upperBoundPrunedCount = 0;
        supportPrunedCount = 0;
        transactionCount = 0;
        minSupportCount = 0;
        databaseUtility = 0d;
        mapItemToSupport = new HashMap<Integer, Integer>();
    }

    /**
     * Scan the database once to calculate support counts, transaction count, and database utility.
     *
     * @param input input file path
     * @throws IOException if an error occurs while reading the file
     */
    private void scanDatabaseToCalculateSupport(String input) throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(input))));
            String line;
            while ((line = reader.readLine()) != null) {
                if (shouldSkipLine(line)) {
                    continue;
                }
                String[] split = line.split(":");
                if (split.length < 3) {
                    throw new IOException("Invalid utility transaction: " + line);
                }
                String[] items = split[0].trim().split("\\s+");
                Set<Integer> itemsSeen = new HashSet<Integer>();
                for (String itemString : items) {
                    int item = Integer.parseInt(itemString);
                    if (itemsSeen.add(item)) {
                        Integer support = mapItemToSupport.get(item);
                        mapItemToSupport.put(item, support == null ? 1 : support + 1);
                    }
                }
                double transactionUtility = Double.parseDouble(split[1]);
                if (transactionUtility <= 0d) {
                    throw new IOException("HAUOPM requires positive transaction utility: " + line);
                }
                databaseUtility += transactionUtility;
                transactionCount++;
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    /**
     * Create AUO-lists for all frequent 1-itemsets and sort them by support.
     *
     * @return the sorted AUO-lists of frequent items
     */
    private List<AUOList> createInitialAUOLists() {
        List<AUOList> lists = new ArrayList<AUOList>();
        for (Integer item : mapItemToSupport.keySet()) {
            if (mapItemToSupport.get(item) >= minSupportCount) {
                lists.add(new AUOList(item));
            }
        }
        Collections.sort(lists, new Comparator<AUOList>() {
            public int compare(AUOList list1, AUOList list2) {
                return compareItems(list1.item, list2.item);
            }
        });
        return lists;
    }

    /**
     * Scan the database a second time to build AUO-lists of frequent 1-itemsets.
     *
     * @param input        input file path
     * @param initialLists lists to fill
     * @throws IOException if an error occurs while reading the file
     */
    private void buildInitialAUOLists(String input, List<AUOList> initialLists) throws IOException {
        Map<Integer, AUOList> mapItemToAUOList = new HashMap<Integer, AUOList>();
        for (AUOList auoList : initialLists) {
            mapItemToAUOList.put(auoList.item, auoList);
        }

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(input))));
            String line;
            int tid = 0;
            while ((line = reader.readLine()) != null) {
                if (shouldSkipLine(line)) {
                    continue;
                }

                String[] split = line.split(":");
                double transactionUtility = Double.parseDouble(split[1]);
                String[] items = split[0].trim().split("\\s+");
                String[] utilities = split[2].trim().split("\\s+");
                if (items.length != utilities.length) {
                    throw new IOException("Items and utilities have different lengths: " + line);
                }

                List<Pair> revisedTransaction = new ArrayList<Pair>();
                for (int i = 0; i < items.length; i++) {
                    int item = Integer.parseInt(items[i]);
                    if (mapItemToAUOList.containsKey(item)) {
                        revisedTransaction.add(new Pair(item, Double.parseDouble(utilities[i])));
                    }
                }

                Collections.sort(revisedTransaction, new Comparator<Pair>() {
                    public int compare(Pair pair1, Pair pair2) {
                        return compareItems(pair1.item, pair2.item);
                    }
                });

                double maxUtilityOccupancy = 0d;
                for (int i = revisedTransaction.size() - 1; i >= 0; i--) {
                    Pair pair = revisedTransaction.get(i);
                    double itemUtilityOccupancy = pair.utility / transactionUtility;
                    maxUtilityOccupancy = Math.max(maxUtilityOccupancy, itemUtilityOccupancy);
                    mapItemToAUOList.get(pair.item).addElement(
                            new AUOElement(tid, itemUtilityOccupancy, maxUtilityOccupancy, transactionUtility));
                }
                tid++;
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    /**
     * Recursive depth-first mining over the AUO-tree.
     *
     * @param prefixLength length of the current prefix
     * @param prefixList   AUO-list of the current prefix, or null for the root
     * @param extensions   extension AUO-lists of the current prefix
     * @throws IOException if writing output fails
     */
    private void search(int prefixLength, AUOList prefixList, List<AUOList> extensions) throws IOException {
        for (int i = 0; i < extensions.size(); i++) {
            AUOList extension = extensions.get(i);
            int support = extension.getSupport();
            int itemsetLength = prefixLength + 1;
            double averageTransactionUtilityOccupancy =
                    extension.getAverageTransactionUtilityOccupancy(itemsetLength, databaseUtility);

            if (support >= minSupportCount && averageTransactionUtilityOccupancy >= minOccupancy) {
                writeOut(prefixLength, extension.item, support, itemsetLength, extension,
                        averageTransactionUtilityOccupancy);
            }

            if (pruningStrategy.isSupportPruningEnabled() && support < minSupportCount) {
                supportPrunedCount++;
                continue;
            }

            if (pruningStrategy.isUpperBoundPruningEnabled()) {
                double upperBound = calculateUpperBound(extension);
                if (upperBound < minOccupancy) {
                    upperBoundPrunedCount++;
                    continue;
                }
            }

            List<AUOList> childExtensions = new ArrayList<AUOList>();
            for (int j = i + 1; j < extensions.size(); j++) {
                AUOList child = construct(prefixList, extension, extensions.get(j));
                joinCount++;
                if (child == null || child.getSupport() == 0) {
                    continue;
                }
                if (pruningStrategy.isSupportPruningEnabled() && child.getSupport() < minSupportCount) {
                    supportPrunedCount++;
                    continue;
                }
                childExtensions.add(child);
            }

            ensureBufferSize(prefixLength);
            itemsetBuffer[prefixLength] = extension.item;
            search(prefixLength + 1, extension, childExtensions);
        }
    }

    /**
     * Construct the AUO-list for the joined itemset represented by prefix + xa.item + xb.item.
     *
     * @param prefixList AUO-list of the prefix, or null for a 2-itemset
     * @param xa         first extension list
     * @param xb         second extension list
     * @return the joined AUO-list
     */
    private AUOList construct(AUOList prefixList, AUOList xa, AUOList xb) {
        AUOList joined = new AUOList(xb.item);
        int indexA = 0;
        int indexB = 0;

        while (indexA < xa.elements.size() && indexB < xb.elements.size()) {
            AUOElement elementA = xa.elements.get(indexA);
            AUOElement elementB = xb.elements.get(indexB);

            if (elementA.tid == elementB.tid) {
                double utilityOccupancy;
                double maxUtilityOccupancy;
                if (prefixList == null) {
                    utilityOccupancy = elementA.itemsetUtilityOccupancy + elementB.itemsetUtilityOccupancy;
                    maxUtilityOccupancy = Math.max(elementA.itemsetMaxUtilityOccupancy,
                            elementB.itemsetMaxUtilityOccupancy);
                } else {
                    AUOElement prefixElement = findElementWithTID(prefixList, elementA.tid);
                    if (prefixElement == null) {
                        indexA++;
                        indexB++;
                        continue;
                    }
                    utilityOccupancy = elementA.itemsetUtilityOccupancy + elementB.itemsetUtilityOccupancy
                            - prefixElement.itemsetUtilityOccupancy;
                    maxUtilityOccupancy = Math.max(prefixElement.itemsetMaxUtilityOccupancy,
                            Math.max(elementA.itemsetMaxUtilityOccupancy, elementB.itemsetMaxUtilityOccupancy));
                }
                joined.addElement(new AUOElement(elementA.tid, utilityOccupancy, maxUtilityOccupancy,
                        elementA.transactionUtility));
                indexA++;
                indexB++;
            } else if (elementA.tid < elementB.tid) {
                indexA++;
            } else {
                indexB++;
            }
        }
        return joined;
    }

    /**
     * Calculate AUOUB from the top min-support itemset maximum utility occupancy values.
     *
     * @param auoList AUO-list of a tree node
     * @return upper bound on average utility occupancy for the subtree
     */
    private double calculateUpperBound(AUOList auoList) {
        if (auoList.getSupport() < minSupportCount) {
            return 0d;
        }
        List<Double> values = new ArrayList<Double>(auoList.elements.size());
        for (AUOElement element : auoList.elements) {
            values.add(element.itemsetMaxUtilityOccupancy);
        }
        Collections.sort(values, Collections.reverseOrder());

        double sumTopK = 0d;
        for (int i = 0; i < minSupportCount; i++) {
            sumTopK += values.get(i);
        }
        return sumTopK / minSupportCount;
    }

    /**
     * Find the element of an AUO-list having a given transaction id.
     *
     * @param auoList the list to search
     * @param tid     transaction id
     * @return the matching element, or null
     */
    private AUOElement findElementWithTID(AUOList auoList, int tid) {
        int first = 0;
        int last = auoList.elements.size() - 1;
        while (first <= last) {
            int middle = (first + last) >>> 1;
            AUOElement element = auoList.elements.get(middle);
            if (element.tid < tid) {
                first = middle + 1;
            } else if (element.tid > tid) {
                last = middle - 1;
            } else {
                return element;
            }
        }
        return null;
    }

    /**
     * Write a high average utility occupancy pattern.
     *
     * @param prefixLength                         prefix length
     * @param item                                 last item to append to the prefix
     * @param support                              pattern support
     * @param itemsetLength                        pattern length
     * @param list                                 AUO-list of the pattern
     * @param averageTransactionUtilityOccupancy   average transaction utility occupancy
     * @throws IOException if writing output fails
     */
    private void writeOut(int prefixLength, int item, int support, int itemsetLength, AUOList list,
                          double averageTransactionUtilityOccupancy) throws IOException {
        hauopCount++;
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < prefixLength; i++) {
            buffer.append(itemsetBuffer[i]);
            buffer.append(' ');
        }
        buffer.append(item);
        buffer.append(" #SUP: ");
        buffer.append(support);
        buffer.append(" #ATUO: ");
        buffer.append(averageTransactionUtilityOccupancy);
        buffer.append(" #AUO: ");
        buffer.append(list.getAverageUtilityOccupancy(itemsetLength));
        buffer.append(" #TUO: ");
        buffer.append(list.getTransactionUtilityOccupancy(databaseUtility));
        writer.write(buffer.toString());
        writer.newLine();
    }

    /**
     * Compare two items according to support ascending order, then lexical order.
     *
     * @param item1 first item
     * @param item2 second item
     * @return comparison result
     */
    private int compareItems(int item1, int item2) {
        int compare = mapItemToSupport.get(item1) - mapItemToSupport.get(item2);
        return compare == 0 ? item1 - item2 : compare;
    }

    /**
     * Convert a support threshold to an absolute support count.
     *
     * @param threshold        threshold as a ratio in (0,1) or a count if >= 1
     * @param transactionCount number of transactions
     * @return absolute support count
     */
    private int convertMinSupportToAbsolute(double threshold, int transactionCount) {
        if (threshold <= 0d) {
            throw new IllegalArgumentException("Minimum support must be greater than 0.");
        }
        int absolute = threshold < 1d ? (int) Math.ceil(threshold * transactionCount) : (int) Math.ceil(threshold);
        return Math.max(1, absolute);
    }

    /**
     * Check if an input line should be skipped.
     *
     * @param line input line
     * @return true if this is a comment, metadata, or empty line
     */
    private boolean shouldSkipLine(String line) {
        return line.isEmpty() || line.charAt(0) == '#' || line.charAt(0) == '%'
                || line.charAt(0) == '@';
    }

    /**
     * Ensure the itemset buffer can store a prefix of a given length.
     *
     * @param prefixLength required prefix length
     */
    private void ensureBufferSize(int prefixLength) {
        if (prefixLength < itemsetBuffer.length) {
            return;
        }
        int[] newBuffer = new int[itemsetBuffer.length + itemsetBuffer.length / 2 + 1];
        System.arraycopy(itemsetBuffer, 0, newBuffer, 0, itemsetBuffer.length);
        itemsetBuffer = newBuffer;
    }

    /**
     * Print statistics about the latest execution.
     */
    public void printStats() {
        System.out.println("=============  HAUOPM ALGORITHM - STATS =============");
        System.out.println(" Total time ~ " + (endTimestamp - startTimestamp) + " ms");
        System.out.println(" Memory ~ " + MemoryLogger.getInstance().getMaxMemory() + " MB");
        System.out.println(" High average utility occupancy patterns count : " + hauopCount);
        System.out.println(" Join count : " + joinCount);
        System.out.println(" Support-pruned count : " + supportPrunedCount);
        System.out.println(" Upper-bound-pruned count : " + upperBoundPrunedCount);
        System.out.println(" Pruning strategy : " + pruningStrategy.getLabel());
        System.out.println("===================================================");
    }

    /**
     * Item and utility pair used while revising a transaction.
     */
    private static class Pair {
        final int item;
        final double utility;

        Pair(int item, double utility) {
            this.item = item;
            this.utility = utility;
        }
    }
}
