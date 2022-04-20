package app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import org.w3c.dom.Document;

import app.files.XMLParser;




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
        fileNameWithoutExtension = removeFileExtension(fileName);
        workingFolderPath = removeFileExtension(filePath);
    }

    public String getFileName() {
        return fileName;
    }

    public boolean run() {
        createWorkingFolder();
        moveFileToWorkingFolder();
        renameFile();

        try {
            unzipFile();
            Document  document = XMLParser.getParsedXML(workingFolderPath + "/geogebra.xml");
            System.out.println("Conversion successful");
        } catch (IOException e) {
            System.err.println("Error while unzipping file: " + e.getMessage());
            e.printStackTrace();
            return false;
        } 


        return true;
    }

    public void createWorkingFolder() {
        String folderPath = removeFileExtension(filePath);
        createNewFolder(folderPath);
    }

    public void moveFileToWorkingFolder() {
        File source = file;
        File dest = new File(workingFolderPath + "/" + fileName);
        try {
            copyFile(source, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void renameFile() {
        File source = new File(workingFolderPath + "/" + fileName);
        File dest = new File(workingFolderPath + "/" + fileNameWithoutExtension + ".zip");
        try {
            copyFile(source, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void unzipFile() throws IOException {
        String zipFilePath = workingFolderPath + "/" + fileNameWithoutExtension + ".zip";
        unzip(zipFilePath, workingFolderPath);
    }

    public void copyFile(File from, File to) throws IOException {
        Files.copy(from.toPath(), to.toPath());
    }
    
    public String removeFileExtension(String fileName) {
        int pos = fileName.lastIndexOf(".");
        if (pos > 0) {
            return fileName.substring(0, pos);
        }
        return fileName;
    }

    public boolean createNewFolder(String path) {
        File theDir = new File(path);
        if (theDir.exists()) {
            return false;
        }
        theDir.mkdirs();
        return true;
    }

    

    
    

    


}
