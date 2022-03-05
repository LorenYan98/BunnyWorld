package edu.stanford.cs108.bunnyworld;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.shapes.Shape;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BPage {
    public static int pageCount = 1;
    private String pageName;
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
        this.shapeMap = new LinkedHashMap<>();
    }

    private String generateNextPageName() {
        String curPageName = "page" + pageCount;
        pageCount++;
        return curPageName;
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

    public boolean isWithinPage(float x, float y) {
        return y <= bottom && y >= top && x <= right && x >= left;
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

    private boolean shapeIsSelectedWithinPage(BShape curShape, float curX, float curY) {
        return curShape.getBottom() >= curY && curShape.getTop() <= curY && curShape.getLeft() <= curX && curShape.getRight() >= curX;
    }

    public void addShape(BShape shape) {
        if (needsRelocate(shape)) {
            relocate(shape);
        }
        shapeMap.put(shape.getShapeName(), shape);
    }

    public BShape selectShape(float curX, float curY) {
        List<BShape> shapeList = new ArrayList<>(shapeMap.values());

        for (int i = shapeList.size() - 1; i > -1; i--) {
            BShape curShape = shapeList.get(i);
            if (shapeIsSelectedWithinPage(curShape,curX,curY)) {
                return curShape;
            }
        }
        return null;
    }

    private void drawBoundary(Canvas canvas){
        Paint greyOutlinePaint;
        greyOutlinePaint = new Paint();
        greyOutlinePaint.setColor(Color.GRAY);
        greyOutlinePaint.setStyle(Paint.Style.STROKE);
        greyOutlinePaint.setStrokeWidth(5.0f);
        canvas.drawLine(0.0f,bottom,right - left,bottom - top, greyOutlinePaint);
    }

    public void drawPage(Canvas canvas) {
        drawBoundary(canvas);
        for (BShape curShape : shapeMap.values()) {
            // Instead of relocate when drawing, already relocated when adding the shapes
            curShape.draw(canvas);
            System.out.println("Page" + curShape.getLeft() + " " + curShape.getTop() + " " + curShape.getRight() + " " + curShape.getBottom());
        }
    }

    public Map<String, BShape> getShapeMap() {
        return shapeMap;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    @Override
    public String toString(){
        return this.pageName;
    }
}