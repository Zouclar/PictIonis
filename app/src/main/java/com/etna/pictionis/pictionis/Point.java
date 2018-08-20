package com.etna.pictionis.pictionis;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Zouclar on 25/06/2018.
 */

public class Point {
    public double X;
    public double Y;
    public String type;

    public Point() {
        X = 0.0;
        Y = 0.0;
        type = "";
    }

    public Point(HashMap<Double,Double> e,String type) {
        X =  e.get("X");
        Y =  e.get("Y");
        this.type = type;
    }

    public Point(HashMap<Double,Double> e) {
        X =  e.get("X");
        Y =  e.get("Y");
        this.type = "start";
    }
}
