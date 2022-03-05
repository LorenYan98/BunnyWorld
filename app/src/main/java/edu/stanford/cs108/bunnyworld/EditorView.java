package edu.stanford.cs108.bunnyworld;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.renderscript.Sampler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditorView extends View {
    public EditorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private static Map<String, BShape> shapeNameRef = new HashMap<>();
    float actionx1, actionx2, actiony1, actiony2;
    float shapeLeft, shapeRight, shapeTop, shapeBottom;
    float preX, preY, curX, curY;
    // screen size, hardcoded for the time being
    int viewWidth;
    int viewHeight;
    boolean shapeIsSelected;
    boolean shapeIsDragging;
    BShape selectedShape;

    BPage currentPage;
    Inventory inventory;
    static Map<String, MediaPlayer> soundMap;
    static Map<String, Bitmap> bitmapMap;

    public static Map<String, BPage> getPageMap() {
        return pageMap;
    }

    static Map<String, BPage> pageMap;

    private BPage firstPage;

    Paint boundaryLine;

    // Initialization
    private void init() {
        shapeIsSelected = false;
        shapeIsDragging = false;
        pageMap = new HashMap<>();
        bitmapMap = new HashMap<>();
        soundMap = new HashMap<>();
        inventory = new Inventory(0.0f, 0.7f * viewHeight, viewWidth, viewHeight);
        loadSound();
        loadBitmap();
        loadPages();
        boundaryLine = new Paint();
        boundaryLine.setColor(Color.BLACK);
        boundaryLine.setStyle(Paint.Style.STROKE);
        boundaryLine.setStrokeWidth(5.0f);

    }

    public BPage getCurrentPage() {
        return currentPage;
    }

    private void loadPages() {
        firstPage = new BPage(0.0f, 0.0f, viewWidth, 0.7f * viewHeight);
        pageMap.put(firstPage.getPageName(),firstPage);
        currentPage = firstPage;
    }

    private void loadPages(List<BPage> pages) {}

    private void loadBitmap() {
        bitmapMap.put("carrot", ((BitmapDrawable) getResources().getDrawable(R.drawable.carrot)).getBitmap());
        bitmapMap.put("carrot2", ((BitmapDrawable) getResources().getDrawable(R.drawable.carrot2)).getBitmap());
        bitmapMap.put("duck", ((BitmapDrawable) getResources().getDrawable(R.drawable.duck)).getBitmap());
        bitmapMap.put("death", ((BitmapDrawable) getResources().getDrawable(R.drawable.death)).getBitmap());
        bitmapMap.put("fire", ((BitmapDrawable) getResources().getDrawable(R.drawable.fire)).getBitmap());
        bitmapMap.put("mystic", ((BitmapDrawable) getResources().getDrawable(R.drawable.mystic)).getBitmap());

    }

    private void loadSound() {
        soundMap.put("carrotcarrotcarrot", MediaPlayer.create(getContext(), R.raw.carrotcarrotcarrot));
        soundMap.put("evillaugh", MediaPlayer.create(getContext(), R.raw.evillaugh));
        soundMap.put("fire", MediaPlayer.create(getContext(), R.raw.fire));
        soundMap.put("hooray", MediaPlayer.create(getContext(), R.raw.hooray));
        soundMap.put("munch", MediaPlayer.create(getContext(), R.raw.munch));
        soundMap.put("munching", MediaPlayer.create(getContext(), R.raw.munching));
        soundMap.put("woof", MediaPlayer.create(getContext(), R.raw.woof));
    }

    public void addPage() {
        BPage newPage = new BPage(0.0f, 0.0f, viewWidth, 0.7f * viewHeight);
        currentPage = newPage;
        pageMap.put(newPage.getPageName(), newPage);
        setCurrentPage(newPage);
    }

    public void deletePage() {
        if (firstPage.getPageName() == currentPage.getPageName()) {
            // add a toast later
            return;
        }
        pageMap.remove(currentPage.getPageName());
        setCurrentPage(firstPage);
    }

    public void setCurrentPage(BPage curPage) {
        this.currentPage = pageMap.get(curPage.getPageName());
    }



    public void update() {
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        System.out.println(widthSize + " Screen height" + heightSize);

        int width = widthSize*70/100;
        int height = 1120*70/100;

        setMeasuredDimension(width, height);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        RadioGroup radioGroup = ((Activity) getContext()).findViewById(R.id.shapeRadioGroup);
        int radioId = radioGroup.getCheckedRadioButtonId();
//        if(radioId == R.id.addShapeRadioButton || radioId == R.id.editShapeRadioButton){
        if(radioId == R.id.addShapeRadioButton){
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    actionx1 = event.getX();
                    actiony1 = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    actionx2 = event.getX();
                    actiony2 = event.getY();

                    if (actionx1 > actionx2) {
                        shapeLeft = actionx2;
                        shapeRight = actionx1;
                    } else {
                        shapeLeft = actionx1;
                        shapeRight = actionx2;
                    }

                    if (actiony1>actiony2) {
                        shapeTop = actiony2;
                        shapeBottom = actiony1;
                    } else {
                        shapeTop = actiony1;
                        shapeBottom = actiony2;
                    }
                    invalidate();
            }
        }
//        if(radioId == R.id.selectRadio || radioId == R.id.eraseRadio){
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    x = event.getX();
//                    y = event.getY();
//                    selectIndexUpdate();
//                    invalidate();
//            }
//        }
        return true;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RadioGroup radioGroup = ((Activity) getContext()).findViewById(R.id.shapeRadioGroup);

        int radioId = radioGroup.getCheckedRadioButtonId();
        if(radioId == R.id.editShapeRadioButton) {
//            drawShape(canvas);
        }else if(radioId == R.id.addShapeRadioButton) {
            RectF newShape = new RectF(shapeLeft, shapeTop, shapeRight, shapeBottom);
            canvas.drawRect(newShape,boundaryLine);
//            String shapeType = radioId == R.id.rectRadio ? "Rect" : "Oval";
//            shapeList.add(new Shape(newShape, shapeType));
//            selectIndex = shapeList.size() - 1;
//            drawShape(canvas);
        }

        canvas.drawLine(0, viewHeight,viewWidth,viewHeight,boundaryLine);
        canvas.drawLine(0,0,viewWidth,0,boundaryLine);
        canvas.drawLine(viewWidth,0,viewWidth,viewHeight,boundaryLine);
        canvas.drawLine(0,0,0,viewHeight,boundaryLine);
        currentPage.drawPage(canvas);
        if (selectedShape != null) selectedShape.draw(canvas);
    }


    /**
     * react to user click on ADD SHAPE TO VIEW BUTTON
     * get and set the ivar of newly created shape, add the shape to page's shape map, and call
     * update to redraw
     */
    public void addShapeToview() {
        // get the needed BShape ivar for the constructor
        String text = ((EditText) findViewById(R.id.shapeTextInput)).getText().toString();
        String imageName = ((Spinner) findViewById(R.id.shapeImageSpinner)).getSelectedItem().toString();
        boolean movable = ((CheckBox) findViewById(R.id.moveable)).isChecked();
        boolean visible = ((CheckBox) findViewById(R.id.visible)).isChecked();

        // don't have moveable or clickable ivar in BShape yet

        // check if there are value in left, right, top and bottom, if not ask use to draw
        

//        , float left, float top, float right, float bottom
        // create a new BShape instance
//        BShape bshape = new BShape()
    }




//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                handleActionDown(event);
//                break;
//            case MotionEvent.ACTION_UP:
//                handleActionUP(event);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                handleActionMove(event);
//        }
//
//        return true;
//    }
}
