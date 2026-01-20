package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.frequentpatterns.slim.AlgoSLIM;

import java.io.IOException;

/**
 * This class describes the SLIM algorithm parameters.
 * It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoSLIM
 */
public class DescriptionAlgoSLIM extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoSLIM() {
    }

    @Override
    public String getName() {
        return "SLIM";
    }

    @Override
    public String getAlgorithmCategory() {
        return "FREQUENT ITEMSET MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/SLIM_compress.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        // Applying the SLIM algorithm,
        AlgoSLIM algorithm = new AlgoSLIM();

        if (parameters.length >= 1 && "".equals(parameters[0]) == false) {
            algorithm.setMaxIteration(getParamAsInteger(parameters[0]));
        }

        algorithm.runAlgorithm(inputFile, outputFile);
        algorithm.printStats();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[1];
        parameters[0] = new DescriptionOfParameter("Max iteration count", "(e.g. 500)", Integer.class, true);

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
        return new String[]{"Patterns", "Frequent patterns", "Frequent itemsets"};
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.DATA_MINING;
    }

}
