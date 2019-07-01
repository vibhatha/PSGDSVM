package edu.iu.psgd.util;

import edu.iu.psgd.constant.SVMType;

public class Params {

    private String dataset = "";
    private int iterations = 100;
    private double alpha = 0.01;
    private String basePath = "";
    private boolean split = false;
    private int features = 1;
    private int trainingSamples = 1;
    private int testingSamples = 1;
    private int noOfThreads = 1;
    private int noOfWorkers = 1;
    private double splitRatio = 0.80;
    private String logSavePath = "";
    private SVMType svmType = SVMType.ENSEMBLE;

    public Params(String dataset, int iterations, double alpha, String basePath, boolean split, int features,
                  int trainingSamples, int testingSamples, int noOfThreads, int noOfWorkers, double splitRatio) {
        this.dataset = dataset;
        this.iterations = iterations;
        this.alpha = alpha;
        this.basePath = basePath;
        this.split = split;
        this.features = features;
        this.trainingSamples = trainingSamples;
        this.testingSamples = testingSamples;
        this.noOfThreads = noOfThreads;
        this.noOfWorkers = noOfWorkers;
        this.splitRatio = splitRatio;
    }

    public Params(String dataset, int iterations, double alpha, String basePath, boolean split, int features, int trainingSamples, int testingSamples, int noOfThreads, int noOfWorkers, double splitRatio, String logSavePath) {
        this.dataset = dataset;
        this.iterations = iterations;
        this.alpha = alpha;
        this.basePath = basePath;
        this.split = split;
        this.features = features;
        this.trainingSamples = trainingSamples;
        this.testingSamples = testingSamples;
        this.noOfThreads = noOfThreads;
        this.noOfWorkers = noOfWorkers;
        this.splitRatio = splitRatio;
        this.logSavePath = logSavePath;
    }

    public Params(String dataset, int iterations, double alpha, String basePath, boolean split, int features, int trainingSamples, int testingSamples, int noOfThreads, int noOfWorkers, double splitRatio, String logSavePath, SVMType svmType) {
        this.dataset = dataset;
        this.iterations = iterations;
        this.alpha = alpha;
        this.basePath = basePath;
        this.split = split;
        this.features = features;
        this.trainingSamples = trainingSamples;
        this.testingSamples = testingSamples;
        this.noOfThreads = noOfThreads;
        this.noOfWorkers = noOfWorkers;
        this.splitRatio = splitRatio;
        this.logSavePath = logSavePath;
        this.svmType = svmType;
    }

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public boolean isSplit() {
        return split;
    }

    public void setSplit(boolean split) {
        this.split = split;
    }

    public int getFeatures() {
        return features;
    }

    public void setFeatures(int features) {
        this.features = features;
    }

    public int getTrainingSamples() {
        return trainingSamples;
    }

    public void setTrainingSamples(int trainingSamples) {
        this.trainingSamples = trainingSamples;
    }

    public int getTestingSamples() {
        return testingSamples;
    }

    public void setTestingSamples(int testingSamples) {
        this.testingSamples = testingSamples;
    }

    public int getNoOfThreads() {
        return noOfThreads;
    }

    public void setNoOfThreads(int noOfThreads) {
        this.noOfThreads = noOfThreads;
    }

    public int getNoOfWorkers() {
        return noOfWorkers;
    }

    public void setNoOfWorkers(int noOfWorkers) {
        this.noOfWorkers = noOfWorkers;
    }

    public double getSplitRatio() {
        return splitRatio;
    }

    public void setSplitRatio(double splitRatio) {
        this.splitRatio = splitRatio;
    }

    public SVMType getSvmType() {
        return svmType;
    }

    public void setSvmType(SVMType svmType) {
        this.svmType = svmType;
    }

    public String getLogSavePath() {
        return logSavePath;
    }

    public void setLogSavePath(String logSavePath) {
        this.logSavePath = logSavePath;
    }

    @Override
    public String toString() {
        return "Params{" +
                "dataset='" + dataset + '\'' +
                ", iterations=" + iterations +
                ", alpha=" + alpha +
                ", basePath='" + basePath + '\'' +
                ", split=" + split +
                ", features=" + features +
                ", trainingSamples=" + trainingSamples +
                ", testingSamples=" + testingSamples +
                ", noOfThreads=" + noOfThreads +
                ", noOfWorkers=" + noOfWorkers +
                ", splitRatio=" + splitRatio +
                ", logSavePath='" + logSavePath + '\'' +
                ", svmType=" + svmType +
                '}';
    }
}
