package edu.iu.psgd.api.data;


import edu.iu.psgd.api.io.CsvFile;
import edu.iu.psgd.api.io.ReadCSV;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

public class DataSet {

    private static final Logger LOG = Logger.getLogger(DataSet.class.getName());

    private String sourceFile;
    private int features=1;
    private int datasize=1;
    private double ratio=1.0;
    private boolean isSplit=false;
    private double [][] Xtrain;
    private double [] ytrain;
    private double [][] Xtest;
    private double [] ytest;
    private String trainFile;
    private String testFile;

    public DataSet(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    public DataSet(String sourceFile, int features, int datasize) {
        this.sourceFile = sourceFile;
        this.features = features;
        this.datasize = datasize;
        this.isSplit = true;
    }

    public DataSet(String sourceFile, int features, int datasize, double ratio, boolean isSplit) {
        this.sourceFile = sourceFile;
        this.features = features;
        this.datasize = datasize;
        this.ratio = ratio;
        this.isSplit = isSplit;
    }

    public DataSet(int features, String trainFile, String testFile) {
        this.features = features;
        this.trainFile = trainFile;
        this.testFile = testFile;
    }

    // TODO:  deal with exception throwing from I/O
    public void load() {
        if(this.isSplit == false) {
            CsvFile csvFile = new CsvFile(this.trainFile, "csv");
            ReadCSV readCSV = new ReadCSV(csvFile);
            readCSV.readX();
            ArrayList<double[]> xvals =  readCSV.getxVals();
            Collections.shuffle(xvals);
            int samples = xvals.size();
            Xtrain = new double[samples][this.features];
            ytrain = new double[samples];
            for (int i = 0; i < samples; i++) {
                double [] row = xvals.get(i);
                ytrain[i] = row[0];
                for (int j = 1; j < row.length; j++) {
                    Xtrain[i][j-1] = row[j];
                }
            }

            CsvFile csvFileTest = new CsvFile(this.testFile, "csv");
            ReadCSV readCSVTest = new ReadCSV(csvFileTest);
            readCSVTest.readX();
            ArrayList<double[]> xvalsTest =  readCSVTest.getxVals();
            //Collections.shuffle(xvalsTest);
            int samplesTest = xvalsTest.size();
            Xtest = new double[samplesTest][this.features];
            ytest = new double[samplesTest];
            for (int i = 0; i < samplesTest; i++) {
                double [] row = xvalsTest.get(i);
                ytest[i] = row[0];
                for (int j = 1; j < row.length; j++) {
                    Xtest[i][j-1] = row[j];
                }
            }


        }

        if(this.isSplit == true) {
            CsvFile csvFile = new CsvFile(this.sourceFile, "csv");
            ReadCSV readCSV = new ReadCSV(csvFile);
            readCSV.readX();
            ArrayList<double[]> xvals =  readCSV.getxVals();
            Collections.shuffle(xvals);
            int samples = xvals.size();
            Xtrain = new double[samples][this.features];
            ytrain = new double[samples];
            for (int i = 0; i < samples; i++) {
                double [] row = xvals.get(i);
                ytrain[i] = row[0];
                for (int j = 1; j < row.length; j++) {
                    Xtrain[i][j-1] = row[j];
                }
            }
        }

    }

    public double[][] getXtrain() {
        return Xtrain;
    }

    public double[] getYtrain() {
        return ytrain;
    }

    public double[][] getXtest() {
        return Xtest;
    }

    public double[] getYtest() {
        return ytest;
    }
}
