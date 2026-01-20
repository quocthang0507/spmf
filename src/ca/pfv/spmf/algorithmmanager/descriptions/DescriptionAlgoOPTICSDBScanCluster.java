package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.clustering.optics.AlgoOPTICS;

import java.io.IOException;

/**
 * This class describes the OPTICS algorithm parameters for discovering DBScan clusters.
 * It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoOPTICS
 */
public class DescriptionAlgoOPTICSDBScanCluster extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoOPTICSDBScanCluster() {
    }

    @Override
    public String getName() {
        return "OPTICS-dbscan-clusters";
    }

    @Override
    public String getAlgorithmCategory() {
        return "CLUSTERING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/Optics.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        int minPts = getParamAsInteger(parameters[0]);
        double epsilon = getParamAsDouble(parameters[1]);
        double epsilonPrime = getParamAsDouble(parameters[2]);

        //The separator
        String separator;
        if (parameters.length > 3 && "".equals(parameters[3]) == false) {
            separator = getParamAsString(parameters[3]);
        } else {
            separator = " ";
        }

        // Apply the algorithm to compute a cluster ordering
        AlgoOPTICS algo = new AlgoOPTICS();
        algo.computerClusterOrdering(inputFile, minPts, epsilon, separator);

        //  generate dbscan clusters from the cluster ordering:
        algo.extractDBScan(minPts, epsilonPrime);

        algo.printStatistics();
        algo.saveToFile(outputFile);
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[4];
        parameters[0] = new DescriptionOfParameter("minPts", "(e.g. 2)", Integer.class, false);
        parameters[1] = new DescriptionOfParameter("epsilon", "(e.g. 2)", Double.class, false);
        parameters[2] = new DescriptionOfParameter("epsilonPrime", "(e.g. 5)", Double.class, false);
        parameters[3] = new DescriptionOfParameter("separator", "(default: ' ')", String.class, true);
        return parameters;
    }

    @Override
    public String getImplementationAuthorNames() {
        return "Philippe Fournier-Viger";
    }

    @Override
    public String[] getInputFileTypes() {
        return new String[]{"Database of instances", "Database of double vectors"};
    }

    @Override
    public String[] getOutputFileTypes() {
        return new String[]{"Clusters", "Density-based clusters"};
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.DATA_MINING;
    }

}
