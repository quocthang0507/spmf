package ca.pfv.spmf.test;

import ca.pfv.spmf.algorithms.frequentpatterns.hauopm.AlgoHAUOPM;
import ca.pfv.spmf.algorithms.frequentpatterns.hauopm.HAUOPMPruningStrategy;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

/**
 * Example of how to run the HAUOPM algorithm.
 */
public class MainTestHAUOPM {

    public static void main(String[] arg) throws IOException {
        String input = fileToPath("contextHAUIMiner.txt");
        String output = ".//output.txt";

        double minSupport = 2;
        double minAverageTransactionUtilityOccupancy = 0.10;

        AlgoHAUOPM algorithm = new AlgoHAUOPM();
        algorithm.runAlgorithm(input, output, minSupport, minAverageTransactionUtilityOccupancy,
                HAUOPMPruningStrategy.ALL);
        algorithm.printStats();
    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestHAUOPM.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), "UTF-8");
    }
}
