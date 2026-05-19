package ca.pfv.spmf.algorithms.frequentpatterns.hauopm;

/**
 * Element of an average utility occupancy list.
 */
class AUOElement {
    final int tid;
    final double itemsetUtilityOccupancy;
    final double itemsetMaxUtilityOccupancy;
    final double transactionUtility;

    /**
     * Create an AUOL element for one supporting transaction.
     *
     * @param tid                         transaction identifier
     * @param itemsetUtilityOccupancy     utility occupancy of the itemset in the transaction
     * @param itemsetMaxUtilityOccupancy  maximum utility occupancy used by AUOUB
     * @param transactionUtility          transaction utility
     */
    AUOElement(int tid, double itemsetUtilityOccupancy, double itemsetMaxUtilityOccupancy,
               double transactionUtility) {
        this.tid = tid;
        this.itemsetUtilityOccupancy = itemsetUtilityOccupancy;
        this.itemsetMaxUtilityOccupancy = itemsetMaxUtilityOccupancy;
        this.transactionUtility = transactionUtility;
    }
}
