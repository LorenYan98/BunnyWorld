package edu.stanford.cs108.bunnyworld;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SingletonDB {
    private static SingletonDB instance;
    private SQLiteDatabase db;

    public static SingletonDB getInstance(Context cxt) {
        if (instance == null) {
            instance = new SingletonDB(cxt.getApplicationContext());
        }
        return instance;
    }
    private SingletonDB(Context cxt) {
        db = cxt.openOrCreateDatabase("BunnyWorld", cxt.MODE_PRIVATE, null);
        Cursor tablesCursor = db.rawQuery(
                "SELECT * FROM sqlite_master WHERE type='table' AND name='games';",
                null);
        if (tablesCursor.getCount() == 0) {
            setupDB();
            populateDB();
        }
    }

    public void resetDB() {
        setupDB();
        populateDB();
    }

    public void execSQL(String str) {
        db.execSQL(str);
    }

    public Cursor rawQuery(String str, String[] args) {
        return db.rawQuery(str, args);
    }

    private void setupDB() {
        String clearStr = "DROP TABLE IF EXISTS games";
        db.execSQL(clearStr);

        String setupStr = "CREATE TABLE games ("
                + "game_name TEXT, pages TEXT,"
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT"
                + ");";

        db.execSQL(setupStr);
    }

    private void populateDB() {
    }
}
