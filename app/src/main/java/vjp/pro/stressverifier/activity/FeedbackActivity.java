package vjp.pro.stressverifier.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import vjp.pro.stressverifier.R;

public class FeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Button btnFinish = findViewById(R.id.btnFinish);

        btnFinish.setOnClickListener(v -> {
            Toast.makeText(this, "Cảm ơn bạn đã phản hồi!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(FeedbackActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}