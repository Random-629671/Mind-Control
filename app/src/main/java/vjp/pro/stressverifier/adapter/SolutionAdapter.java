package vjp.pro.stressverifier.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vjp.pro.stressverifier.activity.FeedbackActivity;
import vjp.pro.stressverifier.R;
import vjp.pro.stressverifier.model.Solution;

public class SolutionAdapter extends RecyclerView.Adapter<SolutionAdapter.SolutionViewHolder> {
    private List<Solution> solutionList;
    private Context context;
    private long historyId;

    public SolutionAdapter(Context context, List<Solution> solutionList, long historyId) {
        this.context = context;
        this.solutionList = solutionList;
        this.historyId = historyId;
    }

    @NonNull
    @Override
    public SolutionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_solution, parent, false);
        return new SolutionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SolutionViewHolder holder, int position) {
        Solution solution = solutionList.get(position);
        holder.tvTitle.setText(solution.title);
        holder.tvDesc.setText(solution.type.name());

        if (solution.isPerformed) {
            holder.btnPerform.setText("Đánh giá");
            holder.btnPerform.setBackgroundColor(Color.parseColor("#FF9800"));
        } else {
            holder.btnPerform.setText("Thực hiện");
            holder.btnPerform.setBackgroundColor(Color.parseColor("#4CAF50"));
        }

        holder.btnPerform.setOnClickListener(v -> {
            if (solution.isPerformed) {
                Intent intent = new Intent(context, FeedbackActivity.class);
                intent.putExtra("HISTORY_ID", historyId);
                intent.putExtra("SOLUTION_TITLE", solution.title);
                context.startActivity(intent);
            } else {
                solution.isPerformed = true;
                notifyItemChanged(position);

                if (solution.content.startsWith("http")) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(solution.content));
                    browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    try {
                        context.startActivity(browserIntent);
                    } catch (Exception e) {
                        Toast.makeText(context, "Không tìm thấy ứng dụng phù hợp", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Bài tập tại chỗ: " + solution.title, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() { return solutionList.size(); }

    public static class SolutionViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDesc;
        Button btnPerform;
        public SolutionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvSolutionTitle);
            tvDesc = itemView.findViewById(R.id.tvSolutionDesc);
            btnPerform = itemView.findViewById(R.id.btnPerform);
        }
    }
}