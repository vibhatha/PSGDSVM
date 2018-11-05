package edu.iu.psgd.util;

import org.apache.commons.cli.Option;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

public class Utils {
    public static Option createOption(String opt, boolean hasArg,
                                      String description, boolean required) {
        Option symbolListOption = new Option(opt, hasArg, description);
        symbolListOption.setRequired(required);
        return symbolListOption;
    }


}
