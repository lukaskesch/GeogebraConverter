package app;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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

    public void run() {
        createWorkingFolder();
        moveFileToWorkingFolder();
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
