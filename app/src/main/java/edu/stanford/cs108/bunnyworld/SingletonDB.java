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
            //populateDB();
        }
    }

    public void resetDB() {
        setupDB();
        //populateDB();
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
        String dataStr = "INSERT INTO cities VALUES "
                +"('Cairo','Africa',15200000, NULL),"
                +"('Lagos','Africa',21000000, NULL),"
                +"('Kyoto','Asia',1474570, NULL),"
                +"('Mumbai','Asia',20400000, NULL),"
                +"('Shanghai','Asia',24152700, NULL),"
                +"('Melbourne','Australia',3900000, NULL),"
                +"('London','Europe',8580000, NULL),"
                +"('Rome','Europe',2715000, NULL),"
                +"('Rostov-on-Don','Europe',1052000, NULL),"
                +"('San Francisco','North America',5780000, NULL),"
                +"('San Jose','North America',7354555, NULL),"
                +"('New York','North America',21295000, NULL),"
                +"('Rio de Janeiro','South America',12280702, NULL),"
                +"('Santiago','South America',5507282, NULL);";
        db.execSQL(dataStr);
    }
}
