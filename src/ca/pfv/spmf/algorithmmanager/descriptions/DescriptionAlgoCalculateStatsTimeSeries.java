package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.tools.dataset_stats.TimeSeriesStats;

import java.io.IOException;

/**
 * This class describes the algorithm to calculate stats about a time series
 *
 * @author Philippe Fournier-Viger
 * @see TimeSeriesStats
 */
public class DescriptionAlgoCalculateStatsTimeSeries extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoCalculateStatsTimeSeries() {
    }

    @Override
    public String getName() {
        return "Calculate_stats_for_time_series";
    }

    @Override
    public String getAlgorithmCategory() {
        return "TOOLS - STATS CALCULATORS";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/CalcStats_for_time_series.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        String separator = getParamAsString(parameters[0]);

        // Applying the algorithm
        TimeSeriesStats algorithm = new TimeSeriesStats();
        algorithm.runAlgorithm(inputFile, separator);
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[1];
        parameters[0] = new DescriptionOfParameter("Separator", "(e.g. , )", String.class, false);
        return parameters;
    }

    @Override
    public String getImplementationAuthorNames() {
        return "Philippe Fournier-Viger";
    }

    @Override
    public String[] getInputFileTypes() {
        return new String[]{"Time series database"};
    }

    @Override
    public String[] getOutputFileTypes() {
        return null;
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.DATA_STATS_CALCULATOR;
    }
}
