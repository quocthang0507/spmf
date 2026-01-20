package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.frequentpatterns.tkuce.AlgoTKUCE;

import java.io.IOException;

/**
 * This class describes the TKU-CE algorithm parameters.
 * It is designed to be used by the graphical and command line interface.
 *
 * @author Song et al.
 * @see AlgoTKUCE
 */
public class DescriptionAlgoTKUCE extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoTKUCE() {
    }

    @Override
    public String getName() {
        return "TKU-CE";
    }

    @Override
    public String getAlgorithmCategory() {
        return "HIGH-UTILITY PATTERN MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/TKUCE_heuristic.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        int k = getParamAsInteger(parameters[0]);
        // Applying the algorithm
        AlgoTKUCE algo = new AlgoTKUCE();
        algo.runAlgorithm(inputFile, outputFile, k);
        algo.printStats();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[1];
        parameters[0] = new DescriptionOfParameter("k", "(e.g. 3)", Integer.class, false);
        return parameters;
    }

    @Override
    public String getImplementationAuthorNames() {
        return "Song et al.";
    }

    @Override
    public String[] getInputFileTypes() {
        return new String[]{"Database of instances", "Transaction database", "Transaction database with utility values"};
    }

    @Override
    public String[] getOutputFileTypes() {
        return new String[]{"Patterns", "High-utility patterns", "High-utility itemsets", "Top-k High-utility itemsets"};
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.DATA_MINING;
    }

}
