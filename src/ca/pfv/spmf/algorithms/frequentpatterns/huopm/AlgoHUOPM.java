package ca.pfv.spmf.algorithms.frequentpatterns.huopm;

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
 * Implementation of HUOPM for mining high-utility occupancy patterns.
 * <br/><br/>
 * The implementation follows the UO-list, FU-table, remaining utility
 * occupancy, and upper-bound pruning ideas from:
 * Gan, W., Lin, J. C.-W., Fournier-Viger, P., Chao, H.-C., Yu, P. S. (2019).
 * HUOPM: High-Utility Occupancy Pattern Mining. IEEE Transactions on
 * Cybernetics.
 * <br/><br/>
 * This version uses the standard SPMF utility transaction format:
 * items:transactionUtility:itemUtilities.
 *
 * @author OpenAI
 */
public class AlgoHUOPM {

    private static final int DEFAULT_BUFFER_SIZE = 200;

    /** Start time of the last run. */
    public long startTimestamp = 0;

    /** End time of the last run. */
    public long endTimestamp = 0;

    /** Number of high-utility occupancy patterns found. */
    public int huopCount = 0;

    /** Number of UO-list joins performed. */
    public long joinCount = 0;

    /** Number of subtrees pruned by the utility-occupancy upper bound. */
    public long upperBoundPrunedCount = 0;

    /** Number of UO-list constructions stopped by remaining-support pruning. */
    public long remainingSupportPrunedCount = 0;

    /** Number of itemsets pruned by support before recursive exploration. */
    public long supportPrunedCount = 0;

    private BufferedWriter writer;
    private Map<Integer, Integer> mapItemToSupport;
    private int[] itemsetBuffer;
    private int transactionCount;
    private int minSupportCount;
    private double minUtilityOccupancy;
    private HUOPMPruningStrategy pruningStrategy;

    /**
     * Run HUOPM using all pruning strategies.
     *
     * @param input                input file in SPMF utility transaction format
     * @param output               output file
     * @param minSupportThreshold  minimum support as a ratio in (0,1) or an absolute count if >= 1
     * @param minUtilityOccupancy  minimum utility occupancy in [0,1]
     * @throws IOException if an error occurs while reading or writing files
     */
    public void runAlgorithm(String input, String output, double minSupportThreshold,
                             double minUtilityOccupancy) throws IOException {
        runAlgorithm(input, output, minSupportThreshold, minUtilityOccupancy, HUOPMPruningStrategy.ALL);
    }

