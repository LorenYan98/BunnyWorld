package edu.stanford.cs108.bunnyworld;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class ScriptActivity extends AppCompatActivity {

    public static final String ON_CLICK = "onclick";
    public static final String ON_DROP = "ondrop";
    public static final String ON_ENTER = "onenter";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_script);
    }

    public void updateSpinner() {
        // initiate ArrayList including all the actions
        ArrayList<String> actionsList =
                new ArrayList<String>(Arrays.asList(new String[]{ON_CLICK, ON_ENTER, ON_DROP}));


    }

}