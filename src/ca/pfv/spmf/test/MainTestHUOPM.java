package ca.pfv.spmf.test;

import ca.pfv.spmf.algorithms.frequentpatterns.huopm.AlgoHUOPM;
import ca.pfv.spmf.algorithms.frequentpatterns.huopm.HUOPMPruningStrategy;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

/**
 * Example of how to run the HUOPM algorithm.
 */
public class MainTestHUOPM {

    public static void main(String[] arg) throws IOException {
        String input = fileToPath("contextHAUIMiner.txt");
        String output = ".//output.txt";

        double minSupport = 0.3;
        double minUtilityOccupancy = 0.30;

        AlgoHUOPM algorithm = new AlgoHUOPM();
        algorithm.runAlgorithm(input, output, minSupport, minUtilityOccupancy, HUOPMPruningStrategy.ALL);
        algorithm.printStats();
    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        URL url = MainTestHUOPM.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), "UTF-8");
    }
}
