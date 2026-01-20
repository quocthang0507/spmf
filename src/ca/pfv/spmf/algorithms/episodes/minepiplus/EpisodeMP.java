package ca.pfv.spmf.algorithms.episodes.minepiplus;

import ca.pfv.spmf.algorithms.episodes.general.AbstractEpisode;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is an Episode (serial episode) in a complex sequence (where an
 * episode can contain multiple symbols for the same time point). This is used
 * by the MINEPIPlus algorithm
 *
 * @author Peng Yang
 * @see AlgoMINEPIPlus
 */
public class EpisodeMP extends AbstractEpisode {


    /**
     * Constructor
     */
    EpisodeMP() {
        super(0);
    }

    /**
     * Constructor
     *
     * @param events  the events of this episode
     * @param support the support of this episode
     */
    EpisodeMP(List<int[]> events, int support) {
        super(events, support);
    }


    public boolean equal(List<int[]> b) {
        return events.equals(b);
    }

    /**
     * Perform an i-extension of this episode with an item
     *
     * @param item    the item
     * @param support the support
     * @return the resulting episode
     */
    public EpisodeMP iExtension(int item, int support) {
        int[] finalEventSet = this.events.get(events.size() - 1);
        int len = finalEventSet.length;
        int[] newEventSet = new int[len + 1];
        System.arraycopy(finalEventSet, 0, newEventSet, 0, len);
        newEventSet[len] = item;
        List<int[]> newEvents = new ArrayList<int[]>(events);
        // set the last eventSet to the new eventSet.
        newEvents.set(events.size() - 1, newEventSet);
        return new EpisodeMP(newEvents, support);
    }

    /**
     * Create an s-extension of this episode
     *
     * @param item    the item used to do the s-extension
     * @param support the support
     * @return a new episode that is the s-extension of this episode
     */
    public EpisodeMP sExtension(int item, int support) {
        List<int[]> newEvents = new ArrayList<int[]>(events);
        newEvents.add(new int[]{item});
        return new EpisodeMP(newEvents, support);
    }

}
