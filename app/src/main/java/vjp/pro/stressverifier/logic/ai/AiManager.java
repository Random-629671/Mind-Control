package vjp.pro.stressverifier.logic.ai;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import vjp.pro.stressverifier.logic.ai.provider.GeminiProvider;
import vjp.pro.stressverifier.model.AiResponse;

public class AiManager {
    private static AiManager instance;
    private Context context;
    private IAiProvider currentProvider;
    private String cachedApiKey = "";

    // Todo: change this inital prompt to something more appropriate.
    private static final String SYSTEM_PROMPT =
            "You are a mental health assistant. Analyze the user's stress level based on their score and notes. " +
                    "The max score is around 120. " +
                    "Return output strictly in JSON format without markdown code blocks. " +
                    "Keys: 'analysis' (string, Vietnamese), 'mood' (string, short), 'advice' (array of strings).";

    private AiManager(Context context) {
        this.context = context.getApplicationContext();
    }

    public static synchronized AiManager getInstance(Context context) {
        if (instance == null) {
            instance = new AiManager(context);
        }
        return instance;
    }

    public interface AnalysisCallback {
        void onSuccess(AiResponse response);
        void onError(String error);
    }

    public void analyzeStress(int score, String notes, AnalysisCallback callback) {
        SharedPreferences prefs = context.getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        boolean useAi = prefs.getBoolean("USE_AI", false);
        String apiKey = prefs.getString("API_KEY", "").trim();

        if (!useAi) {
            callback.onError("AI_DISABLED");
            return;
        }

        if (apiKey.isEmpty()) {
            callback.onError("MISSING_API_KEY");
            return;
        }

        if (currentProvider == null || !apiKey.equals(cachedApiKey)) {
            currentProvider = new GeminiProvider(apiKey);
            cachedApiKey = apiKey;
        }

        String fullPrompt = SYSTEM_PROMPT + "\nUser Input Data -> Score: " + score + ". Notes: " + notes;

        currentProvider.fetchRawResponse(fullPrompt, new IAiProvider.InternalCallback() {
            @Override
            public void onRawSuccess(String rawJson) {
                try {
                    String cleanJson = rawJson.replace("```json", "")
                            .replace("```", "")
                            .trim();

                    AiResponse response = new AiResponse(cleanJson);
                    new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(response));

                } catch (Exception e) {
                    new Handler(Looper.getMainLooper()).post(() ->
                            callback.onError("JSON Parsing Error: " + e.getMessage() + "\nRaw: " + rawJson));
                }
            }

            @Override
            public void onFailure(String error) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onError(error));
            }
        });
    }
}