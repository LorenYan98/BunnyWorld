package edu.stanford.cs108.bunnyworld;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class BShape {


    private String shapeName = null;
    private String text = null;
    private String imageName = null;

    private boolean movable = false;
    private boolean visible = false;
    private boolean selected = false;

    private int textSize = 0;
    private static int shapeCount = 1;

    private float left;
    private float right;
    private float top;
    private float bottom;

    // paint for text with explicitly set textSize
    private Paint textPaint;
    private Paint rectPaint = new Paint(Color.GRAY);

    private Bitmap scaledImg;

    public BShape(String text, String imageName, boolean movable,
                  boolean visible, float left, float top, float right, float bottom) {
        setShapeNameToDefault();
        this.text = text;
        this.imageName = imageName;
        this.movable = movable;
        this.visible = visible;
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        init();

    }

    // constructor taking care of text only
    public BShape(String text, boolean movable,
                  boolean visible, float left, float top, float right, float bottom) {
        setShapeNameToDefault();
        this.text = text;
        this.movable = movable;
        this.visible = visible;
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        init();

    }

    /**
     * init function to initiate the paint, and BitMap
     */
    private void init() {

        // it text exists and textSize is not 0, initiate paint for text using asssigned size
        if (text.length() != 0 &&
                textSize != 0
        ) {
            // textSize was explicitly set, draw using textSize
            textPaint = new Paint();
            textPaint.setStyle(Paint.Style.STROKE);
            textPaint.setTextSize(textSize);
        } else if (!imageName.isEmpty() &&
                GameView.bitmapMap.containsKey(imageName)) {
            // need to draw image, initiate scaled image
            Bitmap curImg = GameView.bitmapMap.get(imageName);
            scaledImg = Bitmap.createScaledBitmap(curImg, (int) getWidth(),(int) getHeight(), true);
        }

    }
    //Resize the image contained in the BShape
    public void scale(float width, float height){
        if(scaledImg != null){
            scaledImg = Bitmap.createScaledBitmap(scaledImg, (int) width,(int) height, true);
        }
    }
    /**
     * will set ShapeName to default shape1, shape2, ... if the name is empty, and increment shapeCount
     */
    private void setShapeNameToDefault() {
        if (shapeName.length() == 0) {
            shapeName = "shape" + shapeCount;
            shapeCount++;
        }
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
            if (textSize == 0) {
                // if textSize wasn't explicitly set, draw with defaul size
                canvas.drawText(text, left, top, null);
            } else {
                // textSize was explicitly set, draw using textSize
                canvas.drawText(text, left, top, textPaint);
            }

        } else if (!imageName.isEmpty() &&
                GameView.bitmapMap.containsKey(imageName)) {
            canvas.drawBitmap(scaledImg, left, top, null);
        } else {
            canvas.drawRect(left, top, right, bottom, rectPaint);
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
        return shapeName;
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

    public void setTextSize(int textSize) {
        this.textSize = textSize;
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
