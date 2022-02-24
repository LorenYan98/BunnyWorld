package edu.stanford.cs108.bunnyworld;

import java.util.List;

public class Pages {
    public static int pageCount = 1;
    private final String pageName;
    private final int boundary;
    private final List<Shapes> shapeList;


    Pages(int boundary, List<Shapes> shapeList) {
        this.shapeList = shapeList;
        this.boundary = boundary;
        this.pageName = generateNextPageName();
    }

    private String generateNextPageName() {
        String curPageName = "page " + pageCount;
        pageCount++;
        return curPageName;
    }

    public boolean isWithinPage(double y) {
        return y <= boundary;
    }

    public void drawPage() {
        for (int i = 0; i < shapeList.size(); i++) {
            shapeList.get(i).draw();
        }
    }

    public int getBoundary() {
        return boundary;
    }

    public List<Shapes> getShapeList() {
        return shapeList;
    }

    public String getPageName() {
        return pageName;
    }
}