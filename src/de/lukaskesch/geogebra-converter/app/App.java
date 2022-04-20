package app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import org.w3c.dom.Document;

import app.files.FileHandler;
import app.files.XMLParser;
import app.files.ZipHandler;

public class App {

    private final File file;
    private final String filePath;
    private final String fileName;
    private final String fileNameWithoutExtension;
    private final String workingFolderPath;

    public App(File file) {
        this.file = file;
        filePath = file.getAbsolutePath();
        fileName = file.getName();
        fileNameWithoutExtension = FileHandler.removeFileExtension(fileName);
        workingFolderPath = FileHandler.removeFileExtension(filePath);
    }

    public String getFileName() {
        return fileName;
    }

    public String run() {
        createWorkingFolder();
        moveFileToWorkingFolder();
        renameFile();

        Document document;
        try {
            unzipFile();

        } catch (IOException e) {
            e.printStackTrace();
            return "Error while unzipping file: " + e.getMessage();
        }

        document = XMLParser.getParsedXML(workingFolderPath + "/geogebra.xml");
        if (document == null) {
            return "Error while parsing XML file";
        }

        try {
            rezipFile();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error while rezipping file: " + e.getMessage();
        }

        return "Conversion successful";
    }

    public void createWorkingFolder() {
        String folderPath = FileHandler.removeFileExtension(filePath);
        FileHandler.createNewFolder(folderPath);
    }

    public void moveFileToWorkingFolder() {
        File source = file;
        File dest = new File(workingFolderPath + "/" + fileName);
        try {
            FileHandler.copyFile(source, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void renameFile() {
        File source = new File(workingFolderPath + "/" + fileName);
        File dest = new File(workingFolderPath + "/" + fileNameWithoutExtension + ".zip");
        try {
            FileHandler.copyFile(source, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void unzipFile() throws IOException {
        String zipFilePath = workingFolderPath + "/" + fileNameWithoutExtension + ".zip";
        ZipHandler.unzip(zipFilePath, workingFolderPath);
    }

    public void rezipFile() throws IOException {
        String destinationPath = workingFolderPath.replace(fileName, fileName) + ".zip";
        ZipHandler.zipFolder(workingFolderPath, destinationPath);
    }

}
