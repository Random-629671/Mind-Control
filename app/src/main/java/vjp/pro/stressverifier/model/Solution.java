package vjp.pro.stressverifier.model;

import vjp.pro.stressverifier.enums.SolutionType;

public class Solution {
    public String title;
    public String content;
    public SolutionType type;

    public Solution(String title, String content, SolutionType type) {
        this.title = title;
        this.content = content;
        this.type = type;
    }
}