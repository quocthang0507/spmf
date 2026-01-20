package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.frequentpatterns.hui_miner.AlgoHUIMiner;
import ca.pfv.spmf.algorithms.frequentpatterns.lcim.AlgoLCIM;

import java.io.IOException;

/**
 * This class describes the LCIM algorithm parameters.
 * It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoHUIMiner
 */
public class DescriptionAlgoLCIM extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoLCIM() {
    }

    @Override
    public String getName() {
        return "LCIM";
    }

    @Override
    public String getAlgorithmCategory() {
        return "HIGH-UTILITY PATTERN MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/LCIM_COST.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        double minutil = getParamAsDouble(parameters[0]);
        double maxcost = getParamAsDouble(parameters[1]);
        double minsup = getParamAsDouble(parameters[2]);

        // Applying the algorithm
        AlgoLCIM algo = new AlgoLCIM();
        algo.runAlgorithm(inputFile, outputFile, minutil, maxcost, minsup);
        algo.printStats();

    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[3];
        parameters[0] = new DescriptionOfParameter("Minimum utility", "(e.g. 10)", Double.class, false);
        parameters[1] = new DescriptionOfParameter("Maximum cost", "(e.g. 10)", Double.class, false);
        parameters[2] = new DescriptionOfParameter("Minimum support", "(e.g. 0..3)", Double.class, false);

        return parameters;
    }

    @Override
    public String getImplementationAuthorNames() {
        return "Philippe Fournier-Viger";
    }

    @Override
    public String[] getInputFileTypes() {
        return new String[]{"Database of instances", "Transaction database", "Transaction database with utility and cost  values"};
    }

    @Override
    public String[] getOutputFileTypes() {
        return new String[]{"Patterns", "High-utility patterns", "High-utility itemsets"};
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.DATA_MINING;
    }
}
