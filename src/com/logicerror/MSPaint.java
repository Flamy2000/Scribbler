package com.logicerror;


import java.awt.*;
import java.util.HashMap;

public class MSPaint extends DrawingBoard {
    // locate images to give to scribbler
    public MSPaint(){
        super();
//        BTM_RIGHT = new Picture("src\\res\\img\\gartic\\btm_right.png");
//        TOP_LEFT = new Picture("src\\res\\img\\gartic\\top_left.png");

        colorImageHashMap = new HashMap<Color, Picture>(){{
            put(new Color(0, 0, 0), new Picture("src/res/img/mspaint/mspaint_0_0.jpg"));
            put(new Color(127, 127, 127), new Picture("src/res/img/mspaint/mspaint_0_1.jpg"));
            put(new Color(136, 0, 21), new Picture("src/res/img/mspaint/mspaint_0_2.jpg"));
            put(new Color(237, 28, 36), new Picture("src/res/img/mspaint/mspaint_0_3.jpg"));
            put(new Color(255, 127, 39), new Picture("src/res/img/mspaint/mspaint_0_4.jpg"));
            put(new Color(255, 242, 0), new Picture("src/res/img/mspaint/mspaint_0_5.jpg"));
            put(new Color(34, 177, 76), new Picture("src/res/img/mspaint/mspaint_0_6.jpg"));
            put(new Color(0, 162, 232), new Picture("src/res/img/mspaint/mspaint_0_7.jpg"));
            put(new Color(63, 72, 204), new Picture("src/res/img/mspaint/mspaint_0_8.jpg"));
            put(new Color(163, 73, 164), new Picture("src/res/img/mspaint/mspaint_0_9.jpg"));
            put(new Color(255, 255, 255), new Picture("src/res/img/mspaint/mspaint_1_0.jpg"));
            put(new Color(195, 195, 195), new Picture("src/res/img/mspaint/mspaint_1_1.jpg"));
            put(new Color(185, 122, 87), new Picture("src/res/img/mspaint/mspaint_1_2.jpg"));
            put(new Color(255, 174, 201), new Picture("src/res/img/mspaint/mspaint_1_3.jpg"));
            put(new Color(255, 201, 14), new Picture("src/res/img/mspaint/mspaint_1_4.jpg"));
            put(new Color(239, 228, 176), new Picture("src/res/img/mspaint/mspaint_1_5.jpg"));
            put(new Color(181, 230, 29), new Picture("src/res/img/mspaint/mspaint_1_6.jpg"));
            put(new Color(153, 217, 234), new Picture("src/res/img/mspaint/mspaint_1_7.jpg"));
            put(new Color(112, 146, 190), new Picture("src/res/img/mspaint/mspaint_1_8.jpg"));
            put(new Color(200, 191, 231), new Picture("src/res/img/mspaint/mspaint_1_9.jpg"));
        }};
    }
    public static String COL_WHEEL;
}
