package vjp.pro.stressverifier.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vjp.pro.stressverifier.model.Question;
import vjp.pro.stressverifier.R;

import com.google.android.material.card.MaterialCardView;

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

    public void markError(int position) {
        questionList.get(position).hasError = true;
        notifyItemChanged(position);
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
        Context context = holder.itemView.getContext();
        holder.tvContent.setText("Câu " + (position + 1) + ": " + question.content);

        if (question.hasError) {
            holder.cardView.setStrokeColor(Color.RED);
            holder.cardView.setStrokeWidth(4);
        } else {
            holder.cardView.setStrokeColor(Color.TRANSPARENT);
            holder.cardView.setStrokeWidth(0);
        }

        holder.rgOptions.setOnCheckedChangeListener(null);
        holder.rgOptions.removeAllViews();

        if (holder.textWatcher != null) {
            holder.etAnswer.removeTextChangedListener(holder.textWatcher);
        }
        holder.rgOptions.setOnCheckedChangeListener(null);

        if (question.isTextQuestion) {
            holder.rgOptions.setVisibility(View.GONE);
            holder.etAnswer.setVisibility(View.VISIBLE);

            holder.etAnswer.setText(textAnswer);

            holder.textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override
                public void afterTextChanged(Editable s) {
                    textAnswer = s.toString();
                }
            };
            holder.etAnswer.addTextChangedListener(holder.textWatcher);
        } else {
            holder.rgOptions.setVisibility(View.VISIBLE);
            holder.etAnswer.setVisibility(View.GONE);

            if (question.options != null) {
                for (Question.Option option : question.options) {
                    RadioButton rb = new RadioButton(context);
                    rb.setText(option.text);
                    rb.setId(View.generateViewId());
                    rb.setPadding(0, 10, 0, 10);

                    holder.rgOptions.addView(rb);

                    if (scores.containsKey(question.id) && scores.get(question.id) == option.score) {
                        rb.setChecked(true);
                    }

                    rb.setOnClickListener(v -> {
                        scores.put(question.id, option.score);
                        question.hasError = false;
                        holder.cardView.setStrokeColor(Color.TRANSPARENT);
                    });
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        TextView tvContent;
        RadioGroup rgOptions;
        EditText etAnswer;
        TextWatcher textWatcher;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (MaterialCardView) itemView;
            tvContent = itemView.findViewById(R.id.tvQuestionContent);
            rgOptions = itemView.findViewById(R.id.rgOptions);
            etAnswer = itemView.findViewById(R.id.etAnswer);
        }
    }
}