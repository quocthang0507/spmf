package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.frequentpatterns.cfhauopm.AlgoCFHAUOPM;
import ca.pfv.spmf.algorithms.frequentpatterns.cfhauopm.CFHAUOPMPruningStrategy;

/**
 * This class describes the CFHAUOPM algorithm parameters for the graphical and
 * command line interfaces.
 *
 * @author OpenAI
 * @see AlgoCFHAUOPM
 */
public class DescriptionAlgoCFHAUOPM extends DescriptionOfAlgorithm {

    /**
     * Default constructor.
     */
    public DescriptionAlgoCFHAUOPM() {
    }

    @Override
    public String getName() {
        return "CFHAUOPM";
    }

    @Override
    public String getAlgorithmCategory() {
        return "HIGH-UTILITY PATTERN MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/CFHAUOPM.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws Exception {
        double minSupport = getParamAsDouble(parameters[0]);
        double minAverageUtilityOccupancy = getParamAsDouble(parameters[1]);
        CFHAUOPMPruningStrategy pruningStrategy = CFHAUOPMPruningStrategy.ALL;
        if (parameters.length > 2) {
            pruningStrategy = CFHAUOPMPruningStrategy.fromString(parameters[2]);
        }

        AlgoCFHAUOPM algorithm = new AlgoCFHAUOPM();
        algorithm.runAlgorithm(inputFile, outputFile, minSupport, minAverageUtilityOccupancy, pruningStrategy);
        algorithm.printStats();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {
        DescriptionOfParameter[] parameters = new DescriptionOfParameter[3];
        parameters[0] = new DescriptionOfParameter("Minimum support", "(e.g. 2 or 30%)", Double.class, false);
        parameters[1] = new DescriptionOfParameter("Minimum average utility occupancy", "(e.g. 0.10)",
                Double.class, false);
        parameters[2] = new DescriptionOfParameter("Pruning strategy", "(e.g. ALL)", String.class, false,
                CFHAUOPMPruningStrategy.labels());
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
        return new String[]{"Patterns", "Closed patterns",
                "Closed frequent high average utility occupancy patterns"};
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.DATA_MINING;
    }
}
