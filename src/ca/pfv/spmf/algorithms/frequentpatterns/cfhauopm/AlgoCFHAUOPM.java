package ca.pfv.spmf.algorithms.frequentpatterns.cfhauopm;

import ca.pfv.spmf.tools.MemoryLogger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of CFHAUOPM for mining closed frequent high average utility
 * occupancy patterns.
 * <br/><br/>
 * The implementation follows the definitions of closed FHAUOIs, lauorho, and
 * tauodelta from the paper "An Efficient Method for Mining Closed Frequent
 * High Average Utility Occupancy Patterns". It uses SPMF utility transaction
 * files where each line has the form items:transactionUtility:itemUtilities.
 *
 * @author quocthang0507
 */
public class AlgoCFHAUOPM {

    /** Start time of the last run. */
    public long startTimestamp = 0;

    /** End time of the last run. */
    public long endTimestamp = 0;

    /** Number of closed frequent high average utility occupancy patterns found. */
    public int cfhauopCount = 0;

    /** Number of list joins performed. */
    public long joinCount = 0;

    /** Number of itemsets pruned by support. */
    public long supportPrunedCount = 0;

    /** Number of itemsets pruned by lauorho. */
    public long lauorhoPrunedCount = 0;

    /** Number of proper branches pruned by tauodelta. */
    public long tauodeltaPrunedCount = 0;

    private BufferedWriter writer;
    private int transactionCount;
    private int minSupportCount;
    private double minAverageUtilityOccupancy;
    private Map<Integer, Integer> mapItemToSupport;
    private List<TransactionData> transactions;
    private List<CFPattern> closedPatterns;
    private CFHAUOPMPruningStrategy pruningStrategy;

    /**
     * Run CFHAUOPM using all pruning strategies.
     *
     * @param input                       input file in SPMF utility transaction format
     * @param output                      output file
     * @param minSupportThreshold         minimum support as a ratio in (0,1) or an absolute count if >= 1
     * @param minAverageUtilityOccupancy  minimum average utility occupancy
     * @throws IOException if an error occurs while reading or writing files
     */
    public void runAlgorithm(String input, String output, double minSupportThreshold,
                             double minAverageUtilityOccupancy) throws IOException {
        runAlgorithm(input, output, minSupportThreshold, minAverageUtilityOccupancy, CFHAUOPMPruningStrategy.ALL);
    }

