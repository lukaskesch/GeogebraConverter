package app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
            Document  document = getParsedXML(workingFolderPath + "/geogebra.xml");
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

    

    public Document getParsedXML(String filePath)
    {
        // Instantiate the Factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        Document document = null;


        try (InputStream is = new FileInputStream(new File(filePath))) {

            // parse XML file
            DocumentBuilder db = dbf.newDocumentBuilder();

            // read from a project's resources folder
            document = db.parse(is);

            System.out.println("Root Element :" + document.getDocumentElement().getNodeName());
            System.out.println("------");

            if (document.hasChildNodes()) {
                printNote(document.getChildNodes());
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        return document;
    }

    private static void printNote(NodeList nodeList) {

        for (int count = 0; count < nodeList.getLength(); count++) {

            Node tempNode = nodeList.item(count);

            // make sure it's element node.
            if (tempNode.getNodeType() == Node.ELEMENT_NODE) {

                // get node name and value
                System.out.println("\nNode Name =" + tempNode.getNodeName() + " [OPEN]");
                System.out.println("Node Value =" + tempNode.getTextContent());

                if (tempNode.hasAttributes()) {

                    // get attributes names and values
                    NamedNodeMap nodeMap = tempNode.getAttributes();
                    for (int i = 0; i < nodeMap.getLength(); i++) {
                        Node node = nodeMap.item(i);
                        System.out.println("attr name : " + node.getNodeName());
                        System.out.println("attr value : " + node.getNodeValue());
                        
                    }

                }

                if (tempNode.hasChildNodes()) {
                    // loop again if has child nodes
                    printNote(tempNode.getChildNodes());
                }

                System.out.println("Node Name =" + tempNode.getNodeName() + " [CLOSE]");

            }

        }

    }

    


}
