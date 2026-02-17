package vjp.pro.stressverifier.model;

import android.content.Context;
import java.util.List;
import vjp.pro.stressverifier.data.MockDataStore;
import vjp.pro.stressverifier.enums.StressLevel;
import vjp.pro.stressverifier.logic.ai.AiManager;

public class StressEvaluator {
    private Context context;

    public StressEvaluator(Context context) {
        this.context = context;
    }

    public void evaluate(int totalScore, String textNotes, EvaluationCallback callback) {
        StressLevel level;
        if (totalScore <= 30) level = StressLevel.LOW;
        else if (totalScore <= 60) level = StressLevel.MEDIUM;
        else level = StressLevel.HIGH;

        List<Solution> solutions = MockDataStore.getSolutions(level);

        AiManager.getInstance(context).analyzeStress(totalScore, textNotes, new AiManager.AnalysisCallback() {
            @Override
            public void onSuccess(AiResponse response) {
                AssessmentResult result = new AssessmentResult(
                        totalScore, level, response.analysis, solutions
                );
                callback.onSuccess(result);
            }

            @Override
            public void onError(String error) {
                String fallbackText = "Đánh giá cơ bản (AI tắt/lỗi): Mức độ " + level.name();
                AssessmentResult result = new AssessmentResult(
                        totalScore, level, fallbackText, solutions
                );
                callback.onSuccess(result);
            }
        });
    }

    public interface EvaluationCallback {
        void onSuccess(AssessmentResult result);
        void onError(String error);
    }
}