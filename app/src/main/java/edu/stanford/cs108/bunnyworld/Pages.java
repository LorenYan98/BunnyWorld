package edu.stanford.cs108.bunnyworld;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pages {
    public static int pageCount = 1;
    private final String pageName;
//    private final int boundary;
    private float leftTopX;
    private float leftTopY;
    private float rightBottomX;
    private float rightBottomY;
    private Map<String, Shapes> shapeMap;

    public Pages(float leftTopX, float leftTopY, float rightBottomX, float rightBottomY) {
//        this.boundary = boundary;
        this.leftTopX = leftTopX;
        this.leftTopY = leftTopY;
        this.rightBottomX = rightBottomX;
        this.rightBottomY = rightBottomY;
        this.pageName = generateNextPageName();
        this.shapeMap = new HashMap<>();
    }

    private String generateNextPageName() {
        String curPageName = "page " + pageCount;
        pageCount++;
        return curPageName;
    }

    public boolean isWithinPage(float x, float y) {
        return y <= rightBottomY;
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

    public void drawPage(Canvas canvas) {
        for (Shapes curShape : shapeMap.values()) {
            curShape.draw(canvas);
        }
    }

    public Map<String, Shapes> getShapeMap() {
        return shapeMap;
    }

    public String getPageName() {
        return pageName;
    }
}