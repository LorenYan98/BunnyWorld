package edu.stanford.cs108.bunnyworld;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ScriptView extends View {
    public ScriptView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

//    BitmapDrawable carrotDrawable, bunnyDrawable;
    Paint partialOpaquePaint;
    static Map<String, Bitmap> bitmapMap;

    Bitmap curBitmapDrawable;


    private void init() {
        bitmapMap = new HashMap<>();
        bitmapMap.put("carrot", ((BitmapDrawable) getResources().getDrawable(R.drawable.carrot)).getBitmap());
        bitmapMap.put("carrot2", ((BitmapDrawable) getResources().getDrawable(R.drawable.carrot2)).getBitmap());
        bitmapMap.put("duck", ((BitmapDrawable) getResources().getDrawable(R.drawable.duck)).getBitmap());
        bitmapMap.put("death", ((BitmapDrawable) getResources().getDrawable(R.drawable.death)).getBitmap());
        bitmapMap.put("fire", ((BitmapDrawable) getResources().getDrawable(R.drawable.fire)).getBitmap());
        bitmapMap.put("mystic", ((BitmapDrawable) getResources().getDrawable(R.drawable.mystic)).getBitmap());
        bitmapMap.put("nothing", ((BitmapDrawable) getResources().getDrawable(R.drawable.nothing)).getBitmap());
        bitmapMap.put("door", ((BitmapDrawable) getResources().getDrawable(R.drawable.door)).getBitmap());

        partialOpaquePaint = new Paint();
        partialOpaquePaint.setAlpha(64);

        curBitmapDrawable = bitmapMap.get("nothing");

    }

    public void updateInC(String currentShapeImageName){
        System.out.println("current shape name !!! " + currentShapeImageName);
        curBitmapDrawable = bitmapMap.get(currentShapeImageName);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        canvas.drawBitmap(carrotDrawable.getBitmap(),0.0f,0.0f,null);
//        canvas.drawBitmap(carrotDrawable.getBitmap(),100.0f,0.0f,partialOpaquePaint);

//        Bitmap bunnyBitmap = bunnyDrawable.getBitmap();

        // I'm creating Rects here for simplicity, but you really shouldn't
        // be creating objects in onDraw, as it can trigger the garbage collector
        // causing your drawing to stutter.
        // And in fact, calculations such as the bunnyH and bunnyW above can
        // also be moved somewhere else to keep onDraw as fast as possible.

//        canvas.drawBitmap(curBitmapDrawable,
//                null,
//                new RectF(0,0,150,75),
//                null);

//        canvas.drawBitmap(curBitmapDrawable,
//                new Rect(120,250,326,418),
//                new RectF(150,0,355,148),
//                null);


            canvas.drawBitmap(curBitmapDrawable,0,200,null);


      canvas.drawBitmap(curBitmapDrawable,0,200,null);

    }
}
