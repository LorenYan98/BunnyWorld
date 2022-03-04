package edu.stanford.cs108.bunnyworld;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        updateSpinner();

        // by default, set Moveable, and Clickable checkbox to disable, only enable them after visible is checked
        ((CheckBox) findViewById(R.id.clickable)).setEnabled(false);
        ((CheckBox) findViewById(R.id.moveable)).setEnabled(false);
    }

    public void addPage(View view) {
        EditorView editorView = (EditorView) findViewById(R.id.editor_view);
        editorView.addPage();
        updateSpinner();
        editorView.update();
    }

    public void deletePage(View view) {
        EditorView editorView = (EditorView) findViewById(R.id.editor_view);
        editorView.deletePage();
        updateSpinner();
        editorView.update();
    }

    public void updateSpinner() {
        Map pageMap = EditorView.getPageMap();
//        List<String> pageList = new ArrayList<String>(pageMap.keySet());
        List<BPage> pageList = new ArrayList<>(pageMap.values());

//        List<Sampler.Value> list = new ArrayList<Sampler.Value>(pageMap.values());
        Spinner spinner = (Spinner) findViewById(R.id.pageSpinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<BPage> adapter = new ArrayAdapter<BPage>(this,
                android.R.layout.simple_spinner_item,
                pageList);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                BPage curPage = (BPage) adapterView.getSelectedItem();
                EditorView editorView = (EditorView) findViewById(R.id.editor_view);
                editorView.setCurrentPage(curPage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /**
     * onclick method for Visible checkbox,
     * if visible is checked, enable moveable and clickable checkbox
     * if visible is unchecked, unchck moveable and clickable checkbox, and disable them
     * @param view
     */
    public void onVisibleCheckBoxClicked(View view) {
        // if visible checked
        if (((CheckBox) view).isChecked()) {
            // enable moveable and clickable checkbox
            ((CheckBox) findViewById(R.id.clickable)).setEnabled(true);
            ((CheckBox) findViewById(R.id.moveable)).setEnabled(true);
        } else {
            // visible is unchecked, uncheck clickable and moveable and disable them
            ((CheckBox) findViewById(R.id.clickable)).setChecked(false);
            ((CheckBox) findViewById(R.id.clickable)).setEnabled(false);
            ((CheckBox) findViewById(R.id.moveable)).setChecked(false);
            ((CheckBox) findViewById(R.id.moveable)).setEnabled(false);
        }
    }

}