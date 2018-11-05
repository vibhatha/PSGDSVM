package edu.iu.psgd.util;

import edu.iu.psgd.constant.Constant;
import org.apache.commons.cli.*;

public class OptArgs {

    private String [] args;
    private Params params;

    public OptArgs(String [] args) {
        this.args = args;
    }

    public void getArgs() throws ParseException {
        Options options = new Options();
        options.addOption(Constant.DATASET, true, "Dataset name (ex: a9a");
        options.addOption(Constant.ITERATIONS, true, "Iterations ");
        options.addOption(Constant.ALPHA, true, "Alpha (learning rate) ");
        options.addOption(Constant.IS_SPLIT, false, "Splitting dataset (ex: true or false ");
        options.addOption(Constant.FEATURES, true, "Features in the dataset ");
        options.addOption(Constant.TRAINING_SAMPLES, true, "Training samples in the dataset ");
        options.addOption(Utils.createOption(Constant.TESTING_SAMPLES, true, "Testing samples in the dataset ", false));
        options.addOption(Utils.createOption(Constant.NO_OF_THREADS, true, "No of threads", false));
        options.addOption(Utils.createOption(Constant.NO_OF_WORKERS, true, "No of workers", false));
        options.addOption(Utils.createOption(Constant.SPLIT_RATIO, true, "Training and Testing data split ration (ex: 0.80)", false));

        CommandLineParser commandLineParser = new DefaultParser();
        CommandLine cmd = commandLineParser.parse(options, this.args);

        String dataset = cmd.getOptionValue(Constant.DATASET);
        int iterations = Integer.parseInt(cmd.getOptionValue(Constant.ITERATIONS));
        double alpha = Double.parseDouble(cmd.getOptionValue(Constant.ALPHA));
        boolean isSplit = cmd.hasOption(Constant.IS_SPLIT);
        int features = Integer.parseInt(cmd.getOptionValue(Constant.FEATURES));
        int trainingSamples = Integer.parseInt(cmd.getOptionValue(Constant.TRAINING_SAMPLES));

        int testingSamples = 0;
        int noOfThreads = 1;
        int noOfWorkers = 1;
        double splitRation = 1.0;

        if(cmd.hasOption(Constant.TESTING_SAMPLES)) {
            testingSamples = Integer.parseInt(cmd.getOptionValue(Constant.TESTING_SAMPLES));
        }

        if(cmd.hasOption(Constant.NO_OF_THREADS)) {
            noOfThreads = Integer.parseInt(cmd.getOptionValue(Constant.NO_OF_THREADS));
        }

        if(cmd.hasOption(Constant.NO_OF_WORKERS)) {
            noOfWorkers = Integer.parseInt(cmd.getOptionValue(Constant.NO_OF_WORKERS));
        }

        if(cmd.hasOption(Constant.SPLIT_RATIO)) {
            splitRation = Double.parseDouble(cmd.getOptionValue(Constant.SPLIT_RATIO));
        }

        params = new Params(dataset, iterations, alpha, "", isSplit, features, trainingSamples, testingSamples, noOfThreads, noOfWorkers, splitRation);
    }

    public Params getParams() {
        return params;
    }
}
