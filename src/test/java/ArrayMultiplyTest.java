import edu.iu.psgd.exceptions.MatrixMultiplicationException;
import edu.iu.psgd.math.Matrix;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    @Test
    public void dot() throws MatrixMultiplicationException {
        double [] X = {1.0,2.0,3.0};
        double [] w = {0.1,0.1,0.1};
        double res = Matrix.dot(X,w);
        System.out.println("Dot Product " + Arrays.toString(X) + ", " + Arrays.toString(w) + ", Result : " + res);
    }

    @Test
    public void scalarMultiply() throws MatrixMultiplicationException {
        double [] X = {1.0,2.0,3.0};
        double b = 0.1;
        double [] res = Matrix.scalarMultiply(X,b);
        System.out.println("Scalar Vector Multiply " + Arrays.toString(X) + ", " + b + ", Result : " + Arrays.toString(res));
    }

    @Test
    public void add() throws MatrixMultiplicationException {
        double [] X = {1.0,2.0,3.0};
        double [] w = {0.1,0.1,0.1};
        double [] res = Matrix.add(X,w);
        System.out.println("Add " + Arrays.toString(X) + ", " + Arrays.toString(w) + ", Result : " + Arrays.toString(res));
    }

    @Test
    public void subtract() throws MatrixMultiplicationException {
        double [] X = {1.0,2.0,3.0};
        double [] w = {0.1,0.1,0.1};
        double [] res = Matrix.subtract(X,w);
        System.out.println("Subtract  " + Arrays.toString(X) + ", " + Arrays.toString(w) + ", Result : " + Arrays.toString(res));
    }

    @Test
    public void arrayListTest() {
        double [] x = {1.0,2.0, 3.0};
        ArrayList<double []> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            double [] newx = Matrix.scalarMultiply(x,i);
            list.add(newx);
        }
        Collections.shuffle(list);
        for (int i = 0; i < list.size(); i++) {
            double [] a = list.get(i);
            for (int j = 0; j < a.length; j++) {
                System.out.print(a[j] + " ");
            }
            System.out.println();
        }
    }



}
