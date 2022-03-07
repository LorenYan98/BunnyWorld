package edu.stanford.cs108.bunnyworld;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScriptActivity extends AppCompatActivity {

    private static final String ON_CLICK = "onclick";
    private static final String ON_ENTER = "onenter";
    public static final ArrayList<String> TRIGGERLIST = new ArrayList<String>(Arrays.asList(new String[]{ON_CLICK, ON_ENTER}));
    private static final String GOTO = "goto";
    private static final String PLAY = "play";
    private static final String HIDE = "hide";
    private static final String SHOW = "show";
    public static final ArrayList<String> ACTIONLIST = new ArrayList<String>(Arrays.asList(new String[]{GOTO, PLAY, HIDE, SHOW}));

    private String onClickString;
    private String onEnterString;
    private String onDropString;

    private String currentTrigger;
    private String currentAction;
    private String currentPageSound;
    private String currentShape;

    public void setCurrentTrigger(String currentTrigger) {
        this.currentTrigger = currentTrigger;
    }

    public void setCurrentAction(String currentAction) {
        this.currentAction = currentAction;
    }

    public void setCurrentPageSound(String currentPageSound) {
        this.currentPageSound = currentPageSound;
    }

    public void setCurrentShape(String currentShape) {
        this.currentShape = currentShape;
    }

    public String getCurrentTrigger() {
        return currentTrigger;
    }

    public String getCurrentAction() {
        return currentAction;
    }

    public String getCurrentPageSound() {
        return currentPageSound;
    }

    public String getCurrentShape() {
        return currentShape;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_script);
        init();

    }

    public void init() {
        onClickString = "";
        onEnterString = "";
        onDropString = "";
        updateSpinner();
//        updateSpinner2();
    }

    public void updateSpinner() {
        //the following three code proves
//        setCurrentTrigger("a");
//        setCurrentAction("b");
//        setCurrentPageSound("c");

        Spinner triggerSpinner = (Spinner) findViewById(R.id.triggerSpinner);
        Spinner actionsSpinner = (Spinner) findViewById(R.id.actionsSpinner);
        Spinner pageSoundSpinner = (Spinner) findViewById(R.id.pageSoundSpinner);

        // initiate ArrayList including all the actions
        Map pageMap = EditorView.getPageMap();
        Map soundMap = EditorView.getSoundMap();

        List<String> pageKeyList = new ArrayList<String>(pageMap.keySet());
        List<String> soundKeyList = new ArrayList<String>(soundMap.keySet());
//        List<BPage> pageList = new ArrayList<>(pageMap.values());
//        List<Bitmap> imgList = new ArrayList<>(imgMap.values());
//        List<String> imgNameList = new ArrayList<>(imgMap.keySet());

        // List<Sampler.Value> list = new ArrayList<Sampler.Value>(pageMap.values());


        if (pageKeyList.contains(currentPageSound)) {
            Spinner shapeSpinner = (Spinner) findViewById(R.id.shapeSpinner);
            List shapeList = new ArrayList<String>(EditorView.getPageMap().get(currentPageSound).getShapeMap().keySet());
            setCurrentShape((String) shapeList.get(0));
        }


        setCurrentTrigger(TRIGGERLIST.get(0));
        setCurrentAction(ACTIONLIST.get(0));
        setCurrentPageSound(pageKeyList.get(0));

        ArrayAdapter<String> triggerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                TRIGGERLIST);

        ArrayAdapter<String> actionAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                ACTIONLIST);

        List<String> newList = new ArrayList<String>(pageKeyList);
        newList.addAll(soundKeyList);

        ArrayAdapter<String> pageAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                newList);

        // Specify the layout to use when the list of choices appears
        triggerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        actionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        triggerSpinner.setAdapter(triggerAdapter);
        actionsSpinner.setAdapter(actionAdapter);
        pageSoundSpinner.setAdapter(pageAdapter);

        triggerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String curItem = (String) adapterView.getSelectedItem();
                setCurrentTrigger(curItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        actionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String curItem = (String) adapterView.getSelectedItem();
                setCurrentAction(curItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        pageSoundSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String curItem = (String) adapterView.getSelectedItem();
                setCurrentPageSound(curItem);
                updateSpinner2(pageKeyList, soundKeyList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        updateSpinner2(pageKeyList, soundKeyList);
    }

    /**
     * update the shape spinner according to the items selected in shapeSoundSpinner
     * @param pageKeyList
     * @param soundKeyList
     */
    public void updateSpinner2(List pageKeyList, List soundKeyList) {
        System.out.println("currpageKeyList: " + pageKeyList);
        System.out.println("currentPageSound: " + currentPageSound);
        if (pageKeyList.contains(currentPageSound)) {
            Spinner shapeSpinner = (Spinner) findViewById(R.id.shapeSpinner);
            List shapeList = new ArrayList<String>(EditorView.getPageMap().get(currentPageSound).getShapeMap().keySet());
            if (!shapeList.isEmpty()) {
                setCurrentShape((String) shapeList.get(0));
                shapeList.add("DESELECT SHAPE");
                for (int i = 0; i < shapeList.size(); i++) {
                    System.out.println("curShape: " + shapeList.get(i).toString());
                }
                System.out.println("doneeeeeeeeeeeee");
                ArrayAdapter<String> shapeAdapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item,
                        shapeList);
                shapeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                shapeSpinner.setAdapter(shapeAdapter);

                shapeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String curItem = (String) adapterView.getSelectedItem();
                        setCurrentShape(curItem);
                        TextView imageName = findViewById(R.id.shapeImageTextView);
                        if (currentShape != null && !currentShape.equals("DESELECT SHAPE")) {
                            Map pageMap = EditorView.getPageMap();
                            imageName.setText(EditorView.getPageMap().get(currentPageSound).getShapeMap().get(currentShape).getImageName());
                        } else {
                            imageName.setText("No shape is currently selected");
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            } else {
                setCurrentShape(null);
                shapeSpinner.setAdapter(null);
                TextView imageName = findViewById(R.id.shapeImageTextView);
                imageName.setText("No shape is currently selected");
            }
        } else {
            setCurrentShape(null);
            System.out.println("im   gere");
            Spinner shapeSpinner = (Spinner) findViewById(R.id.shapeSpinner);
            shapeSpinner.setAdapter(null);
            TextView imageName = findViewById(R.id.shapeImageTextView);
            imageName.setText("No shape is currently selected");
        }
    }

    public void ADD(View view) {
        concatenate();
        TextView combinedTextView = findViewById(R.id.combinedTextView);
        combinedTextView.setText(onClickString + "\n" + onEnterString + "\n" +onDropString);
    }

    public void concatenate() {
        // fi shape exists, ignore currentPageSound
        if (currentTrigger.equals(ON_CLICK)) {
            if (onClickString.equals("")) {
                if (currentShape != null && !currentShape.equals("DESELECT SHAPE")) {
                    onClickString = currentTrigger + " " + currentAction + " " + currentShape;
                } else {
                    onClickString += " " + currentAction + " " + currentPageSound;
                }
            } else {
                if (currentShape != null && !currentShape.equals("DESELECT SHAPE")) {
                    onClickString += " " + currentAction + " " + currentShape;
                } else {
                    onClickString += " " + currentAction + " " + currentPageSound;
                }
            }
        } else if (currentTrigger.equals(ON_ENTER)) {
            if (onEnterString.equals("")) {
                if (currentShape != null && !currentShape.equals("DESELECT SHAPE")) {
                    onEnterString = currentTrigger + " " + currentAction + " " + currentShape;
                } else {
                    onEnterString += currentTrigger + " " + currentAction + " " + currentPageSound;
                }
            } else {
                if (currentShape != null && !currentShape.equals("DESELECT SHAPE")) {
                    onEnterString += " " + currentAction + " " + currentShape;
                } else {
                    onEnterString += " " + currentAction + " " + currentPageSound;
                }
            }
        }
    }

}
