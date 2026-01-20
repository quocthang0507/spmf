package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.tools.dataset_converter.Formats;
import ca.pfv.spmf.tools.dataset_converter.TransactionDatabaseConverter;

import java.io.IOException;

/**
 * This class describes the algorithm to convert a transaction database to SPMF format. It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see TransactionDatabaseConverter
 */
public class DescriptionAlgoConvertTransactionDBToSPMF extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoConvertTransactionDBToSPMF() {
    }

    @Override
    public String getName() {
        return "Convert_a_transaction_database_to_SPMF_format";
    }

    @Override
    public String getAlgorithmCategory() {
        return "TOOLS - DATA TRANSFORMATION";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/Converting_a_transaction_database_to_SPMF.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        String format = getParamAsString(parameters[0]);
        int transactionCount = getParamAsInteger(parameters[1]);

        long startTime = System.currentTimeMillis();
        TransactionDatabaseConverter converter = new TransactionDatabaseConverter();
        converter.convert(inputFile, outputFile, Formats.valueOf(format),
                transactionCount);
        long endTIme = System.currentTimeMillis();
        System.out.println("Transaction database converted.  Time spent for conversion = " + (endTIme - startTime) + " ms.");
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[2];
        parameters[0] = new DescriptionOfParameter("Input format", "(e.g. CSV_INTEGER)", String.class, false);
        parameters[1] = new DescriptionOfParameter("Transaction count", "(e.g. 5)", Integer.class, false);
        return parameters;
    }

    @Override
    public String getImplementationAuthorNames() {
        return "Philippe Fournier-Viger";
    }

    @Override
    public String[] getInputFileTypes() {
        return new String[]{"Transaction database in non SPMF format"};
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
