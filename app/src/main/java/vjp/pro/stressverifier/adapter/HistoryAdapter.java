package vjp.pro.stressverifier.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import vjp.pro.stressverifier.R;
import vjp.pro.stressverifier.model.HistoryItem;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private List<HistoryItem> historyList;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public interface OnItemLongClickListener {
        void onItemLongClick(HistoryItem item);
    }
    private OnItemLongClickListener longClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    public HistoryAdapter(List<HistoryItem> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistoryItem item = historyList.get(position);
        holder.tvDate.setText(sdf.format(new Date(item.timestamp)));
        holder.tvScore.setText(item.scoreAfter + " %");

        int score = item.scoreAfter;
        if(score > 80) holder.tvScore.setTextColor(Color.RED);
        else if(score > 50) holder.tvScore.setTextColor(Color.parseColor("#FF9800"));
        else holder.tvScore.setTextColor(Color.parseColor("#4CAF50"));

        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onItemLongClick(item);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() { return historyList.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvScore;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvHistoryDate);
            tvScore = itemView.findViewById(R.id.tvHistoryScore);
        }
    }
}