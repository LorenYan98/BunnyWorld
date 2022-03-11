package edu.stanford.cs108.bunnyworld;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class EditorActivity extends AppCompatActivity {
    private EditorView editorView;
    private RadioGroup shapeRadioGroup;
    private String userGameName;
    private String gameToLoad;
    SingletonDB db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        editorView = findViewById(R.id.editor_view);
        Intent intent = getIntent();
        if (intent.getStringExtra("gameName") == null) {
            editorView.loadPages();
        } else {
            gameToLoad = intent.getStringExtra("gameName");
            try {
                loadGame();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        updateSpinner();
        // by default, set Moveable checkbox to disable, only enable it after visible is checked
        ((CheckBox) findViewById(R.id.moveable)).setEnabled(true);
        updateCurrentPageText();
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView scriptTextView = findViewById(R.id.scriptTextView);


        TextView curShapeName =  findViewById(R.id.currentShapeName);
        System.out.println("page name: " + EditorView.getPageMap().get(editorView.getCurrentPage().getPageName()));
        System.out.println("shape name: "+ curShapeName.getText());
        if (EditorView.getPageMap().get(editorView.getCurrentPage().getPageName()).getShapeMap().get(curShapeName.getText()) == null) {
            System.out.println("this is null");
        } else {
            scriptTextView.setText(EditorView.getPageMap().get(editorView.getCurrentPage().getPageName()).getShapeMap().get(curShapeName.getText()).getScriptString());
            scriptTextView.setMovementMethod(new ScrollingMovementMethod());
        }
    }


    private void loadGame() throws JSONException {
        String query = "SELECT pages FROM games WHERE game_name = '" + gameToLoad + "'";
        Cursor cursor = SingletonDB.getInstance(this).rawQuery(query,null);
        while (cursor.moveToNext()) {
            List<BPage> pages = parseToPageList(cursor.getString(0));
            editorView.loadPages(pages);
        }
    }


    /**
     * called by ADD/EDIT SCRIPT button to start script activity
     * @param view
     */
    public void handleEditor(View view) {
        Intent intent = new Intent(this, ScriptActivity.class);
        // intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        TextView curShapeName =  findViewById(R.id.currentShapeName);
        intent.putExtra("curShapeName",curShapeName.getText());

        intent.putExtra("currentPageTextView",editorView.getCurrentPage().getPageName());

        startActivity(intent);
    }

    public void addPage(View view) {
        editorView = (EditorView) findViewById(R.id.editor_view);
        editorView.addPage();
        editorView.update();
        updateSpinner();
        updateCurrentPageText();

    }

    public void deletePage(View view) {
        editorView = (EditorView) findViewById(R.id.editor_view);
        editorView.deletePage();
        editorView.update();
        updateSpinner();
        updateCurrentPageText();
    }

    /**
     * on click function for renamePage button
     * fatch the text in edittext and set the name for current page if its not
     * page1
     * @param view
     */
    public void renamePage(View view) {
        editorView = (EditorView) findViewById(R.id.editor_view);
         editorView.renamePage();
         editorView.update();
         updateSpinner();
         updateCurrentPageText();
        Toast.makeText(getApplicationContext(),"Page Renamed Successfully",Toast.LENGTH_SHORT).show();
    }

    public void undoToPreviousState(View view){
        BPage prePageState;
        editorView = (EditorView) findViewById(R.id.editor_view);
        if(!editorView.saveCurrentState.isEmpty()){
            prePageState = editorView.saveCurrentState.pop();
            System.out.println("real page pop:" + prePageState);
            editorView.addcopyPage(prePageState);
//            for(BShape bshape: prePageState.getShapeMap().values()){
//                if(bshape.isSelected()){
//                    editorView.selectedShape = bshape;
//                    System.out.println("Hi I am selected");
//                }
//            }
            editorView.update();
//            updateSpinner();
            updateCurrentPageText();
        }else{
            Toast.makeText(getApplicationContext(),"No previous state saved",Toast.LENGTH_SHORT).show();
        }
    }

    public void addOrEditShape(View view){

        editorView = (EditorView) findViewById(R.id.editor_view);
        shapeRadioGroup = (RadioGroup) findViewById(R.id.shapeRadioGroup);
        Spinner imgSpinner = (Spinner) findViewById(R.id.shapeImageSpinner);
        String curImgName = imgSpinner.getItemAtPosition(imgSpinner.getSelectedItemPosition()).toString();
        if(curImgName.equals("Null")) curImgName = "";
        CheckBox shapeMoveable = (CheckBox) findViewById(R.id.moveable);
        CheckBox shapeVisible = (CheckBox) findViewById(R.id.visible);
        EditText currentText = (EditText) findViewById(R.id.shapeTextInput);
        EditText textSizeEditText = (EditText) findViewById(R.id.textSizeEditText);

        String text = ((EditText) findViewById(R.id.shapeTextInput)).getText().toString();
        int radioId = shapeRadioGroup.getCheckedRadioButtonId();
        boolean moveable = shapeMoveable == null? false: shapeMoveable.isChecked();
        boolean visible = shapeVisible == null? false: shapeVisible.isChecked();
        BShape newShape;
        if(radioId == R.id.addShapeRadioButton){
            if(text.length() != 0){
                newShape = new BShape(text,curImgName,moveable,visible,200.0f,30.0f,350.0f,100.0f);
                if (textSizeEditText.getText() != null &&
                        !(textSizeEditText.getText().toString().isEmpty())) {
                    newShape.setTextSize(Integer.parseInt(textSizeEditText.getText().toString()));
                    textSizeEditText.setText("");
                }

            }else{
                newShape = new BShape(text,curImgName,moveable,visible,200.0f,30.0f,400.0f,220.0f);
            }
            currentText.setText("");
            newShape.setEditorSelected(true);
            editorView.addShapeToview(newShape);
            editorView.update();
        }else{
            if(editorView.selectedShape != null){
                editorView.selectedShape.setMovable(moveable);
                editorView.selectedShape.setVisible(visible);

                // get the text size if in edit mode, there's text size in textSizeEditText
                if (textSizeEditText.getText() != null &&
                        !(textSizeEditText.getText().toString().isEmpty())) {
                    editorView.selectedShape.setTextSize(Integer.parseInt(textSizeEditText.getText().toString()));
                    textSizeEditText.setText("");
                }

                if(currentText.getText().toString().length() != 0) editorView.selectedShape.setText(currentText.getText().toString());
            }else{
                Toast.makeText(getApplicationContext(),"You should selected a shape to edit",Toast.LENGTH_SHORT).show();
            }

        }

    }

    public void copyCurShape(View view){

        editorView = (EditorView) findViewById(R.id.editor_view);
        if(editorView.selectedShape != null){

            BShape copyShape = new BShape(editorView.selectedShape, true);
            copyShape.move(40,40);
            System.out.println(copyShape.toString());
            editorView.selectedShape.setSelected(false);
            copyShape.setSelected(true);
            editorView.addShapeToview(copyShape);
            editorView.updateSelectShape(copyShape);
            editorView.updateSelectShapeName(copyShape);
            editorView.updateSelectShapeLocation(copyShape);
            editorView.update();
        }else{
            Toast.makeText(getApplicationContext(),"You should select a shape first",Toast.LENGTH_SHORT).show();
        }
    }


    public void renameCurShape(View view){
        shapeRadioGroup = (RadioGroup) findViewById(R.id.shapeRadioGroup);
        int radioId = shapeRadioGroup.getCheckedRadioButtonId();
        if(radioId == R.id.editShapeRadioButton) {
            editorView.saveCurrentState.push(new BPage(editorView.currentPage));
            EditText currentNameBox = (EditText) findViewById(R.id.renameShapeName);
            String curName = currentNameBox.getText().toString();

            BShape tempShape = editorView.selectedShape;
            if (tempShape == null) {
                // no shape is selected
                Toast.makeText(getApplicationContext(), "Please select a shape before renaming. " +
                        "Unable to rename.", Toast.LENGTH_SHORT).show();
                return;
            }

            // check the new name already exists, do not allow duplicated names
            Map pageMap = EditorView.getPageMap();
            // iterate through all the pageMap's map shapes
            List shapeNames = new ArrayList<String>();
            for (Object bPage : pageMap.values()) {
                for (BShape bshape : ((BPage)bPage).getShapeMap().values()) {
                    shapeNames.add(bshape.getShapeName());
                }
            }
            // all the names of existing shapes are in shapeNames, if new name exists, do not allow rename
            if (shapeNames.contains(curName)) {
                Toast.makeText(getApplicationContext(), "Duplicate names are not allowed. " +
                        "Unable to rename.", Toast.LENGTH_SHORT).show();
                return;
            }



            editorView.currentPage.getShapeMap().remove(tempShape.getShapeName());
            tempShape.setShapeName(curName);
            editorView.currentPage.addShape(tempShape);
            editorView.updateSelectShapeName(tempShape);
            Toast.makeText(getApplicationContext(), "Shape Renamed Successfully", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(),"You should switch to 'Edit' Mode",Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteCurShape(View view){
        if(editorView.selectedShape != null){
            BShape tempShape = editorView.selectedShape;
            editorView.currentPage.getShapeMap().remove(tempShape.getShapeName());
            editorView.update();
        }else{
            Toast.makeText(getApplicationContext(),"You should select a shape first",Toast.LENGTH_SHORT).show();
        }
    }


    public void updateSpinner() {
        Map pageMap = EditorView.getPageMap();
        Map imgMap = EditorView.getbitmapMap();

        // List<String> pageList = new ArrayList<String>(pageMap.keySet());
        List<String> pageList = new ArrayList<>(pageMap.keySet());
        Collections.reverse(pageList);
        List<Bitmap> imgList = new ArrayList<>(imgMap.values());
        List<String> imgNameList = new ArrayList<>(imgMap.keySet());
        String temp = imgNameList.get(0);
        imgNameList.set(0,"Null");
        imgNameList.add(temp);
        // List<Sampler.Value> list = new ArrayList<Sampler.Value>(pageMap.values());
        final Spinner pageSpinner = (Spinner) findViewById(R.id.pageSpinner);
        final Spinner imgSpinner = (Spinner) findViewById(R.id.shapeImageSpinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<String> pageAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                pageList);
        final ArrayAdapter<String> imgAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                imgNameList);

        // Specify the layout to use when the list of choices appears
        pageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        imgAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        pageSpinner.setAdapter(pageAdapter);
        imgSpinner.setAdapter(imgAdapter);


        pageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String curPage = (String) adapterView.getSelectedItem();
                editorView = (EditorView) findViewById(R.id.editor_view);
                editorView.setCurrentPage(editorView.pageMap.get(curPage));
                updateCurrentPageText();
                editorView.update();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    public void updateCurrentPageText() {
        TextView currentPageTextView = findViewById(R.id.currentPageTextView);
        editorView = (EditorView) findViewById(R.id.editor_view);
        currentPageTextView.setText(editorView.getCurrentPage().getPageName());
    }

    /**
     * onclick method for Visible checkbox,
     * if visible is checked, enable moveable checkbox
     * if visible is unchecked, uncheck moveable checkbox, and disable it
     * @param view
     */
    public void onVisibleCheckBoxClicked(View view) {
        // if visible checked
        if (((CheckBox) view).isChecked()) {
            // enable moveable checkbox
            ((CheckBox) findViewById(R.id.moveable)).setEnabled(true);
        } else {
            // visible is unchecked, uncheck moveable and disable them
            ((CheckBox) findViewById(R.id.moveable)).setChecked(false);
            ((CheckBox) findViewById(R.id.moveable)).setEnabled(false);
        }
    }

    public void saveGame(View view) throws JSONException {

        EditText gameName = (EditText) findViewById(R.id.userGameName);
        userGameName = gameName.getText().toString();

        // get the games names from db as a list
        db = SingletonDB.getInstance(this);
        List<String> gameNames = new ArrayList<>();
        String queryStr = "SELECT game_name FROM games";
        Cursor cursor = db.rawQuery(queryStr, null);
        while (cursor.moveToNext()) {
            gameNames.add(cursor.getString(0));
        }

        // if there is no game name, do not allow user to save the game and shows a toast
        if (userGameName.isEmpty()) {
            // show a toast and do not allow saving
            Toast.makeText(getApplicationContext(),"Game Name cannot be empty. \nUnable to save.",Toast.LENGTH_SHORT).show();
            return;
        }
//        else if (gameNames.contains(userGameName)) {
//            // the same game name already exists in db
//            Toast.makeText(getApplicationContext(),"Duplicate game name. \nUnable to save.",Toast.LENGTH_SHORT).show();
//            return;
//        }
        
        System.out.println("Executing public void saveGame(View view)");
        db = SingletonDB.getInstance(this);
        String currGame = gameToJson();

        System.out.println("Print String currGame as JSONObject: ");
        JSONObject obj = new JSONObject(currGame);
        JSONArray arr = obj.getJSONArray("pages");
        for (int i = 0; i < arr.length(); i++) {
            System.out.println(arr.getJSONObject(i).toString());
        }
        String queStr = "SELECT * FROM games WHERE game_name= '" + userGameName + "'";
        Cursor cursorName = db.rawQuery(queStr,null);
        if(cursorName.getCount()==0){
            System.out.println("hit 399");
            String query = "INSERT INTO games VALUES " + "('" + userGameName + "', '" + currGame + "'" + ", NULL" + ")";
            System.out.println("userGameName: " + userGameName);
            db.execSQL(query);
            gameName.setText("");
            Toast.makeText(getApplicationContext(),"Game Saved Successfully",Toast.LENGTH_SHORT).show();
        }else{
            System.out.println("hit 406");
            String deleteQuery = "DELETE FROM games WHERE game_name= '" + userGameName + "'";
            db.execSQL(deleteQuery);
            String query = "INSERT INTO games VALUES " + "('" + userGameName + "', '" + currGame + "'" + ", NULL" + ")";
            db.execSQL(query);
        }

        Intent intent = new Intent(this, SelectGameToPlay.class);
        startActivity(intent);

    }

    public String gameToJson()throws JSONException {
        System.out.println("Executing public String gameToJson()");
        Map<String,BPage> pageMap = EditorView.getPageMap();
        JSONObject pages = new JSONObject();
        JSONArray pagesArray = new JSONArray();
        for(String pageName:pageMap.keySet()){
            JSONObject currPageObj = new JSONObject();
            BPage currPage = pageMap.get(pageName);
            currPageObj.put("size",currPage.getLeft()+" "+currPage.getTop()+" "+currPage.getRight()+" "+currPage.getBottom());
            currPageObj.put("shapes",shapesToJson(currPage.getShapeMap()));
            currPageObj.put("pageName", currPage.getPageName());
            pagesArray.put(currPageObj);
        }
        pages.put("pages",pagesArray);
        return pages.toString();
    }

    public String shapesToJson(Map<String,BShape> shapeMap) throws JSONException {
        JSONObject shapes = new JSONObject();
        JSONArray shapesArray = new JSONArray();
        for(String shapeName:shapeMap.keySet()){
            JSONObject currShapeObj = new JSONObject();
            BShape currShape = shapeMap.get(shapeName);
            currShapeObj.put("text",currShape.getText());
            currShapeObj.put("imageName",currShape.getImageName());
            currShapeObj.put("movable",currShape.getMovable());
            currShapeObj.put("visible",currShape.getVisible());
            currShapeObj.put("left",currShape.getLeft());
            currShapeObj.put("top",currShape.getTop());
            currShapeObj.put("right",currShape.getRight());
            currShapeObj.put("bottom",currShape.getBottom());
            currShapeObj.put("shapeName",currShape.getShapeName());
            currShapeObj.put("script",currShape.getScriptString());
            currShapeObj.put("isEditorSelected", currShape.isEditorSelected());
            shapesArray.put(currShapeObj);
        }
        shapes.put("shapes",shapesArray);
        return shapes.toString();
    }

    public List<BPage> parseToPageList(String content) throws JSONException {
        List<BPage> result = new ArrayList<>();
        JSONObject contentObj = new JSONObject(content);
        JSONArray array = contentObj.getJSONArray("pages");
        for(int i=0;i<array.length();i++){
            result.add(parseToBPage(array.getJSONObject(i)));
        }
        return result;
    }

    private BPage parseToBPage(JSONObject pageObj) throws JSONException {
        String pageName = pageObj.getString("pageName");
        String shapes = pageObj.getString("shapes");
        String[] size = pageObj.getString("size").split(" ");
        // use 0.7f to resize
        BPage page = new BPage(Float.parseFloat(size[0]), Float.parseFloat(size[1]), Float.parseFloat(size[2]), Float.parseFloat(size[3]));
        page.setPageName(pageName);
        JSONObject shapesObj = new JSONObject(shapes);
        JSONArray array = shapesObj.getJSONArray("shapes");
        for (int i = 0; i < array.length(); i++) {
            page.addShape(parseToBShape(array.getJSONObject(i)));
        }
        return page;
    }

    private BShape parseToBShape(JSONObject shapeObj) throws JSONException {
        String shapeName = shapeObj.getString("shapeName");
        String text = shapeObj.getString("text");
        String imageName = shapeObj.getString("imageName");
        String movable = shapeObj.getString("movable");
        String visible = shapeObj.getString("visible");
        String left = shapeObj.getString("left");
        String top = shapeObj.getString("top");
        String right = shapeObj.getString("right");
        String bottom = shapeObj.getString("bottom");
        Script script = new Script(shapeObj.getString("script"));
        Boolean isEditorSelected = Boolean.parseBoolean(shapeObj.getString("isEditorSelected"));
        // use 0.7f to resize
        BShape shape = new BShape(text, imageName, Boolean.parseBoolean(movable), Boolean.parseBoolean(visible),
                Float.parseFloat(left), Float.parseFloat(top), Float.parseFloat(right),
                Float.parseFloat(bottom));
        shape.setScript(script);
        shape.setEditorSelected(isEditorSelected);

        shape.setScriptString(shapeObj.getString("script"));
        shape.setShapeName(shapeName);
        return shape;
    }
}