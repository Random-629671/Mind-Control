package vjp.pro.stressverifier.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import vjp.pro.stressverifier.R;
import vjp.pro.stressverifier.data.DatabaseHelper;

public class FeedbackActivity extends AppCompatActivity {
    private long historyId;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        historyId = getIntent().getLongExtra("HISTORY_ID", -1);
        dbHelper = new DatabaseHelper(this);

        RadioGroup rgFeedback = findViewById(R.id.rgFeedback);
        Button btnFinish = findViewById(R.id.btnFinish);

        btnFinish.setOnClickListener(v -> {
            int selectedId = rgFeedback.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Vui lòng chọn đánh giá", Toast.LENGTH_SHORT).show();
                return;
            }

            if (historyId != -1) {
                int currentScore = getCurrentScore(historyId);
                double multiplier = 1.0;

                if (selectedId == R.id.rbMuchBetter) multiplier = 0.7;
                else if (selectedId == R.id.rbBetter) multiplier = 0.9;
                else if (selectedId == R.id.rbSame) multiplier = 1.0;
                else if (selectedId == R.id.rbWorse) multiplier = 1.1;

                int newScore = (int) (currentScore * multiplier);

                dbHelper.updateScoreAfter(historyId, newScore);

                String msg = multiplier < 1.0 ? "Chỉ số Stress đã giảm!" : "Đã ghi nhận trạng thái.";
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent(FeedbackActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private int getCurrentScore(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT score_before FROM history WHERE id = ?", new String[]{String.valueOf(id)});
        int score = 0;
        if (cursor.moveToFirst()) {
            score = cursor.getInt(0);
        }
        cursor.close();
        return score;
    }
}