    /**
     * Run HUOPM.
     *
     * @param input                input file in SPMF utility transaction format
     * @param output               output file
     * @param minSupportThreshold  minimum support as a ratio in (0,1) or an absolute count if >= 1
     * @param minUtilityOccupancy  minimum utility occupancy in [0,1]
     * @param pruningStrategy      pruning strategy configuration
     * @throws IOException if an error occurs while reading or writing files
     */
    public void runAlgorithm(String input, String output, double minSupportThreshold,
                             double minUtilityOccupancy, HUOPMPruningStrategy pruningStrategy) throws IOException {
        MemoryLogger.getInstance().reset();
        reset();

        this.minUtilityOccupancy = minUtilityOccupancy;
        this.pruningStrategy = pruningStrategy == null ? HUOPMPruningStrategy.ALL : pruningStrategy;
        this.itemsetBuffer = new int[DEFAULT_BUFFER_SIZE];
        this.writer = new BufferedWriter(new FileWriter(output));
        this.startTimestamp = System.currentTimeMillis();

        try {
            scanDatabaseToCalculateSupport(input);
            this.minSupportCount = convertMinSupportToAbsolute(minSupportThreshold, transactionCount);

            List<UOList> initialLists = createInitialUOLists();
            buildInitialUOLists(input, initialLists);

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
        huopCount = 0;
        joinCount = 0;
        upperBoundPrunedCount = 0;
        remainingSupportPrunedCount = 0;
        supportPrunedCount = 0;
        transactionCount = 0;
        minSupportCount = 0;
        mapItemToSupport = new HashMap<Integer, Integer>();
    }

    /**
     * Scan the database once to calculate support counts and the transaction count.
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
                transactionCount++;
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    /**
     * Create UO-lists for all frequent 1-itemsets and sort them by support.
     *
     * @return the sorted UO-lists of frequent items
     */
    private List<UOList> createInitialUOLists() {
        List<UOList> lists = new ArrayList<UOList>();
        for (Integer item : mapItemToSupport.keySet()) {
            if (mapItemToSupport.get(item) >= minSupportCount) {
                lists.add(new UOList(item));
            }
        }
        Collections.sort(lists, new Comparator<UOList>() {
            public int compare(UOList list1, UOList list2) {
                return compareItems(list1.item, list2.item);
            }
        });
        return lists;
    }

    /**
     * Scan the database a second time to build UO-lists of frequent 1-itemsets.
     *
     * @param input        input file path
     * @param initialLists lists to fill
     * @throws IOException if an error occurs while reading the file
     */
    private void buildInitialUOLists(String input, List<UOList> initialLists) throws IOException {
        Map<Integer, UOList> mapItemToUOList = new HashMap<Integer, UOList>();
        for (UOList uoList : initialLists) {
            mapItemToUOList.put(uoList.item, uoList);
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
                if (transactionUtility <= 0d) {
                    throw new IOException("HUOPM requires positive transaction utility: " + line);
                }
                String[] items = split[0].trim().split("\\s+");
                String[] utilities = split[2].trim().split("\\s+");
                if (items.length != utilities.length) {
                    throw new IOException("Items and utilities have different lengths: " + line);
                }

                List<Pair> revisedTransaction = new ArrayList<Pair>();
                for (int i = 0; i < items.length; i++) {
                    int item = Integer.parseInt(items[i]);
                    if (mapItemToUOList.containsKey(item)) {
                        revisedTransaction.add(new Pair(item, Double.parseDouble(utilities[i])));
                    }
                }

                Collections.sort(revisedTransaction, new Comparator<Pair>() {
                    public int compare(Pair pair1, Pair pair2) {
                        return compareItems(pair1.item, pair2.item);
                    }
                });

                double remainingUtility = 0d;
                for (int i = revisedTransaction.size() - 1; i >= 0; i--) {
                    Pair pair = revisedTransaction.get(i);
                    double utilityOccupancy = pair.utility / transactionUtility;
                    double remainingUtilityOccupancy = remainingUtility / transactionUtility;
                    mapItemToUOList.get(pair.item).addElement(
                            new UOElement(tid, utilityOccupancy, remainingUtilityOccupancy));
                    remainingUtility += pair.utility;
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
     * Recursive depth-first mining over the FU-tree.
     *
     * @param prefixLength length of the current prefix
     * @param prefixList   UO-list of the current prefix, or null for the root
     * @param extensions   extension UO-lists of the current prefix
     * @throws IOException if writing output fails
     */
    private void search(int prefixLength, UOList prefixList, List<UOList> extensions) throws IOException {
        for (int i = 0; i < extensions.size(); i++) {
            UOList extension = extensions.get(i);
            int support = extension.getSupport();
            double utilityOccupancy = extension.getUtilityOccupancy();

            if (support >= minSupportCount && utilityOccupancy >= minUtilityOccupancy) {
                writeOut(prefixLength, extension.item, support, utilityOccupancy);
            }

            if (pruningStrategy.isSupportPruningEnabled() && support < minSupportCount) {
                supportPrunedCount++;
                continue;
            }

            if (pruningStrategy.isUpperBoundPruningEnabled()) {
                double upperBound = calculateUpperBound(extension);
                if (upperBound < minUtilityOccupancy) {
                    upperBoundPrunedCount++;
                    continue;
                }
            }

            List<UOList> childExtensions = new ArrayList<UOList>();
            for (int j = i + 1; j < extensions.size(); j++) {
                UOList child = construct(prefixList, extension, extensions.get(j));
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
     * Construct the UO-list for the joined itemset represented by prefix + xa.item + xb.item.
     *
     * @param prefixList UO-list of the prefix, or null for a 2-itemset
     * @param xa         first extension list
     * @param xb         second extension list
     * @return the joined UO-list, or null if remaining-support pruning proves it infrequent
     */
    private UOList construct(UOList prefixList, UOList xa, UOList xb) {
        UOList joined = new UOList(xb.item);
        int indexA = 0;
        int indexB = 0;
        int remainingSupport = xa.getSupport();

        while (indexA < xa.elements.size() && indexB < xb.elements.size()) {
            UOElement elementA = xa.elements.get(indexA);
            UOElement elementB = xb.elements.get(indexB);

            if (elementA.tid == elementB.tid) {
                double utilityOccupancy;
                if (prefixList == null) {
                    utilityOccupancy = elementA.utilityOccupancy + elementB.utilityOccupancy;
                } else {
                    UOElement prefixElement = findElementWithTID(prefixList, elementA.tid);
                    if (prefixElement == null) {
                        indexA++;
                        indexB++;
                        continue;
                    }
                    utilityOccupancy = elementA.utilityOccupancy + elementB.utilityOccupancy
                            - prefixElement.utilityOccupancy;
                }
                joined.addElement(new UOElement(elementA.tid, utilityOccupancy,
                        elementB.remainingUtilityOccupancy));
                indexA++;
                indexB++;
            } else if (elementA.tid < elementB.tid) {
                indexA++;
                remainingSupport--;
                if (shouldStopConstruction(remainingSupport)) {
                    return null;
                }
            } else {
                indexB++;
            }
        }

        remainingSupport -= xa.elements.size() - indexA;
        if (shouldStopConstruction(remainingSupport)) {
            return null;
        }
        return joined;
    }

    /**
     * Check the remaining-support pruning condition.
     *
     * @param remainingSupport maximum possible support for the list being constructed
     * @return true if construction can stop
     */
    private boolean shouldStopConstruction(int remainingSupport) {
        if (pruningStrategy.isRemainingSupportPruningEnabled() && remainingSupport < minSupportCount) {
            remainingSupportPrunedCount++;
            return true;
        }
        return false;
    }

    /**
     * Calculate the HUOPM upper bound from the top min-support values of uo + ruo.
     *
     * @param uoList UO-list of a tree node
     * @return upper bound on utility occupancy for the subtree
     */
    private double calculateUpperBound(UOList uoList) {
        if (uoList.getSupport() < minSupportCount) {
            return 0d;
        }
        List<Double> values = new ArrayList<Double>(uoList.elements.size());
        for (UOElement element : uoList.elements) {
            values.add(element.utilityOccupancy + element.remainingUtilityOccupancy);
        }
        Collections.sort(values, Collections.reverseOrder());

        double sumTopK = 0d;
        for (int i = 0; i < minSupportCount; i++) {
            sumTopK += values.get(i);
        }
        return sumTopK / minSupportCount;
    }

    /**
     * Find the element of a UO-list having a given transaction id.
     *
     * @param uoList the list to search
     * @param tid    transaction id
     * @return the matching element, or null
     */
    private UOElement findElementWithTID(UOList uoList, int tid) {
        int first = 0;
        int last = uoList.elements.size() - 1;
        while (first <= last) {
            int middle = (first + last) >>> 1;
            UOElement element = uoList.elements.get(middle);
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
     * Write a high-utility occupancy pattern.
     *
     * @param prefixLength     prefix length
     * @param item             last item to append to the prefix
     * @param support          pattern support
     * @param utilityOccupancy pattern utility occupancy
     * @throws IOException if writing output fails
     */
    private void writeOut(int prefixLength, int item, int support, double utilityOccupancy) throws IOException {
        huopCount++;
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < prefixLength; i++) {
            buffer.append(itemsetBuffer[i]);
            buffer.append(' ');
        }
        buffer.append(item);
        buffer.append(" #SUP: ");
        buffer.append(support);
        buffer.append(" #UOCC: ");
        buffer.append(utilityOccupancy);
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
        System.out.println("=============  HUOPM ALGORITHM - STATS =============");
        System.out.println(" Total time ~ " + (endTimestamp - startTimestamp) + " ms");
        System.out.println(" Memory ~ " + MemoryLogger.getInstance().getMaxMemory() + " MB");
        System.out.println(" High-utility occupancy patterns count : " + huopCount);
        System.out.println(" Join count : " + joinCount);
        System.out.println(" Support-pruned count : " + supportPrunedCount);
        System.out.println(" Upper-bound-pruned count : " + upperBoundPrunedCount);
        System.out.println(" Remaining-support-pruned count : " + remainingSupportPrunedCount);
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
