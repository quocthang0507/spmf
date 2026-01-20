package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.tools.dataset_converter.Formats;
import ca.pfv.spmf.tools.dataset_converter.TransactionDatabaseConverter;

import java.io.IOException;

/**
 * This class describes the algorithm to convert an ARFF file to a transaction
 * database. It is designed to be used by the graphical and command line
 * interface.
 *
 * @author Philippe Fournier-Viger
 * @see TransactionDatabaseConverter
 */
public class DescriptionAlgoConvertARFFFileToTransactionDB extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoConvertARFFFileToTransactionDB() {
    }

    @Override
    public String getName() {
        return "Convert_ARFF_file_to_transaction_database";
    }

    @Override
    public String getAlgorithmCategory() {
        return "TOOLS - DATA TRANSFORMATION";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/Using_the_ARFF_format.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        int transactionCount = getParamAsInteger(parameters[0]);

        long startTime = System.currentTimeMillis();
        // Create a converter
        TransactionDatabaseConverter converter = new TransactionDatabaseConverter();
        // Call the method to convert the input file from TEXT to the SPMF format
        converter.convert(inputFile, outputFile, Formats.ARFF, transactionCount);
        long endTIme = System.currentTimeMillis();
        System.out.println(
                "Transaction database converted.  Time spent for conversion = " + (endTIme - startTime) + " ms.");
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[1];
        parameters[0] = new DescriptionOfParameter("Transaction count count", "(e.g. 5)", Integer.class, false);
        return parameters;
    }

    @Override
    public String getImplementationAuthorNames() {
        return "Philippe Fournier-Viger";
    }

    @Override
    public String[] getInputFileTypes() {
        return new String[]{"ARFF file"};
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
