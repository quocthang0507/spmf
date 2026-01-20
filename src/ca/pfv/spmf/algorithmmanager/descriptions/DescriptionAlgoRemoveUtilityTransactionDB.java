package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.tools.other_dataset_tools.TransactionUtilityRemover;

import java.io.IOException;

/**
 * This class describes the algorithm to remove utility values from a transaction database. It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see TransactionUtilityRemover
 */
public class DescriptionAlgoRemoveUtilityTransactionDB extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoRemoveUtilityTransactionDB() {
    }

    @Override
    public String getName() {
        return "Remove_utility_information_from_a_transaction_database";
    }

    @Override
    public String getAlgorithmCategory() {
        return "TOOLS - DATA TRANSFORMATION";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/Remove_utility_information.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        TransactionUtilityRemover tool = new TransactionUtilityRemover();
        tool.convert(inputFile, outputFile);
        System.out.println("Finished fixing the transaction database.");
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[0];
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
        return new String[]{"Database of instances", "Transaction database", "Simple transaction database"};
    }
//
//	@Override
//	String[] getSpecialInputFileTypes() {
//		return null; //new String[]{"ARFF"};
//	}

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.DATA_PROCESSOR;
    }

}
