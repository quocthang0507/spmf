package ca.pfv.spmf.algorithms.frequentpatterns.huopm;

import java.util.Locale;

/**
 * Pruning configurations for the HUOPM algorithm.
 */
public enum HUOPMPruningStrategy {
    /**
     * Use all pruning strategies described for HUOPM.
     */
    ALL("ALL", true, true, true),

    /**
     * Use only support-based pruning.
     */
    SUPPORT_ONLY("SUPPORT_ONLY", true, false, false),

    /**
     * Use support pruning and the utility-occupancy upper bound.
     */
    SUPPORT_AND_UPPER_BOUND("SUPPORT+UPPER_BOUND", true, true, false),

    /**
     * Use support pruning and the remaining-support pruning applied during joins.
     */
    SUPPORT_AND_REMAINING_SUPPORT("SUPPORT+REMAINING_SUPPORT", true, false, true),

    /**
     * Disable optional pruning. Correctness checks are still applied before output.
     */
    NONE("NONE", false, false, false);

    private final String label;
    private final boolean supportPruningEnabled;
    private final boolean upperBoundPruningEnabled;
    private final boolean remainingSupportPruningEnabled;

    HUOPMPruningStrategy(String label, boolean supportPruningEnabled,
                         boolean upperBoundPruningEnabled, boolean remainingSupportPruningEnabled) {
        this.label = label;
        this.supportPruningEnabled = supportPruningEnabled;
        this.upperBoundPruningEnabled = upperBoundPruningEnabled;
        this.remainingSupportPruningEnabled = remainingSupportPruningEnabled;
    }

    /**
     * Get the label displayed by the GUI.
     *
     * @return the display label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Check whether support-based pruning is enabled.
     *
     * @return true if support pruning is enabled
     */
    public boolean isSupportPruningEnabled() {
        return supportPruningEnabled;
    }

    /**
     * Check whether utility-occupancy upper-bound pruning is enabled.
     *
     * @return true if upper-bound pruning is enabled
     */
    public boolean isUpperBoundPruningEnabled() {
        return upperBoundPruningEnabled;
    }

    /**
     * Check whether remaining-support pruning is enabled during UO-list joins.
     *
     * @return true if remaining-support pruning is enabled
     */
    public boolean isRemainingSupportPruningEnabled() {
        return remainingSupportPruningEnabled;
    }

    /**
     * Convert a GUI/command-line parameter to a pruning strategy.
     *
     * @param value the parameter value
     * @return the corresponding strategy
     */
    public static HUOPMPruningStrategy fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return ALL;
        }
        String normalized = value.trim().toUpperCase(Locale.ROOT).replace(' ', '_');
        for (HUOPMPruningStrategy strategy : values()) {
            String labelValue = strategy.label.toUpperCase(Locale.ROOT).replace(' ', '_');
            if (strategy.name().equals(normalized) || labelValue.equals(normalized)) {
                return strategy;
            }
        }
        throw new IllegalArgumentException("Unknown HUOPM pruning strategy: " + value);
    }

    /**
     * Get labels for GUI selection.
     *
     * @return the available labels
     */
    public static String[] labels() {
        HUOPMPruningStrategy[] strategies = values();
        String[] labels = new String[strategies.length];
        for (int i = 0; i < strategies.length; i++) {
            labels[i] = strategies[i].getLabel();
        }
        return labels;
    }
}
