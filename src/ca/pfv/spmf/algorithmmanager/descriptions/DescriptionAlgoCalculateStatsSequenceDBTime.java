package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.tools.dataset_stats.SequenceDBStats;

import java.io.IOException;

/**
 * This class describes the algorithm to calculate stats for a sequence database
 * with timestamps. It is designed to be used by the graphical and command line
 * interface.
 *
 * @author Philippe Fournier-Viger
 * @see SequenceDBStats
 */
public class DescriptionAlgoCalculateStatsSequenceDBTime extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoCalculateStatsSequenceDBTime() {
    }

    @Override
    public String getName() {
        return "Calculate_stats_for_a_time-extended_sequence_database";
    }

    @Override
    public String getAlgorithmCategory() {
        return "TOOLS - STATS CALCULATORS";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/Calculate_stats_time_extended_sdb.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        SequenceDBStats algo = new SequenceDBStats();
        algo.runAlgorithm(inputFile);
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[0];
        return parameters;
    }

    @Override
    public String getImplementationAuthorNames() {
        return "Philippe Fournier-Viger";
    }

    @Override
    public String[] getInputFileTypes() {
        return new String[]{"Database of instances", "Sequence database", "Sequence database with timestamps"};
    }

    @Override
    public String[] getOutputFileTypes() {
        return null;
    }

    //
//	@Override
//	String[] getSpecialInputFileTypes() {
//		return null; //new String[]{"ARFF"};
//	}
    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.DATA_STATS_CALCULATOR;
    }
}
