package edu.iu.psgd.model;

public class Model {

    private HyperParameters hyperParameters;
    private String dataset;
    private String expId;
    private double [] wInit;
    private double [] wFinal;

    public Model(String dataset, double alpha, int iterations, double [] wInit, double [] wFinal) {
        this.dataset = dataset;
        hyperParameters = new HyperParameters(alpha);
        this.wInit = wInit;
        this.wFinal = wFinal;
    }

    public HyperParameters getHyperParameters() {
        return hyperParameters;
    }

    public void setHyperParameters(HyperParameters hyperParameters) {
        this.hyperParameters = hyperParameters;
    }

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public String getExpId() {
        return expId;
    }

    public void setExpId(String expId) {
        this.expId = expId;
    }

    public double[] getwInit() {
        return wInit;
    }

    public void setwInit(double[] wInit) {
        this.wInit = wInit;
    }

    public double[] getwFinal() {
        return wFinal;
    }

    public void setwFinal(double[] wFinal) {
        this.wFinal = wFinal;
    }

    public void saveModel() {

    }

    public Model loadModel(String modelPath) {
        Model model = null;

        return model;
    }
}
