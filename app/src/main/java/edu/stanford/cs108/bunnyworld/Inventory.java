package edu.stanford.cs108.bunnyworld;

import android.graphics.Canvas;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private float left;
    private float top;
    private float right;
    private float bottom;
    public static Map<String, BShape> shapeMap;


    public void setLeft(float left) {
        this.left = left;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public void setRight(float right) {
        this.right = right;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }

    public Inventory(float left, float top, float right, float bottom) {
//        this.boundary = boundary;
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.shapeMap = new HashMap<>();

    }


    public boolean isWithinInventory(float x, float y) {
        return y <= bottom && y >= top && x <= right && x >= left;
    }

    public void addShape(BShape shape) {
        shapeMap.put(shape.getShapeName(), shape);
    }

    public BShape selectShape(float curX, float curY) {
        for (BShape curShape : shapeMap.values()) {
            if (curShape.getBottom() >= curY && curShape.getTop() <= curY && curShape.getLeft() <= curX && curShape.getRight() >= curX) {
                return curShape;
            }
        }
        return null;
    }

    public static void drawInventory(Canvas canvas) {
        for (BShape curShape : shapeMap.values()) {
            curShape.draw(canvas);
        }
    }

//    public List<Shapes> getShapeList() {
//        return shapeList;
//    }

}
