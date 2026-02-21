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

    public void evaluate(int totalScore, int maxPossibleScore, String textNotes, EvaluationCallback callback) {
        double percentage = (maxPossibleScore > 0) ? ((double) totalScore / maxPossibleScore) * 100 : 0;

        StressLevel level;
        if (percentage <= 10) level = StressLevel.NONE;
        else if (percentage <= 35) level = StressLevel.LOW;
        else if (percentage <= 65) level = StressLevel.MEDIUM;
        else if (percentage <= 85) level = StressLevel.HIGH;
        else level = StressLevel.EXTREME;

        List<Solution> solutions = MockDataStore.getSolutions(level);

        AiManager.getInstance(context).analyzeStress(totalScore, textNotes, new AiManager.AnalysisCallback() {
            @Override
            public void onSuccess(AiResponse response) {
                AssessmentResult result = new AssessmentResult(
                        (int) percentage, level, response.analysis, solutions
                );
                callback.onSuccess(result);
            }

            @Override
            public void onError(String error) {
                String fallbackText = "Đánh giá (AI Off): " + level.label + "\n" +
                        "Tỉ lệ stress: " + String.format("%.1f", percentage) + "%";
                AssessmentResult result = new AssessmentResult(
                        (int) percentage, level, fallbackText, solutions
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