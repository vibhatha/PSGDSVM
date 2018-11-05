package edu.iu.psgd.math;

import edu.iu.psgd.exceptions.MatrixMultiplicationException;

public class Matrix {

    public static double[] scalarMultiply(double [] X, double y) {
        double [] result = new double [X.length];
        for (int i = 0; i < X.length; i++) {
            result[i] = X[i] * y;
        }
        return result;
    }

    public static double [] inner(double [] X, double [] w) throws MatrixMultiplicationException {
        if(X.length == w.length) {
            double [] result = new double[X.length];
            for (int i = 0; i < X.length; i++) {
                result[i] = X[i] * w[i];
            }
            return result;
        }else {
            throw new MatrixMultiplicationException("Invalid Dimensions X.length "
                    + X.length + ", w.length : " + w.length );
        }
    }

    public static double [] add(double [] w1, double [] w2) throws MatrixMultiplicationException {
        if(w1.length == w2.length) {
            double [] result = new double[w1.length];
            for (int i = 0; i < w1.length; i++) {
                result[i] = w1[i] + w2[i];
            }
            return result;
        }else {
            throw new MatrixMultiplicationException("Invalid Dimensions X.length "
                    + w1.length + ", w.length : " + w2.length );
        }
    }

    public static double [] subtract(double [] w1, double [] w2) throws MatrixMultiplicationException {
        if(w1.length == w2.length) {
            double [] result = new double[w1.length];
            for (int i = 0; i < w1.length; i++) {
                result[i] = w1[i] - w2[i];
            }
            return result;
        }else {
            throw new MatrixMultiplicationException("Invalid Dimensions X.length "
                    + w1.length + ", w.length : " + w2.length );
        }
    }

    public static double dot(double [] X, double [] w) throws MatrixMultiplicationException {
        if(X.length == w.length) {
            double result = 0;
            for (int i = 0; i < X.length; i++) {
                result += X[i] * w[i];
            }
            return result;
        }else {
            throw new MatrixMultiplicationException("Invalid Dimensions X.length "
                    + X.length + ", w.length : " + w.length );
        }
    }
}