    /**
     * Run CFHAUOPM.
     *
     * @param input                       input file in SPMF utility transaction format
     * @param output                      output file
     * @param minSupportThreshold         minimum support as a ratio in (0,1) or an absolute count if >= 1
     * @param minAverageUtilityOccupancy  minimum average utility occupancy
     * @param pruningStrategy             pruning strategy configuration
     * @throws IOException if an error occurs while reading or writing files
     */
    public void runAlgorithm(String input, String output, double minSupportThreshold,
                             double minAverageUtilityOccupancy,
                             CFHAUOPMPruningStrategy pruningStrategy) throws IOException {
        MemoryLogger.getInstance().reset();
        reset();

        this.minAverageUtilityOccupancy = minAverageUtilityOccupancy;
        this.pruningStrategy = pruningStrategy == null ? CFHAUOPMPruningStrategy.ALL : pruningStrategy;
        this.writer = new BufferedWriter(new FileWriter(output));
        this.startTimestamp = System.currentTimeMillis();

        try {
            loadDatabase(input);
            this.minSupportCount = convertMinSupportToAbsolute(minSupportThreshold, transactionCount);

            List<Integer> promisingItems = findPromisingItems();
            buildItemOrder(promisingItems);
            List<CFList> initialLists = buildInitialLists(promisingItems);

            MemoryLogger.getInstance().checkMemory();
            mine(null, initialLists);
            writeClosedPatterns();
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
        cfhauopCount = 0;
        joinCount = 0;
        supportPrunedCount = 0;
        lauorhoPrunedCount = 0;
        tauodeltaPrunedCount = 0;
        transactionCount = 0;
        minSupportCount = 0;
        mapItemToSupport = new HashMap<Integer, Integer>();
        transactions = new ArrayList<TransactionData>();
        closedPatterns = new ArrayList<CFPattern>();
    }

    /**
     * Load the database and calculate item supports.
     *
     * @param input input file path
     * @throws IOException if an error occurs while reading the file
     */
    private void loadDatabase(String input) throws IOException {
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
                String[] itemStrings = split[0].trim().split("\\s+");
                String[] utilityStrings = split[2].trim().split("\\s+");
                if (itemStrings.length != utilityStrings.length) {
                    throw new IOException("Items and utilities have different lengths: " + line);
                }
                double transactionUtility = Double.parseDouble(split[1]);
                if (transactionUtility <= 0d) {
                    throw new IOException("CFHAUOPM requires positive transaction utility: " + line);
                }

                int[] items = new int[itemStrings.length];
                double[] utilities = new double[utilityStrings.length];
                Set<Integer> itemsSeen = new HashSet<Integer>();
                for (int i = 0; i < itemStrings.length; i++) {
                    int item = Integer.parseInt(itemStrings[i]);
                    items[i] = item;
                    utilities[i] = Double.parseDouble(utilityStrings[i]);
                    if (itemsSeen.add(item)) {
                        Integer support = mapItemToSupport.get(item);
                        mapItemToSupport.put(item, support == null ? 1 : support + 1);
                    }
                }
                transactions.add(new TransactionData(transactionCount, items, utilities, transactionUtility));
                transactionCount++;
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    /**
     * Select items satisfying the initial support and lauorho pruning condition.
     *
     * @return promising items sorted later by support
     */
    private List<Integer> findPromisingItems() {
        List<Integer> promisingItems = new ArrayList<Integer>();
        for (Integer item : mapItemToSupport.keySet()) {
            if (mapItemToSupport.get(item) < minSupportCount) {
                supportPrunedCount++;
                continue;
            }
            if (pruningStrategy.isLauorhoPruningEnabled()
                    && calculateSingletonLauorho(item) < minAverageUtilityOccupancy) {
                lauorhoPrunedCount++;
                continue;
            }
            promisingItems.add(item);
        }
        Collections.sort(promisingItems, new Comparator<Integer>() {
            public int compare(Integer item1, Integer item2) {
                return compareItems(item1, item2);
            }
        });
        return promisingItems;
    }

    /**
     * Build the total order over promising items.
     *
     * @param promisingItems promising items
     */
    private void buildItemOrder(List<Integer> promisingItems) {
        // The list is already sorted by support and item id. The compareItems()
        // method uses that same order while revising transactions and joining lists.
    }

    /**
     * Build utility occupancy lists for promising 1-itemsets.
     *
     * @param promisingItems promising items
     * @return initial CF-lists
     */
    private List<CFList> buildInitialLists(List<Integer> promisingItems) {
        Map<Integer, CFList> mapItemToList = new HashMap<Integer, CFList>();
        List<CFList> initialLists = new ArrayList<CFList>();
        for (Integer item : promisingItems) {
            CFList list = new CFList(new int[]{item});
            mapItemToList.put(item, list);
            initialLists.add(list);
        }

        for (TransactionData transaction : transactions) {
            List<Pair> revisedTransaction = new ArrayList<Pair>();
            for (int i = 0; i < transaction.items.length; i++) {
                if (mapItemToList.containsKey(transaction.items[i])) {
                    revisedTransaction.add(new Pair(transaction.items[i], transaction.utilities[i]));
                }
            }
            Collections.sort(revisedTransaction, new Comparator<Pair>() {
                public int compare(Pair pair1, Pair pair2) {
                    return compareItems(pair1.item, pair2.item);
                }
            });
            transaction.revisedUtilities = new double[revisedTransaction.size()];
            for (int i = 0; i < revisedTransaction.size(); i++) {
                transaction.revisedUtilities[i] = revisedTransaction.get(i).utility;
            }

            for (int i = 0; i < revisedTransaction.size(); i++) {
                Pair pair = revisedTransaction.get(i);
                double[] remainingUtilities = new double[revisedTransaction.size() - i - 1];
                for (int j = i + 1; j < revisedTransaction.size(); j++) {
                    remainingUtilities[j - i - 1] = revisedTransaction.get(j).utility;
                }
                mapItemToList.get(pair.item).addElement(new CFElement(transaction.tid, pair.utility,
                        pair.utility / transaction.transactionUtility, transaction.transactionUtility,
                        remainingUtilities));
            }
        }
        return initialLists;
    }

    /**
     * Recursively mine closed frequent high average utility occupancy itemsets.
     *
     * @param prefixList UO-list of the prefix, or null for root
     * @param extensions extension lists of the prefix
     * @throws IOException if writing output fails
     */
    private void mine(CFList prefixList, List<CFList> extensions) throws IOException {
        for (int i = 0; i < extensions.size(); i++) {
            CFList current = extensions.get(i);
            if (current.getSupport() < minSupportCount) {
                supportPrunedCount++;
                continue;
            }

            if (current.getAverageUtilityOccupancy() >= minAverageUtilityOccupancy) {
                updateClosedPatterns(current);
            }

            if (pruningStrategy.isTauodeltaPruningEnabled()
                    && calculateTauodelta(current) < minAverageUtilityOccupancy) {
                tauodeltaPrunedCount++;
                continue;
            }

            List<CFList> childExtensions = new ArrayList<CFList>();
            for (int j = i + 1; j < extensions.size(); j++) {
                CFList child = construct(prefixList, current, extensions.get(j));
                joinCount++;
                if (child.getSupport() == 0) {
                    continue;
                }
                if (child.getSupport() < minSupportCount) {
                    supportPrunedCount++;
                    continue;
                }
                if (pruningStrategy.isLauorhoPruningEnabled()
                        && calculateLauorho(child) < minAverageUtilityOccupancy) {
                    lauorhoPrunedCount++;
                    continue;
                }
                childExtensions.add(child);
            }
            mine(current, childExtensions);
        }
    }

    /**
     * Construct the utility occupancy list of the joined itemset.
     *
     * @param prefixList prefix list, or null for 2-itemsets
     * @param xa         first extension
     * @param xb         second extension
     * @return joined list
     */
    private CFList construct(CFList prefixList, CFList xa, CFList xb) {
        int[] joinedItemset = Arrays.copyOf(xa.itemset, xa.itemset.length + 1);
        joinedItemset[joinedItemset.length - 1] = xb.item;
        CFList joined = new CFList(joinedItemset);
        int indexA = 0;
        int indexB = 0;

        while (indexA < xa.elements.size() && indexB < xb.elements.size()) {
            CFElement elementA = xa.elements.get(indexA);
            CFElement elementB = xb.elements.get(indexB);
            if (elementA.tid == elementB.tid) {
                double itemsetUtility;
                if (prefixList == null) {
                    itemsetUtility = elementA.itemsetUtility + elementB.itemsetUtility;
                } else {
                    CFElement prefixElement = findElementWithTID(prefixList, elementA.tid);
                    if (prefixElement == null) {
                        indexA++;
                        indexB++;
                        continue;
                    }
                    itemsetUtility = elementA.itemsetUtility + elementB.itemsetUtility
                            - prefixElement.itemsetUtility;
                }
                joined.addElement(new CFElement(elementA.tid, itemsetUtility,
                        itemsetUtility / elementA.transactionUtility, elementA.transactionUtility,
                        elementB.remainingUtilities));
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
     * Update the set of closed FHAUOIs with a newly found FHAUOI.
     *
     * @param candidate candidate FHAUOI
     */
    private void updateClosedPatterns(CFList candidate) {
        for (CFPattern pattern : closedPatterns) {
            if (isProperSuperset(pattern.itemset, candidate.itemset) && pattern.support == candidate.getSupport()) {
                return;
            }
        }

        Iterator<CFPattern> iterator = closedPatterns.iterator();
        while (iterator.hasNext()) {
            CFPattern pattern = iterator.next();
            if (isProperSuperset(candidate.itemset, pattern.itemset) && pattern.support == candidate.getSupport()) {
                iterator.remove();
            }
        }
        closedPatterns.add(new CFPattern(Arrays.copyOf(candidate.itemset, candidate.itemset.length),
                candidate.getSupport(), candidate.getAverageUtilityOccupancy()));
    }

    /**
     * Calculate the anti-monotone lauorho upper bound for a list.
     *
     * @param list list of an itemset
     * @return lauorho value
     */
    private double calculateLauorho(CFList list) {
        if (list.getSupport() < minSupportCount) {
            return 0d;
        }
        List<Double> values = new ArrayList<Double>(list.getSupport());
        for (CFElement element : list.elements) {
            values.add(transactions.get(element.tid).getTopKAverageUtilityOccupancy(list.itemset.length));
        }
        Collections.sort(values, Collections.reverseOrder());
        return averageTopK(values, minSupportCount);
    }

    /**
     * Calculate lauorho for a singleton item during initial pruning.
     *
     * @param item item
     * @return singleton lauorho
     */
    private double calculateSingletonLauorho(int item) {
        List<Double> values = new ArrayList<Double>();
        for (TransactionData transaction : transactions) {
            if (transaction.contains(item)) {
                values.add(transaction.getOriginalTopKAverageUtilityOccupancy(1));
            }
        }
        if (values.size() < minSupportCount) {
            return 0d;
        }
        Collections.sort(values, Collections.reverseOrder());
        return averageTopK(values, minSupportCount);
    }

    /**
     * Calculate the tauodelta weak upper bound for proper extensions of a list.
     *
     * @param list list of an itemset
     * @return tauodelta value
     */
    private double calculateTauodelta(CFList list) {
        List<Double> values = new ArrayList<Double>();
        for (CFElement element : list.elements) {
            if (element.remainingUtilities.length > 0) {
                values.add(calculateTauo(element, list.itemset.length));
            }
        }
        if (values.size() < minSupportCount) {
            return 0d;
        }
        Collections.sort(values, Collections.reverseOrder());
        return averageTopK(values, minSupportCount);
    }

    /**
     * Calculate tauo(A,t), the tightest WUB for proper extensions of A in a transaction.
     *
     * @param element       list element for transaction t
     * @param itemsetLength itemset length
     * @return tauo(A,t)
     */
    private double calculateTauo(CFElement element, int itemsetLength) {
        double[] sortedRemaining = Arrays.copyOf(element.remainingUtilities, element.remainingUtilities.length);
        sortDescending(sortedRemaining);
        double utilitySum = element.itemsetUtility;
        double bestAverageUtility = 0d;
        for (int i = 0; i < sortedRemaining.length; i++) {
            utilitySum += sortedRemaining[i];
            double averageUtility = utilitySum / (itemsetLength + i + 1);
            if (averageUtility > bestAverageUtility) {
                bestAverageUtility = averageUtility;
            }
        }
        return bestAverageUtility / element.transactionUtility;
    }

    /**
     * Average the top k values of a descending sorted list.
     *
     * @param values descending sorted values
     * @param k      number of values
     * @return average top-k value
     */
    private double averageTopK(List<Double> values, int k) {
        double sum = 0d;
        for (int i = 0; i < k; i++) {
            sum += values.get(i);
        }
        return sum / k;
    }

    /**
     * Find an element with a given transaction id.
     *
     * @param list list to search
     * @param tid  transaction id
     * @return matching element, or null
     */
    private CFElement findElementWithTID(CFList list, int tid) {
        int first = 0;
        int last = list.elements.size() - 1;
        while (first <= last) {
            int middle = (first + last) >>> 1;
            CFElement element = list.elements.get(middle);
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
     * Write all closed patterns to the output file.
     *
     * @throws IOException if writing output fails
     */
    private void writeClosedPatterns() throws IOException {
        cfhauopCount = closedPatterns.size();
        for (CFPattern pattern : closedPatterns) {
            StringBuilder buffer = new StringBuilder();
            for (int i = 0; i < pattern.itemset.length; i++) {
                if (i != 0) {
                    buffer.append(' ');
                }
                buffer.append(pattern.itemset[i]);
            }
            buffer.append(" #SUP: ");
            buffer.append(pattern.support);
            buffer.append(" #AUO: ");
            buffer.append(pattern.averageUtilityOccupancy);
            writer.write(buffer.toString());
            writer.newLine();
        }
    }

    /**
     * Check if one itemset is a proper superset of another.
     *
     * @param possibleSuperset possible superset
     * @param possibleSubset   possible subset
     * @return true if possibleSuperset properly contains possibleSubset
     */
    private boolean isProperSuperset(int[] possibleSuperset, int[] possibleSubset) {
        if (possibleSuperset.length <= possibleSubset.length) {
            return false;
        }
        for (int item : possibleSubset) {
            boolean found = false;
            for (int supersetItem : possibleSuperset) {
                if (item == supersetItem) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    /**
     * Compare two items by support ascending order, then lexical order.
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
     * Sort values in descending order.
     *
     * @param values values to sort
     */
    private void sortDescending(double[] values) {
        Arrays.sort(values);
        for (int i = 0, j = values.length - 1; i < j; i++, j--) {
            double temp = values[i];
            values[i] = values[j];
            values[j] = temp;
        }
    }

    /**
     * Print statistics about the latest execution.
     */
    public void printStats() {
        System.out.println("=============  CFHAUOPM ALGORITHM - STATS =============");
        System.out.println(" Total time ~ " + (endTimestamp - startTimestamp) + " ms");
        System.out.println(" Memory ~ " + MemoryLogger.getInstance().getMaxMemory() + " MB");
        System.out.println(" Closed frequent high average utility occupancy patterns count : " + cfhauopCount);
        System.out.println(" Join count : " + joinCount);
        System.out.println(" Support-pruned count : " + supportPrunedCount);
        System.out.println(" lauorho-pruned count : " + lauorhoPrunedCount);
        System.out.println(" tauodelta-pruned count : " + tauodeltaPrunedCount);
        System.out.println(" Pruning strategy : " + pruningStrategy.getLabel());
        System.out.println("===================================================");
    }

    /**
     * Transaction data kept in memory for upper-bound calculations.
     */
    private static class TransactionData {
        final int tid;
        final int[] items;
        final double[] utilities;
        final double transactionUtility;
        double[] revisedUtilities = new double[0];

        TransactionData(int tid, int[] items, double[] utilities, double transactionUtility) {
            this.tid = tid;
            this.items = items;
            this.utilities = utilities;
            this.transactionUtility = transactionUtility;
        }

        boolean contains(int item) {
            for (int transactionItem : items) {
                if (transactionItem == item) {
                    return true;
                }
            }
            return false;
        }

        double getOriginalTopKAverageUtilityOccupancy(int k) {
            return getTopKAverageUtilityOccupancy(utilities, k);
        }

        double getTopKAverageUtilityOccupancy(int k) {
            return getTopKAverageUtilityOccupancy(revisedUtilities, k);
        }

        private double getTopKAverageUtilityOccupancy(double[] values, int k) {
            if (values.length < k || k <= 0) {
                return 0d;
            }
            double[] sorted = Arrays.copyOf(values, values.length);
            Arrays.sort(sorted);
            double sum = 0d;
            for (int i = 0; i < k; i++) {
                sum += sorted[sorted.length - 1 - i];
            }
            return (sum / k) / transactionUtility;
        }
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
