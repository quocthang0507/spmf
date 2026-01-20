package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.sequentialpatterns.prefixspan.AlgoBIDEPlus;

import java.io.IOException;

/**
 * This class describes the BIDE+ algorithm parameters. It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoBIDEPlus
 */
public class DescriptionAlgoBIDEPlus extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoBIDEPlus() {
    }

    @Override
    public String getName() {
        return "BIDE+";
    }

    @Override
    public String getAlgorithmCategory() {
        return "SEQUENTIAL PATTERN MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/BIDEPlus.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {

        // Get the parameter "minsup"
        double minsup = getParamAsDouble(parameters[0]);


        AlgoBIDEPlus algo = new AlgoBIDEPlus();

        // Get the optional parameter to show sequence ids
        if (parameters.length >= 2 && "".equals(parameters[1]) == false) {
            algo.setMaximumPatternLength(getParamAsInteger(parameters[1]));
        }

        if (parameters.length >= 3 && "".equals(parameters[2]) == false) {
            algo.setShowSequenceIdentifiers(getParamAsBoolean(parameters[2]));
        }

        // Run the algorithm
        algo.runAlgorithm(inputFile, minsup, outputFile);
        algo.printStatistics();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[3];
        parameters[0] = new DescriptionOfParameter("Minsup (%)", "(e.g. 0.4 or 40%)", Double.class, false);
        parameters[1] = new DescriptionOfParameter("Max pattern length", "", Integer.class, true);
        parameters[2] = new DescriptionOfParameter("Show sequence ids?", "(default: false)", Boolean.class, true);
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
        return new String[]{"Patterns", "Sequential patterns", "Frequent sequential patterns", "Frequent closed sequential patterns"};
    }

    //
//	@Override
//	String[] getSpecialInputFileTypes() {
//		return null; //new String[]{"ARFF"};
//	}
    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.DATA_MINING;
    }
}
