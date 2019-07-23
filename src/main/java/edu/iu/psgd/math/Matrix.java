package edu.iu.psgd.math;

import edu.iu.psgd.exceptions.MatrixMultiplicationException;

public class Matrix {

    public static double[] scalarMultiply(double [] X, double y) {
        // TODO : make result one time get it from outside
        double [] result = new double [X.length];
        for (int i = 0; i < X.length; i++) {
            result[i] = X[i] * y;
        }
        return result;
    }

    public static double[] scalarMultiplyR(double [] X, double y, double [] result) {
        // TODO : make result one time get it from outside

        for (int i = 0; i < X.length; i++) {
            result[i] = X[i] * y;
        }
        return result;
    }

    public static double[] scalarDivide(double [] X, double y) {
        double [] result = new double [X.length];
        for (int i = 0; i < X.length; i++) {
            result[i] = X[i] / y;
        }
        return result;
    }

    public static double[] scalarDivideR(double [] X, double y, double[] result) {
        for (int i = 0; i < X.length; i++) {
            result[i] = X[i] / y;
        }
        return result;
    }

    public static double[] scalarAddition(double [] X, double y) {
        double [] result = new double [X.length];
        for (int i = 0; i < X.length; i++) {
            result[i] = X[i] + y;
        }
        return result;
    }

    public static double [] multiply(double [] X, double [] w) throws MatrixMultiplicationException {
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

    public static double [] multiplyR(double [] X, double [] w, double [] result) throws MatrixMultiplicationException {
        if(X.length == w.length) {
            for (int i = 0; i < X.length; i++) {
                result[i] = X[i] * w[i];
            }
            return result;
        }else {
            throw new MatrixMultiplicationException("Invalid Dimensions X.length "
                    + X.length + ", w.length : " + w.length );
        }
    }

    public static double [] divide(double [] X, double [] w) throws MatrixMultiplicationException {
        if(X.length == w.length) {
            double [] result = new double[X.length];
            for (int i = 0; i < X.length; i++) {
                result[i] = X[i] / w[i];
            }
            return result;
        }else {
            throw new MatrixMultiplicationException("Invalid Dimensions X.length "
                    + X.length + ", w.length : " + w.length );
        }
    }

    public static double [] sqrt(double [] X) throws MatrixMultiplicationException {
        if(X.length > 0) {
            double [] result = new double[X.length];
            for (int i = 0; i < X.length; i++) {
                result[i] = Math.sqrt(X[i]);
            }
            return result;
        }else {
            throw new MatrixMultiplicationException("Invalid Dimensions X.length "
                    + X.length );
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

    public static double [] subtractR(double [] w1, double [] w2, double[] result) throws MatrixMultiplicationException {
        if(w1.length == w2.length) {
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




    public static void printVector(double [] mat) {
        for (int i = 0; i < mat.length; i++) {
            System.out.print(mat[i]+" ");
        }
        System.out.println();
    }

    public static void printMatrix(double [][] mat) {
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                System.out.print(mat[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
