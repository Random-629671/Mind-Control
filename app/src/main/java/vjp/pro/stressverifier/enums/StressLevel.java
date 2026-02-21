package vjp.pro.stressverifier.enums;

import android.graphics.Color;

public enum StressLevel {
    NONE("Thư giãn tuyệt đối", Color.parseColor("#4FC3F7")),
    LOW("Thấp (An toàn)", Color.parseColor("#4CAF50")),
    MEDIUM("Trung bình (Cần chú ý)", Color.parseColor("#FFC107")),
    HIGH("Cao (Căng thẳng)", Color.parseColor("#FF5722")),
    EXTREME("Báo động (Nguy hiểm)", Color.parseColor("#B71C1C"));

    public final String label;
    public final int color;

    StressLevel(String label, int color) {
        this.label = label;
        this.color = color;
    }
}