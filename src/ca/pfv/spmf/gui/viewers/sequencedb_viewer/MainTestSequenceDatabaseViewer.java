package ca.pfv.spmf.gui.viewers.sequencedb_viewer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

/**
 * Example of how to use Sequence Database viewer
 * <p>
 * algorithm from the source code.
 *
 * @author Philippe Fournier-Viger (Copyright 2024)
 */
public class MainTestSequenceDatabaseViewer {

    public static void main(String[] arg) throws IOException {

        // Input file path
        String input = fileToPath("contextPrefixSpan.txt");
//		String input = fileToPath("contextPrefixSpanWithNames.txt");

        // Applying the Sequence Database viewer
        SequenceDatabaseViewer algorithm = new SequenceDatabaseViewer(false, input);

    }

    public static String fileToPath(String filename) throws UnsupportedEncodingException {
        System.out.println("filename : " + filename);
        URL url = MainTestSequenceDatabaseViewer.class.getResource(filename);
        return java.net.URLDecoder.decode(url.getPath(), "UTF-8");
    }
}