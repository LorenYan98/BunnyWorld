package edu.stanford.cs108.bunnyworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {
    public static final String BUNNY_WORLD = "bunnyWorld";
    GameView gameView;
    List<String> gameNames;
    SingletonDB db;
    String gameToLoad;
    int viewWidth = 2560;
    int viewHeight = 1512;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameView = findViewById(R.id.game_view);
        db = SingletonDB.getInstance(this);
        Intent intent = getIntent();
        if (intent.getStringExtra("gameName") == null) {
            gameView.loadPages();
        } else {
            gameToLoad = intent.getStringExtra("gameName");
            try {
                loadGame();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadGameNames() {
        gameNames = new ArrayList<>();
        String queryStr = "SELECT game_name FROM games";
        Cursor cursor = db.rawQuery(queryStr, null);
        while (cursor.moveToNext()) {
            gameNames.add(cursor.getString(0));
        }
    }

    public static List<String> loadGameNames(SingletonDB dbRef) {
        List<String> result = new ArrayList<>();
        String queryStr = "SELECT game_name FROM games";
        Cursor cursor = dbRef.rawQuery(queryStr, null);
        while (cursor.moveToNext()) {
            result.add(cursor.getString(0));
        }
        return result;
    }

    private void loadGame() throws JSONException {
        String query = "SELECT pages FROM games WHERE game_name = '" + gameToLoad + "'";
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()) {
            List<BPage> pages = parseToPageList(cursor.getString(0));
            gameView.loadPages(pages);
        }
    }

    public List<BPage> parseToPageList(String content) throws JSONException {
        List<BPage> result = new ArrayList<>();
        JSONObject contentObj = new JSONObject(content);
        JSONArray array = contentObj.getJSONArray("pages");
        for(int i=0;i<array.length();i++){
            result.add(parseToBPage(array.getJSONObject(i)));
        }
        return result;
    }

    private BPage parseToBPage(JSONObject pageObj) throws JSONException {
        String pageName = pageObj.getString("pageName");
        String shapes = pageObj.getString("shapes");
        String[] size = pageObj.getString("size").split(" ");
        // use 0.7f to resize
        BPage page = new BPage(Float.parseFloat(size[0]) / 0.7f, Float.parseFloat(size[1]) / 0.7f, Float.parseFloat(size[2]) / 0.7f, Float.parseFloat(size[3]) / 0.7f);
        page.setPageName(pageName);
        JSONObject shapesObj = new JSONObject(shapes);
        JSONArray array = shapesObj.getJSONArray("shapes");
        for (int i = 0; i < array.length(); i++) {
            page.addShape(parseToBShape(array.getJSONObject(i)));
        }
        return page;
    }

    private BShape parseToBShape(JSONObject shapeObj) throws JSONException {
        String shapeName = shapeObj.getString("shapeName");
        String text = shapeObj.getString("text");
        String imageName = shapeObj.getString("imageName");
        String movable = shapeObj.getString("movable");
        String visible = shapeObj.getString("visible");
        String left = shapeObj.getString("left");
        String top = shapeObj.getString("top");
        String right = shapeObj.getString("right");
        String bottom = shapeObj.getString("bottom");
        Script script = new Script(shapeObj.getString("script"));
        // use 0.7f to resize
        BShape shape = new BShape(text, imageName, Boolean.parseBoolean(movable), Boolean.parseBoolean(visible),
                                    Float.parseFloat(left) / 0.7f, Float.parseFloat(top) / 0.7f, Float.parseFloat(right) / 0.7f,
                                    Float.parseFloat(bottom) / 0.7f);
        shape.setScript(script);
        shape.setShapeName(shapeName);
        return shape;
    }
}