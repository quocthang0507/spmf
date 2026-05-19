package ca.pfv.spmf.algorithms.frequentpatterns.huopm;

/**
 * Element of a utility-occupancy list.
 */
class UOElement {
    final int tid;
    final double utilityOccupancy;
    final double remainingUtilityOccupancy;

    /**
     * Create an element for one supporting transaction.
     *
     * @param tid                       transaction identifier
     * @param utilityOccupancy          utility occupancy of the itemset in the transaction
     * @param remainingUtilityOccupancy remaining utility occupancy after the itemset
     */
    UOElement(int tid, double utilityOccupancy, double remainingUtilityOccupancy) {
        this.tid = tid;
        this.utilityOccupancy = utilityOccupancy;
        this.remainingUtilityOccupancy = remainingUtilityOccupancy;
    }
}
