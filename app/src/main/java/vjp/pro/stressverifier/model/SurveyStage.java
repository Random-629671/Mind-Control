package vjp.pro.stressverifier.model;

import java.util.List;

public class SurveyStage {
    public int stageId;
    public String title;
    public String description;
    public List<Question> questions;

    public SurveyStage(int stageId, String title, String description, List<Question> questions) {
        this.stageId = stageId;
        this.title = title;
        this.description = description;
        this.questions = questions;
    }
}