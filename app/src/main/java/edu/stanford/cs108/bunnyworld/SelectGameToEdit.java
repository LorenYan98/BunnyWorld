package edu.stanford.cs108.bunnyworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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

        System.out.println("Running onCreate for SelectGameToEdit");

        db = SingletonDB.getInstance(this);
        gameNames = new ArrayList<>();
        String queryStr = "SELECT game_name FROM games";
        Cursor cursor = db.rawQuery(queryStr, null);
        while (cursor.moveToNext()) {
            gameNames.add(cursor.getString(0));
        }
        Log.d("gameNames List", gameNames.toString());
    }

    public void goToNewEditor(View view) {
        Intent intent = new Intent(this, EditorActivity.class);
        startActivity(intent);
    }
}