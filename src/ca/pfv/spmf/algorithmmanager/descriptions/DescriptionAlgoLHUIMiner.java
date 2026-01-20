package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.frequentpatterns.lhui.AlgoLHUIMiner;

import java.io.IOException;

/**
 * This class describes the LHUI-Miner algorithm parameters.
 * It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoLHUIMiner
 */
public class DescriptionAlgoLHUIMiner extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoLHUIMiner() {
    }

    @Override
    public String getName() {
        return "LHUI-Miner";
    }

    @Override
    public String getAlgorithmCategory() {
        return "HIGH-UTILITY PATTERN MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/LHUIMiner.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        int lminutil = getParamAsInteger(parameters[0]);

        // Window size
        int windowSize = getParamAsInteger(parameters[1]);

        AlgoLHUIMiner lhuiminer = new AlgoLHUIMiner();
        lhuiminer.runAlgorithm(inputFile, outputFile, lminutil, windowSize);
        lhuiminer.printStats();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[2];
        parameters[0] = new DescriptionOfParameter("Minimum utility", "(e.g. 40)", Long.class, false);
        parameters[1] = new DescriptionOfParameter("Window size", "(e.g. 3)", Integer.class, false);
        return parameters;
    }

    @Override
    public String getImplementationAuthorNames() {
        return "Yimin Zhang, Philippe Fournier-Viger";
    }

    @Override
    public String[] getInputFileTypes() {
        return new String[]{"Database of instances", "Transaction database", "Transaction database with utility values and time"};
    }

    @Override
    public String[] getOutputFileTypes() {
        return new String[]{"Patterns", "High-utility patterns", "Local high-utility itemsets"};
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.DATA_MINING;
    }
}
