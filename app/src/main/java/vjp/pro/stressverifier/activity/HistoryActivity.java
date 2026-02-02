package vjp.pro.stressverifier.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

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

        List<Entry> entries = new ArrayList<>();
        final List<Long> timestamps = new ArrayList<>();

        for (int i = 0; i < historyList.size(); i++) {
            entries.add(new Entry(i, historyList.get(i).score));
            timestamps.add(historyList.get(i).timestamp);
        }

        LineDataSet dataSet = new LineDataSet(entries, "Diễn biến Stress");
        dataSet.setLineWidth(3f);
        dataSet.setCircleRadius(5f);
        dataSet.setColor(getColor(R.color.purple_500));
        dataSet.setCircleColor(getColor(R.color.teal_200));

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);

        xAxis.setValueFormatter(new ValueFormatter() {
            private final SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm dd/MM", Locale.getDefault());

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