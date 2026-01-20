package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.episodes.tup.tup_preinsertion.AlgoTUP_preinsertion;

import java.io.IOException;

/**
 * This class describes the TUP_Preinsertion algorithm parameters.
 * It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoTUP_preinsertion
 */
public class DescriptionAlgoTUP_Preinsertion extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoTUP_Preinsertion() {
    }

    @Override
    public String getName() {
        return "TUP_Preinsertion";
    }

    @Override
    public String getAlgorithmCategory() {
        return "EPISODE MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/TUP.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        int maxTimeDuration = getParamAsInteger(parameters[0]);
        int k = getParamAsInteger(parameters[1]);

        // Applying the algorithm
        AlgoTUP_preinsertion algo = new AlgoTUP_preinsertion();

        algo.runAlgorithm(inputFile, maxTimeDuration, k);
        algo.writeResultTofile(outputFile);
        algo.printStats();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[2];
        parameters[0] = new DescriptionOfParameter("Max. Time duration", "(e.g. 2)", Integer.class, false);
        parameters[1] = new DescriptionOfParameter("k", "(e.g. 3)", Integer.class, false);
        return parameters;
    }

    @Override
    public String getImplementationAuthorNames() {
        return "Rathore et al.";
    }

    @Override
    public String[] getInputFileTypes() {
        return new String[]{"Database of instances", "Transaction database", "Transaction database with utility values"};
    }

    @Override
    public String[] getOutputFileTypes() {
        return new String[]{"Patterns", "Episodes", "High-utility patterns", "High-Utility episodes", "Top-k High-Utility episodes"};
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.DATA_MINING;
    }

}
