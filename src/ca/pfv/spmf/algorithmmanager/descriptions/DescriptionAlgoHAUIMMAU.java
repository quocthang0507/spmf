package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.frequentpatterns.haui_mmau.AlgoHAUIMMAU;
import ca.pfv.spmf.algorithms.frequentpatterns.haui_mmau.ItemsetsTP;
import ca.pfv.spmf.algorithms.frequentpatterns.haui_mmau.UtilityTransactionDatabaseTP;

import java.io.IOException;

/**
 * This class describes the HAUI-MMAU algorithm parameters.
 * It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoHAUIMMAU
 */
public class DescriptionAlgoHAUIMMAU extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoHAUIMMAU() {
    }

    @Override
    public String getName() {
        return "HAUI-MMAU";
    }

    @Override
    public String getAlgorithmCategory() {
        return "HIGH-UTILITY PATTERN MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/HAUI-MMAU.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        int GLMAU = getParamAsInteger(parameters[0]);
        String minutilityPath = getParamAsString(parameters[1]);

        // Loading the database into memory
        UtilityTransactionDatabaseTP database = new UtilityTransactionDatabaseTP();
        database.loadFile(inputFile, minutilityPath);
        //database.printDatabase();

        //Applying the HAUIMMAU algorithm
        AlgoHAUIMMAU HAUIMMAU = new AlgoHAUIMMAU();
        ItemsetsTP highAUtilityItemsets = HAUIMMAU.runAlgorithm(database, database.mutipleMinUtilities, GLMAU);
        highAUtilityItemsets.saveResultsToFile(outputFile, database.size(), GLMAU);
        HAUIMMAU.printStats();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[2];
        parameters[0] = new DescriptionOfParameter("GLMAU", "(e.g. 0)", Integer.class, false);
        parameters[1] = new DescriptionOfParameter("MAU file", "(e.g. MAU_Utility.txt)", String.class, false);
        return parameters;
    }

    @Override
    public String getImplementationAuthorNames() {
        return "Ting Li";
    }

    @Override
    public String[] getInputFileTypes() {
        return new String[]{"Database of instances", "Transaction database", "Transaction database with utility values"};
    }

    @Override
    public String[] getOutputFileTypes() {
        return new String[]{"Patterns", "High-utility patterns", "High average-utility itemsets"};
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.DATA_MINING;
    }
}
