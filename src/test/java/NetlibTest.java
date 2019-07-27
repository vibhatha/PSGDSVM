import com.github.fommil.netlib.BLAS;
import com.google.common.base.Stopwatch;
import edu.iu.psgd.exceptions.MatrixMultiplicationException;
import edu.iu.psgd.math.Matrix;
import org.junit.Test;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;



public class NetlibTest {



    private class Stats {
        long value = 0;
        long time = 0;

        public Stats(long value, long time) {
            this.value = value;
            this.time = time;
        }

        public long getValue() {
            return value;
        }

        public long getTime() {
            return time;
        }

        @Override
        public String toString() {
            return "Stats{" +
                    "value=" + value +
                    ", time=" + time +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Stats stats = (Stats) o;
            return value == stats.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }

    private Random r = new Random();

    private final int arrSize = 500;
    private final int iterations  = 200;
    private final int samples = 50000;

    public double[] randomArray(int size) {
        double [] d = new double[size];
        for (int i = 0; i < size; i++) {
            d[i] = r.nextDouble();
        }
        return d;
    }

    public double[][] randomArrays(int samples, int size) {
        double [][] d = new double[samples][size];
        for (int i = 0; i < samples; i++) {
            for (int j = 0; j < size; j++) {
                d[i][j] = r.nextDouble();
            }
        }
        return d;
    }


    public Stats test() {
        double[] array1 = randomArray(arrSize);
        double[] array2 = randomArray(arrSize);
        double[][] data = randomArrays(samples, arrSize);
        final Stopwatch stopwatch = Stopwatch.createStarted();
        BLAS b = BLAS.getInstance();
        long d = 0;
        for (int i = 0; i < iterations; i++) {
            for (int j = 0; j < samples-1; j++) {
                array1 = data[j];
                d += b.ddot(arrSize, array1, 1, array2, 1);
            }
        }
        stopwatch.stop();

        long t =  stopwatch.elapsed(TimeUnit.MILLISECONDS);
        System.out.println("Time in Seconds : " + (double) t / 1000.0 + ", value : " + d);
        return new Stats(d, t);
    }


    public Stats test1() throws MatrixMultiplicationException {
        double[] array1 = randomArray(arrSize);
        double[] array2 = randomArray(arrSize);
        double[][] data = randomArrays(samples, arrSize);
        final Stopwatch stopwatch = Stopwatch.createStarted();
        long d = 0;
        for (int i = 0; i < iterations; i++) {
            for (int j = 0; j < samples-1; j++) {
                array1 = data[j];
                d += Matrix.dot(array1, array2);
            }

        }
        stopwatch.stop();

        long t =  stopwatch.elapsed(TimeUnit.MILLISECONDS);
        System.out.println("Time in Seconds : " + (double) t / 1000.0 + ", value : " + d);
        return new Stats(d, t);
    }

    @Test
    public void test3() throws MatrixMultiplicationException {
        Stats s1 = test();
        Stats s2 = test1();

        System.out.println("Test 1 : " + s1.toString());
        System.out.println("Test 1 : " + s2.toString());
        System.out.println("Result Status : " + s1.equals(s2));

    }
}
