package vjp.pro.stressverifier.model;

public class HistoryItem {
    public long id;
    public int scoreBefore;
    public int scoreAfter;
    public long timestamp;

    public HistoryItem(long id, int scoreBefore, int scoreAfter, long timestamp) {
        this.id = id;
        this.scoreBefore = scoreBefore;
        this.scoreAfter = scoreAfter;
        this.timestamp = timestamp;
    }
}