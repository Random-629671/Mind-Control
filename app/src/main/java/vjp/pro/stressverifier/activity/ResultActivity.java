package vjp.pro.stressverifier.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import vjp.pro.stressverifier.R;
import vjp.pro.stressverifier.data.DatabaseHelper;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.components.XAxis;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    private TextView tvStatus, tvAnalysis;
    private Button btnGoTherapy;
    private String levelName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tvStatus = findViewById(R.id.tvStatus);
        tvAnalysis = findViewById(R.id.tvAnalysis);
        btnGoTherapy = findViewById(R.id.btnGoTherapy);

        Intent intent = getIntent();
        int score = intent.getIntExtra("SCORE", 0);

        DatabaseHelper db = new DatabaseHelper(this);
        db.addResult(score);

        setupChart(db.getLastScores());

        levelName = intent.getStringExtra("LEVEL_NAME");
        String analysis = intent.getStringExtra("ANALYSIS");

        tvStatus.setText("MỨC ĐỘ: " + levelName + " (Điểm: " + score + ")");
        tvAnalysis.setText(analysis);

        btnGoTherapy.setOnClickListener(v -> {
            Intent therapyIntent = new Intent(ResultActivity.this, TherapyActivity.class);
            therapyIntent.putExtra("LEVEL_NAME", levelName);
            startActivity(therapyIntent);
        });
    }

    private void setupChart(List<Integer> scores) {
        LineChart chart = findViewById(R.id.lineChart);

        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < scores.size(); i++) {
            entries.add(new Entry(i + 1, scores.get(i)));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Lịch sử điểm Stress");
        dataSet.setColor(getColor(R.color.black));
        dataSet.setValueTextSize(10f);
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(4f);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);

        chart.getDescription().setEnabled(false);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getAxisRight().setEnabled(false);

        chart.invalidate();
    }
}