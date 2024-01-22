import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ScreenCapture {

    public static BufferedImage capture() throws AWTException {
        // Create an instance of Robot
        Robot robot = new Robot();

        // Capture the entire screen
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

        return robot.createScreenCapture(screenRect);

        // Save the screenshot to a file
//            ImageIO.write(screenshot, "png", new File("screenshot.png"));
    }
}