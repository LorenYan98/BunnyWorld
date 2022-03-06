package edu.stanford.cs108.bunnyworld;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

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

    }
}