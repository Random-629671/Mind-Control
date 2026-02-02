package vjp.pro.stressverifier.model;

public class Question {
    public int id;
    public String content;
    public boolean isTextQuestion;

    public Question(int id, String content, boolean isTextQuestion) {
        this.id = id;
        this.content = content;
        this.isTextQuestion = isTextQuestion;
    }
}