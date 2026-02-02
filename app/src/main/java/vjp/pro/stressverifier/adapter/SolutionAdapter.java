package vjp.pro.stressverifier.adapter;

import android.content.Context;
import android.content.Intent;
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

    public SolutionAdapter(Context context, List<Solution> solutionList) {
        this.context = context;
        this.solutionList = solutionList;
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
        holder.tvDesc.setText("Loại: " + solution.type.name());

        holder.btnPerform.setOnClickListener(v -> {
            if (solution.content.startsWith("http")) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(solution.content));
                context.startActivity(browserIntent);
            } else {
                Toast.makeText(context, "Đang mở: " + solution.title, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, FeedbackActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return solutionList.size();
    }

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