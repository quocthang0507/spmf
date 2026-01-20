package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.sequentialpatterns.skopus.AlgoSkopus;
import ca.pfv.spmf.algorithms.sequentialpatterns.spam.AlgoTKS;

/**
 * This class describes the Skopus algorithm parameters.
 * It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoTKS
 */
public class DescriptionAlgoSkopus extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoSkopus() {
    }

    @Override
    public String getName() {
        return "SKOPUS";
    }

    @Override
    public String getAlgorithmCategory() {
        return "SEQUENTIAL PATTERN MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/SKOPUS.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws Exception {

        int k = getParamAsInteger(parameters[0]);

        //if true, the patterns will be found until the leverage interestingness measure instead of the support
        boolean useLeverageMeasureInsteadOfSupport = getParamAsBoolean(parameters[1]);

        //  this is the maximum sequential pattern length
        int maximumSequentialPatternLength = getParamAsInteger(parameters[2]);

        // if true, smoothed values will be used
        boolean useSmoothedValues = getParamAsBoolean(parameters[3]);

        double smoothingCoefficient = 0;

        // if smoothing is used, this is the smoothing coefficient (e.g. 0.5)
        if (parameters.length >= 5 && "".equals(parameters[4]) == false) {
            smoothingCoefficient = getParamAsDouble(parameters[4]);
        }

        //--------------- Applying the  algorithm  ---------//
        AlgoSkopus algorithm = new AlgoSkopus();
        algorithm.runAlgorithm(inputFile, outputFile, useLeverageMeasureInsteadOfSupport,
                false,
                useSmoothedValues, smoothingCoefficient,
                maximumSequentialPatternLength, k);

        // Print statistics
        algorithm.printStats();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[5];
        parameters[0] = new DescriptionOfParameter("k ", "(e.g. 5 patterns)", Double.class, false);
        parameters[1] = new DescriptionOfParameter("Use leverage?", "(e.g. true)", Boolean.class, false);
        parameters[2] = new DescriptionOfParameter("Max pattern length", "(e.g. 10 items)", Integer.class, false);
        parameters[3] = new DescriptionOfParameter("Use smoothed value?", "(e.g. true)", Boolean.class, false);
        parameters[4] = new DescriptionOfParameter("Smoothing coefficient", "(e.g. 0.5)", Double.class, true);
        return parameters;
    }

    @Override
    public String getImplementationAuthorNames() {
        return "Petijean et al.";
    }

    @Override
    public String[] getInputFileTypes() {
        return new String[]{"Database of instances", "Sequence database", "Simple sequence database"};
    }

    @Override
    public String[] getOutputFileTypes() {
        return new String[]{"Patterns", "Sequential patterns", "Top-k frequent sequential patterns with leverage"};
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.DATA_MINING;
    }

}
