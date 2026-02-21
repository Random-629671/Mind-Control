package vjp.pro.stressverifier.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.appbar.MaterialToolbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import vjp.pro.stressverifier.R;
import vjp.pro.stressverifier.adapter.HistoryAdapter;
import vjp.pro.stressverifier.data.DatabaseHelper;
import vjp.pro.stressverifier.model.HistoryItem;
import java.util.Collections;

public class HistoryActivity extends AppCompatActivity {
    private LineChart chart;
    private RecyclerView rvHistory;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Lịch sử đo");

        toolbar.setNavigationOnClickListener(v -> finish());

        chart = findViewById(R.id.historyChart);
        rvHistory = findViewById(R.id.rvHistoryDetails);
        db = new DatabaseHelper(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvHistory.setLayoutManager(layoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(rvHistory.getContext(), layoutManager.getOrientation());
        rvHistory.addItemDecoration(divider);

        loadData();
    }

    private void loadData() {
        List<HistoryItem> historyList = db.getLastHistory(10);

        if (historyList.isEmpty()) {
            chart.clear();
            rvHistory.setAdapter(new HistoryAdapter(new ArrayList<>()));
            return;
        }

        List<Entry> entriesBefore = new ArrayList<>();
        List<Entry> entriesAfter = new ArrayList<>();
        final List<Long> timestamps = new ArrayList<>();
        List<Integer> afterColors = new ArrayList<>();

        for (int i = 0; i < historyList.size(); i++) {
            HistoryItem item = historyList.get(i);

            entriesBefore.add(new Entry(i, item.scoreBefore));
            entriesAfter.add(new Entry(i, item.scoreAfter));
            timestamps.add(item.timestamp);

            if (item.scoreAfter < item.scoreBefore) afterColors.add(Color.GREEN);
            else if (item.scoreAfter > item.scoreBefore) afterColors.add(Color.RED);
            else afterColors.add(Color.GRAY);
        }

        LineDataSet setBefore = new LineDataSet(entriesBefore, "Trước");
        setBefore.setColor(Color.BLUE);
        setBefore.enableDashedLine(10f, 5f, 0f);

        LineDataSet setAfter = new LineDataSet(entriesAfter, "Sau");
        setAfter.setColor(Color.DKGRAY);
        setAfter.setCircleColors(afterColors);

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(setBefore);
        dataSets.add(setAfter);

        LineData lineData = new LineData(dataSets);
        chart.setData(lineData);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(100f);
        chart.getAxisRight().setEnabled(false);

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

        List<HistoryItem> reversedList = new ArrayList<>(historyList);
        Collections.reverse(reversedList);

        HistoryAdapter historyAdapter = new HistoryAdapter(reversedList);
        historyAdapter.setOnItemLongClickListener(item -> {
            new AlertDialog.Builder(this)
                    .setTitle("Xóa bản ghi")
                    .setMessage("Bạn có chắc muốn xóa bản ghi này?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        db.deleteRecord(item.id);
                        Toast.makeText(this, "Đã xóa!", Toast.LENGTH_SHORT).show();
                        loadData();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
        rvHistory.setAdapter(historyAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_clear_all) {
            new AlertDialog.Builder(this)
                    .setTitle("Xóa dữ liệu")
                    .setMessage("Bạn có chắc muốn xóa toàn bộ lịch sử đo?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        db.clearAllHistory();
                        Toast.makeText(this, "Đã xóa toàn bộ dữ liệu!", Toast.LENGTH_SHORT).show();
                        loadData();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}