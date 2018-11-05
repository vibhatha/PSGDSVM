package edu.iu.psgd.model;

public class HyperParameters {
    private double alpha;
    private double beta1;
    private double beta2;

    public HyperParameters(double alpha) {
        this.alpha = alpha;
    }

    public HyperParameters(double alpha, double beta1, double beta2) {
        this.alpha = alpha;
        this.beta1 = beta1;
        this.beta2 = beta2;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public double getBeta1() {
        return beta1;
    }

    public void setBeta1(double beta1) {
        this.beta1 = beta1;
    }

    public double getBeta2() {
        return beta2;
    }

    public void setBeta2(double beta2) {
        this.beta2 = beta2;
    }
}
