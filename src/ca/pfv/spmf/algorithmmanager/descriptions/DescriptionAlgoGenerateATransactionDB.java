package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.tools.dataset_generator.TransactionDatabaseGenerator;
import ca.pfv.spmf.tools.other_dataset_tools.TransactionUtilityRemover;

import java.io.IOException;

/**
 * This class describes the algorithm to generate a transaction database. It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see TransactionUtilityRemover
 */
public class DescriptionAlgoGenerateATransactionDB extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoGenerateATransactionDB() {
    }

    @Override
    public String getName() {
        return "Generate_a_transaction_database";
    }

    @Override
    public String getAlgorithmCategory() {
        return "TOOLS - DATA GENERATORS";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/Generating_synthetic_transaction_database.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        int p1 = getParamAsInteger(parameters[0]);
        int p2 = getParamAsInteger(parameters[1]);
        int p3 = getParamAsInteger(parameters[2]);

        TransactionDatabaseGenerator generator = new TransactionDatabaseGenerator();
        generator.generateDatabase(p1, p2, p3, outputFile);
        System.out.println("Transaction database generated.  ");
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[3];
        parameters[0] = new DescriptionOfParameter("Transaction count", "(e.g. 100)", Integer.class, false);
        parameters[1] = new DescriptionOfParameter("Max distinct items", "(e.g. 1000)", Integer.class, false);
        parameters[2] = new DescriptionOfParameter("Max item count per transaction", "(e.g. 10)", Integer.class, false);
        return parameters;
    }

    @Override
    public String getImplementationAuthorNames() {
        return "Philippe Fournier-Viger";
    }

    @Override
    public String[] getInputFileTypes() {
        return null;
    }

    @Override
    public String[] getOutputFileTypes() {
        return new String[]{"Database of instances", "Transaction database", "Simple transaction database"};
    }
//
//	@Override
//	String[] getSpecialInputFileTypes() {
//		return null; //new String[]{"ARFF"};
//	}

    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.DATA_GENERATOR;
    }

}
