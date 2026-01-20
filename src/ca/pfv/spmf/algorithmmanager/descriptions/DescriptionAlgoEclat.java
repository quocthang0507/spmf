package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.frequentpatterns.eclat.AlgoEclat;
import ca.pfv.spmf.input.transaction_database_list_integers.TransactionDatabase;

import java.io.IOException;

/**
 * This class describes the Eclat algorithm parameters. It is designed to be
 * used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoEclat
 */
public class DescriptionAlgoEclat extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoEclat() {
    }

    @Override
    public String getName() {
        return "Eclat";
    }

    @Override
    public String getAlgorithmCategory() {
        return "FREQUENT ITEMSET MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/Eclat_dEclat.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        double minsup = getParamAsDouble(parameters[0]);

        // Loading the transaction database
        TransactionDatabase database = new TransactionDatabase();
        try {
            database.loadFile(inputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        AlgoEclat algo = new AlgoEclat();

        if (parameters.length >= 2 && "".equals(parameters[1]) == false) {
            algo.setShowTransactionIdentifiers(getParamAsBoolean(parameters[1]));
        }

        if (parameters.length >= 3 && "".equals(parameters[2]) == false) {
            algo.setMaximumPatternLength(getParamAsInteger(parameters[2]));
        }

        algo.runAlgorithm(outputFile, database, minsup, true);
        algo.printStats();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[3];
        parameters[0] = new DescriptionOfParameter("Minsup (%)", "(e.g. 0.4 or 40%)", Double.class, false);
        parameters[1] = new DescriptionOfParameter("Show transaction ids?", "(default: false)", Boolean.class, true);
        parameters[2] = new DescriptionOfParameter("Max pattern length", "(e.g. 2 items)", Integer.class, true);
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
