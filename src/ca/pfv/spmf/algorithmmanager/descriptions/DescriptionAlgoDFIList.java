package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.frequentpatterns.DFIList.AlgoDFIList;

import java.io.IOException;

/**
 * This class describes the DFI-Growth algorithm parameters. It is designed to
 * be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see DFI-Growth
 */
public class DescriptionAlgoDFIList extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoDFIList() {
    }

    @Override
    public String getName() {
        return "DFI-List";
    }

    @Override
    public String getAlgorithmCategory() {
        return "FREQUENT ITEMSET MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/DFI-List.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {

        AlgoDFIList algo = new AlgoDFIList();
        algo.runAlgorithm(inputFile, outputFile);
        algo.printStats();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[0];
        return parameters;
    }

    @Override
    public String getImplementationAuthorNames() {
        return "Wu et al.";
    }

    @Override
    public String[] getInputFileTypes() {
        return new String[]{"Frequent closed itemsets"};
    }

    @Override
    public String[] getOutputFileTypes() {
        return new String[]{"Patterns", "Frequent patterns", "Frequent itemsets"};
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.DATA_MINING;
    }

}
