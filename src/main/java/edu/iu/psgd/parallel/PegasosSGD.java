package edu.iu.psgd.parallel;

import edu.iu.psgd.exceptions.NullDataSetException;

import java.util.Arrays;
import java.util.logging.Logger;

public class PegasosSGD extends SGD {

    private static final Logger LOG = Logger.getLogger(PegasosSGD.class.getName());

    public PegasosSGD(double[][] X, double[] y, double alpha, int iterations) {
        super(X, y, alpha, iterations);
    }

    @Override
    public void sgd() throws NullDataSetException {
        if (isInvalid) {
            throw new NullDataSetException("Invalid data source with no features or no data");
        } else {
            LOG.info(String.format("X.shape (%d,%d), Y.shape (%d)", X.length, X[0].length, y.length));
        }
        for(int epoch=0; epoch<iterations; epoch++) {
            for (int i = 0; i < samples; i++) {
                double [] xi = X[i];
                double yi = y[i];
                LOG.info(String.format("X :  %s, y : %f ", Arrays.toString(xi), yi));
            }
        }
    }
}
