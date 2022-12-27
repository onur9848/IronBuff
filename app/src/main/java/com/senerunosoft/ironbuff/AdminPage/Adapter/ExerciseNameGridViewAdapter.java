package com.senerunosoft.ironbuff.AdminPage.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.senerunosoft.ironbuff.R;
import com.senerunosoft.ironbuff.table.ExerciseTable;
import com.senerunosoft.ironbuff.table.UserTrainingTable;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ExerciseNameGridViewAdapter extends BaseAdapter {
    private Context context;
    private List<ExerciseTable> exerciseTables;
    private String selecTedExerciseZone;
    TextView textView;
    private ArrayList<String> img;
    private int Id;

    public ExerciseNameGridViewAdapter(Context context, List<ExerciseTable> exerciseTables, TextView textView,int Id ){
        this.context = context;
        this.exerciseTables = exerciseTables;
        this.textView = textView;
        this.Id = Id;
    }

    @Override
    public int getCount() {
        return exerciseTables.size();
    }

    @Override
    public Object getItem(int i) {
        return exerciseTables.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.my_grid_view, viewGroup, false);


        }
        TextView exerciseName = (TextView) view.findViewById(R.id.exercise_zone_textview_gridview);
        ImageView exerciseImg = (ImageView) view.findViewById(R.id.exercise_zone_image);
        exerciseName.setText(exerciseTables.get(i).getExerciseName());
        Picasso.get().load(exerciseTables.get(i).getExerciseImg1()).into(exerciseImg);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Id = i ;
                viewGroup.setVisibility(View.GONE);
                textView.setText(exerciseTables.get(i).getExerciseName());

            }
        });

        return view;
    }
}
