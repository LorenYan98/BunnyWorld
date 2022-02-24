package edu.stanford.cs108.bunnyworld;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Shapes {
    private String ShapeName;
    private String text;
    private int fontSize;
    private String imageName;
    private Bitmap image;

    private boolean movable = false;
    private boolean visible = false;

    private float left;
    private float right;
    private float top;
    private float bottom;

    public Shapes(String shapeName, String text, int fontSize, String imageName, Bitmap image, boolean movable,
                  boolean visible, float left, float right, float top, float bottom) {
        ShapeName = shapeName;
        this.text = text;
        this.fontSize = fontSize;
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

    // maybe unnecessary as handout didn't mention the needs of accessing the font size
    public int getFontSize() { return fontSize; }

    public String getImageName() {
        return imageName;
    }

    public void setText(String text) {
        this.text = text;
    }

    // maybe unnecessary as handout didn't mention the needs of changing the font size
    public void setFontSize(int fontSize) { this.fontSize = fontSize; }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setMovable(boolean movable) {
        this.movable = movable;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * draws the object. exams the internal types of shape(rect vs. image vs. text) and
     * draws accordingly
     * @param canvas: need to pass in the canvas
     */
    public void draw(Canvas canvas) {
        // first check visible, if visible do not draw and returns
        if (visible) { return; }

        boolean drawRetangle = false;

        // If a shape has both an image and text, the text takes precedence --
        // the text draws and the image does not
        if (!text.isEmpty()) {
            // draws the text

        } else if (!imageName.isEmpty()) {

            // if mageName is not empty, try to draw the corresponding image, however if image cannot
            // be loaded, do not draw image, need to draw grey rectangle instead, will change
            // drawRectangle boolean to true

        }

        if (drawRetangle) {
            canvas.drawRect(left, top, right, bottom, new Paint(Color.GRAY));
        }

    }
}
