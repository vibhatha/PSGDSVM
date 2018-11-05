import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

public class ArrayMultiplyTest {

    @Test
    public void justAnExample() {
        System.out.println("This test method should be run");
    }

    @Test
    public void multiply() {
        double [] xi = {1.0,2.0,3.0};
        double yi = -1.0;
        double [] result = {-1.0,-2.0,-3.0};
        System.out.println(Arrays.stream(xi).reduce(yi, (x,y) -> x*y));

    }

    @Test
    public void randomGaussian() {
        int size = 8;
        double [] initW = new double[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            initW[i] = random.nextGaussian();
        }
        System.out.println(Arrays.toString(initW));
    }


}
