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
    private static final int DB_VERSION = 2;
    private static final String TABLE_HISTORY = "history";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_HISTORY + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "score_before INTEGER, " +
                "score_after INTEGER, " +
                "timestamp LONG)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        onCreate(db);
    }

    public long addResult(int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("score_before", score);
        values.put("score_after", score);
        values.put("timestamp", System.currentTimeMillis());

        long id = db.insert(TABLE_HISTORY, null, values);
        db.close();
        return id;
    }

    public void updateScoreAfter(long id, int newScore) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("score_after", newScore);

        db.update(TABLE_HISTORY, values, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public List<HistoryItem> getLastHistory(int limit) {
        List<HistoryItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_HISTORY,
                new String[]{"score_before", "score_after", "timestamp"},
                null, null, null, null, "id DESC", String.valueOf(limit));

        if (cursor.moveToFirst()) {
            do {
                int scoreBefore = cursor.getInt(0);
                int scoreAfter = cursor.getInt(1);
                long time = cursor.getLong(2);
                list.add(0, new HistoryItem(scoreBefore, scoreAfter, time));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}