
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
    private static Map<String, BShape> shapeNameRef = new HashMap<>();

    float preX, preY, curX, curY;
    // screen size, hardcoded for the time being
    int viewWidth = 2560;
    int viewHeight = 1600;
    boolean shapeIsSelected;
    boolean shapeIsDragging;
    BShape selectedShape;
    BPage currentPage;
    Inventory inventory;
    Float[] originalMetrics;
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
        inventory = new Inventory(0.0f, 928.0f, viewWidth, viewHeight);
        loadSound();
        loadBitmap();
        System.out.println("viewHeight:"+viewHeight);
        System.out.println("viewWidth:"+viewWidth);
    }

    private void loadBitmap() {
        bitmapMap.put("carrot", ((BitmapDrawable) getResources().getDrawable(R.drawable.carrot)).getBitmap());
        bitmapMap.put("carrot2", ((BitmapDrawable) getResources().getDrawable(R.drawable.carrot2)).getBitmap());
        bitmapMap.put("duck", ((BitmapDrawable) getResources().getDrawable(R.drawable.duck)).getBitmap());
        bitmapMap.put("death", ((BitmapDrawable) getResources().getDrawable(R.drawable.death)).getBitmap());
        bitmapMap.put("fire", ((BitmapDrawable) getResources().getDrawable(R.drawable.fire)).getBitmap());
        bitmapMap.put("mystic", ((BitmapDrawable) getResources().getDrawable(R.drawable.mystic)).getBitmap());
        bitmapMap.put("door", ((BitmapDrawable) getResources().getDrawable(R.drawable.door)).getBitmap());

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
        if (currentPage != null) {
            currentPage.drawPage(canvas);
        }
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
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
    }

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
                // save original location of selectedShape
                if (selectedShape != null) {
                    System.out.println("selectedShape: " + selectedShape.getShapeName());
                    originalMetrics = new Float[] {selectedShape.getLeft(), selectedShape.getTop(), selectedShape.getRight(), selectedShape.getBottom()};
                } else {
                    System.out.println("Nothing selected");
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
                    // highlight all shapes that can react to selectedShape using onDrop script
                    for (BShape s : currentPage.getShapeMap().values()) {
                        Script script = s.getScript();
                        if (script != null && script.getIsOnDrop() &&
                                script.getOnDropActions().containsKey(selectedShape.getShapeName())) {
                            s.setSelected(true);
                        }
                    }
                    selectedShape.move(curX-preX,curY-preY);
                    invalidate();
                } else {
                    if (selectedShape != null) currentPage.addShape(selectedShape);
                    selectedShape = null;
                    shapeIsDragging = false;
                    shapeIsSelected = false;

                }
                preX = curX;
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

                        // unhighlight all shapes that can react to selectedShape using onDrop script
                        for (BShape s : currentPage.getShapeMap().values()) {
                            Script script = s.getScript();
                            if (script != null && script.getIsOnDrop() &&
                                    script.getOnDropActions().containsKey(selectedShape.getShapeName())) {
                                s.setSelected(false);
                            }
                        }
                        if(inventory.isWithinInventory(curX,curY)){
                            inventory.addShape(selectedShape);
                        } else {
                            // handle onDrop script
                            BShape onDropShape = currentPage.selectShape(curX, curY);
                            if (onDropShape != null) {
                                Script script = onDropShape.getScript();
                                if (script != null && script.isOnDrop && script.getOnDropActions().containsKey(selectedShape.getShapeName())) {
                                    currentPage.addShape(selectedShape);
                                    performActions(script.getOnDropActions().get(selectedShape.getShapeName()));
                                } else {
                                    // no action to be done, snap selectedShape back to original metrics
                                    selectedShape.setLeft(originalMetrics[0]);
                                    selectedShape.setTop(originalMetrics[1]);
                                    selectedShape.setRight(originalMetrics[2]);
                                    selectedShape.setBottom(originalMetrics[3]);
                                    // snap shape back to initial position
                                    if (originalMetrics[1] < inventory.getTop()) {
                                        currentPage.addShape(selectedShape);
                                    } else {
                                        inventory.addShape(selectedShape);
                                    }

                                }
                            } else {
                                // dropped on blank area
                                currentPage.addShape(selectedShape);
                            }

                        }
                    } else {
                        // onClick
                        if(inventory.isWithinInventory(curX,curY)){
                            inventory.addShape(selectedShape);
                        }else{
                            currentPage.addShape(selectedShape);
                            // handle onClick script
                            Script script = selectedShape.getScript();
                            if (script != null && script.getIsOnClick()) {
                                performActions(script.getOnClickActions());
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

    private void performActions(List<String> actions) {
        String verb, target;
        for (int i = 0; i < actions.size(); i += 2) {
            verb = actions.get(i);
            target = actions.get(i + 1);
            switch (verb) {
                case "goto":
                    // if goto another page, handle action
                    if (currentPage != pageMap.get(target)) {
                        currentPage = pageMap.get(target);
                        handleOnEnterScript();
                        invalidate();
                    }
                    break;
                case "play":
                    soundMap.get(target).start();
                    break;
                case "hide":
                    shapeNameRef.get(target).setVisible(false);
                    invalidate();
                    break;
                case "show":
                    shapeNameRef.get(target).setVisible(true);
                    invalidate();
                    break;
            }

        }

    }

    private void handleOnEnterScript() {
        Map<String, BShape> shapes = currentPage.getShapeMap();
        for (BShape shape : shapes.values()) {
            Script script = shape.getScript();
            if (script != null && script.isOnEnter) {
                performActions(script.getOnEnterActions());
            }
        }

    }

    public void loadPages() {
        BPage firstPage = new BPage(0.0f, 0.0f, viewWidth, 0.7f * viewHeight);
        BPage secondPage = new BPage(0.0f, 0.0f, viewWidth, 0.7f * viewHeight);
        BPage thirdPage = new BPage(0.0f, 0.0f, viewWidth, 0.7f * viewHeight);
        BPage fourthPage = new BPage(0.0f, 0.0f, viewWidth, 0.7f * viewHeight);
        BPage fifthPage = new BPage(0.0f, 0.0f, viewWidth, 0.7f * viewHeight);
        pageMap.put("page1",firstPage);
        pageMap.put("page2",secondPage);
        pageMap.put("page3",thirdPage);
        pageMap.put("page4",fourthPage);
        pageMap.put("page5",fifthPage);
        currentPage = firstPage;
        //first page
        BShape door1 = new BShape("","door",false,true,viewWidth*0.1f,350.0f,viewWidth*0.2f,500.0f);
        BShape door2 = new BShape("","door",false,false,viewWidth*0.4f,30.0f,viewWidth*0.6f,500.0f);
        BShape door3 = new BShape("","door",false,true,viewWidth*0.75f,350.0f,viewWidth*0.85f,500.0f);
        BShape hello  = new BShape("Welcome to Bunny World!",false,true,viewWidth*0.075f,viewHeight*0.3f,viewWidth*0.15f,viewHeight*0.4f);
        BShape intro = new BShape("You are in a maze of twisty little passages, all alike.",false,true,viewWidth*0.15f,viewHeight*0.34f,viewWidth*0.25f,viewHeight*0.42f);
        door1.setScript(new Script("onclick goto page2"));
        door2.setScript(new Script("onclick goto page3"));
        door3.setScript(new Script("onclick goto page4"));
        door1.setShapeName("door1");
        door2.setShapeName("door2");
        door3.setShapeName("door3");
        firstPage.addShape(door1);
        firstPage.addShape(door2);
        firstPage.addShape(door3);
        shapeNameRef.put("door1", door1);
        shapeNameRef.put("door2", door2);
        shapeNameRef.put("door3", door3);
        firstPage.addShape(hello);
        firstPage.addShape(intro);
//        //second page
        BShape door4 = new BShape("","door",false,true,viewWidth*0.75f,30.0f,viewWidth*0.95f,500.0f);
        BShape mystic = new BShape( "", "mystic",  false, true, viewWidth*0.1f, 380.0f, viewWidth*0.25f, 700.0f);
        BShape rubTummy  = new BShape("Mystic Bunny-Rub my tummy for a big surprise!",false,true,viewWidth*0.1f, 750.0f, viewWidth*0.3f, 800.0f);
        door4.setShapeName("door4");
        mystic.setShapeName("mystic");
        secondPage.addShape(door4);
        secondPage.addShape(mystic);
        shapeNameRef.put("door4", door4);
        shapeNameRef.put("mystic", mystic);
        secondPage.addShape(rubTummy);
        door4.setScript(new Script("onClick goto page1") );
        //to be implemented: show/hide shape in different page
        mystic.setScript(new Script("onclick hide carrot play munching;onEnter show door2"));
        //third page
        BShape fire = new BShape( "", "fire", false, true, viewWidth*0.2f, 200.0f, viewWidth*0.3f, 500.0f);
        BShape carrot = new BShape( "", "carrot",  true, true,viewWidth*0.8f, viewHeight*0.3f, viewWidth*0.95f, viewHeight*0.53f);
        BShape door5 = new BShape("","door",false,true,viewWidth*0.6f,viewHeight*0.3f,viewWidth*0.75f,viewHeight*0.5f);
        BShape fireRoom = new BShape("Eek! Fire-Room. Run away!",false,true,0.2f*viewWidth,0.3f*viewHeight,0.3f*viewWidth,0.4f*viewHeight);
        fire.setScript(new Script("onEnter play fire"));
        door5.setScript(new Script("onclick goto page2"));
        fire.setShapeName("fire");
        carrot.setShapeName("carrot");
        door5.setShapeName("door5");
        shapeNameRef.put("door5", door5);
        shapeNameRef.put("carrot", carrot);
        shapeNameRef.put("fire", fire);
        thirdPage.addShape(fire);
        thirdPage.addShape(carrot);
        thirdPage.addShape(door5);
        thirdPage.addShape(fireRoom);
        //fourth page
        //BShape duck = new BShape("","duck",false,true,viewWidth*0.8f,0f,viewWidth*0.9f,100f);
        BShape death = new BShape( "", "death", false, true, 300.0f, 100.0f, 600.0f, 500.0f);
        BShape door6 = new BShape("","door",false,false,viewWidth*0.7f,300.0f,viewWidth*0.9f,700.0f);
        BShape bunnyDeath = new BShape("You must appease the Bunny of Death!",false,true,300.0f,550.0f,500.0f,600.0f);
        death.setScript(new Script("onenter play evillaugh;ondrop carrot hide carrot play munching hide death show door6;onclick play evillaugh"));
        door6.setScript(new Script("onClick goto page5") );
        death.setShapeName("death");
        door6.setShapeName("door6");
        //duck.setShapeName("duck");
        shapeNameRef.put("door6", door6);
        shapeNameRef.put("death", death);
        //shapeNameRef.put("duck",duck);
        //fourthPage.addShape(duck);
        fourthPage.addShape(death);
        fourthPage.addShape(door6);
        fourthPage.addShape(bunnyDeath);
        //fifth page
        BShape carrot1 = new BShape( "", "carrot",  false, true, 0.8f*viewWidth, viewHeight*0.18f, 0.88f*viewWidth, viewHeight*0.28f);
        BShape carrot2 = new BShape( "", "carrot",  false, true, 0.8f*viewWidth, viewHeight*0.3f, 0.88f*viewWidth, viewHeight*0.4f);
        BShape carrot3 = new BShape( "", "carrot",  false, true, 0.8f*viewWidth, viewHeight*0.42f, 0.88f*viewWidth, viewHeight*0.52f);
        BShape win = new BShape("You win! Yay!",false,true,viewWidth*0.8f,0.1f*viewHeight,viewWidth*0.9f,0.15f*viewHeight);
        win.setScript(new Script("onenter play hooray"));
        fifthPage.addShape(carrot1);
        fifthPage.addShape(carrot2);
        fifthPage.addShape(carrot3);
        fifthPage.addShape(win);
        shapeNameRef.put("carrot1", carrot1);
        shapeNameRef.put("carrot2", carrot2);
        shapeNameRef.put("carrot3", carrot3);
        handleOnEnterScript();

    }

    void loadPages(List<BPage> pages) {
        for (BPage p : pages) {
            pageMap.put(p.getPageName(), p);
            if (p.getPageName().equals("page1")) {
                currentPage = p;
            }
            for (String s : p.getShapeMap().keySet()) {
                shapeNameRef.put(s, p.getShapeMap().get(s));
            }
        }
        for(String s:shapeNameRef.keySet()){
            System.out.println(s+" "+shapeNameRef.get(s).getScriptString()+" "+shapeNameRef.get(s).getScript());
        }
        handleOnEnterScript();
    }
}