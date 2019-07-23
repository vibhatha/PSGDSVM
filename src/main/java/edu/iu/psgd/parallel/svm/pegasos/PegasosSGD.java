package edu.iu.psgd.parallel.svm.pegasos;

import edu.iu.psgd.exceptions.MatrixMultiplicationException;
import edu.iu.psgd.exceptions.NullDataSetException;
import edu.iu.psgd.math.Initializer;
import edu.iu.psgd.math.Matrix;
import edu.iu.psgd.parallel.SGD;
import mpi.MPI;
import mpi.MPIException;

import java.util.logging.Logger;

public class PegasosSGD extends SGD {

    private static final Logger LOG = Logger.getLogger(PegasosSGD.class.getName());

    private boolean doLog = false;

    private int worldRank = 0;

    private int world_size = 0;

    public PegasosSGD(double[][] X, double[] y, double alpha, int iterations) {
        super(X, y, alpha, iterations);
    }

    @Override
    public void sgd() throws NullDataSetException, MatrixMultiplicationException {
        if (isInvalid) {
            throw new NullDataSetException("Invalid data source with no features or no data");
        } else {
            if (doLog) {
                LOG.info(String.format("X.shape (%d,%d), Y.shape (%d)", X.length, X[0].length, y.length));
            }
        }
        //trainingTime -= System.currentTimeMillis();
        int features = X[0].length;
        w = Initializer.initialWeights(features);
        double[] xi = null;
        double yi = -1;
        double condition = 1;
        // one time creation for Xyia and wa
        // represent X[][] => X1[] with offset (X1.length = X.length * X[0].length)
        double[] Xyia = null;
        double[] wa;
        double[] globalW = Initializer.initZeros(features);

        for (int epoch = 0; epoch < iterations; epoch++) {
//            if(epoch % 10 == 0) {
//                if(doLog) {
//                    System.out.println((String.format("Epoch %d/%d", epoch, iterations)));
//                }
//            }
            for (int i = 0; i < X.length; i++) {
                xi = X[i];
                yi = y[i];
                // TODO: Java AVX Support :D
                condition = yi * Matrix.dot(xi, w);
                //System.out.println(condition);
                if (condition < 1) {
                    Xyia = new double[X.length];
                    //TODO:  matrix mul library usage : pass output array from here
                    Xyia = Matrix.scalarMultiply(Matrix.subtract(w, Matrix.scalarMultiply(xi, yi)), alpha);
                    w = Matrix.subtract(w, Xyia);
                } else {
                    wa = new double[w.length];
                    wa = Matrix.scalarMultiply(w, alpha);
                    w = Matrix.subtract(w, wa);
                }
            }
            globalW = new double[w.length];
            try {
                MPI.COMM_WORLD.allReduce(w, globalW, 1, MPI.DOUBLE, MPI.SUM);
            } catch (MPIException e) {
                System.out.println("Exception : " + e.getMessage());
            }
            w = Matrix.scalarDivide(globalW, world_size);
        }

        //Matrix.printVector(w);
        //trainingTime += System.currentTimeMillis();
        //trainingTime /= 1000.0;
        //LOG.info(String.format("Rank[%d] Training Time  %s s", worldRank, Long.toString(trainingTime)));
    }

    @Override
    public void sgdEnsemble() throws NullDataSetException, MatrixMultiplicationException {
        if (isInvalid) {
            throw new NullDataSetException("Invalid data source with no features or no data");
        } else {
            if (doLog) {
                LOG.info(String.format("X.shape (%d,%d), Y.shape (%d)", X.length, X[0].length, y.length));
            }
        }
        //trainingTime -= System.currentTimeMillis();
        int features = X[0].length;
        w = Initializer.initialWeights(features);
        double[] xi = null;
        double yi = -1;
        double condition = 1;
        double[] Xyia = null;
        double[] wa;
        double[] globalW = Initializer.initZeros(features);

        for (int epoch = 0; epoch < iterations; epoch++) {
//            if(epoch % 10 == 0) {
//                if(doLog) {
//                    System.out.println((String.format("Epoch %d/%d", epoch, iterations)));
//                }
//            }
            for (int i = 0; i < X.length; i++) {
                xi = X[i];
                yi = y[i];
                condition = yi * Matrix.dot(xi, w);
                //System.out.println(condition);

                if (condition < 1) {
                    Xyia = new double[X.length];
                    Xyia = Matrix.scalarMultiply(Matrix.subtract(w, Matrix.scalarMultiply(xi, yi)), alpha);
                    w = Matrix.subtract(w, Xyia);
                } else {
                    wa = new double[w.length];
                    wa = Matrix.scalarMultiply(w, alpha);
                    w = Matrix.subtract(w, wa);
                }
            }

        }

        //Matrix.printVector(w);
        //trainingTime += System.currentTimeMillis();
        //trainingTime /= 1000.0;
        //LOG.info(String.format("Rank[%d] Training Time  %s s", worldRank, Long.toString(trainingTime)));
    }

    public int getWorldRank() {
        return worldRank;
    }

    public void setWorldRank(int worldRank) {
        this.worldRank = worldRank;
    }

    public boolean isDoLog() {
        return doLog;
    }

    public void setDoLog(boolean doLog) {
        this.doLog = doLog;
    }

    public int getWorld_size() {
        return world_size;
    }

    public void setWorld_size(int world_size) {
        this.world_size = world_size;
    }
}
