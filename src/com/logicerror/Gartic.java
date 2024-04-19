package com.logicerror;


public class Gartic extends DrawingBoard {
    // com.logicerror.Gartic:

    // locate images to give to scribbler
    public Gartic(){
        super();
        BTM_RIGHT = "src\\res\\img\\gartic\\btm_right.png";
        TOP_LEFT = "src\\res\\img\\gartic\\top_left.png";
        COLORS = new String[]{
                "000000", "666666", "0050cd",
                "ffffff", "aaaaaa", "26c9ff",
                "017420", "990000", "964112",
                "11b03c", "ff0013", "ff7829",
                "b0701c", "99004e", "cb5a57",
                "ffc126", "ff008f", "feafa8"
        };

    }
    public static String COL_WHEEL;


//    public static Point getColors(){
////        HashMap<Integer, Color> colorRef = new HashMap<>();
//
//    }
}
