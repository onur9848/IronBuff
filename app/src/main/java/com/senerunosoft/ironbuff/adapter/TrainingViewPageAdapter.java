package com.senerunosoft.ironbuff.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.senerunosoft.ironbuff.R;
import com.senerunosoft.ironbuff.table.TrainingProgramTable;
import com.senerunosoft.ironbuff.table.UserTrainingTable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TrainingViewPageAdapter extends RecyclerView.Adapter<TrainingViewPageAdapter.ViewHolder> {

    private final List<UserTrainingTable> trainingTables;
    LayoutInflater layoutInflater;

    public TrainingViewPageAdapter(Context context, List<UserTrainingTable> trainingTables) {
        this.layoutInflater = LayoutInflater.from(context);
        this.trainingTables = trainingTables;
    }


    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.training_card_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
//    holder.text1.setText("deneme");
    }

    @Override
    public int getItemCount() {
        return trainingTables.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        TextView text1;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.container_training_card);
            text1 = itemView.findViewById(R.id.day_test);

        }
    }


}
