package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.tools.dataset_stats.SequenceDBStats;
import ca.pfv.spmf.tools.other_dataset_tools.ResizeDatabaseTool;

import java.io.IOException;

/**
 * This class describes the algorithm to resize a database. It is designed to be used by the graphical and command line interface.
 *
 * @author Philippe Fournier-Viger
 * @see SequenceDBStats
 */
public class DescriptionAlgoResizeADatabase extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoResizeADatabase() {
    }

    @Override
    public String getName() {
        return "Resize_a_database";
    }

    @Override
    public String getAlgorithmCategory() {
        return "TOOLS - DATA TRANSFORMATION";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/Resize_a_database.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        double percentage = getParamAsDouble(parameters[0]);
        ResizeDatabaseTool tool = new ResizeDatabaseTool();
        tool.convert(inputFile, outputFile, percentage);
        System.out.println("Finished resizing the database.");
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[1];
        parameters[0] = new DescriptionOfParameter("Percentage (%)", "(e.g. 0.7 or 70%)", Double.class, false);
        return parameters;
    }

    @Override
    public String getImplementationAuthorNames() {
        return "Philippe Fournier-Viger";
    }

    @Override
    public String[] getInputFileTypes() {
        return new String[]{"Database of instances"};
    }

    @Override
    public String[] getOutputFileTypes() {
        return new String[]{"Database of instances"};
    }
//
//	@Override
//	String[] getSpecialInputFileTypes() {
//		return null; //new String[]{"ARFF"};
//	}

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.DATA_PROCESSOR;
    }

}
