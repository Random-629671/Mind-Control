package vjp.pro.stressverifier.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import vjp.pro.stressverifier.model.HistoryItem;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "MindBalance.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_HISTORY = "history";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_HISTORY + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "score INTEGER, " +
                "timestamp LONG)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        onCreate(db);
    }

    public void addResult(int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("score", score);
        values.put("timestamp", System.currentTimeMillis());
        db.insert(TABLE_HISTORY, null, values);
        db.close();
    }

    public List<Integer> getLastScores() {
        List<Integer> scores = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_HISTORY, new String[]{"score"}, null, null, null, null, "id DESC", "10");

        if (cursor.moveToFirst()) {
            do {
                scores.add(0, cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return scores;
    }

    public List<HistoryItem> getLastHistory(int limit) {
        List<HistoryItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_HISTORY, new String[]{"score", "timestamp"}, null, null, null, null, "id DESC", String.valueOf(limit));

        if (cursor.moveToFirst()) {
            do {
                int score = cursor.getInt(0);
                long time = cursor.getLong(1);
                list.add(0, new HistoryItem(score, time));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}