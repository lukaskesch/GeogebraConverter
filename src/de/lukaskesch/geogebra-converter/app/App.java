package app;

import java.io.File;
import java.io.IOException;

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
        if (!createWorkingFolder()) {
            return "Error, could not create working folder";
        }

        if (!moveFileToWorkingFolder()) {
            return "Error, could not move file to working folder";
        }

        if(!renameFile()) {
            return "Error, could not rename the " + fileName + " file";
        }
        
        if (!unzipFile()) {
            return "Error, could not unzip the " + workingFolderPath + "/" + fileNameWithoutExtension + ".zip file";
        }

        Document document = XMLParser.getParsedXML(workingFolderPath + "/geogebra.xml");
        if (document == null) {
            return "Error while parsing XML file";
        }

        if(!rezipFile()) {
            return "Error, could not rezip the " + workingFolderPath + "folder";
        }
        

        return "Conversion successful";
    }

    public boolean createWorkingFolder() {
        String folderPath = FileHandler.removeFileExtension(filePath);
        return FileHandler.createNewFolder(folderPath);
    }

    public boolean moveFileToWorkingFolder() {
        File source = file;
        File dest = new File(workingFolderPath + "/" + fileName);
        try {
            FileHandler.copyFile(source, dest);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean renameFile() {
        File source = new File(workingFolderPath + "/" + fileName);
        File dest = new File(workingFolderPath + "/" + fileNameWithoutExtension + ".zip");
        try {
            FileHandler.copyFile(source, dest);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean unzipFile() {
        String zipFilePath = workingFolderPath + "/" + fileNameWithoutExtension + ".zip";
        try {
            ZipHandler.unzip(zipFilePath, workingFolderPath);
            
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean rezipFile() {
        String destinationPath = workingFolderPath.replace(fileName, fileName) + ".zip";
        try {
            ZipHandler.zipFolder(workingFolderPath, destinationPath);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
