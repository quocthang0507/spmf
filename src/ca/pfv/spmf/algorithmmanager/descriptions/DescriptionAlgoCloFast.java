package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.sequentialpatterns.clofast.AlgoCloFast;

import java.io.IOException;

/**
 * This class describes the CloFast algorithm parameters. It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoCloFast
 */
public class DescriptionAlgoCloFast extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoCloFast() {
    }

    @Override
    public String getName() {
        return "CloFast";
    }

    @Override
    public String getAlgorithmCategory() {
        return "SEQUENTIAL PATTERN MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/CLOFAST.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {


        AlgoCloFast algo = new AlgoCloFast();

        algo.runAlgorithm(inputFile, outputFile,
                getParamAsFloat(parameters[0]));
        algo.printStatistics();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[1];
        parameters[0] = new DescriptionOfParameter("Minsup (%)", "(e.g. 0.4 or 40%)", Float.class, false);
        return parameters;
    }

    @Override
    public String getImplementationAuthorNames() {
        return "Fumarola, F., Lanotte, P. F., Ceci, M., & Malerba, D.";
    }

    @Override
    public String[] getInputFileTypes() {
        return new String[]{"Database of instances", "Sequence database", "Simple sequence database"};
    }

    @Override
    public String[] getOutputFileTypes() {
        return new String[]{"Patterns", "Sequential patterns", "Frequent sequential patterns", "Frequent closed sequential patterns"};
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.DATA_MINING;
    }
}
