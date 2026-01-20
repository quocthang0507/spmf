package ca.pfv.spmf.algorithmmanager.descriptions;

import ca.pfv.spmf.algorithmmanager.AlgorithmType;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithms.sequentialpatterns.prefixSpan_AGP.AlgoPrefixSpan_AGP;
import ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.idLists.creators.IdListCreator;
import ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.idLists.creators.IdListCreator_FatBitmap;

import java.io.IOException;

/**
 * This class describes the SPAM algorithm parameters (version implemented by
 * Antonio G.P.). It is designed to be used by the graphical and command line
 * interface.
 *
 * @author Philippe Fournier-Viger
 * @see AlgoPrefixSpan_AGP
 */
public class DescriptionAlgoSPAM_AGP extends DescriptionOfAlgorithm {

    /**
     * Default constructor
     */
    public DescriptionAlgoSPAM_AGP() {
    }

    @Override
    public String getName() {
        return "SPAM_AGP";
    }

    @Override
    public String getAlgorithmCategory() {
        return "SEQUENTIAL PATTERN MINING";
    }

    @Override
    public String getURLOfDocumentation() {
        return "http://www.philippe-fournier-viger.com/spmf/SPAM.php";
    }

    @Override
    public void runAlgorithm(String[] parameters, String inputFile, String outputFile) throws IOException {
        ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.dataStructures.creators.AbstractionCreator abstractionCreator = ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.dataStructures.creators.AbstractionCreator_Qualitative
                .getInstance();
        IdListCreator idListCreator = IdListCreator_FatBitmap.getInstance();

        double minSupport = getParamAsDouble(parameters[0]);

        boolean outputSeqIdentifiers = false;
        if (parameters.length >= 2 && "".equals(parameters[1]) == false) {
            outputSeqIdentifiers = getParamAsBoolean(parameters[1]);
        }

        ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.AlgoSPAM_AGP algo = new ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.AlgoSPAM_AGP(
                minSupport);

        /*
         * if("".equals(parameters[1]) == false){
         * algo.setMaximumPatternLength(getParamAsInteger(parameters[1])); }
         */
        ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.dataStructures.database.SequenceDatabase sd = new ca.pfv.spmf.algorithms.sequentialpatterns.spade_spam_AGP.dataStructures.database.SequenceDatabase(
                abstractionCreator, idListCreator);
        sd.loadFile(inputFile, minSupport);

        algo.runAlgorithm(sd, true, false, outputFile, outputSeqIdentifiers);
        System.out.println(algo.printStatistics());
    }

    @Override
    public DescriptionOfParameter[] getParametersDescription() {

        DescriptionOfParameter[] parameters = new DescriptionOfParameter[2];
        parameters[0] = new DescriptionOfParameter("Minsup (%)", "(e.g. 0.4 or 40%)", Double.class, false);
        parameters[1] = new DescriptionOfParameter("Show sequence ids?", "(default: false)", Boolean.class, true);
        return parameters;
    }

    @Override
    public String getImplementationAuthorNames() {
        return "Antonio Gomariz Penalver";
    }

    @Override
    public String[] getInputFileTypes() {
        return new String[]{"Database of instances", "Sequence database", "Simple sequence database"};
    }

    @Override
    public String[] getOutputFileTypes() {
        return new String[]{"Patterns", "Sequential patterns", "Frequent sequential patterns"};
    }

    @Override
    public AlgorithmType getAlgorithmType() {
        return AlgorithmType.DATA_MINING;
    }

}
