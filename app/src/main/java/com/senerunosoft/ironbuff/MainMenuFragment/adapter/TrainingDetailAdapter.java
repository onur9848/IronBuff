package com.senerunosoft.ironbuff.MainMenuFragment.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.senerunosoft.ironbuff.R;
import com.senerunosoft.ironbuff.table.UserTrainingTable;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TrainingDetailAdapter extends RecyclerView.Adapter<TrainingDetailAdapter.ViewHolder> {

    private UserTrainingTable tables;
    private LayoutInflater inflater;
    private ViewPager2 myViewPager;


    public TrainingDetailAdapter(Context context, UserTrainingTable tables, ViewPager2 myViewPager) {
        this.inflater = LayoutInflater.from(context);
        this.tables = tables;
        this.myViewPager = myViewPager;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.user_exercise_detail_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String[] repsAndSets = tables.getExerciseRepsAndSets().get(position).split("x");
        int reps = 1, sets = 0;
        holder.exerciseNameText.setText(tables.getExerciseName().get(position));
        holder.repsText.setText(repsAndSets[reps]);
        holder.setsText.setText(repsAndSets[sets]);
        holder.detailText.setText(tables.getExerciseDetail().get(position));
        holder.targetText.setText(tables.getExerciseZone().get(position));
        Picasso.get().load(tables.getExerciseImg1().get(position)).into(holder.exerciseImg1);
        Picasso.get().load(tables.getExerciseImg2().get(position)).into(holder.exerciseImg2);

        if (position > 0) {
            holder.backbutton.setVisibility(View.VISIBLE);
        }

        holder.backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newPosition = position - 1;
                myViewPager.setCurrentItem(newPosition);
            }
        });
        holder.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newPosition = position + 1;
                myViewPager.setCurrentItem(newPosition);
            }
        });

    }


    @Override
    public int getItemCount() {
        return tables.getExerciseName().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView setsText, repsText, detailText, targetText, exerciseNameText;
        ImageView exerciseImg1, exerciseImg2;
        ImageView nextButton, backbutton;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            setsText = itemView.findViewById(R.id.training_detail_sets);
            repsText = itemView.findViewById(R.id.training_detail_reps);
            targetText = itemView.findViewById(R.id.training_detail_target);
            detailText = itemView.findViewById(R.id.training_detail_exercise_detail);
            exerciseNameText = itemView.findViewById(R.id.training_detail_exercise_name);
            exerciseImg1 = itemView.findViewById(R.id.training_detail_img1);
            exerciseImg2 = itemView.findViewById(R.id.training_detail_img2);
            nextButton = itemView.findViewById(R.id.training_detail_nextbutton);
            backbutton = itemView.findViewById(R.id.training_detail_backbutton);


        }
    }
}
