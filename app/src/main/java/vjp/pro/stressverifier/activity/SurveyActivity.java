package vjp.pro.stressverifier.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Map;

import vjp.pro.stressverifier.R;
import vjp.pro.stressverifier.adapter.QuestionAdapter;
import vjp.pro.stressverifier.logic.SurveyManager;
import vjp.pro.stressverifier.model.AssessmentResult;
import vjp.pro.stressverifier.model.Question;
import vjp.pro.stressverifier.model.StressEvaluator;
import vjp.pro.stressverifier.model.SurveyStage;

import android.widget.ImageButton;
import androidx.appcompat.app.AlertDialog;

public class SurveyActivity extends AppCompatActivity {
    private RecyclerView rvQuestions;
    private Button btnNext;
    private TextView tvLayerTitle;
    private QuestionAdapter adapter;
    private SurveyManager surveyManager;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        rvQuestions = findViewById(R.id.rvQuestions);
        btnNext = findViewById(R.id.btnSubmitSurvey);
        tvLayerTitle = findViewById(R.id.tvLayerTitle);
        progressBar = findViewById(R.id.loadingBar);

        surveyManager = SurveyManager.getInstance();
        surveyManager.initData(this);

        setupCurrentStage();

        btnNext.setOnClickListener(v -> handleNextStep());

        ImageButton btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Thoát bài kiểm tra?")
                    .setMessage("Tiến trình hiện tại sẽ không được lưu lại. Bạn có chắc chắn muốn thoát?")
                    .setPositiveButton("Thoát", (dialog, which) -> finish())
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    private void setupCurrentStage() {
        SurveyStage stage = surveyManager.getCurrentStage();
        if (stage == null) return;

        rvQuestions.animate().alpha(0f).setDuration(200).withEndAction(() -> {
            tvLayerTitle.setText(stage.title);
            adapter = new QuestionAdapter(stage.questions);
            rvQuestions.setLayoutManager(new LinearLayoutManager(this));
            rvQuestions.setAdapter(adapter);

            if (surveyManager.hasNextStage()) {
                btnNext.setText("Tiếp tục >>");
            } else {
                btnNext.setText("Hoàn thành & Phân tích");
            }

            rvQuestions.scrollToPosition(0);

            rvQuestions.animate().alpha(1f).setDuration(300).start();
        }).start();
    }

    private void handleNextStep() {
        Map<Integer, Integer> currentScores = adapter.getScores();
        SurveyStage currentStage = surveyManager.getCurrentStage();
        boolean hasError = false;

        for (int i = 0; i < currentStage.questions.size(); i++) {
            Question q = currentStage.questions.get(i);
            if (!q.isTextQuestion && !currentScores.containsKey(q.id)) {
                adapter.markError(i);
                hasError = true;
            }
        }

        if (hasError) {
            Toast.makeText(this, "Vui lòng hoàn thành các câu hỏi được tô đỏ!", Toast.LENGTH_SHORT).show();
            return;
        }

        surveyManager.saveAnswers(currentScores, adapter.getTextAnswer());

        if (surveyManager.hasNextStage()) {
            surveyManager.moveToNextStage();
            setupCurrentStage();
        } else {
            finishSurvey();
        }
    }

    private void finishSurvey() {
        progressBar.setVisibility(View.VISIBLE);
        btnNext.setEnabled(false);
        rvQuestions.setAlpha(0.5f);

        StressEvaluator evaluator = new StressEvaluator(this);

        evaluator.evaluate(surveyManager.getTotalScore(),
            surveyManager.getTotalMaxScore(),
            surveyManager.getCombinedTextAnswers(),
            new StressEvaluator.EvaluationCallback() {
                @Override
                public void onSuccess(AssessmentResult result) {
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(SurveyActivity.this, ResultActivity.class);
                    intent.putExtra("SCORE", result.score);
                    intent.putExtra("LEVEL_NAME", result.level.name());
                    intent.putExtra("ANALYSIS", result.analysis);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onError(String error) {
                    progressBar.setVisibility(View.GONE);
                    rvQuestions.setAlpha(1f);
                    btnNext.setEnabled(true);
                    Toast.makeText(SurveyActivity.this, error, Toast.LENGTH_LONG).show();
                }
            });
    }
}