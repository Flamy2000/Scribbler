package com.logicerror;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EdgeMagnitudePicture {
    private final Picture picture;
    private int maxMagnitude;
    private int minMagnitude;

    public EdgeMagnitudePicture(Picture picture){
        this.picture = picture;
    }

    private static final int BATCH_SIZE = 5000; // Adjust batch size as needed

    /**
     * Detects edges in the image using the Sobel operator and returns an ArrayList containing
     * MagnitudePoints (x, y, magnitude), sorted by magnitude ascending.
     */
    public ArrayList<MagnitudePoint> getEdgeMagnitudes() {
        int width = picture.width();
        int height = picture.height();
        int totalPixelBatches = (int) Math.ceil(((double) width * height) / BATCH_SIZE);

        // Define Sobel operators for horizontal and vertical gradients
        int[][] horizontalSobel = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
        int[][] verticalSobel = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};

        // Initialize an ArrayList to store occurrences of gradient magnitudes
        ArrayList<MagnitudePoint> magnitudeOccurrences = new ArrayList<>();
        maxMagnitude = Integer.MIN_VALUE;
        minMagnitude = Integer.MAX_VALUE;

        try (ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()/4)) {
            for (int batch = 0; batch < totalPixelBatches; batch++) {
                int startIndex = batch * BATCH_SIZE;
                int endIndex = Math.min((batch + 1) * BATCH_SIZE, width * height);

                executor.submit(() -> {
                    for (int index = startIndex; index < endIndex; index++) {
                        int x = index % width;
                        int y = index / width;

                        // Compute horizontal and vertical gradients
                        int horizontalGradient = 0;
                        int verticalGradient = 0;
                        for (int j = -1; j <= 1; j++) {
                            for (int i = -1; i <= 1; i++) {
                                int xPos = Math.max(0, Math.min(x + i, width - 1));
                                int yPos = Math.max(0, Math.min(y + j, height - 1));
                                int rgb = picture.getBufferedImage().getRGB(xPos, yPos);
                                int gray = getGrayScale(rgb);
                                horizontalGradient += gray * horizontalSobel[j + 1][i + 1];
                                verticalGradient += gray * verticalSobel[j + 1][i + 1];
                            }
                        }

                        // Compute the magnitude of the gradient
                        int magnitude = (int) Math.sqrt(horizontalGradient * horizontalGradient + verticalGradient * verticalGradient);

                        // Update magnitude occurrences
                        synchronized (magnitudeOccurrences) {
                            magnitudeOccurrences.add(new MagnitudePoint(x, y, magnitude));
                            maxMagnitude = Math.max(maxMagnitude, magnitude);
                            minMagnitude = Math.min(minMagnitude, magnitude);
                        }
                    }
                });
            }
            executor.shutdown();

            // Wait for all tasks to complete
            try {
                executor.awaitTermination(Long.MAX_VALUE, java.util.concurrent.TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return magnitudeOccurrences;
    }


    /**
     * Detects edges in the image using the Sobel operator and returns an ArrayList containing
     * MagnitudePoints (x, y, magnitude), sorted by magnitude ascending.
     */
    public Picture getEdgeMagnitudeImage() {
        ArrayList<MagnitudePoint> magnitudeOccurrences = getEdgeMagnitudes();
        int width = picture.width();
        int height = picture.height();
        BufferedImage edgesImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()/4);

        try {
            int totalMagnitudes = magnitudeOccurrences.size();
            int totalBatches = (int) Math.ceil((double) totalMagnitudes / BATCH_SIZE);

            for (int batch = 0; batch < totalBatches; batch++) {
                int startIndex = batch * BATCH_SIZE;
                int endIndex = Math.min((batch + 1) * BATCH_SIZE, totalMagnitudes);
                ArrayList<MagnitudePoint> batchList = new ArrayList<>(magnitudeOccurrences.subList(startIndex, endIndex));

                executor.submit(() -> {
                    for (MagnitudePoint mPoint : batchList) {
                        // Normalize the magnitude to fit within the range [0, 255]
                        int scaledMagnitude = (int) (255 * (double) (mPoint.magnitude - minMagnitude) / (maxMagnitude - minMagnitude));

                        // Set RGB values with the scaled magnitude
                        edgesImage.setRGB(mPoint.x, mPoint.y, scaledMagnitude << 16 | scaledMagnitude << 8 | scaledMagnitude);
                    }
                });
            }
        } finally {
            executor.shutdown();
        }

        // Wait for all tasks to complete
        try {
            executor.awaitTermination(Long.MAX_VALUE, java.util.concurrent.TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new Picture(edgesImage, picture.imgName);
    }




    // Helper method to convert RGB color to grayscale
    private static int getGrayScale(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        return (int) (0.2126 * r + 0.7152 * g + 0.0722 * b);
    }
}
