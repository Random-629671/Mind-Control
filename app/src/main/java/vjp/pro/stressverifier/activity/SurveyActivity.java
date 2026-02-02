package vjp.pro.stressverifier.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import vjp.pro.stressverifier.model.AssessmentResult;
import vjp.pro.stressverifier.model.Question;
import vjp.pro.stressverifier.R;
import vjp.pro.stressverifier.model.StressEvaluator;
import vjp.pro.stressverifier.adapter.QuestionAdapter;
import vjp.pro.stressverifier.data.MockDataStore;

public class SurveyActivity extends AppCompatActivity {

    private RecyclerView rvQuestions;
    private Button btnSubmitSurvey;
    private QuestionAdapter adapter;
    private List<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        rvQuestions = findViewById(R.id.rvQuestions);
        btnSubmitSurvey = findViewById(R.id.btnSubmitSurvey);

        questionList = MockDataStore.getQuestions();

        SharedPreferences prefs = getSharedPreferences("AppConfig", MODE_PRIVATE);
        boolean useAI = prefs.getBoolean("USE_AI", false);

        List<Question> fullList = MockDataStore.getQuestions();

        List<Question> filteredList = new ArrayList<>();
        for (Question q : fullList) {
            if (useAI) {
                filteredList.add(q);
            } else {
                if (!q.isTextQuestion) {
                    filteredList.add(q);
                }
            }
        }

        this.questionList = filteredList;

        adapter = new QuestionAdapter(filteredList);
        rvQuestions.setLayoutManager(new LinearLayoutManager(this));
        rvQuestions.setAdapter(adapter);

        btnSubmitSurvey.setOnClickListener(v -> processSubmission());
    }

    private void processSubmission() {
        Map<Integer, Integer> scores = adapter.getScores();
        String textAnswer = adapter.getTextAnswer();

        long requiredQuestions = questionList.stream().filter(q -> !q.isTextQuestion).count();
        if (scores.size() < requiredQuestions) {
            Toast.makeText(this, "Vui lòng trả lời đủ các câu hỏi trắc nghiệm!", Toast.LENGTH_SHORT).show();
            return;
        }

        StressEvaluator evaluator = new StressEvaluator();
        evaluator.evaluate(scores, textAnswer, new StressEvaluator.EvaluationCallback() {
            @Override
            public void onSuccess(AssessmentResult result) {
                Intent intent = new Intent(SurveyActivity.this, ResultActivity.class);
                intent.putExtra("SCORE", result.score);
                intent.putExtra("LEVEL_NAME", result.level.name());
                intent.putExtra("ANALYSIS", result.analysis);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(SurveyActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}