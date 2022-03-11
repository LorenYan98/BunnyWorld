package edu.stanford.cs108.bunnyworld;

import android.graphics.Canvas;
import android.graphics.RectF;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private float left;
    private float top;
    private float right;
    private float bottom;
    public static Map<String, BShape> shapeMap;
    private RectF inventoryBoundary;

    public Inventory(float left, float top, float right, float bottom) {
//        this.boundary = boundary;
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.shapeMap = new HashMap<>();
        inventoryBoundary = new RectF(left, top, right, bottom);

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
        if (shape.getRight() > right) {
            // the movement should be negative in this case
            shape.move(right - shape.getRight(), 0);
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
        return  y >= top;
    }

    private boolean shapeIsSelectedWithinInventory(BShape curShape, float curX, float curY) {
        return curShape.getBottom() >= curY && curShape.getTop() <= curY && curShape.getLeft() <= curX && curShape.getRight() >= curX;
    }

    public void addShape(BShape shape) {
        if (needsRelocate(shape)) {
            relocate(shape);
        }
        shapeMap.put(shape.getShapeName(), shape);
    }

    public BShape selectShape(float curX, float curY) {
        for (BShape curShape : shapeMap.values()) {
            if (shapeIsSelectedWithinInventory(curShape,curX,curY)) {
                return curShape;
            }
        }
        return null;
    }

    public Map<String, BShape> getShapeMap() {
        return shapeMap;
    }

    public float getLeft() {
        return left;
    }

    public float getTop() {
        return top;
    }

    public float getRight() {
        return right;
    }

    public float getBottom() {
        return bottom;
    }



    public static void drawInventory(Canvas canvas) {
        for (BShape curShape : shapeMap.values()) {
            // Instead of relocate when drawing, already relocated when adding the shapes
            curShape.setWithinInventory(true);
            curShape.draw(canvas);
            System.out.println("Invent " + curShape.getLeft() + " " + curShape.getTop() + " " + curShape.getRight() + " " + curShape.getBottom());
        }
    }

}
