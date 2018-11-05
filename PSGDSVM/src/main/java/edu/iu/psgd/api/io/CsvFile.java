package edu.iu.psgd.api.io;

public class CsvFile {

    private String filepath;
    private String fileType;


    public CsvFile(String filepath, String fileType) {
        this.filepath = filepath;
        this.fileType = fileType;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
