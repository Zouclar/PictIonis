package com.etna.pictionis.pictionis;

public class PathAction {

    private float x,y;
    private String type;

    public PathAction(String type, float x, float y, float x2, float y2){

        this.type = type;
        this.x = x;
        this.y = y;
    }

    public String getType() {
        return this.type;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
