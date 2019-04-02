package edu.iu.psgd.util;

import org.apache.commons.cli.Option;
import org.yaml.snakeyaml.Yaml;

import java.io.*;

public class Utils {
    public static Option createOption(String opt, boolean hasArg,
                                      String description, boolean required) {
        Option symbolListOption = new Option(opt, hasArg, description);
        symbolListOption.setRequired(required);
        return symbolListOption;
    }

    //TODO : Modify this method to support a full log saving functionality
    public static void logSave(Params params, double trainingTime, double dataLoadinTime) throws IOException {
        File file = new File(params.getLogSavePath());
        FileWriter fileWriter = new FileWriter(file, true);
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(fileWriter);
            String s = params.toString();
            s += dataLoadinTime + "," + trainingTime + "," + (dataLoadinTime + trainingTime);
            bufferedWriter.write(s);
            bufferedWriter.newLine();
        }finally {
            bufferedWriter.close();
        }
    }


}
