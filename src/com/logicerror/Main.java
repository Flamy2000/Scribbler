package com.logicerror;

import org.opencv.core.Core;
import org.opencv.core.Scalar;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import javax.swing.*;

public class Main {
    public static void main (String[]args) {
        // load OpenCV
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Picture pic = new Picture("src/res/img/SampleImages/desktop.jpg");
        pic.scalePercent(1, 1);
        pic.show();

        Gartic board = new Gartic();
//        System.out.println(Arrays.toString(board.getBoundingImgs()));

//        BufferedImage img;
//        try {
//            img = ScreenCapture.capture();
//        } catch (AWTException ex) {
//            System.out.println("failed");
//            return;
//        }
//
//        Scribbler scribbler = new Scribbler();
//        scribbler.setBounds(img);
//
//        displayImage(TemplateSearch.templateSearch(img, board.getBoundingImgs(), new Scalar(0, 255, 0)));

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