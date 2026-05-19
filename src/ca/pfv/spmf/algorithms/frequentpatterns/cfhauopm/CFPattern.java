package ca.pfv.spmf.algorithms.frequentpatterns.cfhauopm;

/**
 * A closed frequent high average utility occupancy pattern.
 */
class CFPattern {
    final int[] itemset;
    final int support;
    final double averageUtilityOccupancy;

    /**
     * Create a closed pattern.
     *
     * @param itemset                 itemset
     * @param support                 support count
     * @param averageUtilityOccupancy average utility occupancy
     */
    CFPattern(int[] itemset, int support, double averageUtilityOccupancy) {
        this.itemset = itemset;
        this.support = support;
        this.averageUtilityOccupancy = averageUtilityOccupancy;
    }
}
