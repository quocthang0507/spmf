package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.frequentpatterns.apriori.AlgoApriori;
import ca.pfv.spmf.algorithms.frequentpatterns.cori.AlgoCORI;
import ca.pfv.spmf.input.transaction_database_list_integers.TransactionDatabase;

import java.io.IOException;

/**
 * This class describes the Apriori algorithm parameters. It is designed to be
 * used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoApriori
 */
public class DescriptionAlgoCORI extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoCORI() {
    }

    @Override
    public String getName() {
        return "CORI";
    }

    @Override
    public String getAlgorithmCategory() {
        return "FREQUENT ITEMSET MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/CORI.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        double minsup = getParamAsDouble(parameters[0]);
        double minbond = getParamAsDouble(parameters[1]);

        // Loading the transaction database
        TransactionDatabase database = new TransactionDatabase();
        try {
            database.loadFile(inputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        AlgoCORI algo = new AlgoCORI();

        if (parameters.length >= 3 && "".equals(parameters[2]) == false) {
            algo.setShowTransactionIdentifiers(getParamAsBoolean(parameters[2]));
        }

        if (parameters.length >= 4 && "".equals(parameters[3]) == false) {
            algo.setMaximumPatternLength(getParamAsInteger(parameters[3]));
        }

        algo.runAlgorithm(outputFile, database, minsup, minbond, false);
        algo.printStats();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[4];
        parameters[0] = new DescriptionOfParameter("Maxsup (%)", "(e.g. 0.8 or 80%)", Double.class, false);
        parameters[1] = new DescriptionOfParameter("Minbond (%)", "(e.g. 0.2 or 20%)", Double.class, false);
        parameters[2] = new DescriptionOfParameter("Show transaction ids?", "(default: false)", Boolean.class, true);
        parameters[3] = new DescriptionOfParameter("Max pattern length", "(e.g. 2 items)", Integer.class, true);
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
        return new String[]{"Patterns", "Rare patterns", "Correlated patterns", "Rare itemsets",
                "Correlated itemsets", "Rare correlated itemsets"};
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.DATA_MINING;
    }
}
