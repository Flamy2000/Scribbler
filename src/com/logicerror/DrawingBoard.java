package com.logicerror;

public class DrawingBoard {


    protected String BTM_RIGHT;
    protected String TOP_LEFT;

    protected static String[] COLORS;

    public DrawingBoard(){
        BTM_RIGHT = "";
        TOP_LEFT = "";
        COLORS = new String[]{"000000"};
    }

    public String[] getBoundingImgs(){
        return new String[]{BTM_RIGHT, TOP_LEFT};
    }


//    public static Point getColors(){
////        HashMap<Integer, Color> colorRef = new HashMap<>();
//
//    }
}
