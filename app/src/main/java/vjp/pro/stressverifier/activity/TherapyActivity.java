package vjp.pro.stressverifier.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import vjp.pro.stressverifier.R;
import vjp.pro.stressverifier.model.Solution;
import vjp.pro.stressverifier.adapter.SolutionAdapter;
import vjp.pro.stressverifier.data.MockDataStore;
import vjp.pro.stressverifier.enums.StressLevel;

public class TherapyActivity extends AppCompatActivity {

    private RecyclerView rvSolutions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapy);

        rvSolutions = findViewById(R.id.rvSolutions);

        String levelName = getIntent().getStringExtra("LEVEL_NAME");
        StressLevel level = StressLevel.valueOf(levelName);

        List<Solution> solutions = MockDataStore.getSolutions(level);

        SolutionAdapter adapter = new SolutionAdapter(this, solutions);
        rvSolutions.setLayoutManager(new LinearLayoutManager(this));
        rvSolutions.setAdapter(adapter);
    }
}