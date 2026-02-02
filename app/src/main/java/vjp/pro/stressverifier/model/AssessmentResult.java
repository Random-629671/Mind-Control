package vjp.pro.stressverifier.model;

import java.util.List;

import vjp.pro.stressverifier.enums.StressLevel;

public class AssessmentResult {
    public int score;
    public StressLevel level;
    public String analysis;
    public List<Solution> solutions;

    public AssessmentResult(int score, StressLevel level, String analysis, List<Solution> solutions) {
        this.score = score;
        this.level = level;
        this.analysis = analysis;
        this.solutions = solutions;
    }
}