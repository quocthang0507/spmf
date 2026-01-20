package ca.pfv.spmf.test;

import ca.pfv.spmf.tools.dataset_stats.TransactionDBCostUtilityStats;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

/**
 * Example of how to calculate stats for a cost Utility Transaction Database  algorithm from the
 * source code.
 *
 * @author Philippe Fournier-Viger (Copyright 2024)
 */
public class MainTestStatsCostUtilityTDB {

    public static void main(String[] arg) throws IOException {

        String input = fileToPath("DB_cost.txt");

        // Applying the algorithm
        TransactionDBCostUtilityStats algo = new TransactionDBCostUtilityStats();
        algo.runAlgorithm(input);
    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
//		System.out.println("filename : " + filename);
        URL url = MainTestStatsCostUtilityTDB.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), "UTF-8");
    }
}
