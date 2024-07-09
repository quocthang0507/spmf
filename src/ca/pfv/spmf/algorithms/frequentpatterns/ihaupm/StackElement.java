package ca.pfv.spmf.algorithms.frequentpatterns.ihaupm;

/**
 * This is an implementation of the "IHAUPM" algorithm for High-Average-Utility Itemsets Mining
 * as described in the conference paper : <br/><br/>
 * <p>
 * Jerry Chun-Wei Lin, Shifeng Ren, and Philippe Fournier-Viger. Efficiently Updating the Discovered High Average-Utility Itemsets with Transaction Insertion. EAAI (unpublished, minor revision)
 *
 * @author Shi-Feng Ren
 * @see algorithm.IHAUPM
 * @see tree.IAUTree
 * @see tree.IAUNode
 * @see tree.TableNode
 * @see ItemTHUI.Item
 * @see Pattern.Itemset
 */

public class StackElement {
    public String name;
    public double utility;
    public int location;
    public long auub;
}
