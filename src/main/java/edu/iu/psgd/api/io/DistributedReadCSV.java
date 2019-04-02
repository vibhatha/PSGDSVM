package edu.iu.psgd.api.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class DistributedReadCSV {
    private static final Logger LOG = Logger.getLogger(ReadCSV.class.getName());

    private CsvFile csvFile;
    private int world_rank = 0;
    private int world_size = 1;
    private int totalSamples = 1;
    private boolean isSplit = false;
    private double ratio = 0.80;
    private int dataPerMachine = 1;
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

    public DistributedReadCSV(CsvFile csvFile, int world_rank, int world_size, int totalSamples) {
        this.csvFile = csvFile;
        this.world_rank = world_rank;
        this.world_size = world_size;
        this.totalSamples = totalSamples;
    }

    public DistributedReadCSV(CsvFile csvFile, int world_rank, int world_size, int totalSamples, boolean isSplit, double ratio) {
        this.csvFile = csvFile;
        this.world_rank = world_rank;
        this.world_size = world_size;
        this.totalSamples = totalSamples;
        this.isSplit = isSplit;
        this.ratio = ratio;
    }

    public void read() {
        String csvFile = this.csvFile.getFilepath();
        String line = "";
        String cvsSplitBy = ",";

        if(this.isSplit) {
            int totalSamples = this.totalSamples;
            int trainingSet = (int)(totalSamples * ratio);
            int testingSet = totalSamples - trainingSet;
            int dataPerMachine = trainingSet / world_size ;
            int totalVisibleSamples = dataPerMachine * world_size;
            int start = world_rank * dataPerMachine;
            int end = start + dataPerMachine;
            this.setDataPerMachine(dataPerMachine);
            System.out.println(String.format("Rank %d, Start : %d, End : %d, Data Per Machine : %d", world_rank, start, end, dataPerMachine));
            int row = 0;
            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

                while ((line = br.readLine()) != null) {
                    if(row>=start && row<end) {
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
                    if(row == end) {
                        break;
                    }

                    row++;
                }

            } catch (IOException e) {
                LOG.info(String.format("CSV File path :%s \n ",csvFile));
                LOG.info("IOException : " + e.getMessage());
            }
            System.out.println(String.format("Rank[%d] Data Loading Completed.", world_rank));

        }

        if(!this.isSplit) {

        }


    }

    public int getDataPerMachine() {
        return dataPerMachine;
    }

    public void setDataPerMachine(int dataPerMachine) {
        this.dataPerMachine = dataPerMachine;
    }
}

