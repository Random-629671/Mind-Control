package vjp.pro.stressverifier.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
import vjp.pro.stressverifier.enums.StressLevel;
import vjp.pro.stressverifier.logic.SurveyManager;
import vjp.pro.stressverifier.model.HistoryItem;

import android.graphics.drawable.GradientDrawable;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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

        SurveyManager.getInstance().initData(this);

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
        UpdateSummaryAndBackground();
    }

    private void UpdateSummaryAndBackground() {
        DatabaseHelper db = new DatabaseHelper(this);
        List<HistoryItem> history = db.getLastHistory(5);

        if (history.isEmpty()) {
            tvSummary.setText("Chưa có dữ liệu. Hãy làm bài test đầu tiên!");
            return;
        }

        int sum = 0;
        for (HistoryItem item : history) {
            sum += item.scoreAfter;
        }
        int percentage = sum / history.size();

        StressLevel level;
        if (percentage <= 10) level = StressLevel.NONE;
        else if (percentage <= 35) level = StressLevel.LOW;
        else if (percentage <= 65) level = StressLevel.MEDIUM;
        else if (percentage <= 85) level = StressLevel.HIGH;
        else level = StressLevel.EXTREME;

        tvSummary.setText("TB 5 lần gần nhất: " + percentage + "%\nTình trạng: " + level.label);
        tvSummary.setTextColor(level.color);

        btnStartSurvey.setCardBackgroundColor(level.color);
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cấu hình Ứng dụng");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20);

        SharedPreferences prefs = getSharedPreferences("AppConfig", MODE_PRIVATE);
        String currentKey = prefs.getString("API_KEY", "");

        Switch switchAI = new Switch(this);
        switchAI.setText("Chế độ phân tích AI");
        switchAI.setChecked(isUseAI);
        switchAI.setTextSize(16);
        layout.addView(switchAI);

        EditText etApiKey = new EditText(this);
        etApiKey.setHint("Nhập API Key");
        etApiKey.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        etApiKey.setText(currentKey);
        layout.addView(etApiKey);

        builder.setView(layout);

        TextView tvProvider = new TextView(this);
        tvProvider.setText("Chọn AI Provider:");
        tvProvider.setPadding(0, 20, 0, 10);
        layout.addView(tvProvider);

        Spinner spinnerProvider = new Spinner(this);
        String[] providers = new String[]{"Google Gemini", "OpenAI (Coming Soon)"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, providers);
        spinnerProvider.setAdapter(adapter);
        layout.addView(spinnerProvider);

        int savedProvIdx = prefs.getInt("PROVIDER_IDX", 0);
        spinnerProvider.setSelection(savedProvIdx);

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            isUseAI = switchAI.isChecked();
            String apiKey = etApiKey.getText().toString();

            SharedPreferences.Editor editor = getSharedPreferences("AppConfig", MODE_PRIVATE).edit();
            editor.putBoolean("USE_AI", isUseAI);
            editor.putString("API_KEY", apiKey);
            editor.putInt("PROVIDER_IDX", spinnerProvider.getSelectedItemPosition());
            editor.apply();

            Toast.makeText(this, "Đã lưu cấu hình", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}