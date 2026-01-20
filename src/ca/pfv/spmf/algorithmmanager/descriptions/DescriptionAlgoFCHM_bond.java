package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.frequentpatterns.hui_miner.AlgoFCHM_bond;

import java.io.IOException;

/**
 * This class describes the FCHM algorithm parameters.
 * It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoFCHM_bond
 */
public class DescriptionAlgoFCHM_bond extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoFCHM_bond() {
    }

    @Override
    public String getName() {
        return "FCHM_bond";
    }

    @Override
    public String getAlgorithmCategory() {
        return "HIGH-UTILITY PATTERN MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/FCHMbond.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        int minutil = getParamAsInteger(parameters[0]);
        double minbond = getParamAsDouble(parameters[1]);
        // Applying the algorithm
        AlgoFCHM_bond algo = new AlgoFCHM_bond();
        algo.runAlgorithm(inputFile, outputFile, minutil, minbond);
        algo.printStats();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[2];
        parameters[0] = new DescriptionOfParameter("Minimum utility", "(e.g. 30)", Integer.class, false);
        parameters[1] = new DescriptionOfParameter("Minimum bond", "(e.g. 0.5)", Double.class, false);
        return parameters;
    }

    @Override
    public String getImplementationAuthorNames() {
        return "Philippe Fournier-Viger";
    }

    @Override
    public String[] getInputFileTypes() {
        return new String[]{"Database of instances", "Transaction database", "Transaction database with utility values"};
    }

    @Override
    public String[] getOutputFileTypes() {
        return new String[]{"Patterns", "Correlated patterns", "High-utility patterns", "High-utility itemsets", "Correlated High-utility itemsets"};
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.DATA_MINING;
    }
}
