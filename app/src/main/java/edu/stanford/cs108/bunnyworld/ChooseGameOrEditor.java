package edu.stanford.cs108.bunnyworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ChooseGameOrEditor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_game_or_editor);
    }

    public void goToGamePlayer(View view) {
        Intent intent = new Intent(this, SelectGameToPlay.class);
        startActivity(intent);
    }

    public void goToGameEditor(View view) {
        Intent intent = new Intent(this, SelectGameToEdit.class);
        startActivity(intent);
    }

}