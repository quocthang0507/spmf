package ca.pfv.spmf.experimental.iolayer;

import java.io.*;

public class SPMFTextFileReader extends AbstractSPMFReader {
    // We scan the database a first time to calculate the support of each item.
    BufferedReader myInput = null;

    SPMFTextFileReader(IOContext context, String input) throws IOException {
        super(context);
        myInput = new BufferedReader(new InputStreamReader(new FileInputStream(new File(input))));
    }

    public String readLine() throws IOException {
        return myInput.readLine();
    }

    public void doClose() throws IOException {
        myInput.close();
        System.out.println("reader close");
    }
}
