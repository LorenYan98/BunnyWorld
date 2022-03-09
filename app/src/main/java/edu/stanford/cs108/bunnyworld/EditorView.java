package edu.stanford.cs108.bunnyworld;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

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

    public static Map<String, Bitmap> getbitmapMap() {
        return bitmapMap;
    }

    public static Map<String, MediaPlayer> getSoundMap() {
        return soundMap;
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
        BPage.pageCount = 1;
        firstPage = new BPage(0.0f, 0.0f, 1785.0f, 784.0f);
        pageMap.put(firstPage.getPageName(), firstPage);
        currentPage = firstPage;
    }

    private void loadPages(List<BPage> pages) {
    }

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
     * on click function for renamePage button
     * fatch the text in edittext and set the name for current page if its not
     * page1
     */
    public void renamePage() {
        if (firstPage.getPageName() == currentPage.getPageName()) {
            // add a toast later
            return;
        }
        EditText renamePageNameEditText = ((Activity) getContext()).findViewById(R.id.renamePageName);

        String pageNewName = renamePageNameEditText.getText().toString();
        pageMap.remove(currentPage.getPageName());
        currentPage.setPageName(pageNewName);
        pageMap.put(currentPage.getPageName(),currentPage);
        renamePageNameEditText.setText("");
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


    public void selectIndexUpdate() {
        Map<String, BShape> curMap = currentPage.getShapeMap();
        List<String> shapeKeys = new ArrayList<String>(curMap.keySet());
        for (int i = shapeKeys.size() - 1; i >= 0; i--) {
            if (currentPage.getShapeMap().get(shapeKeys.get(i)).shapeSize.contains(curX, curY)) {
                selectedShape = currentPage.getShapeMap().get(shapeKeys.get(i));
                selectedShape.setSelected(true);
                System.out.println("update!! "+currentPage.getShapeMap().get(shapeKeys.get(i)).shapeSize.toString());
                System.out.println(selectedShape.getImageName());
                selectIndex = i;
                return;
            }
        }
        selectedShape = null;
        selectIndex = -1;
    }
    private void closeHighlights(){
        Map<String, BShape> curMap = currentPage.getShapeMap();
        List<String> shapeKeys = new ArrayList<String>(curMap.keySet());
        for (int i = shapeKeys.size() - 1; i >= 0; i--) {
            curMap.get(shapeKeys.get(i)).setSelected(false);
        }
    }

    public void scaleCurShape(){
        SeekBar scaleSeekbar = ((Activity) getContext()).findViewById(R.id.scaleSeekbar);
        float curWidth = selectedShape.getWidth();
        float curHeight = selectedShape.getHeight() ;

        scaleSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float scaleRatio = (float) (progress+10)/100;
                System.out.println(scaleRatio);
                if(selectedShape != null){
                    System.out.println(curWidth + " and height " + curWidth);
                    BShape tempShape = selectedShape;
                    tempShape.scale(curWidth*scaleRatio,curHeight*scaleRatio);
                    addShapeToview(tempShape);
                    update();
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    public void updateSelectShapeLocation(BShape selectedShape){
        TextView shapeLeft =  ((Activity) getContext()).findViewById(R.id.shapeLeft);
        TextView shapeTop =  ((Activity) getContext()).findViewById(R.id.shapeTop);
        TextView shapeRight = ((Activity) getContext()).findViewById(R.id.shapeRight);
        TextView shapeBottom = ((Activity) getContext()).findViewById(R.id.shapeBottom);
        if(selectedShape != null) {
            float leftPoint = selectedShape.getLeft();
            float topPoint = selectedShape.getTop();
            shapeLeft.setText(Integer.toString((int) (leftPoint >= 0 ? leftPoint : 0)));
            shapeTop.setText(Integer.toString((int) (topPoint >= 0 ? topPoint : 0)));
            shapeRight.setText(Integer.toString((int) (leftPoint + selectedShape.getWidth())));
            shapeBottom.setText(Integer.toString((int) (topPoint + selectedShape.getHeight())));
        }else{
            shapeLeft.setText("");
            shapeTop.setText("");
            shapeRight.setText("");
            shapeBottom.setText("");
        }
    }

    public void updateSelectShapeName(BShape selectedShape){
        TextView curShapeName =  ((Activity) getContext()).findViewById(R.id.currentShapeName);
        EditText currentName = ((Activity) getContext()).findViewById(R.id.renameShapeName);
        EditText currentText = ((Activity) getContext()).findViewById(R.id.shapeTextInput);
        if(selectedShape != null) {
            curShapeName.setText(selectedShape.getShapeName());
        }else{
            curShapeName.setText("");
            currentName.setText("");
        }
        currentText.setText("");
    }

    public void displayShapeInfo(){
        CheckBox isShapeMovable =  ((Activity) getContext()).findViewById(R.id.moveable);
        CheckBox isShapeVisible =  ((Activity) getContext()).findViewById(R.id.visible);
        EditText currentName = ((Activity) getContext()).findViewById(R.id.renameShapeName);
        EditText currentText = ((Activity) getContext()).findViewById(R.id.shapeTextInput);
        updateSelectShapeLocation(selectedShape);
        updateSelectShapeName(selectedShape);
        if(selectedShape.getMovable()){
            isShapeMovable.setChecked(true);
        } else isShapeMovable.setChecked(false);
        if(selectedShape.getVisible()){
            isShapeVisible.setChecked(true);
        }else isShapeVisible.setChecked(false);
        currentName.setText(selectedShape.getShapeName());
        currentText.setText(selectedShape.getText());
    }


    public void updatePageMap() {
        Map<String, BShape> curMap = currentPage.getShapeMap();
        curMap.put(selectedShape.getShapeName(), selectedShape);
    }


    public void update() {
        invalidate();
    }

//    TextView imageName = findViewById(R.id.scriptTextView);
//
//    public static void triggerUpdate(String finalString) {
//        imageName.setText("success");
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        System.out.println(widthSize + " Screen height" + heightSize);

        int width = widthSize * 70 / 100;
        int height = 1120 * 70 / 100;

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
        if (radioId == R.id.addShapeRadioButton || radioId == R.id.editShapeRadioButton) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    curX = event.getX();
                    curY = event.getY();

                    if (selectedShape != null && selectedShape.getMovable()) {
                        selectedShape.move(curX - preX, curY - preY);
                        System.out.println("shape move to " + selectedShape.toString());
                        updatePageMap();
                        invalidate();
                    }
                    preX = curX;
                    preY = curY;
                    updateSelectShapeLocation(selectedShape);
                    break;
                case MotionEvent.ACTION_DOWN:
                    curX = event.getX();
                    curY = event.getY();
                    preX = curX;
                    preY = curY;
                    closeHighlights();
                    selectIndexUpdate();
                    if(selectedShape != null)scaleCurShape();
                    if(radioId == R.id.editShapeRadioButton && selectedShape != null){
                        displayShapeInfo();
                    }else{
                        updateSelectShapeName(selectedShape);
                        updateSelectShapeLocation(selectedShape);
                    }
                    invalidate();
                    System.out.println("current select shape is " + selectedShape);
                    System.out.println("cur x :" + curX + " cur Y : " + curY);
                    System.out.println("current index : " + selectIndex);
                    break;
                case MotionEvent.ACTION_UP:
                    curX = event.getX();
                    curY = event.getY();
                    updateSelectShapeName(selectedShape);
                    if(selectedShape != null)updateScript();
                    invalidate();
            }
        }
        return true;
    }

    public void updateScript() {
        TextView imageName = ((Activity) getContext()).findViewById(R.id.scriptTextView);

        TextView curShapeName =  ((Activity) getContext()).findViewById(R.id.currentShapeName);
        System.out.println("page name: " + EditorView.getPageMap().get(getCurrentPage().getPageName()));
        System.out.println("shape name: "+ curShapeName.getText());
        if (EditorView.getPageMap().get(getCurrentPage().getPageName()).getShapeMap().get(curShapeName.getText()).getScript().toString().equals("")) {
            imageName.setText("No script now");
        } else {
            imageName.setText(EditorView.getPageMap().get(getCurrentPage().getPageName()).getShapeMap().get(curShapeName.getText()).getScript().toString());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RadioGroup radioGroup = ((Activity) getContext()).findViewById(R.id.shapeRadioGroup);

        int radioId = radioGroup.getCheckedRadioButtonId();
//        if(radioId == R.id.editShapeRadioButton && selectedShape != null) {
////            highlightSelectShape(canvas);
//        }else if(radioId == R.id.addShapeRadioButton) {
//            RectF newShape = new RectF(shapeLeft, shapeTop, shapeRight, shapeBottom);
//            canvas.drawRect(newShape,boundaryLine);
//        }

        canvas.drawLine(0, viewHeight, viewWidth, viewHeight, boundaryLine);
        canvas.drawLine(0, 0, viewWidth, 0, boundaryLine);
        canvas.drawLine(viewWidth, 0, viewWidth, viewHeight, boundaryLine);
        canvas.drawLine(0, 0, 0, viewHeight, boundaryLine);
        currentPage.drawPage(canvas);
    }
}




