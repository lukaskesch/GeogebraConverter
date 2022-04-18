import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GUI implements ActionListener {
    
    private static final String TITLE = "Geogebra Converter";
    private static final String LOAD_BUTTON_TEXT = "Load file";

    private int clicks = 0;
    private JLabel label = new JLabel("Number of clicks:  0     ");
    private JFrame frame = new JFrame();

    public GUI() {

        // the clickable button
        JButton button = new JButton(LOAD_BUTTON_TEXT);
        button.addActionListener(this);

        // the panel with the button and text
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        panel.setLayout(new GridLayout(0, 1));
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
        clicks++;
        //label.setText("Number of clicks:  " + clicks);


        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new java.io.File("."));
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
        }
    }

    // create one Frame
    public static void main(String[] args) {
        new GUI();
    }
}