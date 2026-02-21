package vjp.pro.stressverifier.model;

import java.util.List;

public class SurveyStage {
    public int stageId;
    public String title;
    public String description;
    public List<Question> questions;
    public int maxQuestions;

    public SurveyStage(int stageId, String title, String description, List<Question> questions, int maxQuestions) {
        this.stageId = stageId;
        this.title = title;
        this.description = description;
        this.questions = questions;
        this.maxQuestions = maxQuestions;
    }
}