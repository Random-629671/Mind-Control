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
import vjp.pro.stressverifier.model.HistoryItem;

import android.graphics.Color;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

public class ResultActivity extends AppCompatActivity {
    private TextView tvStatus, tvAnalysis;
    private Button btnGoTherapy;
    private long historyId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tvStatus = findViewById(R.id.tvStatus);
        tvAnalysis = findViewById(R.id.tvAnalysis);
        btnGoTherapy = findViewById(R.id.btnGoTherapy);

        Intent intent = getIntent();
        int score = intent.getIntExtra("SCORE", 0);
        String levelName = intent.getStringExtra("LEVEL_NAME");
        String analysis = intent.getStringExtra("ANALYSIS");

        if (savedInstanceState != null) {
            historyId = savedInstanceState.getLong("HISTORY_ID", -1);
        } else {
            DatabaseHelper db = new DatabaseHelper(this);
            historyId = db.addResult(score);
        }

        tvStatus.setText("MỨC ĐỘ: " + levelName + " (Điểm: " + score + ")");
        tvAnalysis.setText(analysis);

        btnGoTherapy.setOnClickListener(v -> {
            Intent therapyIntent = new Intent(ResultActivity.this, TherapyActivity.class);
            therapyIntent.putExtra("LEVEL_NAME", levelName);
            therapyIntent.putExtra("HISTORY_ID", historyId);
            startActivity(therapyIntent);
        });

        setupChart();
    }

    private void setupChart() {
        LineChart chart = findViewById(R.id.lineChart);
        DatabaseHelper db = new DatabaseHelper(this);

        List<HistoryItem> historyList = db.getLastHistory(5);
        if (historyList.isEmpty()) return;

        List<Entry> entriesBefore = new ArrayList<>();
        List<Entry> entriesAfter = new ArrayList<>();
        List<Integer> afterColors = new ArrayList<>();

        for (int i = 0; i < historyList.size(); i++) {
            HistoryItem item = historyList.get(i);
            entriesBefore.add(new Entry(i, item.scoreBefore));
            entriesAfter.add(new Entry(i, item.scoreAfter));

            if (item.scoreAfter < item.scoreBefore) afterColors.add(Color.GREEN);
            else if (item.scoreAfter > item.scoreBefore) afterColors.add(Color.RED);
            else afterColors.add(Color.GRAY);
        }

        LineDataSet setBefore = new LineDataSet(entriesBefore, "Trước");
        setBefore.setColor(Color.BLUE);
        setBefore.setCircleColor(Color.BLUE);
        setBefore.enableDashedLine(10f, 5f, 0f);

        LineDataSet setAfter = new LineDataSet(entriesAfter, "Sau");
        setAfter.setColor(Color.DKGRAY);
        setAfter.setCircleColors(afterColors);
        setAfter.setCircleRadius(5f);

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(setBefore);
        dataSets.add(setAfter);

        LineData lineData = new LineData(dataSets);
        chart.setData(lineData);

        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisRight().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.invalidate();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("HISTORY_ID", historyId);
    }
}