package ca.pfv.spmf.algorithms.frequentpatterns.hauopm;

import java.util.Locale;

/**
 * Pruning configurations for the HAUOPM algorithm.
 */
public enum HAUOPMPruningStrategy {
    /**
     * Use support pruning, AUOUB pruning, and empty-list pruning.
     */
    ALL("ALL", true, true),

    /**
     * Use only support pruning.
     */
    SUPPORT_ONLY("SUPPORT_ONLY", true, false),

    /**
     * Use support pruning and the average utility occupancy upper bound.
     */
    SUPPORT_AND_AUOUB("SUPPORT+AUOUB", true, true),

    /**
     * Disable optional pruning. Correctness checks are still applied before output.
     */
    NONE("NONE", false, false);

    private final String label;
    private final boolean supportPruningEnabled;
    private final boolean upperBoundPruningEnabled;

    HAUOPMPruningStrategy(String label, boolean supportPruningEnabled, boolean upperBoundPruningEnabled) {
        this.label = label;
        this.supportPruningEnabled = supportPruningEnabled;
        this.upperBoundPruningEnabled = upperBoundPruningEnabled;
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
     * Check whether average utility occupancy upper-bound pruning is enabled.
     *
     * @return true if upper-bound pruning is enabled
     */
    public boolean isUpperBoundPruningEnabled() {
        return upperBoundPruningEnabled;
    }

    /**
     * Convert a GUI/command-line parameter to a pruning strategy.
     *
     * @param value the parameter value
     * @return the corresponding strategy
     */
    public static HAUOPMPruningStrategy fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return ALL;
        }
        String normalized = value.trim().toUpperCase(Locale.ROOT).replace(' ', '_');
        for (HAUOPMPruningStrategy strategy : values()) {
            String labelValue = strategy.label.toUpperCase(Locale.ROOT).replace(' ', '_');
            if (strategy.name().equals(normalized) || labelValue.equals(normalized)) {
                return strategy;
            }
        }
        throw new IllegalArgumentException("Unknown HAUOPM pruning strategy: " + value);
    }

    /**
     * Get labels for GUI selection.
     *
     * @return the available labels
     */
    public static String[] labels() {
        HAUOPMPruningStrategy[] strategies = values();
        String[] labels = new String[strategies.length];
        for (int i = 0; i < strategies.length; i++) {
            labels[i] = strategies[i].getLabel();
        }
        return labels;
    }
}
