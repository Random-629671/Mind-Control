package vjp.pro.stressverifier.model;

import java.util.List;
import java.util.Map;

import vjp.pro.stressverifier.data.MockDataStore;
import vjp.pro.stressverifier.enums.StressLevel;

public class StressEvaluator {
    private boolean USE_AI_MODE = false;

    public interface EvaluationCallback {
        void onSuccess(AssessmentResult result);
        void onError(String error);
    }

    public void evaluate(Map<Integer, Integer> scores, String textAnswer, EvaluationCallback callback) {

        if (USE_AI_MODE) {
            // TODO: Gọi API OpenAI/Gemini ở đây
            // callAiApi(scores, textAnswer, callback);
            callback.onError("Tính năng AI đang phát triển, vui lòng tắt USE_AI_MODE");
        } else {
            processLocalLogic(scores, callback);
        }
    }

    private void processLocalLogic(Map<Integer, Integer> scores, EvaluationCallback callback) {
        int totalScore = 0;

        for (int score : scores.values()) {
            totalScore += score;
        }

        StressLevel level;
        String analysis;

        if (totalScore <= 13) {
            level = StressLevel.LOW;
            analysis = "Bạn đang kiểm soát tốt cảm xúc. Hãy duy trì thói quen hiện tại.";
        } else if (totalScore <= 26) {
            level = StressLevel.MEDIUM;
            analysis = "Bạn có dấu hiệu căng thẳng nhẹ. Nên dành thời gian nghỉ ngơi.";
        } else {
            level = StressLevel.HIGH;
            analysis = "Mức độ báo động! Bạn cần áp dụng các liệu pháp thư giãn ngay.";
        }

        List<Solution> solutions = MockDataStore.getSolutions(level);

        AssessmentResult result = new AssessmentResult(totalScore, level, analysis, solutions);
        callback.onSuccess(result);
    }
}