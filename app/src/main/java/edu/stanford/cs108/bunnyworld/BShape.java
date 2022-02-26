package edu.stanford.cs108.bunnyworld;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class BShape {


    private String ShapeName = null;
    private String text = null;
    private String imageName = null;

    private boolean movable = false;
    private boolean visible = false;
    private boolean selected = false;

    private float left;
    private float right;
    private float top;
    private float bottom;

    public BShape(String shapeName, String text, String imageName, boolean movable,
                  boolean visible, float left, float top, float right, float bottom) {
        this.ShapeName = shapeName;
        this.text = text;
        this.imageName = imageName;
        this.movable = movable;
        this.visible = visible;
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    // constructor taking care of text only
    public BShape(String shapeName, String text, boolean movable,
                  boolean visible, float left, float top, float right, float bottom) {
        this.ShapeName = shapeName;
        this.text = text;
        this.movable = movable;
        this.visible = visible;
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }


    public void move(float xDistance, float yDistance){
        this.left = left + xDistance;
        this.right = right + xDistance;
        this.top = top + yDistance;
        this.bottom = bottom + yDistance;
    }


    /**
     * draws the object. exams the internal types of shape(rect vs. image vs. text) and
     * draws accordingly
     * @param canvas: need to pass in the canvas
     */
    public void draw(Canvas canvas) {
        // first check visible, if visible do not draw and returns
        if (!getVisible()) { return; }

        // If a shape has both an image and text, the text takes precedence --
        // the text draws and the image does not
        if (text.length() != 0) {

            // draws the text
            canvas.drawText(text, left, top, null);

        } else if (!imageName.isEmpty()) {
            Bitmap curImg = GameView.bitmapMap.get(imageName);
            Bitmap scaledImg = Bitmap.createScaledBitmap(curImg, (int) getWidth(),(int) getHeight(), true);
            canvas.drawBitmap(scaledImg, left, top, null);
        }else{
            canvas.drawRect(left, top, right, bottom, new Paint(Color.GRAY));
        }

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
        return Math.abs(right - left);
    }
    public float getHeight(){
        return Math.abs(top - bottom);
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

    public boolean getMovable() {
        return movable;
    }

    public boolean getVisible() {
        return visible;
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
