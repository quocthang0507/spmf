package ca.pfv.spmf.algorithms.frequentpatterns.huopm;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility-occupancy list used by HUOPM.
 */
class UOList {
    final int item;
    final List<UOElement> elements = new ArrayList<UOElement>();
    double sumUtilityOccupancy = 0d;
    double sumRemainingUtilityOccupancy = 0d;

    /**
     * Create a UO-list identified by its last extension item.
     *
     * @param item the last item of the represented itemset
     */
    UOList(int item) {
        this.item = item;
    }

    /**
     * Add an element and update aggregate values.
     *
     * @param element the element to add
     */
    void addElement(UOElement element) {
        elements.add(element);
        sumUtilityOccupancy += element.utilityOccupancy;
        sumRemainingUtilityOccupancy += element.remainingUtilityOccupancy;
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
     * Get the average utility occupancy in the supporting transactions.
     *
     * @return utility occupancy, or 0 if the itemset has no support
     */
    double getUtilityOccupancy() {
        if (elements.isEmpty()) {
            return 0d;
        }
        return sumUtilityOccupancy / elements.size();
    }
}
