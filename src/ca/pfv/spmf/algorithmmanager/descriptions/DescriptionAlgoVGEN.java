package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.sequentialpatterns.spam.AlgoVGEN;

import java.io.IOException;

/**
 * This class describes the VGEN algorithm parameters. It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoVGEN
 */
public class DescriptionAlgoVGEN extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoVGEN() {
    }

    @Override
    public String getName() {
        return "VGEN";
    }

    @Override
    public String getAlgorithmCategory() {
        return "SEQUENTIAL PATTERN MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/VGEN.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        AlgoVGEN algo = new AlgoVGEN();
        if (parameters.length >= 2 && "".equals(parameters[1]) == false) {
            algo.setMaximumPatternLength(getParamAsInteger(parameters[1]));
        }
        if (parameters.length >= 3 && "".equals(parameters[2]) == false) {
            algo.setMaxGap(getParamAsInteger(parameters[2]));
        }

        if (parameters.length >= 4 && "".equals(parameters[3]) == false) {
            boolean outputSeqIdentifiers = getParamAsBoolean(parameters[3]);
            algo.showSequenceIdentifiersInOutput(outputSeqIdentifiers);
        }

        // execute the algorithm
        algo.runAlgorithm(inputFile, outputFile,
                getParamAsDouble(parameters[0]));
        algo.printStatistics();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[4];
        parameters[0] = new DescriptionOfParameter("Minsup (%)", "(e.g. 0.5 or 50%)", Double.class, false);
        parameters[1] = new DescriptionOfParameter("Max pattern length", "(e.g. 4 items)", Integer.class, true);
        parameters[2] = new DescriptionOfParameter("Max gap", "(e.g. 1 item)", Integer.class, true);
        parameters[3] = new DescriptionOfParameter("Show sequence ids?", "(default: false)", Boolean.class, true);
        return parameters;
    }


    @Override
    public String getImplementationAuthorNames() {
        return "Philippe Fournier-Viger";
    }

    @Override
    public String[] getInputFileTypes() {
        return new String[]{"Database of instances", "Sequence database", "Simple sequence database"};
    }

    @Override
    public String[] getOutputFileTypes() {
        return new String[]{"Patterns", "Sequential patterns", "Frequent sequential patterns", "Frequent sequential generators"};
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.DATA_MINING;
    }

}
