package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.frequentpatterns.ffi_miner.AlgoFFIMiner;
import ca.pfv.spmf.algorithms.frequentpatterns.mffi_miner.AlgoMFFIMiner;

import java.io.IOException;

/**
 * This class describes the MFFI-Miner algorithm parameters.
 * It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoFFIMiner
 */
public class DescriptionAlgoMFFIMiner extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoMFFIMiner() {
    }

    @Override
    public String getName() {
        return "MFFI-Miner";
    }

    @Override
    public String getAlgorithmCategory() {
        return "FREQUENT ITEMSET MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/MFFIMiner.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        float minSupport = getParamAsFloat(parameters[0]);

        // Applying the MFFI-Miner algorithm
        AlgoMFFIMiner MFFIminer = new AlgoMFFIMiner();
        MFFIminer.runAlgorithm(inputFile, outputFile, minSupport);
        MFFIminer.printStats();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[1];
        parameters[0] = new DescriptionOfParameter("Minimum support", "(e.g. 2)", Float.class, false);
        return parameters;
    }

    @Override
    public String getImplementationAuthorNames() {
        return "Ting Li";
    }

    @Override
    public String[] getInputFileTypes() {
        return new String[]{"Database of instances", "Transaction database", "Transaction database with utility values"};
    }

    @Override
    public String[] getOutputFileTypes() {
        return new String[]{"Patterns", "Frequent patterns", "Frequent itemsets", "Multiple Frequent fuzzy itemsets"};
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.DATA_MINING;
    }

}
