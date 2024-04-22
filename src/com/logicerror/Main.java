package com.logicerror;

import org.opencv.core.Core;

import java.awt.*;

public class Main {
    public static void main (String[]args) throws AWTException, InterruptedException {
        // load OpenCV
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Scribbler scribbler = new Scribbler();
//        scribbler.testBounds();


        Picture pic = new Picture(System.getProperty("user.dir") + "\\src\\res\\img\\SampleImages\\forestFire.jpg");
        pic.scalePercent(0.25f, 0.25f);

//        pic.degrade(2f);

        Color[] palette = scribbler.getColors();
        ColorApproximation ca = new ColorApproximation(pic, palette);
        Picture approxPic = ca.approximateColorsPicture();

//        EdgeMagnitudePicture edgeMagPic = new EdgeMagnitudePicture(approxPic);

//        approxPic.show();
//        edgeMagPic.getEdgeMagnitudeImage().show();


        scribbler.setOffset(200, 267);
        scribbler.setRobotDelay(3);
        Thread.sleep(1500);
        scribbler.printPicture(approxPic);

//        scribbler.printPicture(approxPic, edgeMagPic.getEdgeMagnitudes());
    }
}