package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.frequentpatterns.opusminer.AlgoOpusMiner;

import java.io.IOException;

/**
 * This class describes the OPUS-Miner algorithm parameters.
 * It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see OPUS-Miner
 */
public class DescriptionAlgoOPUSMiner extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoOPUSMiner() {
    }

    @Override
    public String getName() {
        return "OPUS-Miner";
    }

    @Override
    public String getAlgorithmCategory() {
        return "FREQUENT ITEMSET MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/OPUSMINER.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {

        // Set k to the integer value <i>.  By default it is 100.
        int k = getParamAsInteger(parameters[0]);

        // Filter out itemsets that are not independently productive.
        boolean filter = getParamAsBoolean(parameters[1]);

        // Set the measure of interest to lift.  By default it is leverage.
        boolean searchByLift = getParamAsBoolean(parameters[2]);

        // Remove redundant itemsets.
        boolean redundancyTests = getParamAsBoolean(parameters[3]);

        boolean correctionForMultiCompare = getParamAsBoolean(parameters[4]);

        //Each output itemset is followed by its closure.
        boolean printClosure = getParamAsBoolean(parameters[5]);

        //------------ End of parameters ----------------------//

        // Applying the  algorithm
        AlgoOpusMiner algorithm = new AlgoOpusMiner();
        algorithm.runAlgorithm(inputFile, outputFile, printClosure, filter,
                k, searchByLift,
                correctionForMultiCompare, redundancyTests, false);
        algorithm.printStats();
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[6];
        parameters[0] = new DescriptionOfParameter("k", "(e.g. 3)", Integer.class, false);
        parameters[1] = new DescriptionOfParameter("Check independency?", "(e.g. true)", Boolean.class, false);
        parameters[2] = new DescriptionOfParameter("Search by lift?", "(e.g. true)", Boolean.class, false);
        parameters[3] = new DescriptionOfParameter("Check redundancy?", "(e.g. true)", Boolean.class, false);
        parameters[4] = new DescriptionOfParameter("Correction for multicompare?", "(e.g. true)", Boolean.class, false);
        parameters[5] = new DescriptionOfParameter("Print closure?", "(e.g. false)", Boolean.class, false);
        return parameters;
    }

    @Override
    public String getImplementationAuthorNames() {
        return "Java conversion by Xiang Li and Philippe Fournier-Viger";
    }

    @Override
    public String[] getInputFileTypes() {
        return new String[]{"Database of instances", "Transaction database", "Simple transaction database"};
    }

    @Override
    public String[] getOutputFileTypes() {
        return new String[]{"Patterns", "Frequent patterns", "Frequent itemsets", "Self-Sufficient Itemsets"};
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.DATA_MINING;
    }

}
