package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.sequentialpatterns.prosecco.AlgoProsecco;

import java.io.IOException;

/**
 * This class describes the PrefixSpan algorithm parameters. It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoProsecco
 */
public class DescriptionAlgoProSecCo extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoProSecCo() {
    }

    @Override
    public String getName() {
        return "ProSecCo";
    }

    @Override
    public String getAlgorithmCategory() {
        return "SEQUENTIAL PATTERN MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/ProSecCo.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {


        // Get the parameter "minsup"
        int blockSize = getParamAsInteger(parameters[0]); // number of transactions to process in each block
        int dbSize = getParamAsInteger(parameters[1]); // number of transactions in the dataset
        double errorTolerance = getParamAsDouble(parameters[2]); // failure probability
        double minsupRelative = getParamAsDouble(parameters[3]); // 50%

        // create an instance of the algorithm with minsup = 50 %
        AlgoProsecco algo = new AlgoProsecco();

        // execute the algorithm
        algo.runAlgorithm(inputFile, outputFile, blockSize, dbSize, errorTolerance, minsupRelative);
        algo.printStatistics();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[4];
        parameters[0] = new DescriptionOfParameter("Block size ", "(e.g. 1)", Integer.class, false);
        parameters[1] = new DescriptionOfParameter("Database size", "(e.g. 4)", Integer.class, true);
        parameters[2] = new DescriptionOfParameter("Error tolerance (%)", "(e.g. 0.05)", Double.class, true);
        parameters[3] = new DescriptionOfParameter("Minimum support (%)", "(e.g. 50%)", Double.class, true);
        return parameters;
    }

    @Override
    public String getImplementationAuthorNames() {
        return "Sacha Servan-Schreiber";
    }

    @Override
    public String[] getInputFileTypes() {
        return new String[]{"Database of instances", "Sequence database", "Simple sequence database"};
    }

    @Override
    public String[] getOutputFileTypes() {
        return new String[]{"Patterns", "Sequential patterns", "Progressive Frequent Sequential patterns"};
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.DATA_MINING;
    }

}
