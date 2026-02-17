package vjp.pro.stressverifier.logic;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vjp.pro.stressverifier.data.MockDataStore;
import vjp.pro.stressverifier.model.Question;
import vjp.pro.stressverifier.model.SurveyStage;

public class SurveyManager {
    private static SurveyManager instance;
    private List<SurveyStage> stages;
    private int currentStageIndex = 0;
    private Map<Integer, Integer> scoreMap = new HashMap<>();
    private Map<Integer, String> textAnswerMap = new HashMap<>();

    private SurveyManager() {}

    public static synchronized SurveyManager getInstance() {
        if (instance == null) {
            instance = new SurveyManager();
        }
        return instance;
    }

    public void initData(Context context) {
        currentStageIndex = 0;
        scoreMap.clear();
        textAnswerMap.clear();

        SharedPreferences prefs = context.getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        boolean useAI = prefs.getBoolean("USE_AI", false);

        List<SurveyStage> rawStages = MockDataStore.getAllStages();
        stages = new ArrayList<>();

        for (SurveyStage stage : rawStages) {
            List<Question> filteredQuestions = new ArrayList<>();
            for (Question q : stage.questions) {
                if (q.isTextQuestion && !useAI) {
                    continue;
                }
                filteredQuestions.add(q);
            }

            if (!filteredQuestions.isEmpty()) {
                stages.add(new SurveyStage(stage.stageId, stage.title, stage.description, filteredQuestions));
            }
        }
    }

    public SurveyStage getCurrentStage() {
        if (stages != null && currentStageIndex < stages.size()) {
            return stages.get(currentStageIndex);
        }
        return null;
    }

    public boolean hasNextStage() {
        return stages != null && currentStageIndex < stages.size() - 1;
    }

    public void moveToNextStage() {
        if (hasNextStage()) {
            currentStageIndex++;
        }
    }

    public void saveAnswers(Map<Integer, Integer> scores, String textAnswer) {
        scoreMap.putAll(scores);
        SurveyStage current = getCurrentStage();
        if (current != null) {
            for (Question q : current.questions) {
                if (q.isTextQuestion) {
                    textAnswerMap.put(q.id, textAnswer);
                }
            }
        }
    }

    public int getTotalScore() {
        int sum = 0;
        for (int s : scoreMap.values()) sum += s;
        return sum;
    }

    public String getCombinedTextAnswers() {
        StringBuilder sb = new StringBuilder();
        for (String s : textAnswerMap.values()) sb.append(s).append(". ");
        return sb.toString();
    }
}