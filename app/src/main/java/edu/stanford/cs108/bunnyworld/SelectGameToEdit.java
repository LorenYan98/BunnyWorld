package edu.stanford.cs108.bunnyworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class SelectGameToEdit extends AppCompatActivity {

    GameView gameView;
    List<String> gameNames;
    SingletonDB db;
    String gameToLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_game_to_edit);


        gameNames = GameActivity.loadGameNames(SingletonDB.getInstance(this));
        ArrayAdapter<String> gameNamesAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                gameNames);
        Spinner gameNamesSpinner = (Spinner) findViewById(R.id.selectGameSpinner);
        gameNamesSpinner.setAdapter(gameNamesAdapter);
        gameNamesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String currItem = (String) adapterView.getSelectedItem();
                gameToLoad = currItem;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (gameNames.size() == 0) {
            gameNamesSpinner.setEnabled(false);
        }
    }

    public void goToNewEditor(View view) {
        Intent intent = new Intent(this, EditorActivity.class);
        startActivity(intent);
    }

    public void resetDB(View view) {
        SingletonDB.getInstance(this).resetDB();
        Toast.makeText(getApplicationContext(), "DB reset", Toast.LENGTH_SHORT).show();
    }

    public void handleEditSelectedGame(View view) {
        Intent intent = new Intent(this, EditorActivity.class);
        intent.putExtra("gameName", gameToLoad);
        startActivity(intent);
    }
}