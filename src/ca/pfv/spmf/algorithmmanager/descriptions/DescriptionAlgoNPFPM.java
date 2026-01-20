package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.frequentpatterns.npfpm.AlgoNPFPM;

import java.io.IOException;

/**
 * This class describes the NPFPM algorithm parameters.
 * It is designed to be used by the graphical and command line interface.
 *
 * @author Vincent M. Nofong (modified from Philippe Fournier-Viger's implementation of FPGrowth)
 * @see AlgoNPFPM
 */
public class DescriptionAlgoNPFPM extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoNPFPM() {
    }

    @Override
    public String getName() {
        return "NPFPM";
    }

    @Override
    public String getAlgorithmCategory() {
        return "PERIODIC PATTERN MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/periodic_NPFPM.php";
    } // 22819

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        double minsup = getParamAsDouble(parameters[0]);
        double periodicity = getParamAsDouble(parameters[1]);
        double difference = getParamAsDouble(parameters[2]);
        AlgoNPFPM algorithm = new AlgoNPFPM();

        if (parameters.length >= 2 && "".equals(parameters[1]) == false) {
            algorithm.setMaximumPatternLength(getParamAsInteger(parameters[1]));
        }
        if (parameters.length >= 3 && "".equals(parameters[2]) == false) {
            algorithm.setMinimumPatternLength(getParamAsInteger(parameters[2]));
        }
        algorithm.runAlgorithm(inputFile, outputFile, minsup, periodicity, difference);
        algorithm.printStats();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[5];
        parameters[0] = new DescriptionOfParameter("Minsup (%)", "(e.g. 0.4 or 40%)", Double.class, false);
        parameters[1] = new DescriptionOfParameter("Periodicity ", "(e.g. 0.4 or 40)", Double.class, false);
        parameters[2] = new DescriptionOfParameter("Difference ", "(e.g. 0.4 or 40)", Double.class, false);
        parameters[3] = new DescriptionOfParameter("Max pattern length", "(e.g. 2 items)", Integer.class, true);
        parameters[4] = new DescriptionOfParameter("Min pattern length", "(e.g. 2 items)", Integer.class, true);
        return parameters;
    }

    @Override
    public String getImplementationAuthorNames() {
        return "Vincent M. Nofong modified from Philippe Fournier-Viger";
    }

    @Override
    public String[] getInputFileTypes() {
        return new String[]{"Database of instances", "Transaction database", "Simple transaction database"};
    }

    @Override
    public String[] getOutputFileTypes() {
        return new String[]{"Periodic patterns", "Periodic frequent patterns", "Non-redundant Periodic frequent itemsets"};
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.DATA_MINING;
    }

}
