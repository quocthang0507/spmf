package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.frequentpatterns.skymine.AlgoSkyMine;

import java.io.File;
import java.io.IOException;

/**
 * This class describes the SkyMine algorithm parameters.
 * It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoSkyMine
 */
public class DescriptionAlgoSkyMine extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoSkyMine() {
    }

    @Override
    public String getName() {
        return "SkyMine";
    }

    @Override
    public String getAlgorithmCategory() {
        return "HIGH-UTILITY PATTERN MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/SkyMine.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        // file for sensitive
        String itemUtilities = parameters[0];

        File file = new File(inputFile);
        String itemUtilitiesPath;
        if (file.getParent() == null) {
            itemUtilitiesPath = itemUtilities;
        } else {
            itemUtilitiesPath = file.getParent() + File.separator + itemUtilities;
        }

        // Create an instance of the algorithm
        AlgoSkyMine up = new AlgoSkyMine();
        up.runAlgorithm(inputFile, itemUtilitiesPath, outputFile, true, true);
        // print statistics about the algorithm execution
        up.printStats();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[1];
        parameters[0] = new DescriptionOfParameter("Utility file name", "(e.g. SkyMineItemUtilities.txt)", String.class, false);
        return parameters;
    }

    @Override
    public String getImplementationAuthorNames() {
        return "Vikram Goyal, Ashish Sureka, Dhaval Patel, Siddharth Dawar";
    }

    @Override
    public String[] getInputFileTypes() {
        return new String[]{"Database of instances", "Transaction database", "Transaction database with utility values skymine format"};
    }

    @Override
    public String[] getOutputFileTypes() {
        return new String[]{"Patterns", "Skyline patterns", "High-utility patterns", "Skyline High-utility itemsets"};
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.DATA_MINING;
    }

}
