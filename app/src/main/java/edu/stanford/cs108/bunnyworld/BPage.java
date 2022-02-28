package edu.stanford.cs108.bunnyworld;

import android.graphics.Canvas;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class BPage {
    public static int pageCount = 1;
    private final String pageName;
//    private final int boundary;
    private float left;
    private float top;
    private float right;
    private float bottom;
    private Map<String, BShape> shapeMap;

    public BPage(float left, float top, float right, float bottom) {
//        this.boundary = boundary;
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.pageName = generateNextPageName();
        this.shapeMap = new HashMap<>();
    }

    private String generateNextPageName() {
        String curPageName = "page " + pageCount;
        pageCount++;
        return curPageName;
    }

    public boolean isWithinPage(float x, float y) {
        return y <= bottom;
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

    public void drawPage(Canvas canvas) {
        for (BShape curShape : shapeMap.values()) {
            curShape.draw(canvas);
        }
    }

    public Map<String, BShape> getShapeMap() {
        return shapeMap;
    }

    public String getPageName() {
        return pageName;
    }
}