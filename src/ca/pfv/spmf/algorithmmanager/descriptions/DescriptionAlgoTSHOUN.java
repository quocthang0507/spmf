package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.frequentpatterns.tshoun.AlgoTSHoun;
import ca.pfv.spmf.algorithms.frequentpatterns.tshoun.DatabaseWithPeriods;

/**
 * This class describes the TS-HOUN algorithm parameters.
 * It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoTSHoun
 */
public class DescriptionAlgoTSHOUN extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoTSHOUN() {
    }

    @Override
    public String getName() {
        return "TS-HOUN";
    }

    @Override
    public String getAlgorithmCategory() {
        return "HIGH-UTILITY PATTERN MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/TS-HOUN.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws Exception {
        double min_utility_ratio = getParamAsDouble(parameters[0]);
        int periodCount = getParamAsInteger(parameters[1]);
        // Loading the database into memory
        DatabaseWithPeriods database = new DatabaseWithPeriods(periodCount);
        database.loadFile(inputFile);

        // Applying the algorithm
        AlgoTSHoun algo = new AlgoTSHoun();
        algo.runAlgorithm(database, min_utility_ratio, outputFile, periodCount);
        algo.printStats();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[2];
        parameters[0] = new DescriptionOfParameter("Minimum utility ratio", "(e.g. 0.8)", Double.class, false);
        parameters[1] = new DescriptionOfParameter("Period count", "(e.g. 3)", Double.class, false);
        return parameters;
    }

    @Override
    public String getImplementationAuthorNames() {
        return "Philippe Fournier-Viger";
    }

    @Override
    public String[] getInputFileTypes() {
        return new String[]{"Database of instances", "Transaction database", "Transaction database with shelf-time periods and utility values"};
    }

    @Override
    public String[] getOutputFileTypes() {
        return new String[]{"Patterns", "High-utility patterns", "On-shelf high-utility itemsets"};
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.DATA_MINING;
    }

}
