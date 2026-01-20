package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.frequentpatterns.two_phase.AlgoHUINIVMine;

import java.io.IOException;

/**
 * This class describes the HUINIV-Mine algorithm parameters.
 * It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoHUINIVMine
 */
public class DescriptionAlgoHUINIVMine extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoHUINIVMine() {
    }

    @Override
    public String getName() {
        return "HUINIV-Mine";
    }

    @Override
    public String getAlgorithmCategory() {
        return "HIGH-UTILITY PATTERN MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/HUINIVMine.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        int minutil = getParamAsInteger(parameters[0]);
        ca.pfv.spmf.input.utility_transaction_database.UtilityTransactionDatabaseTP database = new ca.pfv.spmf.input.utility_transaction_database.UtilityTransactionDatabaseTP();
        database.loadFile(inputFile);

        // Applying the Two-Phase algorithm
        AlgoHUINIVMine algo = new AlgoHUINIVMine();
        ca.pfv.spmf.algorithms.frequentpatterns.two_phase.ItemsetsTP highUtilityItemsets = algo
                .runAlgorithm(database, minutil);

        highUtilityItemsets.saveResultsToFile(outputFile, database
                .getTransactions().size());

        algo.printStats();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[1];
        parameters[0] = new DescriptionOfParameter("Minimum utility", "(e.g. 30)", Integer.class, false);
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
        return new String[]{"Patterns", "High-utility patterns", "High-utility itemsets"};
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.DATA_MINING;
    }
}
