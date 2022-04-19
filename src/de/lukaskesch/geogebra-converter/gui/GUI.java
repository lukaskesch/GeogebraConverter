package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import app.App;

public class GUI implements ActionListener {

    private static final String TITLE = "Geogebra Converter";
    private static final String LOAD_BUTTON_TEXT = "Load and convert file";

    private JLabel label = new JLabel("No file selected");
    private JFrame frame = new JFrame();

    private App app;

    public GUI() {

        // the clickable button
        JButton button = new JButton(LOAD_BUTTON_TEXT);
        button.addActionListener(this);

        // the panel with the button and text
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        panel.setLayout(new GridLayout(0, 1));
        panel.setSize(200, 100);
        panel.add(button);
        panel.add(label);

        // set up the frame and display it
        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle(TITLE);
        frame.pack();
        frame.setVisible(true);
    }

    // process the button clicks
    public void actionPerformed(ActionEvent e) {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new java.io.File("."));
        fileChooser.setDialogTitle("Select Geogebra file");
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".ggb") || f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Geogebra files (*.ggb)";
            }
        });
        
        int result = fileChooser.showOpenDialog(frame);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File selectedFile = fileChooser.getSelectedFile();

        app = new App(selectedFile);



        label.setText("Opened file: " + app.getFileName());

        app.run();

        // try {
        //     unzip(filePath);
        // } catch (Exception exception) {
        //     // TODO: handle exception
        // }
    }

    // create one Frame
    public static void main(String[] args) {
        new GUI();
    }

    

}