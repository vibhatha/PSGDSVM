package edu.iu.psgd.parallel;

import edu.iu.psgd.exceptions.MatrixMultiplicationException;
import edu.iu.psgd.exceptions.NullDataSetException;

public abstract class SGD {

    protected double[][] X;
    protected double[] y;
    protected double alpha = 0.01;
    protected int features = 1;
    protected int samples = 1;
    protected boolean isInvalid = false;
    protected double [] w;
    protected int iterations = 100;
    protected long trainingTime = 0;
    protected long testingTime = 0;
    protected long dataLoadingTime = 0;

    public SGD (double[][] X, double[] y, double alpha, int iterations) {
        this.X = X;
        this.y = y;
        this.alpha = alpha;
        if (X.length == 0) {
            this.isInvalid = true;
        }

        if (X.length > 0) {
            if (X[0].length < 1) {
                this.isInvalid = true;
            } else {
                this.samples = X.length;
                this.features = X[0].length;
                this.w = new double[this.features];
            }

        }
        this.iterations = iterations;

    }

    public abstract void sgd() throws NullDataSetException, MatrixMultiplicationException;

    public double[] getW() {
        return w;
    }
}
