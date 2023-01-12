package com.senerunosoft.ironbuff.MainMenuFragment.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.senerunosoft.ironbuff.MainMenuFragment.TrainingProgramFragmentDirections;
import com.senerunosoft.ironbuff.R;
import com.senerunosoft.ironbuff.table.UserTrainingTable;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
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
        final UserTrainingTable getPositionTable = trainingTables.get(position);
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
        holder.dateText.setText(myFormat.format(getPositionTable.getDate()));
        TrainingListViewAdapter adapter = new TrainingListViewAdapter(layoutInflater.getContext(),getPositionTable.getExerciseName(),getPositionTable.getExerciseRepsAndSets());
        holder.listView.setAdapter(adapter);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("DocId",getPositionTable.getDocID());
                NavDirections directions = TrainingProgramFragmentDirections.gotoTrainingDetail();
                Navigation.findNavController(view).navigate(directions.getActionId(),bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return trainingTables.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        TextView dateText;
        ListView listView;
        Button button;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.container_training_card);
            dateText = itemView.findViewById(R.id.day_test);
            listView = itemView.findViewById(R.id.trainingExerciseList);
            button = itemView.findViewById(R.id.training_card_view_button);
        }
    }


}
