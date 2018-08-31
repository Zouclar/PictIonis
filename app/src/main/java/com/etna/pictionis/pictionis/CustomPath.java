package com.etna.pictionis.pictionis;

import android.graphics.Path;

import java.io.Serializable;
import java.util.ArrayList;

public class CustomPath extends Path implements Serializable{

    public ArrayList<PathAction> actions;

    public CustomPath(ArrayList<PathAction> actions){
        this.actions = actions;
    }

    @Override
    public void moveTo(float x, float y) {
        actions.add(new PathAction("MOVE_TO", x, y, 0, 0));
        super.moveTo(x, y);
    }

    @Override
    public void lineTo(float x, float y){
        actions.add(new PathAction("LINE_TO", x, y, 0, 0));
        super.lineTo(x, y);
    }

    @Override
    public void quadTo(float x1, float y1, float x2, float y2){
        actions.add(new PathAction("QUAD_TO", x1, y1, x2, y2));
        super.quadTo(x1, y1, x2, y2);
    }

    public void setActions(ArrayList<PathAction> actions){
        this.actions = actions;
    }
}
