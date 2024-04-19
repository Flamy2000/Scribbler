package com.logicerror;

import org.opencv.core.Point;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;


public class Scribbler {

    // com.logicerror.Scribbler
    // Draw:
    // get bounds from gartic()
    // scale image down or crop if it doesn't fit
    // (call on color approximator to find colors)
    // call on gartic to find color locations
    // click on respective color
    // using bounds given by gartic draw the image

    Point[] bounds;
    Gartic board;
    Scribbler(){
        board = new Gartic();
    }

    public void setBounds(BufferedImage screen){
        this.bounds = TemplateSearch.templateSearch(screen, board.getBoundingImgs());
        System.out.println(Arrays.toString(this.bounds));
    }

    public void getApproximateColors() throws FileNotFoundException {
        Color[] palette = ColorApproximation.readColorFile(new File(System.getProperty("user.dir") + "\\colors\\Crayola100SuperTipMarkers.txt"));
        ColorApproximation ca = new ColorApproximation(System.getProperty("user.dir") + "\\SampleImages\\windows.jpg", palette);
        ca.run();

    }

    public void clickColor(){

    }


}
