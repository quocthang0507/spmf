package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.frequentpatterns.krimp.AlgoKrimp;

import java.io.File;
import java.io.IOException;

/**
 * This class describes the KRIMP algorithm parameters.
 * It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoKrimp
 */
public class DescriptionAlgoKrimp extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoKrimp() {
    }

    @Override
    public String getName() {
        return "KRIMP";
    }

    @Override
    public String getAlgorithmCategory() {
        return "FREQUENT ITEMSET MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/MDL_KRIMP.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        String patternPath = getParamAsString(parameters[0]);

        // Applying the algorithm
        AlgoKrimp algorithm = new AlgoKrimp();

        File file = new File(inputFile);
        String fullPatternPath;
        if (file.getParent() == null) {
            fullPatternPath = patternPath;
        } else {
            fullPatternPath = file.getParent() + File.separator + patternPath;
        }

        algorithm.runAlgorithm(inputFile, fullPatternPath, outputFile);
        algorithm.printStats();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[1];
        parameters[0] = new DescriptionOfParameter("Pattern file", "(e.g. patterns60.txt)", String.class, false);
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
