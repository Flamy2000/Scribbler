package com.logicerror;

import org.opencv.core.Point;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Scribbler {

    // com.logicerror.Scribbler
    // Draw:
    // get bounds from gartic()
    // scale image down or crop if it doesn't fit
    // (call on color approximator to find colors)
    // call on gartic to find color locations
    // click on respective color
    // using bounds given by gartic draw the image

    private Point[] bounds;
    private DrawingBoard board;
    private BufferedImage screen;

    private Robot robot;

    private int robotDelay = 2;

    private Point offset;
    Scribbler() throws AWTException {
        board = new MSPaint();
        robot = new Robot();
    }

    public void setBounds(){
        TemplateSearch tempSearch = new TemplateSearch();
        this.bounds = tempSearch.templateSearch(screen, board.getBoundingImgs());
        System.out.println(Arrays.toString(this.bounds));
    }

    public void setRobotDelay(int millis){
        robotDelay = millis;
    }

    public void testBounds(){
        captureScreen();
        TemplateSearch tempSearch = new TemplateSearch();
        BufferedImage buffImg = tempSearch.templateSearch(screen, board.getBoundingImgs(), new Color(0, 255, 0));
        new Picture(buffImg, "bounds").show();
    }

    public void captureScreen(){
        try {
            screen = ScreenCapture.capture();
        } catch (AWTException ex) {
            Logger.getLogger(Scribbler.class.getName()).log(Level.SEVERE, "Screen capture failed", ex);
        }
    }

    public void setOffset(int x, int y){
        offset = new Point(x, y);
    }

    public void printPicture(Picture pic) {
        captureScreen();
        // key hook to listen for cancel event
        KeyHook hook = new KeyHook();
        hook.start();

        ArrayList<MagnitudePoint> points = pic.getPoints();
        Comparator<MagnitudePoint> colorComparator = Comparator.comparingInt((MagnitudePoint p) -> pic.getPixelColor(p.x, p.y).getRGB());
        Comparator<MagnitudePoint> yComparator = Comparator.comparingInt((MagnitudePoint p) -> p.y);
        points.sort(colorComparator.thenComparing(yComparator));

        printPictureHelper(pic, points);
    }


    public void printPicture(Picture pic, ArrayList<MagnitudePoint> edgeMagPoints){
        System.out.println(edgeMagPoints.size());


        Comparator<MagnitudePoint> magnitudeComparator = Comparator.comparingInt((MagnitudePoint p) -> p.magnitude);
        Comparator<MagnitudePoint> colorComparator = Comparator.comparingInt((MagnitudePoint p) -> pic.getPixelColor(p.x, p.y).getRGB());
        Comparator<MagnitudePoint> yComparator = Comparator.comparingInt((MagnitudePoint p) -> p.y);

        edgeMagPoints.sort(magnitudeComparator
                .thenComparing(colorComparator)
                .thenComparing(yComparator));
//        saveMagnitudePointsToFile(edgeMagPoints, pic, "MagnitudePointsAdjacent.txt");

//        System.out.println("done");

        printPictureHelper(pic, edgeMagPoints);
    }

    private void printPictureHelper(Picture pic, ArrayList<MagnitudePoint> points){
        captureScreen();
        // key hook to listen for cancel event
        KeyHook hook = new KeyHook();
        hook.start();

        Color currentColor = null;

        // for each pixel of edgeMagPoints
        for (int i = 0; i < points.size(); i++){
            MagnitudePoint point = points.get(i);
            // look for cancel event
            if (hook.isKeybindDown()){
                System.out.println("Printing cancelled with shortcut");
                hook.quit();
                return;
            }

            // use x,y of point to get rgb from pic
            Color rgb = pic.getPixelColor(point.x, point.y);

            if (currentColor == null || !currentColor.equals(rgb)){
                // use rgb to click on color choice
                clickColor(rgb);
                currentColor = rgb;
            }

            int lastX = point.x;
            int j = 1;
            while (i+j < points.size() && point.y == points.get(i+j).y && points.get(i+j).x - lastX == 1){
                lastX = points.get(i+j).x;
                j++;
            }
            j--;


            if (j >= 1){
                mouseDrag((int) (point.x + offset.x), (int) (point.y + offset.y),
                        (int) (points.get(i+j).x + offset.x), (int) (points.get(i+j).y + offset.y));
                i = i+j;
            }
            else {
                // click on pixel location
                mouseClick((int) (point.x + offset.x), (int) (point.y + offset.y));
            }
        }
        hook.quit();
    }

    // Function to save the list of MagnitudePoint objects to a text file
    private static void saveMagnitudePointsToFile(ArrayList<MagnitudePoint> points, Picture pic, String filename ) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (MagnitudePoint point : points) {
                Color rgb = pic.getPixelColor(point.x, point.y);
                writer.println(point.toString() + " --- " + "(" + rgb.getRed() + ", " + rgb.getBlue() + ", " + rgb.getGreen() + ")");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    public DrawingBoard getBoard(){
        return board;
    }

    public Color[] getColors(){
        return board.getColors();
    }

    public void clickColor(Color color) {
        TemplateSearch tempSearch = new TemplateSearch();
        Point point = null;
        try {
//            tempSearch.setScalingFactors(0.5, 3, 0.25);
            tempSearch.setScalingFactors(0.5, 2, .5);
            tempSearch.setThreshold(0.8);
            point = tempSearch.templateSearch(screen, board.getColorPicture(color));
//            System.out.println(tempSearch.getRecentMatchScale());
//            BufferedImage buff = tempSearch.templateSearch(screen, board.getColorPicture(color), new Color(255, 0, 0));
//            Picture pic = new Picture(buff, "tempalate");
//            pic.show();
        }catch (NullPointerException ex){
            Logger.getLogger(Scribbler.class.getName()).log(Level.SEVERE, "Template search failed. Ensure screen is not null", new NullPointerException());
        }

        if (point == null){
            Logger.getLogger(Scribbler.class.getName()).log(Level.SEVERE, "Color not found / is null", new NullPointerException());
            return;
        }

        mouseClick((int)point.x, (int)point.y);
    }

    private void mouseDrag(int x1, int y1, int x2, int y2){
        robot.mouseMove(x1, y1);

        try {
            Thread.sleep(robotDelay); // Delay
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        try {
            Thread.sleep(robotDelay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        robot.mouseMove(x2, y2);

        try {
            Thread.sleep(robotDelay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);  // Release mouse

        try {
            Thread.sleep(robotDelay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void mouseClick(int x, int y){
        robot.mouseMove(x, y);

        try {
            Thread.sleep(robotDelay); // Delay
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        try {
            Thread.sleep(robotDelay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);  // Release mouse

        try {
            Thread.sleep(robotDelay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
