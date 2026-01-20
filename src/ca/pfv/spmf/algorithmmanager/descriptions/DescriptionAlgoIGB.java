package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.associationrules.IGB.AlgoIGB;
import ca.pfv.spmf.algorithms.frequentpatterns.zart.AlgoZart;
import ca.pfv.spmf.algorithms.frequentpatterns.zart.TZTableClosed;
import ca.pfv.spmf.input.transaction_database_list_integers.TransactionDatabase;

import java.io.IOException;

/**
 * This class describes parameters of the algorithm for generating association rules
 * with the IGB algorithm.
 * It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoIGB
 */
public class DescriptionAlgoIGB extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoIGB() {
    }

    @Override
    public String getName() {
        return "IGB";
    }

    @Override
    public String getAlgorithmCategory() {
        return "ASSOCIATION RULE MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://philippe-fournier-viger.com/spmf/IGBAssociationRules.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        double minsup = getParamAsDouble(parameters[0]);
        double minconf = getParamAsDouble(parameters[1]);

        TransactionDatabase database = new TransactionDatabase();
        try {
            database.loadFile(inputFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Applying the Zart algorithm
        AlgoZart zart = new AlgoZart();
        TZTableClosed results = zart.runAlgorithm(database, minsup);
        zart.printStatistics();

        System.out.println("STEP 2 : RUNNING THE IGB ALGORITHM");
        // Apply the IGB algorithm
        AlgoIGB algoIGB = new AlgoIGB();
        algoIGB.runAlgorithm(results, database.getTransactions().size(), minconf, outputFile);
        algoIGB.printStatistics();

//		AlgoIGB apriori = new AlgoIGB();
//		ca.pfv.spmf.patterns.itemset_array_integers_with_count.Itemsets patterns = apriori
//				.runAlgorithm(minsup, inputFile, null);
//		apriori.printStats();
//		int databaseSize = apriori.getDatabaseSize();
//
//		// STEP 2: Generating all rules from the set of frequent itemsets
//		// (based on Agrawal & Srikant, 94)
//		ca.pfv.spmf.algorithms.associationrules.agrawal94_association_rules.AlgoAgrawalFaster94 algoAgrawal = new ca.pfv.spmf.algorithms.associationrules.agrawal94_association_rules.AlgoAgrawalFaster94();
//		algoAgrawal.runAlgorithm(patterns, outputFile, databaseSize,
//				minconf);
//		algoAgrawal.printStats();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[2];
        parameters[0] = new DescriptionOfParameter("Minsup (%)", "(e.g. 0.5 or 50%)", Double.class, false);
        parameters[1] = new DescriptionOfParameter("Minconf (%)", "(e.g. 0.61 or 61%)", Double.class, false);
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
        return new String[]{"Patterns", "Association rules"};
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.DATA_MINING;
    }
}
