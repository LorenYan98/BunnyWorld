package edu.stanford.cs108.bunnyworld;

import android.graphics.Bitmap;

public class Shapes {
    private String ShapeName;
    private String text;
    private String imageName;
    private Bitmap image;

    private boolean movable = false;
    private boolean visible = false;

    private float left;
    private float right;
    private float top;
    private float bottom;

    public Shapes(String shapeName, String text, String imageName, Bitmap image, boolean movable, boolean visible, float left, float right, float top, float bottom) {
        ShapeName = shapeName;
        this.text = text;
        this.imageName = imageName;
        this.image = image;
        this.movable = movable;
        this.visible = visible;
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }



    public float getLeft() {
        return left;
    }

    public float getRight() {
        return right;
    }

    public float getTop() {
        return top;
    }

    public float getBottom() {
        return bottom;
    }
    public float getWidth(){
        return right - left;
    }
    public float getHeight(){
        return top - bottom;
    }

    public String getShapeName() {
        return ShapeName;
    }

    public String getText() {
        return text;
    }

    public String getImageName() {
        return imageName;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setMovable(boolean movable) {
        this.movable = movable;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
