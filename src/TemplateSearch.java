import java.awt.image.BufferedImage;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.nio.ByteBuffer;

public class TemplateSearch{
    public static BufferedImage templateSearch(BufferedImage screenshot, String[] templatePath, Scalar color) {
        try {

            // Convert the screenshot to Mat
            Mat screenshotMat = convertToMat(screenshot);

            // Convert the screenshot to grayscale
            Mat screenshotGray = new Mat();
            Imgproc.cvtColor(screenshotMat, screenshotGray, Imgproc.COLOR_BGR2GRAY);


            // template
            for (String path : templatePath){
                // Load the template image
                Mat template = Imgcodecs.imread(path);

                // Convert the template to grayscale
                Mat templateGray = new Mat();
                Imgproc.cvtColor(template, templateGray, Imgproc.COLOR_BGR2GRAY);

                // Perform template matching
                Mat result = new Mat();
                Imgproc.matchTemplate(screenshotGray, templateGray, result, Imgproc.TM_CCOEFF_NORMED);

                // Set a threshold (adjust as needed)
                double threshold = 1.0;

                // Find locations where the result is above the threshold
                Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
                Point matchLoc = mmr.maxLoc;

                // Draw a rectangle around the match
                Imgproc.rectangle(screenshotMat, matchLoc, new Point(matchLoc.x + template.cols(), matchLoc.y + template.rows()), color, 2);
            }

            // Convert the result back to BufferedImage
            return convertToBufferedImage(screenshotMat);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Point[] templateSearch(BufferedImage screenshot, String[] templatePaths) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        try {

            // Convert the screenshot to Mat
            Mat screenshotMat = convertToMat(screenshot);

            // Convert the screenshot to grayscale
            Mat screenshotGray = new Mat();
            Imgproc.cvtColor(screenshotMat, screenshotGray, Imgproc.COLOR_BGR2GRAY);

            Point[] points = new Point[templatePaths.length];
            int i = 0;
            // template
            for (String path : templatePaths){
                // Load the template image
                Mat template = Imgcodecs.imread(path);

                // Convert the template to grayscale
                Mat templateGray = new Mat();
                Imgproc.cvtColor(template, templateGray, Imgproc.COLOR_BGR2GRAY);

                // Perform template matching
                Mat result = new Mat();
                Imgproc.matchTemplate(screenshotGray, templateGray, result, Imgproc.TM_CCOEFF_NORMED);

                // Set a threshold (adjust as needed)
                double threshold = 1.0;

                // Find locations where the result is above the threshold
                Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
                points[i++] = mmr.maxLoc;
            }

            // Convert the result back to BufferedImage
            return points;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    private static Mat convertToMat(BufferedImage image) {
//        byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
//        Mat mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
//        mat.put(0, 0, data);
//        return mat;
//    }

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