import edu.iu.psgd.exceptions.MatrixMultiplicationException;
import edu.iu.psgd.math.Matrix;
//import org.jblas.DoubleMatrix;
//import org.jblas.util.Random;
import org.junit.Test;

public class JBlassTest {

//    private int fsize = 22;
//    private int samples = 10000;
//    private int itr = 10000;
//    private final Random r = new Random();
//    private final double N2S = 1000000000.0;
//
//    public double[][] get2D() {
//        double[][] featureData = new double[samples][fsize];
//
//        for (int i = 0; i < samples; i++) {
//            for (int j = 0; j < fsize; j++) {
//                featureData[i][j] = r.nextGaussian();
//            }
//        }
//        return featureData;
//    }
//
//    public double[] get1D() {
//        double[] w = new double[fsize];
//        for (int i = 0; i < fsize; i++) {
//            w[i] = r.nextGaussian();
//        }
//        return w;
//    }
//
//    @Test
//    public void test1() {
//        int size = samples;
//        int itr = this.itr;
//        DoubleMatrix doubleMatrix = new DoubleMatrix(size, fsize);
//        DoubleMatrix w = new DoubleMatrix(1, fsize);
//        int rows = doubleMatrix.rows;
//        int cols = doubleMatrix.columns;
//        System.out.println(String.format("Dimensions %d,%d", rows, cols));
//        long t1 = 0;
//        t1 -= System.nanoTime();
//        for (int i = 0; i < itr; i++) {
//            for (int j = 0; j < size; j++) {
//                w.muli(doubleMatrix.getRow(j));
//            }
//        }
//        t1 += System.nanoTime();
//        t1 /= N2S;
//
//        System.out.println(String.format("Time Taken : %f ", (double) t1));
//
//    }
//
//    @Test
//    public void test2() throws MatrixMultiplicationException {
//        double[][] X = get2D();
//        double[] w = get1D();
//        double[] wtemp = new double[fsize];
//        long t1 = 0;
//        t1 -= System.nanoTime();
//        for (int i = 0; i < itr; i++) {
//            for (int j = 0; j < samples; j++) {
//                w = Matrix.multiplyR(X[j], w, wtemp);
//            }
//        }
//        t1 += System.nanoTime();
//        t1 /= N2S;
//        System.out.println(String.format("Time Taken : %f ", (double) t1));
//    }
}
