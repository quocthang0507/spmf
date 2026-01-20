package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.frequentpatterns.uapriori.AlgoUApriori;

import java.io.IOException;

/**
 * This class describes the UAPriori algorithm parameters.
 * It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoUApriori
 */
public class DescriptionAlgoUApriori extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoUApriori() {
    }

    @Override
    public String getName() {
        return "UApriori";
    }

    @Override
    public String getAlgorithmCategory() {
        return "FREQUENT ITEMSET MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/uapriori.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        double expectedsup = getParamAsDouble(parameters[0]);

        ca.pfv.spmf.algorithms.frequentpatterns.uapriori.UncertainTransactionDatabase context = new ca.pfv.spmf.algorithms.frequentpatterns.uapriori.UncertainTransactionDatabase();
        context.loadFile(inputFile);
        AlgoUApriori algorithm = new AlgoUApriori(context);

        if (parameters.length >= 2 && "".equals(parameters[1]) == false) {
            algorithm.setMaximumPatternLength(getParamAsInteger(parameters[1]));
        }

        algorithm.runAlgorithm(expectedsup, outputFile);
        algorithm.printStats();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[2];
        parameters[0] = new DescriptionOfParameter("Expected support (%)", "(e.g. 0.1 or 10%)", Double.class, false);
        parameters[1] = new DescriptionOfParameter("Max pattern length", "(e.g. 2 items)", Integer.class, true);
        return parameters;
    }

    @Override
    public String getImplementationAuthorNames() {
        return "Philippe Fournier-Viger";
    }

    @Override
    public String[] getInputFileTypes() {
        return new String[]{"Database of instances", "Transaction database", "Uncertain transaction database"};
    }

    @Override
    public String[] getOutputFileTypes() {
        return new String[]{"Patterns", "Frequent patterns", "Uncertain patterns", "Uncertain frequent itemsets"};
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.DATA_MINING;
    }

}
