package ca.pfv.spmf.algorithms.frequentpatterns.cfhauopm;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility occupancy list used by CFHAUOPM.
 */
class CFList {
    final int[] itemset;
    final int item;
    final List<CFElement> elements = new ArrayList<CFElement>();
    double sumUtilityOccupancy = 0d;

    /**
     * Create a list for an itemset.
     *
     * @param itemset itemset represented by this list
     */
    CFList(int[] itemset) {
        this.itemset = itemset;
        this.item = itemset[itemset.length - 1];
    }

    /**
     * Add an element and update aggregate values.
     *
     * @param element the element to add
     */
    void addElement(CFElement element) {
        elements.add(element);
        sumUtilityOccupancy += element.itemsetUtilityOccupancy;
    }

    /**
     * Get the support count of the represented itemset.
     *
     * @return support count
     */
    int getSupport() {
        return elements.size();
    }

    /**
     * Get the utility occupancy of the represented itemset.
     *
     * @return utility occupancy, or 0 if the itemset has no support
     */
    double getUtilityOccupancy() {
        if (elements.isEmpty()) {
            return 0d;
        }
        return sumUtilityOccupancy / elements.size();
    }

    /**
     * Get the average utility occupancy of the represented itemset.
     *
     * @return average utility occupancy
     */
    double getAverageUtilityOccupancy() {
        if (itemset.length == 0) {
            return 0d;
        }
        return getUtilityOccupancy() / itemset.length;
    }
}
