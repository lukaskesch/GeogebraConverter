package app.files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileHandler {

    public static boolean createNewFolder(String path) {
        File theDir = new File(path);
        if (theDir.exists()) {
            return false;
        }
        theDir.mkdirs();
        return true;
    }

    public static void copyFile(File from, File to) throws IOException {
        Files.copy(from.toPath(), to.toPath());
    }

    public static String removeFileExtension(String fileName) {
        int pos = fileName.lastIndexOf(".");
        if (pos > 0) {
            return fileName.substring(0, pos);
        }
        return fileName;
    }
    
}
