package com.logicerror;

import java.awt.image.BufferedImage;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;

public class TemplateSearch{
    private static boolean grayscale = false;
    public static void setGrayscale(boolean grayscale){
        TemplateSearch.grayscale = grayscale;
    }

    private static double threshold = 0.5f;
    public static void setThreshold(double threshold){
        TemplateSearch.threshold = threshold;
    }

    public static BufferedImage templateSearch(BufferedImage screenshot, String[] templatePaths, Scalar color) {
        // Convert the screenshot to Mat
        Mat screenshotMat = convertToMat(screenshot);
        for (String path : templatePaths) {
            Mat template = Imgcodecs.imread(path);
            Point point = templateSearch(screenshot, path);
            Imgproc.rectangle(screenshotMat, point, new Point(point.x + template.cols(), point.y + template.rows()), color, 2);
        }

        // Convert the result back to BufferedImage
        return convertToBufferedImage(screenshotMat);
    }

    public static Point[] templateSearch(BufferedImage screenshot, String[] templatePaths) {
        Point[] points = new Point[templatePaths.length];
        for (int i = 0; i < templatePaths.length; i++){
            points[i] = templateSearch(screenshot, templatePaths[i]);
        }

        return points;
    }

    public static Point templateSearch(BufferedImage screenshot, String templatePath) {
        try {
            Point point = new Point();

            // Convert the screenshot to Mat
            Mat screenshotMat = convertToMat(screenshot);

            // Load the template image
            Mat template = Imgcodecs.imread(templatePath);

            // Perform template matching
            Mat result = new Mat();

            if (grayscale){
                // Convert the screenshot to grayscale
                Mat screenshotGray = new Mat();
                Imgproc.cvtColor(screenshotMat, screenshotGray, Imgproc.COLOR_BGR2GRAY);

                // Convert the template to grayscale
                Mat templateGray = new Mat();
                Imgproc.cvtColor(template, templateGray, Imgproc.COLOR_BGR2GRAY);
                Imgproc.matchTemplate(screenshotGray, templateGray, result, Imgproc.TM_CCOEFF_NORMED);
            }else{
                Imgproc.matchTemplate(screenshotMat, template, result, Imgproc.TM_CCOEFF_NORMED);
            }

            // Find locations where the result is above the threshold
            Core.MinMaxLocResult mmr = Core.minMaxLoc(result);

            if (mmr.maxVal >= threshold) {
                point = mmr.maxLoc;
            }

            // Convert the result back to BufferedImage
            return point;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private static Mat convertToMat(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        int[] data = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

        Mat mat = new Mat(height, width, CvType.CV_8UC3);

        byte[] byteData = new byte[width * height * 3];

        for (int i = 0, j = 0; i < data.length; i++) {
            int pixel = data[i];
            byteData[j++] = (byte) (pixel & 0xFF);         // Blue
            byteData[j++] = (byte) ((pixel >> 8) & 0xFF);  // Green
            byteData[j++] = (byte) ((pixel >> 16) & 0xFF); // Red
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