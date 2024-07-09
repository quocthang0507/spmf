package ca.pfv.spmf.algorithms.timeseries.paa;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ca.pfv.spmf.algorithms.timeseries.TimeSeries;
import ca.pfv.spmf.algorithms.timeseries.reader_writer.AlgoTimeSeriesReader;
import ca.pfv.spmf.algorithms.timeseries.reader_writer.AlgoTimeSeriesWriter;

/**
 * Example of how to calculate the Piecewise Aggregate Approximation of time series, using
 * the source code of SPMF, by reading a time series file and writing a time series file as output
 *
 * @author Philippe Fournier-Viger, 2016.
 */
public class MainTestPAAFromFileToFile {

    public static void main(String[] arg) throws IOException {

        // the input file
        String input = fileToPath("contextSAXblog.txt");
        // the output file
        String output = "./output.txt";

        // the number of data points that we want as output
        int numberOfSegments = 8;

        // The separator to be used for reading/writting the input/output file
        String separator = ",";

        // (1) Read the time series
        AlgoTimeSeriesReader reader = new AlgoTimeSeriesReader();
        List<TimeSeries> multipleTimeSeries = reader.runAlgorithm(input, separator);

        // (2) Calculate the moving average of each time series
        List<TimeSeries> movingAveragemultipleTimeSeries = new ArrayList<TimeSeries>();
        for (TimeSeries timeSeries : multipleTimeSeries) {
            AlgoPiecewiseAggregateApproximation algorithm = new AlgoPiecewiseAggregateApproximation();
            TimeSeries movingAverageSeries = algorithm.runAlgorithm(timeSeries, numberOfSegments);
            movingAveragemultipleTimeSeries.add(movingAverageSeries);
            algorithm.printStats();
        }

        // (3) write the time series to a file
        AlgoTimeSeriesWriter algorithm2 = new AlgoTimeSeriesWriter();
        algorithm2.runAlgorithm(output, movingAveragemultipleTimeSeries, separator);
        algorithm2.printStats();

    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestPAAFromFileToFile.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), "UTF-8");
    }
}
