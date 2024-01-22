import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class Main {
    public static void main (String[]args) {
        BufferedImage img;
        try {
            img = ScreenCapture.capture();
        } catch (AWTException ex) {
            System.out.println("failed");
            return;
        }

        String[] paths = {"img\\btm_right.png", "img\\top_left.png"};
        BufferedImage resultImage = TemplateSearch.templateSearch(img, paths);

        displayImage(resultImage);

        // Gartic:
        // give image

        // Scribbler
        // Draw:
        // get bounds from gartic()
        // scale image down or crop if it doesnt fit
        // (call on color approximator to find colors)
        // call on gartic to find color locations
        // click on respective color
        // using bounds given by gartic draw the image

    }

    private static void displayImage(BufferedImage image) {
        if (image != null) {
            ImageIcon icon = new ImageIcon(image);
            JFrame frame = new JFrame("Result");
            frame.setLayout(new FlowLayout());
            frame.setSize(image.getWidth(), image.getHeight());
            JLabel lbl = new JLabel();
            lbl.setIcon(icon);
            frame.add(lbl);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }

}