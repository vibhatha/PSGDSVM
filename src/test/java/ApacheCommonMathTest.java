import edu.iu.psgd.exceptions.MatrixMultiplicationException;
import edu.iu.psgd.math.Matrix;
import org.apache.commons.math3.geometry.Vector;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

import java.util.Random;

public class ApacheCommonMathTest {
    private int fsize = 22;
    private int samples = 10000;
    private int itr = 100;
    private final Random r = new Random();
    private final double N2S = 1000000000.0;

    public double[][] getX() {
        double[][] featureData = new double[samples][fsize];

        for (int i = 0; i < samples; i++) {
            for (int j = 0; j < fsize; j++) {
                featureData[i][j] = r.nextGaussian();
            }
        }
        return featureData;
    }

    public double[] getY() {
        double[]featureData = new double[samples];

        for (int i = 0; i < samples; i++) {
            featureData[i] = r.nextGaussian();
        }
        return featureData;
    }

    public double[] get1D() {
        double[] w = new double[fsize];
        for (int i = 0; i < fsize; i++) {
            w[i] = r.nextGaussian();
        }
        return w;
    }

    @Test
    public void test1() {
        double[][] X = getX();
        double[] w = get1D();
        double[] y = getY();
        RealMatrix[] vr = new RealMatrix[samples];
        for (int i = 0; i < samples; i++) {
            vr[i] = MatrixUtils.createRowRealMatrix(X[i]);
        }
        RealMatrix Xm = MatrixUtils.createRealMatrix(X);
        RealMatrix ym = MatrixUtils.createColumnRealMatrix(y);
        RealMatrix wm = MatrixUtils.createRowRealMatrix(w).transpose();
        long t1 = System.currentTimeMillis();
        int id = 0;
        for (int i = 0; i < itr ; i++) {
            for (int j = 0; j < samples; j++) {
                Xm.getRowMatrix(j).multiply(wm);
                //vr[j].multiply(wm);
            }
        }
        double time = (double)(System.currentTimeMillis() - t1)/1000.0;
        System.out.println("Time : " + time);
    }

    @Test
    public void test2() throws MatrixMultiplicationException {
        double[][] X = getX();
        double[] w = get1D();
        double[] y = getY();
        double[] temp = new double[fsize];

        //RealMatrix Xm = MatrixUtils.createRealMatrix(X);
        //RealMatrix ym = MatrixUtils.createColumnRealMatrix(y);
        //RealMatrix wm = MatrixUtils.createRowRealMatrix(w);
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < itr ; i++) {
            for (int j = 0; j < samples; j++) {
                Matrix.multiplyR(X[j], w, temp);
            }
        }
        double time = (double)(System.currentTimeMillis() - t1)/1000.0;
        System.out.println("Time : " + time);
    }
}
