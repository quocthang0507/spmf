package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.sequential_rules.rulegrowth.AlgoRULEGROWTH;

import java.io.IOException;

/**
 * This class describes the RuleGrowth algorithm parameters.
 * It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoRULEGROWTH
 */
public class DescriptionAlgoRuleGrowth extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoRuleGrowth() {
    }

    @Override
    public String getName() {
        return "RuleGrowth";
    }

    @Override
    public String getAlgorithmCategory() {
        return "SEQUENTIAL RULE MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/RuleGrowth.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        double minsup = getParamAsDouble(parameters[0]);
        double minconf = getParamAsDouble(parameters[1]);
        AlgoRULEGROWTH algo = new AlgoRULEGROWTH();
        if (parameters.length >= 3 && "".equals(parameters[2]) == false) {
            algo.setMaxAntecedentSize(getParamAsInteger(parameters[2]));
        }
        if (parameters.length >= 4 && "".equals(parameters[3]) == false) {
            algo.setMaxConsequentSize(getParamAsInteger(parameters[3]));
        }
        algo.runAlgorithm(minsup, minconf, inputFile, outputFile);
        algo.printStats();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[4];
        parameters[0] = new DescriptionOfParameter("Minsup (%)", "(e.g. 0.5 or 50%)", Double.class, false);
        parameters[1] = new DescriptionOfParameter("Minconf (%)", "(e.g. 0.6 or 60%)", Double.class, false);
        parameters[2] = new DescriptionOfParameter("Max antecedent size", "(e.g. 1 items)", Integer.class, true);
        parameters[3] = new DescriptionOfParameter("Max consequent size", "(e.g. 2 items)", Integer.class, true);
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
        return new String[]{"Patterns", "Sequential rules", "Frequent sequential rules"};
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.DATA_MINING;
    }
}
