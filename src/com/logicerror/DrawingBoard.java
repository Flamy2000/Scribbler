package com.logicerror;

import java.awt.*;
import java.util.HashMap;

public class DrawingBoard {
    protected Picture BTM_RIGHT;
    protected Picture TOP_LEFT;

    protected HashMap<Color, Picture> colorImageHashMap;

    public DrawingBoard(){
//        BTM_RIGHT = new Picture("");
//        TOP_LEFT = new Picture("");
        colorImageHashMap = new HashMap<Color, Picture>();
    }

    public Picture[] getBoundingImgs(){
        return new Picture[]{BTM_RIGHT, TOP_LEFT};
    }

    public HashMap<Color, Picture> getColorImages(){
        return colorImageHashMap;
    }

    public Color[] getColors(){
        return colorImageHashMap.keySet().toArray(new Color[0]);
    }

    public Picture getColorPicture(int r, int g, int b){
        return colorImageHashMap.get(new Color(r, g, b));
    }

    public Picture getColorPicture(Color color){
        return colorImageHashMap.get(color);
    }
}
