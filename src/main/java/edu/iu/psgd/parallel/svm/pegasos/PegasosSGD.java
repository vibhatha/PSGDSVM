package edu.iu.psgd.parallel.svm.pegasos;

import edu.iu.psgd.exceptions.MatrixMultiplicationException;
import edu.iu.psgd.exceptions.NullDataSetException;
import edu.iu.psgd.math.Initializer;
import edu.iu.psgd.math.Matrix;
import edu.iu.psgd.parallel.SGD;
import edu.iu.psgd.util.BlassUtils;
import mpi.MPI;
import mpi.MPIException;
import com.github.fommil.netlib.BLAS;

import java.util.logging.Logger;

public class PegasosSGD extends SGD {

    private static final Logger LOG = Logger.getLogger(PegasosSGD.class.getName());

    private boolean doLog = false;

    private int worldRank = 0;

    private int world_size = 0;

    public double communicationTime = 0;

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
        double commTime = 0;

        //trainingTime -= System.currentTimeMillis();
        if (worldRank == 0) {
            System.out.println("Iterations : " + iterations);
            System.out.println("Data Size : " + X.length);
        }
        int features = X[0].length;
        w = Initializer.initialWeights(features);
        double[] xi = null;
        double yi = -1;
        double condition = 1;
        // one time creation for Xyia and wa
        // represent X[][] => X1[] with offset (X1.length = X.length * X[0].length)
        double[] Xyia = new double[features];
        double[] wa;
        double[] globalW = Initializer.initZeros(features);
        double[] temp1 = new double[features];
        double[] temp2 = new double[features];
        double[] temp3 = new double[features];
        double[] temp4 = new double[features];
        double[] temp5 = new double[features];
        double[] temp6 = new double[features];
        globalW = new double[w.length];
        int count = 0;
        BLAS b = BLAS.getInstance();
        for (int epoch = 0; epoch < iterations; epoch++) {
//            if(epoch % 100 == 0 && worldRank == 0) {
//                if(true) {
//                    System.out.println((String.format("Epoch %d/%d", epoch, iterations)));
//                }
//            }
            for (int i = 0; i < X.length; i++) {
                xi = X[i];
                yi = y[i];
                // TODO: Java AVX Support :D
                condition = yi * Matrix.dot(xi, w);
                //condition = b.ddot(features, xi, 1, w, 1 );
                if (condition < 1) {
                    //TODO:  matrix mul library usage : pass output array from here
                    /*
                    * C++ Blass Corresponding
                    cblas_daxpy(features, alpha * y[j], X[j], 1, xiyi, 1.0);
                    cblas_daxpy(features, alpha , xiyi, 1, w, 1.0);*/

                    Xyia = Matrix.scalarMultiplyR(Matrix.subtractR(w, Matrix.scalarMultiplyR(xi, yi, temp1), temp2), alpha, temp3);

                    w = Matrix.subtractR(w, Xyia, temp4);
                } else {

                    wa = Matrix.scalarMultiplyR(w, alpha, temp5);
                    w = Matrix.subtractR(w, wa, temp6);
                }

            }
            try {
                commTime = MPI.wtime();
            } catch (MPIException e) {
                e.printStackTrace();
            }
            try {
                MPI.COMM_WORLD.allReduce(w, globalW, 1, MPI.DOUBLE, MPI.SUM);
            } catch (MPIException e) {
                System.out.println("Exception : " + e.getMessage());
            }
            try {
                communicationTime += MPI.wtime() - commTime;
            } catch (MPIException e) {
                e.printStackTrace();
            }
            w = Matrix.scalarDivideR(globalW, world_size, temp1);
        }

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

    @Override
    public void bSgd() throws NullDataSetException, MatrixMultiplicationException {
        if (isInvalid) {
            throw new NullDataSetException("Invalid data source with no features or no data");
        } else {
            if (doLog) {
                LOG.info(String.format("X.shape (%d,%d), Y.shape (%d)", X.length, X[0].length, y.length));
            }
        }
        double commTime = 0;

        //trainingTime -= System.currentTimeMillis();
        if (worldRank == 0) {
            System.out.println("Iterations : " + iterations);
            System.out.println("Data Size : " + X.length);
        }
        int features = X[0].length;
        w = Initializer.initialWeights(features);
        double[] xi = null;
        double yi = -1;
        double condition = 1;
        // one time creation for Xyia and wa
        // represent X[][] => X1[] with offset (X1.length = X.length * X[0].length)
        double[] Xyia = new double[features];
        double[] wa;
        double[] globalW = Initializer.initZeros(features);
        double[] temp1 = new double[features];
        double[] temp2 = new double[features];
        double[] temp3 = new double[features];
        double[] temp4 = new double[features];
        double[] temp5 = new double[features];
        double[] temp6 = new double[features];
        globalW = new double[w.length];
        int count = 0;
        long t1 = System.currentTimeMillis();
        BLAS b = BLAS.getInstance();
        long t2 = System.currentTimeMillis();
        final int incX = 1;
        final int incY  = 1;
        for (int epoch = 0; epoch < iterations; epoch++) {
//            if(epoch % 100 == 0 && worldRank == 0) {
//                if(true) {
//                    System.out.println((String.format("Epoch %d/%d", epoch, iterations)));
//                }
//            }
            for (int i = 0; i < X.length; i++) {
                xi = X[i];
                yi = y[i];
                // TODO: Java AVX Support :D
                //condition = yi * Matrix.dot(xi, w);
                condition = yi * b.ddot(features, xi, 1, w, 1);
                if (condition < 1) {
                    //TODO:  matrix mul library usage : pass output array from here
                    /*
                    * C++ Blass Corresponding
                    cblas_daxpy(features, alpha * y[j], X[j], 1, xiyi, 1.0);
                    cblas_daxpy(features, alpha , xiyi, 1, w, 1.0);*/

                    b.daxpy(features, alpha * yi, xi, incX, Xyia, incY);
                    b.daxpy(features, alpha, Xyia, incX, w, incY);
                    //Xyia = Matrix.scalarMultiplyR(Matrix.subtractR(w, Matrix.scalarMultiplyR(xi, yi, temp1), temp2), alpha, temp3);

                    //w = Matrix.subtractR(w, Xyia, temp4);
                } else {
                    /*
                     * C++ Blass Corresponding
                     * cblas_daxpy(features, alpha , w, 1, w, 1.0);
                     * */
                    b.daxpy(features, alpha, w, incX, w, incY);
                    //wa = Matrix.scalarMultiplyR(w, alpha, temp5);
                    //w = Matrix.subtractR(w, wa, temp6);
                }

            }
            try {
                commTime = MPI.wtime();
            } catch (MPIException e) {
                e.printStackTrace();
            }
            try {
                MPI.COMM_WORLD.allReduce(w, globalW, 1, MPI.DOUBLE, MPI.SUM);
            } catch (MPIException e) {
                System.out.println("Exception : " + e.getMessage());
            }
            try {
                communicationTime += MPI.wtime() - commTime;
            } catch (MPIException e) {
                e.printStackTrace();
            }
            w = Matrix.scalarDivideR(globalW, world_size, temp1);
        }

        //Matrix.printVector(w);
        //trainingTime += System.currentTimeMillis();
        //trainingTime /= 1000.0;
        //LOG.info(String.format("Rank[%d] Training Time  %s s", worldRank, Long.toString(trainingTime)));
        System.out.println("Count : " + count);
        System.out.println("Lib Load Time : " + (double) (t2 - t1) / 1000.0 + ", d : " + condition);
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
