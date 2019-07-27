package edu.iu.psgd.util;

import edu.iu.psgd.exceptions.MatrixMultiplicationException;
import edu.iu.psgd.math.Matrix;

import java.util.Random;

import static org.bytedeco.openblas.global.openblas.*;

public class BlassUtils {
    public static void print_matrix_rowmajor(String desc, int m, int n, double[] mat, int ldm) {
        int i, j;
        System.out.printf("\n %s\n", desc);

        for (i = 0; i < m; i++) {
            for (j = 0; j < n; j++) System.out.printf(" %6.2f", mat[i * ldm + j]);
            System.out.printf("\n");
        }
    }


    /* Auxiliary routine: printing a matrix */
    public static void print_matrix_colmajor(String desc, int m, int n, double[] mat, int ldm) {
        int i, j;
        System.out.printf("\n %s\n", desc);

        for (i = 0; i < m; i++) {
            for (j = 0; j < n; j++) System.out.printf(" %6.2f", mat[i + j * ldm]);
            System.out.printf("\n");
        }
    }

    /* Auxiliary routine: printing a vector of integers */
    public static void print_vector(String desc, int n, int[] vec) {
        int j;
        System.out.printf("\n %s\n", desc);
        for (j = 0; j < n; j++) System.out.printf(" %6i", vec[j]);
        System.out.printf("\n");
    }

    public static void run() {
        blas_set_num_threads(4);
        System.out.println("vendor = " + blas_get_vendor() + ", num_threads = " + blas_get_num_threads());

        /* Locals */
        double[] A = {1, 1, 1, 2, 3, 4, 3, 5, 2, 4, 2, 5, 5, 4, 3};
        double[] b = {-10, -3, 12, 14, 14, 12, 16, 16, 18, 16};
        int info, m, n, lda, ldb, nrhs;
        int i, j;

        /* Initialization */
        m = 5;
        n = 3;
        nrhs = 2;
        lda = 3;
        ldb = 2;

        /* Print Entry Matrix */
        print_matrix_rowmajor("Entry Matrix A", m, n, A, lda);
        /* Print Right Rand Side */
        print_matrix_rowmajor("Right Hand Side b", n, nrhs, b, ldb);
        System.out.println();

        /* Executable statements */
        System.out.println("LAPACKE_dgels (row-major, high-level) Example Program Results");
        /* Solve least squares problem*/
        info = LAPACKE_dgels(LAPACK_ROW_MAJOR, (byte) 'N', m, n, nrhs, A, lda, b, ldb);

        /* Print Solution */
        print_matrix_rowmajor("Solution", n, nrhs, b, ldb);
        System.out.println();
        System.exit(0);
    }

    public static void withBlas() {
        blas_set_num_threads(1);
        final int features = 22;
        final int samples = 1000;
        final int iterations = 1000;
        double yixiw = -1;
        final double alpha = 0.001;
        double[] xiyi = new double[features];
        double[][]X = new double[samples][features];
        double[] y = new double[samples];
        double[] w = new double[features];
        Random r = new Random();
        for (int i = 0; i < samples; i++) {
            y[i] = r.nextDouble();
            for (int j = 0; j < features; j++) {
                X[i][j] = r.nextDouble();
            }
        }
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            for (int j = 0; j < samples; j++) {
                yixiw = cblas_ddot(features, X[j], 1, w, 1);
                if (yixiw < 1) {
                    //matrix.scalarMultiply(X[j], y[j] *  alpha, xiyi);
                    //matrix.add(w1, xiyi, w);
                    cblas_daxpy(features, alpha * y[j], X[j], 1, xiyi, 1);
                    cblas_daxpy(features, alpha , xiyi, 1, w, 1);

                } else {
                    //matrix.scalarMultiply(w, invAlpha, w);
                    cblas_daxpy(features, alpha , w, 1, w, 1);
                }
            }
        }
        long trainingTime = System.currentTimeMillis() - t1;
        System.out.println("[BLAS] Training Time " + String.valueOf((double) trainingTime / 1000.0));
    }

    public static void withoutBlas() throws MatrixMultiplicationException {

        final int features = 22;
        final int samples = 1000;
        final int iterations = 1000;
        double yixiw = -1;
        final double alpha = 0.001;
        final double invAlpha = -alpha;
        double[] xiyi = new double[features];
        double[][]X = new double[samples][features];
        double[] y = new double[samples];
        double[] w = new double[features];
        double[] w1 = new double[features];
        double[] temp1 = new double[features];
        double[] temp2 = new double[features];
        double[] temp3 = new double[features];
        double[] temp4 = new double[features];
        double[] temp5 = new double[features];
        double[] temp6 = new double[features];
        double[] xi = null;
        double yi = -1;
        double condition = 1;
        double[] Xyia = null;
        double[] wa;
        Random r = new Random();
        for (int i = 0; i < samples; i++) {
            y[i] = r.nextDouble();
            for (int j = 0; j < features; j++) {
                X[i][j] = r.nextDouble();
            }
        }



        long t1 = System.currentTimeMillis();

        for (int i = 0; i < iterations; i++) {
            for (int j = 0; j < samples; j++) {
                xi = X[i];
                yi = y[i];
                // TODO: Java AVX Support :D
                condition = yi * Matrix.dot(xi, w);

                if (condition < 1) {
                    //TODO:  matrix mul library usage : pass output array from here
                    Xyia = Matrix.scalarMultiplyR(Matrix.subtractR(w, Matrix.scalarMultiplyR(xi, yi, temp1), temp2), alpha, temp3);
                    w = Matrix.subtractR(w, Xyia, temp4);
                } else {
                    wa = Matrix.scalarMultiplyR(w, alpha, temp5);
                    w = Matrix.subtractR(w, wa, temp6);
                }
            }
        }
        long trainingTime = System.currentTimeMillis() - t1;
        System.out.println("[NON-BLAS] Training Time " + String.valueOf((double) trainingTime / 1000.0));
    }
}
