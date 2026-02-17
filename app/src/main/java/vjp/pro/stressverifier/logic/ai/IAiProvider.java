package vjp.pro.stressverifier.logic.ai;

public interface IAiProvider {
    void fetchRawResponse(String prompt, InternalCallback callback);

    interface InternalCallback {
        void onRawSuccess(String rawJson);
        void onFailure(String error);
    }
}