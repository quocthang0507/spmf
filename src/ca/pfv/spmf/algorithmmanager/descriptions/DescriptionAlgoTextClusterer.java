package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.clustering.text_clusterer.TextClusterAlgo;

import java.io.IOException;

/**
 * This class describes the TextClusterer algorithm parameters.
 * It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see TextClusterAlgo
 */
public class DescriptionAlgoTextClusterer extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoTextClusterer() {
    }

    @Override
    public String getName() {
        return "TextClusterer";
    }

    @Override
    public String getAlgorithmCategory() {
        return "CLUSTERING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/TextClusterer.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        boolean performStemming = getParamAsBoolean(parameters[0]);
        boolean removeStopWords = getParamAsBoolean(parameters[0]);
        // Apply the algorithm
        TextClusterAlgo algo = new TextClusterAlgo();
        algo.runAlgorithm(inputFile, outputFile, performStemming, removeStopWords);
        algo.printStatistics();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[2];
        parameters[0] = new DescriptionOfParameter("Perform stemming", "(e.g. true)", Boolean.class, false);
        parameters[1] = new DescriptionOfParameter("Remove stop words", "(e.g. true)", Boolean.class, false);
        return parameters;
    }

    @Override
    public String getImplementationAuthorNames() {
        return "Sabarish Raghu";
    }

    @Override
    public String[] getInputFileTypes() {
        return new String[]{"Set of text documents"};
    }

    @Override
    public String[] getOutputFileTypes() {
        return new String[]{"Text clusters"};
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.DATA_MINING;
    }
}
