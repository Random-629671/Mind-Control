package vjp.pro.stressverifier.logic.ai.provider;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import vjp.pro.stressverifier.logic.ai.IAiProvider;

public class GeminiProvider implements IAiProvider {
    private static final String TAG = "GeminiProvider";
    private final String apiKey;
    private final OkHttpClient client;
    private static final String BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent";

    public GeminiProvider(String apiKey) {
        this.apiKey = apiKey;
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    @Override
    public void fetchRawResponse(String prompt, InternalCallback callback) {
        new Thread(() -> {
            try {
                String overridePrompt = prompt; // Todo: Override inital prompt here.
                Log.d(TAG, "Bắt đầu gọi API với Key: " + apiKey.substring(0, 5) + "...");

                JSONObject userPart = new JSONObject();
                userPart.put("text", overridePrompt);

                JSONObject parts = new JSONObject();
                parts.put("parts", new JSONArray().put(userPart));
                parts.put("role", "user");

                JSONArray contents = new JSONArray();
                contents.put(parts);

                JSONObject generationConfig = new JSONObject();
                generationConfig.put("response_mime_type", "application/json");

                JSONObject root = new JSONObject();
                root.put("contents", contents);
                root.put("generationConfig", generationConfig);

                String jsonBody = root.toString();
                Log.d(TAG, "Request Body: " + jsonBody);

                MediaType JSON = MediaType.get("application/json; charset=utf-8");
                RequestBody body = RequestBody.create(jsonBody, JSON);

                String url = BASE_URL + "?key=" + apiKey;
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    String responseBody = response.body() != null ? response.body().string() : "";

                    Log.d(TAG, "Response Code: " + response.code());
                    Log.d(TAG, "Response Body: " + responseBody);

                    if (!response.isSuccessful()) {
                        String errorMsg = "Lỗi HTTP: " + response.code() + " - " + response.message() + "\nBody: " + responseBody;
                        Log.e(TAG, errorMsg);
                        callback.onFailure(errorMsg);
                        return;
                    }

                    JSONObject responseJson = new JSONObject(responseBody);
                    JSONArray candidates = responseJson.optJSONArray("candidates");

                    if (candidates != null && candidates.length() > 0) {
                        JSONObject content = candidates.getJSONObject(0).getJSONObject("content");
                        String text = content.getJSONArray("parts").getJSONObject(0).getString("text");
                        Log.d(TAG, "AI Response Text: " + text);
                        callback.onRawSuccess(text);
                    } else {
                        Log.e(TAG, "Empty Candidates: " + responseBody);
                        callback.onFailure("AI không trả về nội dung, này bác chịu.");
                    }
                }

            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage(), e);
                e.printStackTrace();
                callback.onFailure("Lỗi kết nối: " + e.getMessage());
            }
        }).start();
    }
}