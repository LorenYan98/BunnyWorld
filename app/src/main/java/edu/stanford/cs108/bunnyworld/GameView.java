package edu.stanford.cs108.bunnyworld;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameView extends View {

    float preX, preY, curX, curY;
    // screen size, hardcoded for the time being
    int viewWidth = 2208;
    int viewHeight = 860;
    boolean shapeIsSelected;
    boolean shapeIsDragging;
    BShape selectedShape;
    BPage currentPage;
    Inventory inventory;
    static Map<String, MediaPlayer> soundMap;
    static Map<String, Bitmap> bitmapMap;
    static Map<String, BPage> pageMap;



    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

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
    }

    private void loadPages() {
        BPage firstPage = new BPage(0.0f, 0.0f, viewWidth, 0.7f * viewHeight);
        Script carrotScript = new Script("oNClick play hooray hide bunny;");
        BShape carrot = new BShape( "", "carrot",  true, true, 0.0f, 30.0f, 300.0f, 500.0f);
        carrot.setScript(carrotScript);
        carrot.setShapeName("carrot");
        BShape bunny = new BShape( "", "duck", true, true, 300.0f, 100.0f, 500.0f, 500.0f);
        bunny.setShapeName("bunny");
        firstPage.addShape(carrot);
        firstPage.addShape(bunny);
        currentPage = firstPage;
        pageMap.put("page1", firstPage);
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        currentPage.drawPage(canvas);
        Inventory.drawInventory(canvas);
        if (selectedShape != null) selectedShape.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handleActionDown(event);
                break;
            case MotionEvent.ACTION_UP:
                handleActionUP(event);
                break;
            case MotionEvent.ACTION_MOVE:
                handleActionMove(event);
        }

        return true;
    }


    // get screen size. Don't need, for now
//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//        viewWidth = w;
//        viewHeight = h;
//    }

    private void handleActionDown(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                curX = event.getX();
                curY = event.getY();
                // user clicked on inventory
                if (inventory.isWithinInventory(curX, curY)) {
                    if (inventory.selectShape(curX, curY) != null && inventory.selectShape(curX,curY).getVisible()) {
                        selectedShape = inventory.selectShape(curX, curY);
                        inventory.getShapeMap().remove(selectedShape.getShapeName());
                        shapeIsSelected = true;
                    }
                } else {
                // user clicked on currentPage
                    if (currentPage.selectShape(curX, curY) != null && currentPage.selectShape(curX,curY).getVisible()) {
                        selectedShape = currentPage.selectShape(curX, curY);
                        currentPage.getShapeMap().remove(selectedShape.getShapeName());
                        shapeIsSelected = true;
                    }
                }
                preX = curX;
                preY = curY;
                invalidate();
        }
    }

    private void handleActionMove(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                shapeIsDragging = true;
                curX = event.getX();
                curY = event.getY();
                if(shapeIsSelected && selectedShape.getMovable()){
                    selectedShape.move(curX-preX,curY-preY);
                    invalidate();
                }
                preX = curX;
                preY = curY;
        }
    }
    private void handleActionUP(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                curX = event.getX();
                curY = event.getY();
                if(shapeIsSelected){
                    if (shapeIsDragging) {
                        // onDrag
                        if(inventory.isWithinInventory(curX,curY)){
                            inventory.addShape(selectedShape);
                        } else {
                            currentPage.addShape(selectedShape);
                        }
                    } else {
                        // onClick
                        if(inventory.isWithinInventory(curX,curY)){
                            inventory.addShape(selectedShape);
                        }else{
                            currentPage.addShape(selectedShape);
                            Script script = selectedShape.getScript();
                            if (script.getIsOnClick()) {
                                doAction(script.getOnClickActions());
                            }
                        }

                    }
                    selectedShape = null;
                    shapeIsDragging = false;
                    shapeIsSelected = false;
                    invalidate();
                }
        }
    }

    private void doAction(List<String> onClickActions) {
        String verb, target;
        for (int i = 0; i < onClickActions.size(); i += 2) {
            verb = onClickActions.get(i);
            target = onClickActions.get(i + 1);
            switch (verb) {
                case "goto":
                    currentPage = pageMap.get(target);
                    break;
                case "play":
                    soundMap.get(target).start();
                    break;
                case "hide":
                    // for testing, 需要改
                    currentPage.getShapeMap().get(target).setVisible(false);
                    break;
                case "show":
                    currentPage.getShapeMap().get(target).setVisible(true);
                    break;
            }

        }

    }
}
