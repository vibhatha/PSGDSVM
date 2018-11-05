package edu.iu.psgd;

import edu.iu.psgd.api.data.DataSet;

public class Program {
    public static void main(String[] args) {

        String dataset = "a9a";
        String basePath = "/home/vibhatha/";
        int features = 68;
        int samples = 32561;
        String dataFile = "data/svm/"+dataset+"/training.csv";
        String sourceFile = basePath + dataFile;
        DataSet dataSet = new DataSet(sourceFile, features, samples);
        dataSet.load();
    }
}
