package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.sequentialpatterns.prefixspan.AlgoBIDEPlus;
import ca.pfv.spmf.input.sequence_database_list_strings.SequenceDatabase;

import java.io.IOException;

/**
 * This class describes the BIDE+ algorithm parameters. It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoBIDEPlus
 */
public class DescriptionAlgoBIDEPlusWithStrings extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoBIDEPlusWithStrings() {
    }

    @Override
    public String getName() {
        return "BIDE+_with_strings";
    }

    @Override
    public String getAlgorithmCategory() {
        return "SEQUENTIAL PATTERN MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/BIDEPlus.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {


        SequenceDatabase sequenceDatabase = new SequenceDatabase();
        sequenceDatabase.loadFile(inputFile);
        // sequenceDatabase.print();
        int minsup = (int) Math
                .ceil((getParamAsDouble(parameters[0]) * sequenceDatabase
                        .size())); // we use a minimum support of 2
        // sequences.

        ca.pfv.spmf.algorithms.sequentialpatterns.BIDE_and_prefixspan_with_strings.AlgoBIDEPlus_withStrings algo = new ca.pfv.spmf.algorithms.sequentialpatterns.BIDE_and_prefixspan_with_strings.AlgoBIDEPlus_withStrings();
        algo.runAlgorithm(sequenceDatabase, outputFile, minsup);
        algo.printStatistics(sequenceDatabase.size());
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[1];
        parameters[0] = new DescriptionOfParameter("Minsup (%)", "(e.g. 0.4 or 40%)", Double.class, false);
        return parameters;
    }

    @Override
    public String getImplementationAuthorNames() {
        return "Philippe Fournier-Viger";
    }

    @Override
    public String[] getInputFileTypes() {
        return new String[]{"Database of instances", "Sequence database with strings", "Simple sequence database with strings"};
    }

    @Override
    public String[] getOutputFileTypes() {
        return new String[]{"Patterns", "Sequential patterns", "Frequent sequential patterns", "Frequent closed sequential patterns"};
    }

    //
//	@Override
//	String[] getSpecialInputFileTypes() {
//		return null; //new String[]{"ARFF"};
//	}
    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.DATA_MINING;
    }
}
