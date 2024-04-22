package com.logicerror;

import java.awt.*;
import java.awt.image.BufferedImage;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import java.awt.image.DataBufferByte;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TemplateSearch{
    private boolean grayscale = false;
    public void setGrayscale(boolean grayscale){
        this.grayscale = grayscale;
    }

    private double threshold = 0.75f;
    public void setThreshold(double threshold){
        this.threshold = threshold;
    }

    private double startScale = 0.5f;
    private double endScale = 2f;
    private double scalingInterval = 0.5f;
    private double recentMatchScale = 1f;
    public void setScalingFactors(double start, double end, double interval){
        startScale = start;
        endScale = end;
        scalingInterval = interval;
    }

    public double getRecentMatchScale(){
        return recentMatchScale;
    }

    public BufferedImage templateSearch(BufferedImage screenshot, Picture[] templatePics, Color color) {
        // Convert the screenshot to Mat
        Mat screenshotMat = convertToMat(screenshot);
        for (Picture pic : templatePics) {
            // Convert the screenshot to Mat
            Mat template = convertToMat(pic.getBufferedImage());
            Point point = templateSearch(screenshot, pic);
            Scalar scalar = new Scalar(color.getRed(), color.getGreen(), color.getBlue());
            Imgproc.rectangle(screenshotMat, new Point(point.x - template.cols()* recentMatchScale *0.5,
                    point.y - template.rows()* recentMatchScale *0.5),
                    new Point(point.x + template.cols()* recentMatchScale *0.5,
                            point.y + template.rows()* recentMatchScale *0.5), scalar, 2);
        }

        // Convert the result back to BufferedImage
        return convertToBufferedImage(screenshotMat);
    }

    public BufferedImage templateSearch(BufferedImage screenshot, Picture templatePic, Color color) {
        // Convert the screenshot to Mat
        Mat screenshotMat = convertToMat(screenshot);
        Mat template = convertToMat(templatePic.getBufferedImage());
        Point point = templateSearch(screenshot, templatePic);
        Scalar scalar = new Scalar(color.getRed(), color.getGreen(), color.getBlue());
        Imgproc.rectangle(screenshotMat, new Point(point.x - template.cols()* recentMatchScale *0.5,
                        point.y - template.rows()* recentMatchScale *0.5),
                new Point(point.x + template.cols()* recentMatchScale *0.5,
                        point.y + template.rows()* recentMatchScale *0.5), scalar, 2);
        // Convert the result back to BufferedImage
        return convertToBufferedImage(screenshotMat);
    }

    public Point[] templateSearch(BufferedImage screenshot, Picture[] templatePics) {
        Point[] points = new Point[templatePics.length];
        for (int i = 0; i < templatePics.length; i++){
            points[i] = templateSearch(screenshot, templatePics[i]);
        }

        return points;
    }

    public Point templateSearch(BufferedImage screenshot, Picture templatePic) {

        Point point = new Point();

        // Convert the screenshot to Mat
        Mat screenshotMat = convertToMat(screenshot);

        // Load the template image
        Mat template = convertToMat(templatePic.getBufferedImage());


        // Perform template matching
//            Mat result = new Mat();

        // Iterate over different scales
        double bestMatchValue = Double.MIN_VALUE;
        Point bestMatch = null;
        Mat bestResizedTemplate = null;


        for (double scale = startScale; scale <= endScale; scale += scalingInterval) {
            // Resize the template image
            Mat resizedTemplate = new Mat();
            Imgproc.resize(template, resizedTemplate, new Size(template.cols() * scale, template.rows() * scale));

            // Perform template matching
            Mat result = new Mat();
            if (grayscale){
                // Convert the screenshot to grayscale
                Mat screenshotGray = new Mat();
                Imgproc.cvtColor(screenshotMat, screenshotGray, Imgproc.COLOR_BGR2GRAY);

                // Convert the template to grayscale
                Mat templateGray = new Mat();
                Imgproc.cvtColor(resizedTemplate, templateGray, Imgproc.COLOR_BGR2GRAY);
                Imgproc.matchTemplate(screenshotGray, templateGray, result, Imgproc.TM_CCOEFF_NORMED);
            }else{
                Imgproc.matchTemplate(screenshotMat, resizedTemplate, result, Imgproc.TM_CCOEFF_NORMED);
            }

            // Find the maximum match value
            Core.MinMaxLocResult mmr = Core.minMaxLoc(result);

            if (mmr.maxVal > bestMatchValue) {
                bestMatchValue = mmr.maxVal;
                bestMatch = mmr.maxLoc;
                recentMatchScale = scale;
                bestResizedTemplate = resizedTemplate;
            }
        }

        try {
            // If a match is found above the threshold, return the coordinates
            if (bestMatchValue >= threshold) {
                return new Point(bestMatch.x + (double) bestResizedTemplate.cols() / 2, bestMatch.y + (double) bestResizedTemplate.rows() / 2);
            } else {
                return null; // No match found
            }
        }catch (NullPointerException ex){
            Logger.getLogger(TemplateSearch.class.getName()).log(Level.SEVERE, "Template search failed", ex);
            return null;
        }

    }


    private static Mat convertToMat(BufferedImage bi) {
        int width = bi.getWidth();
        int height = bi.getHeight();
        int type = CvType.CV_8UC3; // We assume 3 channels (RGB)

        Mat mat = new Mat(height, width, type);

        int[] data = new int[width * height];
        bi.getRGB(0, 0, width, height, data, 0, width);

        byte[] byteData = new byte[width * height * 3];

        // Convert int pixel values to byte
        int byteIndex = 0;
        for (int i = 0; i < data.length; i++) {
            int pixel = data[i];
            byteData[byteIndex++] = (byte) (pixel & 0xFF);         // Blue
            byteData[byteIndex++] = (byte) ((pixel >> 8) & 0xFF);  // Green
            byteData[byteIndex++] = (byte) ((pixel >> 16) & 0xFF); // Red
        }

        mat.put(0, 0, byteData);
        return mat;
    }

    private static BufferedImage convertToBufferedImage(Mat mat) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (mat.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        BufferedImage image = new BufferedImage(mat.width(), mat.height(), type);
        byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        mat.get(0, 0, data);
        return image;
    }
}