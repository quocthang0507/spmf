package ca.pfv.spmf.input.cost_utility_transaction_database;

// this class represent an item and its cost in a transaction
public class ItemCost {

    public int item;
    public int cost;
    public ItemCost(int item, int cost) {
        this.item = item;
        this.cost = cost;
    }

    public String toString() {
        return "[" + item + "," + cost + "]";
    }
}