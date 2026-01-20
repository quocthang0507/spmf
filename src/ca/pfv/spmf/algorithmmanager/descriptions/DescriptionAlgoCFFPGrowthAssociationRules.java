package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.frequentpatterns.cfpgrowth.AlgoCFPGrowth;

import java.io.File;
import java.io.IOException;

/**
 * This class describes parameters of the algorithm for generating association rules
 * with the CFP-Growth algorithm.
 * It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoCFPGrowth, AlgoAgrawalFaster94
 */
public class DescriptionAlgoCFFPGrowthAssociationRules extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoCFFPGrowthAssociationRules() {
    }

    @Override
    public String getName() {
        return "CFPGrowth++_association_rules";
    }

    @Override
    public String getAlgorithmCategory() {
        return "ASSOCIATION RULE MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/AssociationRules.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        String misFile = parameters[0];
        double minconf = getParamAsDouble(parameters[1]);

        File file = new File(inputFile);
        String misFileFullPath;
        if (file.getParent() == null) {
            misFileFullPath = misFile;
        } else {
            misFileFullPath = file.getParent() + File.separator + misFile;
        }

        AlgoCFPGrowth cfpgrowth = new AlgoCFPGrowth();
        ca.pfv.spmf.patterns.itemset_array_integers_with_count.Itemsets patterns = cfpgrowth
                .runAlgorithm(inputFile, null, misFileFullPath);
        cfpgrowth.printStats();
        int databaseSize = cfpgrowth.getDatabaseSize();

        // STEP 2: Generating all rules from the set of frequent itemsets
        // (based on Agrawal & Srikant, 94)
        ca.pfv.spmf.algorithms.associationrules.agrawal94_association_rules.AlgoAgrawalFaster94 algoAgrawal = new ca.pfv.spmf.algorithms.associationrules.agrawal94_association_rules.AlgoAgrawalFaster94();
        algoAgrawal.runAlgorithm(patterns, outputFile, databaseSize,
                minconf);
        algoAgrawal.printStats();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[2];
        parameters[0] = new DescriptionOfParameter("MIS file name", "(e.g. MIS.txt)", String.class, false);
        parameters[1] = new DescriptionOfParameter("Minimum confidence (%)", "(e.g. 0.6 or 60%)", Double.class, false);
        return parameters;
    }

    @Override
    public String getImplementationAuthorNames() {
        return "Azadeh Soltani, Philippe Fournier-Viger";
    }

    @Override
    public String[] getInputFileTypes() {
        return new String[]{"Database of instances", "Transaction database", "Simple transaction database"};
    }

    @Override
    public String[] getOutputFileTypes() {
        return new String[]{"Patterns", "Association rules", "Association rules with lift and multiple support thresholds"};
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.DATA_MINING;
    }
}
