package edu.stanford.cs108.bunnyworld;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScriptActivity extends AppCompatActivity {

    private static final String ON_CLICK = "onclick";
    private static final String ON_ENTER = "onenter";
    public static final ArrayList<String> ACTIONLIST = new ArrayList<String>(Arrays.asList(new String[]{ON_CLICK, ON_ENTER}));
    private static final String GOTO = "goto";
    private static final String PLAY = "play";
    private static final String HIDE = "hide";
    private static final String SHOW = "show";
    public static final ArrayList<String> TRIGGERLIST = new ArrayList<String>(Arrays.asList(new String[]{GOTO, PLAY, HIDE, SHOW}));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_script);
    }

    public void updateSpinner() {
        // initiate ArrayList including all the actions
        Map pageMap = EditorView.getPageMap();
        Map soundMap = EditorView.getSoundMap();

         List<String> pageList = new ArrayList<String>(pageMap.keySet());
//        List<BPage> pageList = new ArrayList<>(pageMap.values());
//        List<Bitmap> imgList = new ArrayList<>(imgMap.values());
//        List<String> imgNameList = new ArrayList<>(imgMap.keySet());

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
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    public void ADD(View view) {
        Spinner triggerSpinner = (Spinner) findViewById(R.id.triggerSpinner);
        Spinner actionsSpinner = (Spinner) findViewById(R.id.actionsSpinner);
        Spinner pageSoundSpinner = (Spinner) findViewById(R.id.pageSoundSpinner);
        Spinner shapeSpinner = (Spinner) findViewById(R.id.shapeSpinner);

    }
}