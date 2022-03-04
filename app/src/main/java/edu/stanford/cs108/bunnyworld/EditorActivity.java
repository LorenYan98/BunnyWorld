package edu.stanford.cs108.bunnyworld;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
    }

    public void addPage(View view) {
        EditorView editorView = (EditorView) findViewById(R.id.editor_view);
        editorView.addPage();
        updateSpinner();
    }

    public void deletePage(View view) {
        EditorView editorView = (EditorView) findViewById(R.id.editor_view);
        editorView.deletePage();
        updateSpinner();
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

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}