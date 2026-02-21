package vjp.pro.stressverifier.model;

import java.util.List;

public class Question {
    public int id;
    public String content;
    public boolean isTextQuestion;
    public List<Option> options;
    public boolean hasError = false;

    public Question(int id, String content, boolean isTextQuestion, List<Option> options) {
        this.id = id;
        this.content = content;
        this.isTextQuestion = isTextQuestion;
        this.options = options;
    }

    public static class Option {
        public String text;
        public int score;

        public Option(String text, int score) {
            this.text = text;
            this.score = score;
        }
    }
}