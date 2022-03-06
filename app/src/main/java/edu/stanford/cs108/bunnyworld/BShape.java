package edu.stanford.cs108.bunnyworld;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class BShape {

    public RectF shapeSize;
    private String shapeName = "";
    private String text = "";
    private String imageName = "";

    private boolean moveable = false;
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

    // add script ivar by Shuangshan LI / Mabel Jiang
    private Script script;

    public BShape(String text, String imageName, boolean moveable,
                  boolean visible, float left, float top, float right, float bottom) {
        setShapeNameToDefault();
        this.text = text;
        this.imageName = imageName;
        this.moveable = moveable;
        this.visible = visible;
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        this.shapeSize = new RectF(left, top, right, bottom);
        init();

    }

    // constructor taking care of text only
    public BShape(String text, boolean moveable,
                  boolean visible, float left, float top, float right, float bottom) {
        setShapeNameToDefault();
        this.text = text;
        this.moveable = moveable;
        this.visible = visible;
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        this.shapeSize = new RectF(left, top, right, bottom);
        init();

    }

    /**
     * init function to initiate the paint, and BitMap
     */
    private void init() {
        // set up the paint for text
        textPaint = new Paint();
        textPaint.setStyle(Paint.Style.STROKE);
        // set a default textSize at 20.0f
        textPaint.setTextSize(20.0f);
//        System.out.println(EditorView.bitmapMap.get(imageName));
        if(!EditorView.bitmapMap.isEmpty()){
            if (textSize != 0) {
                textPaint.setTextSize(textSize);
            } else if (imageName.length() != 0 &&
                    EditorView.bitmapMap.containsKey(imageName)) {
                Bitmap curImg = EditorView.bitmapMap.get(imageName);
                scaledImg = Bitmap.createScaledBitmap(curImg, (int) getWidth(),(int) getHeight(), true);
            }
        }else if(!GameView.bitmapMap.isEmpty()){
            if (textSize != 0) {
                textPaint.setTextSize(textSize);
            } else if (imageName.length() != 0 &&
                    GameView.bitmapMap.containsKey(imageName)) {
                Bitmap curImg = GameView.bitmapMap.get(imageName);
                scaledImg = Bitmap.createScaledBitmap(curImg, (int) getWidth(),(int) getHeight(), true);
            }
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
                canvas.drawText(text, left, top, textPaint);
        } else if (imageName.length() != 0) {
            if(!EditorView.bitmapMap.isEmpty()){
                Rect newshape = new Rect((int)left, (int)top, (int)right, (int)bottom);
                System.out.println(newshape.toString());
                canvas.drawBitmap(scaledImg,null ,newshape, null);
            }else if(!GameView.bitmapMap.isEmpty()){
                canvas.drawBitmap(scaledImg, 5.0f, 5.0f, null);
            }
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

    public void setShapeName(String shapeName) { this.shapeName = shapeName; }

    public String getText() {
        return text;
    }

    public String getImageName() {
        return imageName;
    }

    public boolean getMovable() {
        return moveable;
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

    public void setMoveable(boolean moveable) {
        this.moveable = moveable;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Script getScript() { return script; }

    public void setScript(Script script) { this.script = script; }

    @Override
    public String toString(){
        return "Text: " + text +" Image Name: "+ imageName + " Moveable: " + moveable +" Visible: "+ visible + "Left: " + left + " Top: " + top +" Right: " + right + " Bottom:" + bottom;
    }
}
