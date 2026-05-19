package ca.pfv.spmf.algorithms.frequentpatterns.cfhauopm;

import java.util.Locale;

/**
 * Pruning configurations for the CFHAUOPM algorithm.
 */
public enum CFHAUOPMPruningStrategy {
    /**
     * Use support, lauorho, and tauodelta pruning.
     */
    ALL("ALL", true, true),

    /**
     * Use support pruning and the anti-monotone lauorho upper bound.
     */
    SUPPORT_AND_LAUORHO("SUPPORT+LAUORHO", true, false),

    /**
     * Use only support pruning.
     */
    SUPPORT_ONLY("SUPPORT_ONLY", false, false);

    private final String label;
    private final boolean lauorhoPruningEnabled;
    private final boolean tauodeltaPruningEnabled;

    CFHAUOPMPruningStrategy(String label, boolean lauorhoPruningEnabled, boolean tauodeltaPruningEnabled) {
        this.label = label;
        this.lauorhoPruningEnabled = lauorhoPruningEnabled;
        this.tauodeltaPruningEnabled = tauodeltaPruningEnabled;
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
     * Check whether lauorho pruning is enabled.
     *
     * @return true if lauorho pruning is enabled
     */
    public boolean isLauorhoPruningEnabled() {
        return lauorhoPruningEnabled;
    }

    /**
     * Check whether tauodelta branch pruning is enabled.
     *
     * @return true if tauodelta pruning is enabled
     */
    public boolean isTauodeltaPruningEnabled() {
        return tauodeltaPruningEnabled;
    }

    /**
     * Convert a GUI/command-line parameter to a pruning strategy.
     *
     * @param value the parameter value
     * @return the corresponding strategy
     */
    public static CFHAUOPMPruningStrategy fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return ALL;
        }
        String normalized = value.trim().toUpperCase(Locale.ROOT).replace(' ', '_');
        for (CFHAUOPMPruningStrategy strategy : values()) {
            String labelValue = strategy.label.toUpperCase(Locale.ROOT).replace(' ', '_');
            if (strategy.name().equals(normalized) || labelValue.equals(normalized)) {
                return strategy;
            }
        }
        throw new IllegalArgumentException("Unknown CFHAUOPM pruning strategy: " + value);
    }

    /**
     * Get labels for GUI selection.
     *
     * @return the available labels
     */
    public static String[] labels() {
        CFHAUOPMPruningStrategy[] strategies = values();
        String[] labels = new String[strategies.length];
        for (int i = 0; i < strategies.length; i++) {
            labels[i] = strategies[i].getLabel();
        }
        return labels;
    }
}
