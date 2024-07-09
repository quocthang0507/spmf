package ca.pfv.spmf.algorithms.frequentpatterns.clhminer;

/**
 * Implementation of a utility list element as used by CLH-Miner
 *
 * @author Bay Vo et al.
 * @see AlgoCLHMiner
 */
public class Element {
    /**
     * transaction id
     */
    public int tid;
    /**
     * itemset utility
     */
    public double iutils;
    /**
     * remaining utility
     */
    public double rutils;

    public double TU;

    /**
     * Constructor.
     *
     * @param tid    the transaction id
     * @param iutils the itemset utility
     * @param rutils the remaining utility
     */
    public Element(int tid, double iutils, double rutils, double TU) {
        this.tid = tid;
        this.iutils = iutils;
        this.rutils = rutils;
        this.TU = TU;
    }

    public Element(int tid) {
        this.tid = tid;
        this.iutils = 0;
        this.rutils = 0;
    }

    public Element(int tid, double iutils) {
        this.tid = tid;
        this.iutils = iutils;
        this.rutils = 0;
    }
}
