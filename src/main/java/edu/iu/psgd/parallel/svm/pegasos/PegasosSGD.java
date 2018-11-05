package edu.iu.psgd.parallel.svm.pegasos;

import edu.iu.psgd.exceptions.MatrixMultiplicationException;
import edu.iu.psgd.exceptions.NullDataSetException;
import edu.iu.psgd.math.Initializer;
import edu.iu.psgd.math.Matrix;
import edu.iu.psgd.parallel.SGD;

import java.util.logging.Logger;

public class PegasosSGD extends SGD {

    private static final Logger LOG = Logger.getLogger(PegasosSGD.class.getName());

    public PegasosSGD(double[][] X, double[] y, double alpha, int iterations) {
        super(X, y, alpha, iterations);
    }

    @Override
    public void sgd() throws NullDataSetException, MatrixMultiplicationException {
        if (isInvalid) {
            throw new NullDataSetException("Invalid data source with no features or no data");
        } else {
            LOG.info(String.format("X.shape (%d,%d), Y.shape (%d)", X.length, X[0].length, y.length));
        }
        trainingTime -= System.currentTimeMillis();
        int features = X[0].length;
        w = Initializer.initialWeights(features);
        for(int epoch=0; epoch<iterations; epoch++) {
            for (int i = 0; i < X.length; i++) {
                double [] xi = X[i];
                double yi = y[i];
                double condition = yi * Matrix.dot(xi,w);
                if(condition < 1) {
                    double [] Xyia = new double[X.length];
                    Xyia = Matrix.scalarMultiply(Matrix.scalarMultiply(xi, yi), alpha);
                    w = Matrix.subtract(w, Xyia);
                } else {
                    double [] wa = new double[w.length];
                    wa = Matrix.scalarMultiply(w, alpha);
                    w = Matrix.subtract(w, wa);
                }
            }
        }
        trainingTime += System.currentTimeMillis();
        trainingTime /= 1000;
    }
}
