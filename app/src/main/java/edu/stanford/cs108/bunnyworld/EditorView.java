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
    private int selectIndex = 0;
    private static Map<String, BShape> shapeNameRef = new HashMap<>();
    private float actionX1, actionX2, actionY1, actionY2;
    private float shapeLeft, shapeRight, shapeTop, shapeBottom;
    float preX, preY, curX, curY;
    // screen size, hardcoded for the time being
    int viewWidth;
    int viewHeight;
    boolean shapeIsSelected;
    boolean shapeIsDragging;
    BShape selectedShape;

    BPage currentPage;
    static Map<String, MediaPlayer> soundMap;
    static Map<String, Bitmap> bitmapMap;

    public static Map<String, BPage> getPageMap() {
        return pageMap;
    }
    public static Map<String, Bitmap> getbitmapMap() { return bitmapMap; }
    public static Map<String, MediaPlayer> getSoundMap() { return soundMap; }

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
        firstPage = new BPage(0.0f, 0.0f, 1785.0f, 784.0f);
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
        BPage newPage = new BPage(0.0f, 0.0f, viewWidth, viewHeight);

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
    /**
     * react to user click on ADD SHAPE TO VIEW BUTTON
     * get and set the ivar of newly created shape, add the shape to page's shape map, and call
     * update to redraw
     */
    public void addShapeToview(BShape item) {
        BPage tempPage = currentPage;
        tempPage.addShape(item);
        pageMap.put(currentPage.getPageName(), tempPage);
    }

    public void selectIndexUpdate(){
        Map<String, BShape> curMap = currentPage.getShapeMap();
        List<String> shapeKeys = new ArrayList<String>(curMap.keySet());
//        System.out.println(shapeKeys.toString());
        for(int i = shapeKeys.size() - 1; i >= 0; i--){
            if(curMap.get(shapeKeys.get(i)).shapeSize.contains(curX,curY)){

                selectedShape = curMap.get(shapeKeys.get(i));
                System.out.println(curMap.get(shapeKeys.get(i)).shapeSize.toString());
                System.out.println(selectedShape.getImageName());
                selectIndex = i;
                return;
            }
        }
        selectIndex = -1;
    }
    public void highlightSelectShape(Canvas canvas){
        Map<String, BShape> curMap = currentPage.getShapeMap();
        List<String> shapeKeys = new ArrayList<String>(curMap.keySet());
        for(int i = 0; i < shapeKeys.size(); i++){
            String curName = shapeKeys.get(i);
            System.out.println(curName);
            if(curName.equals(selectedShape.getShapeName())) {
                Bitmap curImg = EditorView.bitmapMap.get(curName);
                canvas.drawBitmap(curImg, 5.0f, 5.0f, boundaryLine);
            }
        }

    }

    public void updatePageMap(){
        Map<String, BShape> curMap = currentPage.getShapeMap();
        curMap.put(selectedShape.getShapeName(),selectedShape);
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
        if(radioId == R.id.addShapeRadioButton){
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    actionX1 = event.getX();
                    actionY1 = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    actionX2 = event.getX();
                    actionY2 = event.getY();

                    if (actionX1 > actionX2) {
                        shapeLeft = actionX2;
                        shapeRight = actionX1;
                    } else {
                        shapeLeft = actionX1;
                        shapeRight = actionX2;
                    }

                    if (actionY1>actionY2) {
                        shapeTop = actionY2;
                        shapeBottom = actionY1;
                    } else {
                        shapeTop = actionY1;
                        shapeBottom = actionY2;
                    }
                    invalidate();
            }
        }
        if(radioId == R.id.editShapeRadioButton || radioId == R.id.addShapeRadioButton){
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    curX = event.getX();
                    curY = event.getY();

                    System.out.println(selectedShape);
                    if(selectedShape != null && selectedShape.getMoveable()){
                        selectedShape.move(curX-preX,curY-preY);
                        System.out.println("shape move to " + selectedShape.toString());
                        updatePageMap();
                        invalidate();
                     }
                    preX = curX;
                    preY = curY;

                    break;
                case MotionEvent.ACTION_DOWN:
                    curX = event.getX();
                    curY = event.getY();
                    preX = curX;
                    preY = curY;
                    selectIndexUpdate();
//                    selectedShape = currentPage.selectShape(curX,curY);
                    System.out.println("current select shape is " + selectedShape);
                    System.out.println("cur x :" + curX + " cur Y : " + curY);
                    System.out.println("current index : " + selectIndex);
                    invalidate();
            }
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RadioGroup radioGroup = ((Activity) getContext()).findViewById(R.id.shapeRadioGroup);

        int radioId = radioGroup.getCheckedRadioButtonId();
        if(radioId == R.id.editShapeRadioButton && selectedShape != null) {
//            highlightSelectShape(canvas);
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
//        if (selectedShape != null) selectedShape.draw(canvas);
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
