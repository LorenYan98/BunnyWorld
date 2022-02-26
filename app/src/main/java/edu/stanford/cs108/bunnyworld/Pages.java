package edu.stanford.cs108.bunnyworld;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

public class Pages {
    public static int pageCount = 1;
    private final String pageName;
//    private final int boundary;
    private float leftTopX;
    private float leftTopY;
    private float pageWidth;
    private float pageHeight;
    private List<Shapes> shapeList;


//    Pages(int boundary, List<Shapes> shapeList) {
//        this.shapeList = shapeList;
//        this.boundary = boundary;
//        this.pageName = generateNextPageName();
//    }

    public Pages(float leftTopX, float leftTopY, float viewWidth, float viewHeight) {
        this.shapeList = new ArrayList<>();
//        this.boundary = boundary;
        this.leftTopX = leftTopX;
        this.leftTopY = leftTopY;
        this.pageWidth = viewWidth;
        this.pageHeight = viewHeight;
        this.pageName = generateNextPageName();
    }

    private String generateNextPageName() {
        String curPageName = "page " + pageCount;
        pageCount++;
        return curPageName;
    }

    public boolean isWithinPage(float x, float y) {
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

    public void drawPage(Canvas canvas) {
        for (int i = 0; i < shapeList.size(); i++) {
            shapeList.get(i).draw(canvas);
        }
    }

    public List<Shapes> getShapeList() {
        return shapeList;
    }

    public String getPageName() {
        return pageName;
    }
}