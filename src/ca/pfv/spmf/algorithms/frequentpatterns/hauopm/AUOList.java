package ca.pfv.spmf.algorithms.frequentpatterns.hauopm;

import java.util.ArrayList;
import java.util.List;

/**
 * Average utility occupancy list used by HAUOPM.
 */
class AUOList {
    final int item;
    final List<AUOElement> elements = new ArrayList<AUOElement>();
    double sumItemsetUtilityOccupancy = 0d;
    double sumTransactionUtility = 0d;

    /**
     * Create an AUO-list identified by its last extension item.
     *
     * @param item the last item of the represented itemset
     */
    AUOList(int item) {
        this.item = item;
    }

    /**
     * Add an element and update aggregate values.
     *
     * @param element the element to add
     */
    void addElement(AUOElement element) {
        elements.add(element);
        sumItemsetUtilityOccupancy += element.itemsetUtilityOccupancy;
        sumTransactionUtility += element.transactionUtility;
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
     * Calculate utility occupancy in the supporting transactions.
     *
     * @return utility occupancy, or 0 if the itemset has no support
     */
    double getUtilityOccupancy() {
        if (elements.isEmpty()) {
            return 0d;
        }
        return sumItemsetUtilityOccupancy / elements.size();
    }

    /**
     * Calculate average utility occupancy by considering the itemset length.
     *
     * @param itemsetLength itemset length
     * @return average utility occupancy
     */
    double getAverageUtilityOccupancy(int itemsetLength) {
        if (itemsetLength <= 0) {
            return 0d;
        }
        return getUtilityOccupancy() / itemsetLength;
    }

    /**
     * Calculate transaction utility occupancy.
     *
     * @param databaseUtility total transaction utility of the database
     * @return transaction utility occupancy
     */
    double getTransactionUtilityOccupancy(double databaseUtility) {
        if (databaseUtility <= 0d) {
            return 0d;
        }
        return sumTransactionUtility / databaseUtility;
    }

    /**
     * Calculate average transaction utility occupancy.
     *
     * @param itemsetLength   itemset length
     * @param databaseUtility total transaction utility of the database
     * @return average transaction utility occupancy
     */
    double getAverageTransactionUtilityOccupancy(int itemsetLength, double databaseUtility) {
        return getAverageUtilityOccupancy(itemsetLength) * getTransactionUtilityOccupancy(databaseUtility);
    }
}
