package edu.stanford.cs108.bunnyworld;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventory {
    private float leftTopX;
    private float leftTopY;
    private float rightBottomX;
    private float rightBottomY;
    public static Map<String, Shapes> shapeMap;



    public Inventory(float leftTopX, float leftTopY, float rightBottomX, float rightBottomY) {
//        this.boundary = boundary;
        this.leftTopX = leftTopX;
        this.leftTopY = leftTopY;
        this.rightBottomX = rightBottomX;
        this.rightBottomY = rightBottomY;
        this.shapeMap = new HashMap<>();

    }


    public boolean isWithinInventory(float x, float y) {
        return y <= leftTopY;
    }

    public void addShape(Shapes shape) {
        shapeMap.put(shape.getShapeName(), shape);
    }

    public Shapes selectShape(float curX, float curY) {
        for (Shapes curShape : shapeMap.values()) {
            if (curShape.getBottom() <= curY && curShape.getTop() >= curY && curShape.getLeft() <= curX && curShape.getRight() >= curX) {
                return curShape;
            }
        }
        return null;
    }

    public static void drawInventory(Canvas canvas) {
        for (Shapes curShape : shapeMap.values()) {
            curShape.draw(canvas);
        }
    }

//    public List<Shapes> getShapeList() {
//        return shapeList;
//    }

}
