package edu.iu.psgd;

import edu.iu.psgd.api.data.DataSet;
import edu.iu.psgd.exceptions.MatrixMultiplicationException;
import edu.iu.psgd.exceptions.NullDataSetException;
import edu.iu.psgd.parallel.svm.adam.AdamSGD;
import edu.iu.psgd.parallel.svm.pegasos.PegasosSGD;
import edu.iu.psgd.predict.Predict;
import edu.iu.psgd.resource.ResourceManager;
import edu.iu.psgd.util.OptArgs;
import edu.iu.psgd.util.Params;
import org.apache.commons.cli.ParseException;

import java.util.logging.Logger;

public class Program {

    private static final Logger LOG = Logger.getLogger(Program.class.getName());

    public static void main(String[] args) throws NullDataSetException, ParseException, MatrixMultiplicationException {

//        String dataset = "a9a";
//        String basePath = "/home/vibhatha/";
//        int features = 123;
//        int samples = 32561;
//        String dataFile = "data/svm/"+dataset+"/training.csv";
//        String sourceFile = basePath + dataFile;
//        DataSet dataSet = new DataSet(sourceFile, features, samples);
//        dataSet.load();
//        double [][] X = dataSet.getXtrain();
//        double [] y = dataSet.getYtrain();
//        PegasosSGD pegasosSGD = new PegasosSGD(X, y, 0.01, 1);
//        pegasosSGD.sgd();
        OptArgs optArgs = new OptArgs(args);
        optArgs.getArgs();
        Params params = optArgs.getParams();
        ResourceManager resourceManager = new ResourceManager(params);
        DataSet dataSet = resourceManager.load();
        double[][] X = dataSet.getXtrain();
        //Matrix.printMatrix(X);
        double[] y = dataSet.getYtrain();

        double[][] Xtest = dataSet.getXtest();
        //Matrix.printMatrix(X);
        double[] ytest = dataSet.getYtest();

        double[][] yp = new double[1][params.getFeatures()];
        yp[0] = y;
        //Matrix.printMatrix(yp);
        //PegasosSGD pegasosSGD = new PegasosSGD(X, y, params.getAlpha(), params.getIterations());
        //pegasosSGD.sgd();
        double[] beta1 = {0.5, 0.55, 0.6, 0.65, 0.7, 0.75, 0.8, 0.85, 0.90, 0.93, 0.95, 0.99, 0.999};
        double[] beta2 = {0.5, 0.55, 0.6, 0.65, 0.7, 0.75, 0.8, 0.85, 0.90, 0.93, 0.95, 0.99, 0.999};

        for (int j = 0; j < beta1.length; j++) {
            for (int k = 0; k < beta2.length; k++) {
                for (int i = 0; i < 1; i++) {
                    AdamSGD adamSGD = new AdamSGD(X, y, params.getAlpha(), params.getIterations(), beta1[j], beta2[k]);
                    adamSGD.sgd();
                    double[] wFinal = adamSGD.getW();
                    Predict predict = new Predict(Xtest, ytest, wFinal);
                    double acc = predict.predict();
                    System.out.println("Prediction Accuracy : " + acc + " %");
                }
            }
        }
//        AdamSGD adamSGD = new AdamSGD(X, y, params.getAlpha(), params.getIterations(), 0.55, 0.75);
//        adamSGD.sgd();
//        double [] wFinal = adamSGD.getW();
//        Predict predict = new Predict(Xtest, ytest, wFinal);
//        double acc = predict.predict();
//        System.out.println("Prediction Accuracy : " + acc + " %");

    }
}
