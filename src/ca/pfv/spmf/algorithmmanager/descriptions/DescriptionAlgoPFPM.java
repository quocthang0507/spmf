package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.frequentpatterns.apriori.AlgoApriori;
import ca.pfv.spmf.algorithms.frequentpatterns.pfpm.AlgoPFPM;

import java.io.IOException;

/**
 * This class describes the Apriori algorithm parameters.
 * It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoApriori
 */
public class DescriptionAlgoPFPM extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoPFPM() {
    }

    @Override
    public String getName() {
        return "PFPM";
    }

    @Override
    public String getAlgorithmCategory() {
        return "PERIODIC PATTERN MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/PFPM.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        int minPeriodicity = getParamAsInteger(parameters[0]);  // transactions
        int maxPeriodicity = getParamAsInteger(parameters[1]);  // transactions
        int minAveragePeriodicity = getParamAsInteger(parameters[2]);  // transactions
        int maxAveragePeriodicity = getParamAsInteger(parameters[3]);  // transactions

        // Applying the algorithm
        AlgoPFPM algo = new AlgoPFPM();

        if (parameters.length >= 5 && "".equals(parameters[4]) == false) {
            algo.setMinimumLength(getParamAsInteger(parameters[4]));
        }

        if (parameters.length >= 6 && "".equals(parameters[5]) == false) {
            algo.setMaximumLength(getParamAsInteger(parameters[5]));
        }

        algo.runAlgorithm(inputFile, outputFile, minPeriodicity, maxPeriodicity, minAveragePeriodicity, maxAveragePeriodicity);
        algo.printStats();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[6];
        parameters[0] = new DescriptionOfParameter("Minimum periodicity", "(e.g. 1 transactions)", Integer.class, false);
        parameters[1] = new DescriptionOfParameter("Maximum periodicity", "(e.g. 3 transactions)", Integer.class, false);
        parameters[2] = new DescriptionOfParameter("Minimum average periodicity", "(e.g. 1 transactions)", Integer.class, false);
        parameters[3] = new DescriptionOfParameter("Maximum average periodicity", "(e.g. 2 transactions)", Integer.class, false);
        // optional parameters
        parameters[4] = new DescriptionOfParameter("Minimum number of items", "(e.g. 1 items)", Integer.class, true);
        parameters[5] = new DescriptionOfParameter("Maximum number of items", "(e.g. 5 items)", Integer.class, true);
        return parameters;

    }

    @Override
    public String getImplementationAuthorNames() {
        return "Philippe Fournier-Viger";
    }

    @Override
    public String[] getInputFileTypes() {
        return new String[]{"Database of instances", "Transaction database", "Simple transaction database"};
    }

    @Override
    public String[] getOutputFileTypes() {
        return new String[]{"Patterns", "Frequent patterns", "Periodic patterns", "Periodic frequent patterns", "Periodic frequent itemsets"};
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.DATA_MINING;
    }

}
