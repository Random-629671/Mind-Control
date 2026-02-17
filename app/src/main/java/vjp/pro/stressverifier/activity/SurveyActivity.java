package vjp.pro.stressverifier.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import vjp.pro.stressverifier.model.StressEvaluator;
import vjp.pro.stressverifier.model.SurveyStage;

public class SurveyActivity extends AppCompatActivity {
    private RecyclerView rvQuestions;
    private Button btnNext;
    private TextView tvLayerTitle;
    private QuestionAdapter adapter;
    private SurveyManager surveyManager;
    private ProgressDialog progressDialog;
    // Todo: Verify if 'ProgressDialog' is fully deprecated and perform a replacement.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        rvQuestions = findViewById(R.id.rvQuestions);
        btnNext = findViewById(R.id.btnSubmitSurvey);
        tvLayerTitle = findViewById(R.id.tvLayerTitle);

        surveyManager = SurveyManager.getInstance();
        surveyManager.initData(this);

        setupCurrentStage();

        btnNext.setOnClickListener(v -> handleNextStep());
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

        long required = currentStage.questions.stream().filter(q -> !q.isTextQuestion).count();
        if (currentScores.size() < required) {
            Toast.makeText(this, "Vui lòng trả lời hết các câu hỏi trắc nghiệm!", Toast.LENGTH_SHORT).show();
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
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang tổng hợp kết quả & phân tích...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        btnNext.setEnabled(false);

        StressEvaluator evaluator = new StressEvaluator(this);

        evaluator.evaluate(surveyManager.getTotalScore(), surveyManager.getCombinedTextAnswers(),
                new StressEvaluator.EvaluationCallback() {
                    @Override
                    public void onSuccess(AssessmentResult result) {
                        if (progressDialog.isShowing()) progressDialog.dismiss();
                        Intent intent = new Intent(SurveyActivity.this, ResultActivity.class);
                        intent.putExtra("SCORE", result.score);
                        intent.putExtra("LEVEL_NAME", result.level.name());
                        intent.putExtra("ANALYSIS", result.analysis);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(String error) {
                        if (progressDialog.isShowing()) progressDialog.dismiss();
                        Toast.makeText(SurveyActivity.this, "Lỗi: " + error, Toast.LENGTH_LONG).show();
                        btnNext.setEnabled(true);
                    }
                });
    }
}