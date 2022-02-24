package edu.stanford.cs108.bunnyworld;

import java.util.List;

public class Inventory {
    private final int boundary;
    private final List<Shapes> shapeList;

    Inventory(int boundary, List<Shapes> shapeList) {
        this.shapeList = shapeList;
        this.boundary = boundary;
    }

    public boolean isWithinInventory(double y) {
        return y > boundary;
    }

    public void drawInventory() {
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
}
