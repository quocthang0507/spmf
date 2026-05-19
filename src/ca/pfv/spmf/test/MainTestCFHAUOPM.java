package ca.pfv.spmf.test;

import ca.pfv.spmf.algorithms.frequentpatterns.cfhauopm.AlgoCFHAUOPM;
import ca.pfv.spmf.algorithms.frequentpatterns.cfhauopm.CFHAUOPMPruningStrategy;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

/**
 * Example of how to run the CFHAUOPM algorithm.
 */
public class MainTestCFHAUOPM {

    public static void main(String[] arg) throws IOException {
        String input = fileToPath("contextHAUIMiner.txt");
        String output = ".//output.txt";

        double minSupport = 2;
        double minAverageUtilityOccupancy = 0.10;

        AlgoCFHAUOPM algorithm = new AlgoCFHAUOPM();
        algorithm.runAlgorithm(input, output, minSupport, minAverageUtilityOccupancy,
                CFHAUOPMPruningStrategy.ALL);
        algorithm.printStats();
    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestCFHAUOPM.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), "UTF-8");
    }
}
