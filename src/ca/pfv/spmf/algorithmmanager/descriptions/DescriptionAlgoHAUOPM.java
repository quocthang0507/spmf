package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.frequentpatterns.hauopm.AlgoHAUOPM;
import ca.pfv.spmf.algorithms.frequentpatterns.hauopm.HAUOPMPruningStrategy;

/**
 * This class describes the HAUOPM algorithm parameters for the graphical and
 * command line interfaces.
 *
 * @author OpenAI
 * @see AlgoHAUOPM
 */
public class DescriptionAlgoHAUOPM extends DescriptionOfAlgorithm {

    /**
     * Default constructor.
     */
    public DescriptionAlgoHAUOPM() {
    }

    @Override
    public String getName() {
        return "HAUOPM";
    }

    @Override
    public String getAlgorithmCategory() {
        return "HIGH-UTILITY PATTERN MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/HAUOPM.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws Exception {
        double minSupport = getParamAsDouble(parameters[0]);
        double minOccupancy = getParamAsDouble(parameters[1]);
        HAUOPMPruningStrategy pruningStrategy = HAUOPMPruningStrategy.ALL;
        if (parameters.length > 2) {
            pruningStrategy = HAUOPMPruningStrategy.fromString(parameters[2]);
        }

        AlgoHAUOPM algorithm = new AlgoHAUOPM();
        algorithm.runAlgorithm(inputFile, outputFile, minSupport, minOccupancy, pruningStrategy);
        algorithm.printStats();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {
        DescriptionOfParameter[] parameters = new DescriptionOfParameter[3];
        parameters[0] = new DescriptionOfParameter("Minimum support", "(e.g. 2 or 30%)", Double.class, false);
        parameters[1] = new DescriptionOfParameter("Minimum average transaction utility occupancy", "(e.g. 0.10)",
                Double.class, false);
        parameters[2] = new DescriptionOfParameter("Pruning strategy", "(e.g. ALL)", String.class, false,
                HAUOPMPruningStrategy.labels());
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
        return new String[]{"Patterns", "High-utility patterns", "High average utility occupancy patterns"};
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.DATA_MINING;
    }
}
