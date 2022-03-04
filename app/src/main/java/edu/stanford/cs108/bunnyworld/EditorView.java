package edu.stanford.cs108.bunnyworld;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.renderscript.Sampler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
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
        boundaryLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        boundaryLine.setColor(Color.BLACK);
        boundaryLine.setStyle(Paint.Style.STROKE);
        boundaryLine.setStrokeWidth(5.0f);

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
        update();
    }

    public void deletePage() {
        if (pageMap.get(firstPage.getPageName()) == currentPage) {
            // add a toast later
            return;
        }
        pageMap.remove(currentPage.getPageName());
        setCurrentPage(firstPage);
        update();
    }

    public void setCurrentPage(BPage currentPage) {
        this.currentPage = pageMap.get(currentPage.getPageName());
        update();
    }



    public void update() {
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        currentPage.drawPage(canvas);
        canvas.drawLine(0, viewHeight*7/10,viewWidth,viewHeight*7/10,boundaryLine);
        canvas.drawLine(0,0,viewWidth,0,boundaryLine);
        canvas.drawLine(viewWidth,0,viewWidth,viewHeight,boundaryLine);
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
        

        , float left, float top, float right, float bottom
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
