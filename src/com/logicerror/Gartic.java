package com.logicerror;


import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;

public class Gartic extends DrawingBoard {
    // locate images to give to scribbler
    public Gartic(){
        super();
        BTM_RIGHT = new Picture("src\\res\\img\\gartic\\btm_right.png");
        TOP_LEFT = new Picture("src\\res\\img\\gartic\\top_left.png");

        colorImageHashMap = new HashMap<Color, Picture>(){{
            put(new Color(0, 0, 0), new Picture("src/res/img/gartic/gartic_black.png"));
//            put(new Color(102, 102, 102), new Picture(""));
//            put(new Color(0, 80, 205), new Picture(""));
//            put(new Color(255, 255, 255), new Picture(""));
//            put(new Color(170, 170, 170), new Picture(""));
//            put(new Color(38, 201, 255), new Picture(""));
//            put(new Color(1, 116, 32), new Picture(""));
//            put(new Color(153, 0, 0), new Picture(""));
//            put(new Color(150, 65, 18), new Picture(""));
//            put(new Color(17, 176, 60), new Picture(""));
//            put(new Color(255, 0, 19), new Picture(""));
//            put(new Color(255, 120, 41), new Picture(""));
//            put(new Color(176, 112, 28), new Picture(""));
//            put(new Color(153, 0, 78), new Picture(""));
//            put(new Color(203, 90, 87), new Picture(""));
//            put(new Color(255, 193, 38), new Picture(""));
//            put(new Color(255, 0, 143), new Picture(""));
//            put(new Color(254, 175, 168), new Picture(""));
        }};
    }
    public static String COL_WHEEL;
}
