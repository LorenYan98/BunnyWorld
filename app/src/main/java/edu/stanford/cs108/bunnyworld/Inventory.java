package edu.stanford.cs108.bunnyworld;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private float leftTopX;
    private float leftTopY;
    private float pageWidth;
    private float pageHeight;
    public static List<Shapes> shapeList;



    public Inventory(float leftTopX, float leftTopY, float viewWidth, float viewHeight) {
        this.shapeList = new ArrayList<>();
//        this.boundary = boundary;
        this.leftTopX = leftTopX;
        this.leftTopY = leftTopY;
        this.pageWidth = viewWidth;
        this.pageHeight = viewHeight;
    }


    public boolean isWithinInventory(float x, float y) {
        return y <= leftTopY + pageHeight;
    }

    public void addShape(Shapes shape) {
        shapeList.add(shape);
    }

    public Shapes selectShape(float curX, float curY) {
        for (int i = 0; i < shapeList.size(); i++) {
            if (shapeList.get(i).getBottom() <= curY && shapeList.get(i).getTop() >= curY && shapeList.get(i).getLeft() <= curX && shapeList.get(i).getRight() >= curX) {
                return shapeList.get(i);
            }
        }
        return null;
    }

    public static void drawInventory(Canvas canvas) {
        for (int i = 0; i < shapeList.size(); i++) {
            shapeList.get(i).draw(canvas);
        }
    }

//    public List<Shapes> getShapeList() {
//        return shapeList;
//    }

}
