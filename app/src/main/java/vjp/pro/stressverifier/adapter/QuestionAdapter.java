package vjp.pro.stressverifier.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vjp.pro.stressverifier.model.Question;
import vjp.pro.stressverifier.R;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    private List<Question> questionList;

    private Map<Integer, Integer> scores = new HashMap<>();

    private String textAnswer = "";

    public QuestionAdapter(List<Question> questionList) {
        this.questionList = questionList;
    }

    public Map<Integer, Integer> getScores() {
        return scores;
    }

    public String getTextAnswer() {
        return textAnswer;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = questionList.get(position);
        holder.tvContent.setText("Câu " + (position + 1) + ": " + question.content);

        if (question.isTextQuestion) {
            holder.rgOptions.setVisibility(View.GONE);
            holder.etAnswer.setVisibility(View.VISIBLE);

            holder.etAnswer.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override
                public void afterTextChanged(Editable s) {
                    textAnswer = s.toString();
                }
            });

        } else {
            holder.rgOptions.setVisibility(View.VISIBLE);
            holder.etAnswer.setVisibility(View.GONE);

            holder.rgOptions.setOnCheckedChangeListener(null);

            holder.rgOptions.clearCheck();
            if (scores.containsKey(question.id)) {
                int score = scores.get(question.id);
                switch (score) {
                    case 0: holder.rgOptions.check(R.id.rbOp0); break;
                    case 1: holder.rgOptions.check(R.id.rbOp1); break;
                    case 2: holder.rgOptions.check(R.id.rbOp2); break;
                    case 3: holder.rgOptions.check(R.id.rbOp3); break;
                    case 4: holder.rgOptions.check(R.id.rbOp4); break;
                }
            }

            holder.rgOptions.setOnCheckedChangeListener((group, checkedId) -> {
                int score = 0;
                if (checkedId == R.id.rbOp0) score = 0;
                else if (checkedId == R.id.rbOp1) score = 1;
                else if (checkedId == R.id.rbOp2) score = 2;
                else if (checkedId == R.id.rbOp3) score = 3;
                else if (checkedId == R.id.rbOp4) score = 4;

                scores.put(question.id, score);
            });
        }
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView tvContent;
        RadioGroup rgOptions;
        EditText etAnswer;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tvQuestionContent);
            rgOptions = itemView.findViewById(R.id.rgOptions);
            etAnswer = itemView.findViewById(R.id.etAnswer);
        }
    }
}