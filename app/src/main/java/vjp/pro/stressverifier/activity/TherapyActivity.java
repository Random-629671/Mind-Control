package vjp.pro.stressverifier.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import vjp.pro.stressverifier.R;
import vjp.pro.stressverifier.adapter.SolutionAdapter;
import vjp.pro.stressverifier.data.MockDataStore;
import vjp.pro.stressverifier.model.Solution;
import vjp.pro.stressverifier.enums.StressLevel;

public class TherapyActivity extends AppCompatActivity {
    private RecyclerView rvSolutions;
    private Button btnSkip;
    private long historyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapy);

        rvSolutions = findViewById(R.id.rvSolutions);
        btnSkip = findViewById(R.id.btnSkip);

        String levelName = getIntent().getStringExtra("LEVEL_NAME");
        historyId = getIntent().getLongExtra("HISTORY_ID", -1);

        List<Solution> solutions = MockDataStore.getSolutions(StressLevel.valueOf(levelName));

        SolutionAdapter adapter = new SolutionAdapter(this, solutions, historyId);
        rvSolutions.setLayoutManager(new LinearLayoutManager(this));
        rvSolutions.setAdapter(adapter);

        btnSkip.setOnClickListener(v -> {
            Intent intent = new Intent(TherapyActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}