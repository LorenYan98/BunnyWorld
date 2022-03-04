package edu.stanford.cs108.bunnyworld;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class EditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
    }

    public void addPage(View view) {
        EditorView editorView = (EditorView) findViewById(R.id.editor_view);
        editorView.addPage();
    }

    public void deletePage(View view) {
        EditorView editorView = (EditorView) findViewById(R.id.editor_view);
        editorView.deletePage();
    }
}