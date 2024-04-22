package com.logicerror;

import java.awt.*;

public class MagnitudePoint extends Point {
    public int x;
    public int y;
    public int magnitude;
    public MagnitudePoint(int x, int y, int magnitude) {
        this.x = x;
        this.y = y;
        this.magnitude = magnitude;
    }

    @Override
    public String toString(){
        return "(" + x + ", " + y + ", " + magnitude + ")";
    }
}
