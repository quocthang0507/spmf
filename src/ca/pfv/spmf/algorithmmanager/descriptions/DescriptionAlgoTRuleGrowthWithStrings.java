package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.sequential_rules.trulegrowth.AlgoTRuleGrowth;
import ca.pfv.spmf.algorithms.sequential_rules.trulegrowth_with_strings.AlgoTRuleGrowth_withStrings;

import java.io.IOException;

/**
 * This class describes the TRuleGrowth algorithm parameters with strings.
 * It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoTRuleGrowth
 */
public class DescriptionAlgoTRuleGrowthWithStrings extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoTRuleGrowthWithStrings() {
    }

    @Override
    public String getName() {
        return "TRuleGrowth_with_strings";
    }

    @Override
    public String getAlgorithmCategory() {
        return "SEQUENTIAL RULE MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/TRuleGrowth.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        double minsup = getParamAsDouble(parameters[0]);
        double minconf = getParamAsDouble(parameters[1]);
        int window = getParamAsInteger(parameters[2]);

        AlgoTRuleGrowth_withStrings algo = new AlgoTRuleGrowth_withStrings();
        algo.runAlgorithm(minsup, minconf, inputFile, outputFile, window);
        algo.printStats();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[5];
        parameters[0] = new DescriptionOfParameter("Minsup (%)", "(e.g. 0.7 or 70%)", Double.class, false);
        parameters[1] = new DescriptionOfParameter("Minconf (%)", "(e.g. 0.8 or 80%)", Double.class, false);
        parameters[2] = new DescriptionOfParameter("Window size", "(e.g. 3)", Integer.class, false);
        parameters[3] = new DescriptionOfParameter("Max antecedent size", "(e.g. 1 items)", Integer.class, true);
        parameters[4] = new DescriptionOfParameter("Max consequent size", "(e.g. 2 items)", Integer.class, true);
        return parameters;
    }

    @Override
    public String getImplementationAuthorNames() {
        return "Philippe Fournier-Viger";
    }

    @Override
    public String[] getInputFileTypes() {
        return new String[]{"Database of instances", "Sequence database", "Simple sequence database"};
    }

    @Override
    public String[] getOutputFileTypes() {
        return new String[]{"Patterns", "Sequential rules", "Frequent sequential rules with strings"};
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.DATA_MINING;
    }

}
