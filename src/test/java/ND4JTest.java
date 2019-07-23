import edu.iu.psgd.exceptions.MatrixMultiplicationException;
import edu.iu.psgd.math.Matrix;
import org.junit.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.Arrays;
import java.util.Random;

public class ND4JTest {

    @Test
    public void simpleTest() {
        int fsize = 22;
        int samples = 100;
        int iterations = 100;
        Random r = new Random();
        double[][] featureData = new double[samples][fsize];
        double [] w = new double[fsize];
        for (int i = 0; i < fsize; i++) {
            w[i] = r.nextGaussian();
        }
        for (int i = 0; i < samples; i++) {
            for (int j = 0; j < fsize; j++) {
                featureData[i][j] = r.nextGaussian();
            }
        }
        INDArray features = Nd4j.create(featureData);
        INDArray wArray = Nd4j.create(w);
        //System.out.println(String.format("Init Array : %s", Arrays.toString(wArray.data().asDouble())));
        double t1 = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            for (int j = 0; j < samples; j++) {
                //System.out.println(String.format("%s, %s", Arrays.toString(features.getRow(j).shape()), Arrays.toString(wArray.shape())));
                wArray.mul(features.getRow(j), wArray);

            }
            //System.out.println(String.format("%d Step Array : %s", i,  Arrays.toString(wArray.data().asDouble())));
        }
        //System.out.println(String.format("Final Array : %s", Arrays.toString(wArray.data().asDouble())));
        double totalTime = System.currentTimeMillis() - t1;
        totalTime /= 1000.0;
        double[] wTemp = new double[fsize];
        //
        double t3 = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            for (int j = 0; j < samples; j++) {
                //System.out.println(String.format("%s, %s", Arrays.toString(features.getRow(j).shape()), Arrays.toString(wArray.shape())));
                try {
                    w = Matrix.multiplyR(w, featureData[j], wTemp);
                } catch (MatrixMultiplicationException e) {
                    e.printStackTrace();
                }

            }
        }
        double totalTime2 = System.currentTimeMillis() - t3;
        totalTime2 /= 1000.0;

        System.out.println(String.format("[m1] total time : %f ", totalTime));
        System.out.println(String.format("[m2] total time : %f ", totalTime2));

    }
}
