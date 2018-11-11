package edu.iu.psgd.resource;

import edu.iu.psgd.api.data.DataSet;
import edu.iu.psgd.util.Params;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResourceManager {

    private static final Logger LOG = Logger.getLogger(ResourceManager.class.getName());

    private String basePath = "";

    private DataSet dataSet;

    private Params params;

    public ResourceManager(Params params) {
        this.params = params;
    }



    public String getBasePath() {
        String basePath = "";
        String dataSource = "src/main/resources/datasource.yaml";
        List<String> params = loadParams(dataSource);
        for (String s :
                params) {
            if (s.contains("datasource")) {
                basePath = s.split(": ")[1];
            }
        }

        return basePath;
    }

    public List<String> loadParams(String filename) {
        List<String> list = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(filename))) {

            //br returns as stream and convert it into a List
            list = br.lines().collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void loadParamsByYaml() {
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("src/main/resources/datasource.yaml");
        Map<String, Object> obj = yaml.load(inputStream);
        System.out.println(obj);
    }

    public DataSet load() {
        this.dataSet = null;
        if(!params.isSplit()) {
            String datasourceBasePath = this.getBasePath();
            //LOG.info("Datasource Base Path : " + datasourceBasePath);
            String dataFileTrain = datasourceBasePath + params.getDataset() + "/training.csv";
            String dataFileTest = datasourceBasePath + params.getDataset() + "/testing.csv";
            //LOG.info("Data Train file Path : " + dataFileTrain);
            //LOG.info("Data Test file Path : " + dataFileTest);
            dataSet = new DataSet(params.getFeatures(), dataFileTrain, dataFileTest);
            dataSet.load();
        }

        if(params.isSplit()) {
            String datasourceBasePath = this.getBasePath();
            String dataFileTrain = datasourceBasePath + params.getDataset() + "/training.csv";
            dataSet = new DataSet(dataFileTrain, params.getFeatures(), params.getTrainingSamples());
            dataSet.load();
        }


        return dataSet;
    }
}
