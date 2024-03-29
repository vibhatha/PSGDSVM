package edu.iu.psgd.math;

import java.util.Random;

public class Initializer {

    public static double [] initialWeights(int size) {
        double [] initW = new double[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            initW[i] = random.nextGaussian();
        }

        return initW;
    }

    public static double [] initZeros(int size) {
        double [] initW = new double[size];

        for (int i = 0; i < size; i++) {
            initW[i] = 0;
        }

        return initW;
    }

    public static double [] initOnes(int size) {
        double [] initW = new double[size];

        for (int i = 0; i < size; i++) {
            initW[i] = 1.0;
        }

        return initW;
    }
}
