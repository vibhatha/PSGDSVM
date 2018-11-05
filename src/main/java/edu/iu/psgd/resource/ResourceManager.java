package edu.iu.psgd.resource;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class ResourceManager {

    private String basePath = "";

    public ResourceManager() {
    }

    public String getBasePath() {
        String basePath = "";
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("/home/vibhatha/github/PSGDSVM/src/main/resources/datasource.yaml");
        DataSource customer = yaml.load(inputStream);

        return basePath;
    }
}
