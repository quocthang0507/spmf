package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.sequentialpatterns.uspan.AlgoUSpan;

import java.io.IOException;

/**
 * This class describes the USpan algorithm parameters. It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoUSpan
 */
public class DescriptionAlgoUSpan extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoUSpan() {
    }

    @Override
    public String getName() {
        return "USpan";
    }

    @Override
    public String getAlgorithmCategory() {
        return "HIGH-UTILITY PATTERN MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/USpan.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        AlgoUSpan algo = new AlgoUSpan();
        if (parameters.length >= 2 && "".equals(parameters[1]) == false) {
            algo.setMaxPatternLength(getParamAsInteger(parameters[1]));
        }

        // execute the algorithm
        algo.runAlgorithm(inputFile, outputFile,
                getParamAsInteger(parameters[0])); //
        algo.printStatistics();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[2];
        parameters[0] = new DescriptionOfParameter("Minimum utility", "(e.g. 35)", Integer.class, false);
        parameters[1] = new DescriptionOfParameter("Maximum length", "(e.g. 4)", Integer.class, true);
        return parameters;
    }

    @Override
    public String getImplementationAuthorNames() {
        return "Philippe Fournier-Viger";
    }

    @Override
    public String[] getInputFileTypes() {
        return new String[]{"Database of instances", "Sequence database", "Sequence database with utility values"};
    }

    @Override
    public String[] getOutputFileTypes() {
        return new String[]{"Patterns", "High-utility patterns", "Sequential patterns", "High-utility patterns", "High-utility sequential patterns"};
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.DATA_MINING;
    }

}
