package vjp.pro.stressverifier.model;

public class HistoryItem {
    public int scoreBefore;
    public int scoreAfter;
    public long timestamp;

    public HistoryItem(int scoreBefore, int scoreAfter, long timestamp) {
        this.scoreBefore = scoreBefore;
        this.scoreAfter = scoreAfter;
        this.timestamp = timestamp;
    }
}