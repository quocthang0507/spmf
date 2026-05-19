package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.frequentpatterns.huopm.AlgoHUOPM;
import ca.pfv.spmf.algorithms.frequentpatterns.huopm.HUOPMPruningStrategy;

/**
 * This class describes the HUOPM algorithm parameters for the graphical and
 * command line interfaces.
 *
 * @author OpenAI
 * @see AlgoHUOPM
 */
public class DescriptionAlgoHUOPM extends DescriptionOfAlgorithm {

    /**
     * Default constructor.
     */
    public DescriptionAlgoHUOPM() {
    }

    @Override
    public String getName() {
        return "HUOPM";
    }

    @Override
    public String getAlgorithmCategory() {
        return "HIGH-UTILITY PATTERN MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/HUOPM.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws Exception {
        double minSupport = getParamAsDouble(parameters[0]);
        double minUtilityOccupancy = getParamAsDouble(parameters[1]);
        HUOPMPruningStrategy pruningStrategy = HUOPMPruningStrategy.ALL;
        if (parameters.length > 2) {
            pruningStrategy = HUOPMPruningStrategy.fromString(parameters[2]);
        }

        AlgoHUOPM algorithm = new AlgoHUOPM();
        algorithm.runAlgorithm(inputFile, outputFile, minSupport, minUtilityOccupancy, pruningStrategy);
        algorithm.printStats();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {
        DescriptionOfParameter[] parameters = new DescriptionOfParameter[3];
        parameters[0] = new DescriptionOfParameter("Minimum support", "(e.g. 30% or 3)", Double.class, false);
        parameters[1] = new DescriptionOfParameter("Minimum utility occupancy", "(e.g. 0.30)", Double.class, false);
        parameters[2] = new DescriptionOfParameter("Pruning strategy", "(e.g. ALL)", String.class, false,
                HUOPMPruningStrategy.labels());
        return parameters;
    }

    @Override
    public String getImplementationAuthorNames() {
        return "OpenAI";
    }

    @Override
    public String[] getInputFileTypes() {
        return new String[]{"Database of instances", "Transaction database",
                "Transaction database with utility values"};
    }

    @Override
    public String[] getOutputFileTypes() {
        return new String[]{"Patterns", "High-utility patterns", "High-utility occupancy patterns"};
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.DATA_MINING;
    }
}
