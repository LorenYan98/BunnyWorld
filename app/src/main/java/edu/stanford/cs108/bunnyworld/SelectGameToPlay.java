package edu.stanford.cs108.bunnyworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class SelectGameToPlay extends AppCompatActivity {
    List<String> gameNames;
    String selectedGameName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_game_to_play);
        gameNames = GameActivity.loadGameNames(SingletonDB.getInstance(this));
        ArrayAdapter<String> gameNamesAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                gameNames);
        Spinner gameNamesSpinner = (Spinner) findViewById(R.id.selectPlayGameSpinner);
        gameNamesSpinner.setAdapter(gameNamesAdapter);
        gameNamesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String currItem = (String) adapterView.getSelectedItem();
                selectedGameName = currItem;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (gameNames.size() == 0) {
            gameNamesSpinner.setEnabled(false);
        }


    }

    public void startBunnyWorld(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void startSelectedGame(View view) {
        Spinner gameNamesSpinner = (Spinner) findViewById(R.id.selectPlayGameSpinner);
        if (gameNamesSpinner.getSelectedItem() == null) {
            Toast.makeText(getApplicationContext(), "No game selected. Unable to start Game Player.", Toast.LENGTH_SHORT).show();
            return;
        }


        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("gameName", selectedGameName);
        startActivity(intent);

    }
}