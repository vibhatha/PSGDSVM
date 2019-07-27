package edu.iu.psgd.parallel.svm.adam;

import edu.iu.psgd.exceptions.MatrixMultiplicationException;
import edu.iu.psgd.exceptions.NullDataSetException;
import edu.iu.psgd.math.Initializer;
import edu.iu.psgd.math.Matrix;
import edu.iu.psgd.parallel.SGD;

import java.util.logging.Logger;

public class AdamSGD extends SGD {

    private static final Logger LOG = Logger.getLogger(AdamSGD.class.getName());
    private double beta1 = 0.93;
    private double beta2 = 0.999;

    public AdamSGD(double[][] X, double[] y, double alpha, int iterations, double beta1, double beta2) {
        super(X, y, alpha, iterations);
        this.beta1 = beta1;
        this.beta2 = beta2;
    }

    @Override
    public void sgd() throws NullDataSetException, MatrixMultiplicationException {
        if (isInvalid) {
            throw new NullDataSetException("Invalid data source with no features or no data");
        } else {
            //LOG.info(String.format("X.shape (%d,%d), Y.shape (%d)", X.length, X[0].length, y.length));
        }
        System.out.println(String.format("Beta1 %f , Beta2 %f ", beta1, beta2));
        trainingTime -= System.currentTimeMillis();
        int features = X[0].length;
        w = Initializer.initialWeights(features);
        double [] v = Initializer.initZeros(features);
        double [] r = Initializer.initZeros(features);
        double [] v1 = Initializer.initZeros(features);
        double [] r1 = Initializer.initZeros(features);
        double [] v2 = Initializer.initZeros(features);
        double [] r2 = Initializer.initZeros(features);
        double [] v_hat = Initializer.initZeros(features);
        double [] r_hat = Initializer.initZeros(features);
        double epsilon = 0.00000001;
        double [] gradient = Initializer.initZeros(features);
        double [] w1 = Initializer.initZeros(features);
        double [] w2 = Initializer.initZeros(features);

        for(int epoch=1; epoch<iterations; epoch++) {
            if(epoch % 10 == 0) {
                //System.out.println((String.format("Epoch %d/%d", epoch, iterations)));
            }
            for (int i = 0; i < X.length; i++) {
                double [] xi = X[i];
                double yi = y[i];
                double condition = yi * Matrix.dot(xi,w);
                double coefficient = 1.0 / (1.0 + (double)epoch);
                //System.out.println(condition);

                if(condition < 1) {
                    gradient = Matrix.scalarMultiply(Matrix.subtract(w,Matrix.scalarMultiply(xi, yi)), alpha);
                } else {
                    gradient = Matrix.scalarMultiply(w, alpha);
                }
                v1 = Matrix.scalarMultiply(v, beta1);
                v2 = Matrix.scalarMultiply(gradient, (1-beta1));
                v = Matrix.add(v1, v2);
                v_hat = Matrix.scalarDivide(v, (1-(Math.pow(beta1, (double)epoch))));
                r1 = Matrix.scalarMultiply(r, beta2);
                r2 = Matrix.scalarMultiply(Matrix.multiply(gradient,gradient),(1-beta2));
                r = Matrix.add(r1,r2);
                r_hat = Matrix.scalarDivide(r,(1-(Math.pow(beta2, (double)epoch))));
                w1 = Matrix.scalarAddition(Matrix.sqrt(r_hat), epsilon);
                w2 = Matrix.divide(v_hat, w1);
                w = Matrix.subtract(w, Matrix.scalarMultiply(w2, alpha));
            }
        }
        //Matrix.printVector(w);
        trainingTime += System.currentTimeMillis();
        trainingTime /= 1000.0;
        System.out.println((String.format("Training Time  %s s", Long.toString(trainingTime))));
    }

    @Override
    public void sgdEnsemble() throws NullDataSetException, MatrixMultiplicationException {

    }

    @Override
    public void bSgd() throws NullDataSetException, MatrixMultiplicationException {

    }


}
