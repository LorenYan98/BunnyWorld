package edu.stanford.cs108.bunnyworld;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

    private String curShapeName;
    private String currentPageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_script);
        init();
        init_on_drop();



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            curShapeName = getIntent().getStringExtra("curShapeName");
            currentPageTextView = getIntent().getStringExtra("currentPageTextView");
//            curShapeName = extras.getString("curShapeName");
//            currentPageTextView = extras.getString("currentPageTextView");
        } else {
            System.out.println("No script passed back");
        }
    }

    public void init() {
        onClickString = "";
        onEnterString = "";
        updateSpinner();
//        updateSpinner2();
        onDropRearPart = "";
        onClickRearPart = "";
        onEnterRearPart = "";
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

    private String onClickRearPart;
    private String onEnterRearPart;


    public void concatenate() {
        // fi shape exists, ignore currentPageSound
        if (currentTrigger.equals(ON_CLICK)) {
            if (onClickString.equals("")) {
                if (currentShape != null && !currentShape.equals("DESELECT SHAPE")) {
                    onClickRearPart = currentAction + " " + currentShape;
                    onClickString = currentTrigger + " " + currentAction + " " + currentShape;
                } else {
                    onClickRearPart = currentAction + " " + currentPageSound;
                    onClickString = currentTrigger + " " + currentAction + " " + currentPageSound;
                }
            } else {
                if (currentShape != null && !currentShape.equals("DESELECT SHAPE")) {
                    onClickRearPart += " " + currentAction + " " + currentShape;
                    onClickString += " " + currentAction + " " + currentShape;
                } else {
                    onClickRearPart += " " +currentAction + " " + currentPageSound;
                    onClickString += " " + currentAction + " " + currentPageSound;
                }
            }
        } else if (currentTrigger.equals(ON_ENTER)) {
            if (onEnterString.equals("")) {
                if (currentShape != null && !currentShape.equals("DESELECT SHAPE")) {
                    onEnterRearPart = currentAction + " " + currentShape;
                    onEnterString = currentTrigger + " " + currentAction + " " + currentShape;
                } else {
                    onEnterRearPart = currentAction + " " + currentPageSound;
                    onEnterString = currentTrigger + " " + currentAction + " " + currentPageSound;
                }
            } else {
                if (currentShape != null && !currentShape.equals("DESELECT SHAPE")) {
                    onEnterRearPart += " " + currentAction + " " + currentShape;
                    onEnterString += " " + currentAction + " " + currentShape;
                } else {
                    onEnterRearPart += " " + currentAction + " " + currentPageSound;
                    onEnterString += " " + currentAction + " " + currentPageSound;
                }
            }
        }
    }

    // VERY IMPORTANT!!!!! THE FOLLOWING CODE IS FOR ONDROP
    private static final String ON_DROP = "ondrop";
    private String onDropString;

//    private String currentTrigger_on_drop; no need
    private String currentAction_on_drop;
    private String currentPageSound_on_drop_1;
    private String currentPageSound_on_drop_2;
    private String currentShape_on_drop_1;
    private String currentShape_on_drop_2;

    public void setCurrentAction_on_drop(String currentAction_on_drop) {
        this.currentAction_on_drop = currentAction_on_drop;
    }

    public void setCurrentPageSound_on_drop_1(String currentPageSound_on_drop_1) {
        this.currentPageSound_on_drop_1 = currentPageSound_on_drop_1;
    }

    public void setCurrentPageSound_on_drop_2(String currentPageSound_on_drop_2) {
        this.currentPageSound_on_drop_2 = currentPageSound_on_drop_2;
    }

    public void setCurrentShape_on_drop_1(String currentShape_on_drop_1) {
        this.currentShape_on_drop_1 = currentShape_on_drop_1;
    }

    public void setCurrentShape_on_drop_2(String currentShape_on_drop_2) {
        this.currentShape_on_drop_2 = currentShape_on_drop_2;
    }

    private void init_on_drop() {
        onDropString = "";
        updateSpinner_on_drop();
    }

    private void updateSpinner_on_drop() {
        Spinner action_on_drop_spinner = (Spinner) findViewById(R.id.currentAction_on_drop);
        Spinner pageSound_on_drop_1_spinner = (Spinner) findViewById(R.id.currentPageSound_on_drop_1);
        Spinner pageSound_on_drop_2_spinner = (Spinner) findViewById(R.id.currentPageSound_on_drop_2);

        // initiate ArrayList including all the actions
        Map pageMap = EditorView.getPageMap();
        Map soundMap = EditorView.getSoundMap();

        List<String> pageKeyList = new ArrayList<String>(pageMap.keySet());
        List<String> soundKeyList = new ArrayList<String>(soundMap.keySet());
//        List<BPage> pageList = new ArrayList<>(pageMap.values());
//        List<Bitmap> imgList = new ArrayList<>(imgMap.values());
//        List<String> imgNameList = new ArrayList<>(imgMap.keySet());

        // List<Sampler.Value> list = new ArrayList<Sampler.Value>(pageMap.values());


        if (pageKeyList.contains(currentPageSound_on_drop_1)) {
            List shapeList = new ArrayList<String>(EditorView.getPageMap().get(currentPageSound_on_drop_1).getShapeMap().keySet());
            setCurrentShape_on_drop_1((String) shapeList.get(0));
        }

        if (pageKeyList.contains(currentPageSound_on_drop_2)) {
            List shapeList = new ArrayList<String>(EditorView.getPageMap().get(currentPageSound_on_drop_2).getShapeMap().keySet());
            setCurrentShape_on_drop_2((String) shapeList.get(0));
        }

        setCurrentAction_on_drop(ACTIONLIST.get(0));
        setCurrentShape_on_drop_1(pageKeyList.get(0));
        setCurrentShape_on_drop_2(pageKeyList.get(0));


        ArrayAdapter<String> actionAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                ACTIONLIST);

        List<String> newList = new ArrayList<String>(pageKeyList);
        newList.addAll(soundKeyList);

        ArrayAdapter<String> pageAdapter_on_drop_1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                newList);

        ArrayAdapter<String> pageAdapter_on_drop_2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                newList);

        // Specify the layout to use when the list of choices appears
        actionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pageAdapter_on_drop_1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pageAdapter_on_drop_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        action_on_drop_spinner.setAdapter(actionAdapter);
        pageSound_on_drop_1_spinner.setAdapter(pageAdapter_on_drop_1);
        pageSound_on_drop_2_spinner.setAdapter(pageAdapter_on_drop_2);


        action_on_drop_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String curItem = (String) adapterView.getSelectedItem();
                setCurrentAction_on_drop(curItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        pageSound_on_drop_1_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String curItem = (String) adapterView.getSelectedItem();
                setCurrentPageSound_on_drop_1(curItem);
                updateSpinner2_1(pageKeyList, soundKeyList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        pageSound_on_drop_2_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String curItem = (String) adapterView.getSelectedItem();
                setCurrentPageSound_on_drop_2(curItem);
                updateSpinner2_2(pageKeyList, soundKeyList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        updateSpinner2_1(pageKeyList, soundKeyList);
        updateSpinner2_2(pageKeyList, soundKeyList);
    }

    public void updateSpinner2_1(List pageKeyList, List soundKeyList) {
        System.out.println("currpageKeyList: " + pageKeyList);
        System.out.println("currentPageSound: " + currentPageSound);
        if (pageKeyList.contains(currentPageSound_on_drop_1)) {
            Spinner shape_on_drop_1_spinner = (Spinner) findViewById(R.id.currentShape_on_drop_1);
            List shapeList = new ArrayList<String>(EditorView.getPageMap().get(currentPageSound_on_drop_1).getShapeMap().keySet());
            if (!shapeList.isEmpty()) {
                setCurrentShape_on_drop_1((String) shapeList.get(0));
                shapeList.add("DESELECT SHAPE");
                for (int i = 0; i < shapeList.size(); i++) {
                    System.out.println("curShape: " + shapeList.get(i).toString());
                }
                System.out.println("doneeeeeeeeeeeee");
                ArrayAdapter<String> shapeAdapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item,
                        shapeList);
                shapeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                shape_on_drop_1_spinner.setAdapter(shapeAdapter);

                shape_on_drop_1_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String curItem = (String) adapterView.getSelectedItem();
                        setCurrentShape_on_drop_1(curItem);
                        TextView imageName = findViewById(R.id.shapeImageTextView);
                        if (currentShape_on_drop_1 != null && !currentShape_on_drop_1.equals("DESELECT SHAPE")) {
                            Map pageMap = EditorView.getPageMap();
                            imageName.setText(EditorView.getPageMap().get(currentPageSound_on_drop_1).getShapeMap().get(currentShape_on_drop_1).getImageName());
                        } else {
                            imageName.setText("No shape is currently selected");
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            } else {
                setCurrentShape_on_drop_1(null);
                shape_on_drop_1_spinner.setAdapter(null);
                TextView imageName = findViewById(R.id.shapeImageTextView);
                imageName.setText("No shape is currently selected");
            }
        } else {
            setCurrentShape_on_drop_1(null);
            System.out.println("im   gere");
            Spinner shapeSpinner = (Spinner) findViewById(R.id.currentShape_on_drop_1);
            shapeSpinner.setAdapter(null);
            TextView imageName = findViewById(R.id.shapeImageTextView);
            imageName.setText("No shape is currently selected");
        }
    }

    public void updateSpinner2_2(List pageKeyList, List soundKeyList) {
        System.out.println("currpageKeyList: " + pageKeyList);
        System.out.println("currentPageSound: " + currentPageSound);
        if (pageKeyList.contains(currentPageSound_on_drop_2)) {
            Spinner shape_on_drop_2_spinner = (Spinner) findViewById(R.id.currentShape_on_drop_2);
            List shapeList = new ArrayList<String>(EditorView.getPageMap().get(currentPageSound_on_drop_2).getShapeMap().keySet());
            if (!shapeList.isEmpty()) {
                setCurrentShape_on_drop_2((String) shapeList.get(0));
                shapeList.add("DESELECT SHAPE");
                for (int i = 0; i < shapeList.size(); i++) {
                    System.out.println("curShape: " + shapeList.get(i).toString());
                }
                System.out.println("doneeeeeeeeeeeee");
                ArrayAdapter<String> shapeAdapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item,
                        shapeList);
                shapeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                shape_on_drop_2_spinner.setAdapter(shapeAdapter);

                shape_on_drop_2_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String curItem = (String) adapterView.getSelectedItem();
                        setCurrentShape_on_drop_2(curItem);
                        TextView imageName = findViewById(R.id.shapeImageTextView);
                        if (currentShape_on_drop_2 != null && !currentShape_on_drop_2.equals("DESELECT SHAPE")) {
                            Map pageMap = EditorView.getPageMap();
                            imageName.setText(EditorView.getPageMap().get(currentPageSound_on_drop_2).getShapeMap().get(currentShape_on_drop_2).getImageName());
                        } else {
                            imageName.setText("No shape is currently selected");
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            } else {
                setCurrentShape_on_drop_2(null);
                shape_on_drop_2_spinner.setAdapter(null);
                TextView imageName = findViewById(R.id.shapeImageTextView);
                imageName.setText("No shape is currently selected");
            }
        } else {
            setCurrentShape_on_drop_2(null);
            System.out.println("im   gere");
            Spinner shapeSpinner = (Spinner) findViewById(R.id.currentShape_on_drop_2);
            shapeSpinner.setAdapter(null);
            TextView imageName = findViewById(R.id.shapeImageTextView);
            imageName.setText("No shape is currently selected");
        }
    }

    public void ADD_on_drop(View view) {
        if (true) {
            concatenate_on_drop();
            TextView combinedTextView = findViewById(R.id.combinedTextView);
            combinedTextView.setText(onClickString + "\n" + onEnterString + "\n" +onDropString);
        } else {
            return;
        }
    }

    private String onDropRearPart;

    public void concatenate_on_drop() {
        // fi shape exists, ignore currentPageSound
            if (onDropString.equals("")) {
                if (currentShape_on_drop_1 != null && !currentShape_on_drop_1.equals("DESELECT SHAPE")) {
                    if (currentShape_on_drop_2 != null && !currentShape_on_drop_2.equals("DESELECT SHAPE")) {
                        onDropRearPart = currentAction_on_drop + " " + currentShape_on_drop_2;
                        onDropString = ON_DROP + " " + currentShape_on_drop_1 + " " + currentAction_on_drop + " " + currentShape_on_drop_2;
                    } else {
                        onDropRearPart = currentAction_on_drop + " " + currentPageSound_on_drop_2;
                        onDropString = ON_DROP + " " + currentShape_on_drop_1 + " " + currentAction_on_drop + " " + currentPageSound_on_drop_2;
                    }
                } else {
                    if (currentShape_on_drop_2 != null && !currentShape_on_drop_2.equals("DESELECT SHAPE")) {
                        onDropRearPart = currentAction_on_drop + " " + currentShape_on_drop_2;
                        onDropString = ON_DROP + " " + currentPageSound_on_drop_1 + " " + currentAction_on_drop + " " + currentShape_on_drop_2;
                    } else {
                        onDropRearPart = currentAction_on_drop + " " + currentPageSound_on_drop_2;
                        onDropString = ON_DROP + " " + currentPageSound_on_drop_1 + " " + currentAction_on_drop + " " + currentPageSound_on_drop_2;
                    }
                }
            } else {
                if (currentShape_on_drop_1 != null && !currentShape_on_drop_1.equals("DESELECT SHAPE")) {
                    if (currentShape_on_drop_2 != null && !currentShape_on_drop_2.equals("DESELECT SHAPE")) {
                        onDropRearPart += " " + currentAction_on_drop + " " + currentShape_on_drop_2;
                        onDropString += " " + currentAction_on_drop + " " + currentShape_on_drop_2;
                    } else {
                        onDropRearPart += " " + currentAction_on_drop + " " + currentPageSound_on_drop_2;
                        onDropString += " " + currentAction_on_drop + " " + currentPageSound_on_drop_2;
                    }
                } else {
                    if (currentShape_on_drop_2 != null && !currentShape_on_drop_2.equals("DESELECT SHAPE")) {
                        onDropRearPart += " " + currentAction_on_drop + " " + currentShape_on_drop_2;
                        onDropString += " " + currentAction_on_drop + " " + currentShape_on_drop_2;
                    } else {
                        onDropRearPart += " " + currentAction_on_drop + " " + currentPageSound_on_drop_2;
                        onDropString += " " + currentAction_on_drop + " " + currentPageSound_on_drop_2;
                    }
                }
            }
    }

    public String returnFinalStringByOrder() {
//        String finalString = "";
//        if (onEnterString.equals("")) {
//            if (onDropString.equals("")) {
//                if (onClickString.equals("")) {
//                    return finalString;
//                } else {
//                    return finalString + onClickString;
//                }
//            } else {
//                finalString += onDropString;
//                if (onClickString.equals("")) {
//                    return finalString;
//                } else {
//                    return finalString + ";" + onClickString;
//                }
//            }
//        } else {
//            finalString += onEnterString;
//            if (onDropString.equals("")) {
//                if (onClickString.equals("")) {
//                    return finalString;
//                } else {
//                    return finalString + ";" + onClickString;
//                }
//            } else {
//                finalString += ";" + onDropString;
//                if (onClickString.equals("")) {
//                    return finalString;
//                } else {
//                    return finalString + ";" + onClickString;
//                }
//            }
//        }
        return onEnterString + ";" + onClickString + ";" + onDropString;
    }

    /**
     * return the final string, and return to editor
     */
    public void confirmScriptReturnToEditor(View view){
//        String finalString = returnFinalStringByOrder();
        Intent intent = new Intent(this, EditorActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//        TextView imageName = findViewById(R.id.scriptTextView);
//        imageName.setText("success");

        if (!onClickString.equals("")) {
            EditorView.getPageMap().get(currentPageTextView).getShapeMap().get(curShapeName).getScript().getOnClickActions().add(onClickRearPart);
            EditorView.getPageMap().get(currentPageTextView).getShapeMap().get(curShapeName).getScript().setOnClick(true);
        }
        if (!onEnterString.equals("")) {
            EditorView.getPageMap().get(currentPageTextView).getShapeMap().get(curShapeName).getScript().getOnEnterActions().add(onEnterRearPart);
            EditorView.getPageMap().get(currentPageTextView).getShapeMap().get(curShapeName).getScript().setOnEnter(true);
        }
        if (!onDropString.equals("")) {
            EditorView.getPageMap().get(currentPageTextView).getShapeMap().get(curShapeName).getScript().setOnDrop(true);
            if (EditorView.getPageMap().get(currentPageTextView).getShapeMap().get(curShapeName).getScript().getOnDropActions().containsKey(currentShape_on_drop_1)) {
                EditorView.getPageMap().get(currentPageTextView).getShapeMap().get(curShapeName).getScript().getOnDropActions().get(currentShape_on_drop_1).add(onDropRearPart);
            } else {
                EditorView.getPageMap().get(currentPageTextView).getShapeMap().get(curShapeName).getScript().getOnDropActions().put(currentShape_on_drop_1,new ArrayList<String>());
                EditorView.getPageMap().get(currentPageTextView).getShapeMap().get(curShapeName).getScript().getOnDropActions().get(currentShape_on_drop_1).add(onDropRearPart);
            }
        }

//        intent.putExtra("curScript",finalString);
        // This is the next step to figure out.
//        shape.setScript(finalString);
        startActivity(intent);
    }

}
