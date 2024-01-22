import org.opencv.core.Point;

import java.awt.image.BufferedImage;

public class Scribbler {

    // Scribbler
    // Draw:
    // get bounds from gartic()
    // scale image down or crop if it doesnt fit
    // (call on color approximator to find colors)
    // call on gartic to find color locations
    // click on respective color
    // using bounds given by gartic draw the image

    Point[] bounds;
    Scribbler(){

    }

    public void setBounds(BufferedImage screen){
        this.bounds = TemplateSearch.templateSearch(screen, Gartic.getBoundingImgs());
    }

    public void clickColor(){

    }


}
