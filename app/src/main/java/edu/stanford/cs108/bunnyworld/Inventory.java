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


    public Inventory(float left, float top, float right, float bottom) {
//        this.boundary = boundary;
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.shapeMap = new HashMap<>();

    }

    public boolean needsRelocate(BShape shape) {
        if (shape.getBottom() > bottom || shape.getTop() < top || shape.getLeft() < left || shape.getRight() > right) {
            return true;
        }
        return false;
    }

    public void relocate(BShape shape) {
        // DOES NOT CONSIDER THE CASE WHEN THE SHAPE IS LARGE ENOUGH TO FILL THE WHOLE PAGE/INVENTORY, ADD LATER
        if (shape.getBottom() > bottom) {
            // the movement should be negative in this case
            shape.move(0, bottom - shape.getBottom());
        }
        if (shape.getTop() < top) {
            // the movement should be positive in this case
            shape.move(0, top - shape.getTop());
        }
        if (shape.getLeft() < left) {
            // the movement should be positive in this case
            shape.move(left - shape.getLeft(), 0);
        }
        if (shape.getTop() > right) {
            // the movement should be negative in this case
            shape.move(right = shape.getRight(), 0);
        }
    }
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

    public boolean isWithinInventory(float x, float y) {
        return y <= bottom && y >= top && x <= right && x >= left;
    }

    public boolean shapeIsWithinInventory(BShape curShape, float curX, float curY) {
        return curShape.getBottom() >= curY && curShape.getTop() <= curY && curShape.getLeft() <= curX && curShape.getRight() >= curX;
    }

    public void addShape(BShape shape) {
        shapeMap.put(shape.getShapeName(), shape);
    }

    public BShape selectShape(float curX, float curY) {
        for (BShape curShape : shapeMap.values()) {
            if (shapeIsWithinInventory(curShape,curX,curY)) {
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
