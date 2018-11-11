package edu.iu.psgd.predict;

import edu.iu.psgd.exceptions.MatrixMultiplicationException;
import edu.iu.psgd.math.Matrix;

public class Predict {

    private double [][] Xtest;
    private double [] ytest;
    private double [] w;
    private double accuracy = 0.0;
    private int correctCount = 0;

    public Predict(double[][] xtest, double[] ytest, double[] w) {
        Xtest = xtest;
        this.ytest = ytest;
        this.w = w;
    }

    public double predict() throws MatrixMultiplicationException {

        for (int i = 0; i < Xtest.length; i++) {
            double pred = 1;
            double d = Matrix.dot(Xtest[i],w);
            pred = Math.signum(d);
            if(ytest[i] == pred) {
                correctCount++;
            }
            //System.out.println(pred+"/"+ytest[i]+ " ==> " + d);
        }
        accuracy = ((double)correctCount / (double)Xtest.length) * 100.0;

        return accuracy;
    };
}
