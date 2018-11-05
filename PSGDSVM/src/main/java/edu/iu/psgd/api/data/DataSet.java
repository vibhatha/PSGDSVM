package edu.iu.psgd.api.data;

import edu.iu.psgd.api.io.CsvFile;
import edu.iu.psgd.api.io.ReadCSV;
import java.util.ArrayList;
import java.util.logging.Logger;

public class DataSet {

    private static final Logger LOG = Logger.getLogger(DataSet.class.getName());

    private String sourceFile;
    private int features=1;
    private int datasize=1;
    private double ratio=1.0;
    private boolean isSplit=false;

    public DataSet(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    public DataSet(String sourceFile, int features, int datasize) {
        this.sourceFile = sourceFile;
        this.features = features;
        this.datasize = datasize;
    }

    public DataSet(String sourceFile, int features, int datasize, double ratio, boolean isSplit) {
        this.sourceFile = sourceFile;
        this.features = features;
        this.datasize = datasize;
        this.ratio = ratio;
        this.isSplit = isSplit;
    }

    public void load() {
        if(this.isSplit == false) {
            CsvFile csvFile = new CsvFile(this.sourceFile, "csv");
            ReadCSV readCSV = new ReadCSV(csvFile);
            readCSV.readX();
            ArrayList<double[]> xvals =  readCSV.getxVals();
            LOG.info(String.format("Data Samples %d ", xvals.size()));
        }

        if(this.isSplit == true) {

        }

    }

}
