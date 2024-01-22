import org.opencv.core.Core;
import org.opencv.core.Point;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

public class Main {
    public static void main (String[]args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        BufferedImage img;
        try {
            img = ScreenCapture.capture();
        } catch (AWTException ex) {
            System.out.println("failed");
            return;
        }

        Scribbler scribbler = new Scribbler();
        scribbler.setBounds(img);

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