package edu.stanford.cs108.bunnyworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EditorActivity extends AppCompatActivity {
    private EditorView editorView;
    private RadioGroup shapeRadioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        updateSpinner();
        // by default, set Moveable checkbox to disable, only enable it after visible is checked
        ((CheckBox) findViewById(R.id.moveable)).setEnabled(false);

        updateCurrentPageText();
    }

    /**
     * called by ADD/EDIT SCRIPT button to start script activity
     * @param view
     */
    public void handleEditor(View view) {
        Intent intent = new Intent(this, ScriptActivity.class);
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
        System.out.println("before: " + editorView.getCurrentPage());
        updateSpinner();
        updateCurrentPageText();
        System.out.println("after: " + editorView.getCurrentPage());
    }

    public void addOrEditShape(View view){
        editorView = (EditorView) findViewById(R.id.editor_view);
        shapeRadioGroup = (RadioGroup) findViewById(R.id.shapeRadioGroup);
        Spinner imgSpinner = (Spinner) findViewById(R.id.shapeImageSpinner);
        String curImgName = imgSpinner.getItemAtPosition(imgSpinner.getSelectedItemPosition()).toString();
        CheckBox shapeMoveable = (CheckBox) findViewById(R.id.moveable);
        CheckBox shapeVisible = (CheckBox) findViewById(R.id.visible);
        String text = ((EditText) findViewById(R.id.shapeTextInput)).getText().toString();
        int radioId = shapeRadioGroup.getCheckedRadioButtonId();
        boolean moveable = shapeMoveable == null? false: shapeMoveable.isChecked();
        boolean visible = shapeVisible == null? false: shapeVisible.isChecked();
//        System.out.println(moveable + " and " + visible);
//        System.out.println(curImgName + " and " +text );
        if(radioId == R.id.addShapeRadioButton){
            BShape newShape = new BShape(text,curImgName,moveable,visible,200.0f,30.0f,700.0f,500.0f);
            editorView.addShapeToview(newShape);
            editorView.update();
        }

    }
    public void updateSpinner() {
        Map pageMap = EditorView.getPageMap();
        Map imgMap = EditorView.getbitmapMap();

        // List<String> pageList = new ArrayList<String>(pageMap.keySet());
        List<BPage> pageList = new ArrayList<>(pageMap.values());
        List<Bitmap> imgList = new ArrayList<>(imgMap.values());
        List<String> imgNameList = new ArrayList<>(imgMap.keySet());

        // List<Sampler.Value> list = new ArrayList<Sampler.Value>(pageMap.values());
        Spinner pageSpinner = (Spinner) findViewById(R.id.pageSpinner);
        Spinner imgSpinner = (Spinner) findViewById(R.id.shapeImageSpinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<BPage> pageAdapter = new ArrayAdapter<BPage>(this,
                android.R.layout.simple_spinner_item,
                pageList);
        ArrayAdapter<String> imgAdapter = new ArrayAdapter<String>(this,
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
                BPage curPage = (BPage) adapterView.getSelectedItem();
                editorView = (EditorView) findViewById(R.id.editor_view);
                editorView.setCurrentPage(curPage);
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

    public void saveGame(View view) {
    }
}