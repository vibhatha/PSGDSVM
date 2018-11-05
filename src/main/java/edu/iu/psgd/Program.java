package edu.iu.psgd;

import edu.iu.psgd.api.data.DataSet;
import edu.iu.psgd.exceptions.NullDataSetException;
import edu.iu.psgd.parallel.PegasosSGD;

import java.util.logging.Logger;

public class Program {

    private static final Logger LOG = Logger.getLogger(Program.class.getName());

    public static void main(String[] args) throws NullDataSetException {

        String dataset = "a9a";
        String basePath = "/home/vibhatha/";
        int features = 123;
        int samples = 32561;
        String dataFile = "data/svm/"+dataset+"/training.csv";
        String sourceFile = basePath + dataFile;
        DataSet dataSet = new DataSet(sourceFile, features, samples);
        dataSet.load();
        double [][] X = dataSet.getX();
        double [] y = dataSet.getY();
        PegasosSGD pegasosSGD = new PegasosSGD(X, y, 0.01, 200);
        pegasosSGD.sgd();
    }
}
