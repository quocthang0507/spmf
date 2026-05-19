package ca.pfv.spmf.algorithms.frequentpatterns.cfhauopm;

/**
 * Element of a CFHAUOPM utility occupancy list.
 */
class CFElement {
    final int tid;
    final double itemsetUtility;
    final double itemsetUtilityOccupancy;
    final double transactionUtility;
    final double[] remainingUtilities;

    /**
     * Create an element for one supporting transaction.
     *
     * @param tid                     transaction identifier
     * @param itemsetUtility          utility of the itemset in the transaction
     * @param itemsetUtilityOccupancy utility occupancy of the itemset in the transaction
     * @param transactionUtility      transaction utility
     * @param remainingUtilities      utilities of items appearing after the itemset
     */
    CFElement(int tid, double itemsetUtility, double itemsetUtilityOccupancy, double transactionUtility,
              double[] remainingUtilities) {
        this.tid = tid;
        this.itemsetUtility = itemsetUtility;
        this.itemsetUtilityOccupancy = itemsetUtilityOccupancy;
        this.transactionUtility = transactionUtility;
        this.remainingUtilities = remainingUtilities;
    }
}
