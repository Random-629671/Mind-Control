package vjp.pro.stressverifier.model;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class AiResponse {
    public String analysis;
    public String mood;
    public List<String> advice;
    public AiResponse(String jsonString) {
        try {
            JSONObject json = new JSONObject(jsonString);

            this.analysis = json.optString("analysis", "Không có dữ liệu phân tích.");
            this.mood = json.optString("mood", "Không xác định");

            this.advice = new ArrayList<>();
            JSONArray arr = json.optJSONArray("advice");
            if (arr != null) {
                for (int i = 0; i < arr.length(); i++) {
                    this.advice.add(arr.getString(i));
                }
            }
        } catch (Exception e) {
            this.analysis = "Lỗi đọc dữ liệu AI: " + e.getMessage();
            this.mood = "Error";
            this.advice = new ArrayList<>();
        }
    }
}