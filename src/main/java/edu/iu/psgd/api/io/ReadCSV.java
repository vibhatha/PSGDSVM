package edu.iu.psgd.api.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class ReadCSV {

    private static final Logger LOG = Logger.getLogger(ReadCSV.class.getName());

    private CsvFile csvFile;
    private ArrayList<double[]> xVals = new ArrayList<>();
    private ArrayList<Double> yVals = new ArrayList<>();

    public ArrayList<double[]> getxVals() {
        return xVals;
    }

    public void setxVals(ArrayList<double[]> xVals) {
        this.xVals = xVals;
    }

    public ArrayList<Double> getyVals() {
        return yVals;
    }

    public void setyVals(ArrayList<Double> yVals) {
        this.yVals = yVals;
    }

    public ReadCSV(CsvFile csvFile) {
        this.csvFile = csvFile;
    }

    public void readX() {
        String csvFile = this.csvFile.getFilepath();
        String line = "";
        String cvsSplitBy = ",";


        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {

                String[] values = line.split(cvsSplitBy);
                int len = values.length;
                double[] xComp = new double[len];

                int count = 0;
                for (String value : values) {

                    xComp[count] = Double.parseDouble(value);
                    count++;

                }

                xVals.add(xComp);
            }

        } catch (IOException e) {
            LOG.info(String.format("CSV File path :%s \n ",csvFile));
            LOG.info("IOException : " + e.getMessage());
        }
    }

    public void readY() {

        String csvFile = this.csvFile.getFilepath();
        String line;


        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {

                double val = Double.parseDouble(line);
                yVals.add(val);

            }

        } catch (IOException e) {
            LOG.info("IOException : " + e.getMessage());
        }

    }
}
