package vjp.pro.stressverifier.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.List;

import vjp.pro.stressverifier.R;
import vjp.pro.stressverifier.activity.HistoryActivity;
import vjp.pro.stressverifier.activity.SurveyActivity;
import vjp.pro.stressverifier.data.DatabaseHelper;
import vjp.pro.stressverifier.model.HistoryItem;

public class MainActivity extends AppCompatActivity {

    private CardView btnStartSurvey;
    private Button btnViewHistory;
    private ImageButton btnSettings;
    private TextView tvSummary;

    private boolean isUseAI = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStartSurvey = findViewById(R.id.btnStartSurvey);
        btnViewHistory = findViewById(R.id.btnViewHistory);
        btnSettings = findViewById(R.id.btnSettings);
        tvSummary = findViewById(R.id.tvSummary);

        SharedPreferences prefs = getSharedPreferences("AppConfig", MODE_PRIVATE);
        isUseAI = prefs.getBoolean("USE_AI", false);

        btnStartSurvey.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SurveyActivity.class);
            startActivity(intent);
        });

        btnViewHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        btnSettings.setOnClickListener(v -> showSettingsDialog());
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateSummary();
    }

    private void updateSummary() {
        DatabaseHelper db = new DatabaseHelper(this);
        List<HistoryItem> history = db.getLastHistory(5);

        if (history.isEmpty()) {
            tvSummary.setText("Chưa có dữ liệu. Hãy làm bài test đầu tiên!");
            return;
        }

        int sum = 0;
        for (HistoryItem item : history) {
            sum += item.score;
        }
        int avg = sum / history.size();

        String status = "";
        if (avg <= 13) status = "Tốt (Thấp)";
        else if (avg <= 26) status = "Cần chú ý (Trung bình)";
        else status = "Báo động (Cao)";

        tvSummary.setText("Trung bình 5 lần gần nhất: " + avg + "/40\nTình trạng: " + status);
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cấu hình Ứng dụng");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20);

        Switch switchAI = new Switch(this);
        switchAI.setText("Chế độ phân tích AI");
        switchAI.setChecked(isUseAI);
        switchAI.setTextSize(16);
        layout.addView(switchAI);

        EditText etApiKey = new EditText(this);
        etApiKey.setHint("Nhập API Key (OpenAI/Gemini)");
        etApiKey.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(etApiKey);

        builder.setView(layout);

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            isUseAI = switchAI.isChecked();
            String apiKey = etApiKey.getText().toString();

            SharedPreferences.Editor editor = getSharedPreferences("AppConfig", MODE_PRIVATE).edit();
            editor.putBoolean("USE_AI", isUseAI);
            editor.apply();

            Toast.makeText(this, "Đã lưu cấu hình", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}