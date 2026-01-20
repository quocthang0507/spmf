package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.sequentialpatterns.goKrimp.AlgoGoKrimp;
import ca.pfv.spmf.algorithms.sequentialpatterns.goKrimp.DataReader;

import java.io.File;
import java.io.IOException;

/**
 * This class describes the GoKrimp algorithm.
 * It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoGoKrimp
 */
public class DescriptionAlgoGoKrimp extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoGoKrimp() {
    }

    @Override
    public String getName() {
        return "GoKrimp";
    }

    @Override
    public String getAlgorithmCategory() {
        return "SEQUENTIAL PATTERN MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/GoKrimp.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {

        String labelFilePath = "";
        if (parameters.length >= 1 && "".equals(parameters[0]) == false) {
            // file for sensitive
            labelFilePath = parameters[0];
            if (labelFilePath == null) {
                labelFilePath = "";
            } else {
                File file = new File(inputFile);
                if (file.getParent() == null) {
                    labelFilePath = parameters[0];
                } else {
                    labelFilePath = file.getParent() + File.separator
                            + parameters[0];
                }
            }
        }

        DataReader d = new DataReader();
        AlgoGoKrimp g = d.readData_SPMF(inputFile, labelFilePath);
        g.setOutputFilePath(outputFile); // if not set, then result will be
        // printed to console
        g.gokrimp();

    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {
        DescriptionOfParameter[] parameters = new DescriptionOfParameter[1];
        parameters[0] = new DescriptionOfParameter("Label file name ", "(e.g. test_goKrimp.lab)", String.class, true);
        return parameters;
    }

    @Override
    public String getImplementationAuthorNames() {
        return "Hoang Thanh Lam";
    }

    @Override
    public String[] getInputFileTypes() {
        return new String[]{"Database of instances", "Sequence database", "Simple sequence database"};
    }

    @Override
    public String[] getOutputFileTypes() {
        return new String[]{"Patterns", "Sequential patterns", "Frequent Sequential patterns", "Compressing sequential patterns"};
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.DATA_MINING;
    }
}
