package vjp.pro.stressverifier.activity;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import vjp.pro.stressverifier.R;
import vjp.pro.stressverifier.data.DatabaseHelper;
import vjp.pro.stressverifier.model.HistoryItem;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        LineChart chart = findViewById(R.id.historyChart);
        DatabaseHelper db = new DatabaseHelper(this);

        List<HistoryItem> historyList = db.getLastHistory(10);
        if (historyList.isEmpty()) return;

        List<Entry> entriesBefore = new ArrayList<>();
        List<Entry> entriesAfter = new ArrayList<>();
        final List<Long> timestamps = new ArrayList<>();
        List<Integer> afterColors = new ArrayList<>();

        for (int i = 0; i < historyList.size(); i++) {
            HistoryItem item = historyList.get(i);

            entriesBefore.add(new Entry(i, item.scoreBefore));
            entriesAfter.add(new Entry(i, item.scoreAfter));
            timestamps.add(item.timestamp);

            if (item.scoreAfter < item.scoreBefore) {
                afterColors.add(Color.GREEN);
            } else if (item.scoreAfter > item.scoreBefore) {
                afterColors.add(Color.RED);
            } else {
                afterColors.add(Color.GRAY);
            }
        }

        LineDataSet setBefore = new LineDataSet(entriesBefore, "Điểm ban đầu");
        setBefore.setColor(Color.BLUE);
        setBefore.setCircleColor(Color.BLUE);
        setBefore.setLineWidth(2f);
        setBefore.setCircleRadius(4f);
        setBefore.enableDashedLine(10f, 5f, 0f);

        LineDataSet setAfter = new LineDataSet(entriesAfter, "Sau trị liệu");
        setAfter.setColor(Color.DKGRAY);
        setAfter.setLineWidth(2f);
        setAfter.setCircleRadius(6f);
        setAfter.setCircleColors(afterColors);
        setAfter.setValueTextSize(10f);

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(setBefore);
        dataSets.add(setAfter);

        LineData lineData = new LineData(dataSets);
        chart.setData(lineData);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new ValueFormatter() {
            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM", Locale.getDefault());
            @Override
            public String getAxisLabel(float value, com.github.mikephil.charting.components.AxisBase axis) {
                int index = (int) value;
                if (index >= 0 && index < timestamps.size()) {
                    return mFormat.format(new Date(timestamps.get(index)));
                }
                return "";
            }
        });

        chart.getDescription().setEnabled(false);
        chart.invalidate();
    }
}