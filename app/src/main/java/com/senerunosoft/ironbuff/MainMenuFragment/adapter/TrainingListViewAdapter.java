package com.senerunosoft.ironbuff.MainMenuFragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.senerunosoft.ironbuff.R;

import java.util.ArrayList;

public class TrainingListViewAdapter extends ArrayAdapter<String> {
    private ArrayList<String> exerciseZone;
    private ArrayList<String> exerciseCount;
    private final LayoutInflater inflater;
    private ViewHolder holder;


    public TrainingListViewAdapter(@NonNull Context context, ArrayList<String> exerciseZone, ArrayList<String> exerciseCount) {
        super(context,0);
        this.exerciseZone = exerciseZone;
        this.exerciseCount =exerciseCount;
        inflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

       if (convertView == null){
           convertView = inflater.inflate(R.layout.list_item_program,null);

           holder = new ViewHolder();
           holder.exerciseZoneText = (TextView) convertView.findViewById(R.id.exercise_zone_listview);
           holder.exerciseCountText = (TextView) convertView.findViewById(R.id.exercise_count_listview);
           convertView.setTag(holder);
       }
       else {
           holder = (ViewHolder) convertView.getTag();
       }
       if (exerciseZone != null && exerciseCount != null){
           holder.exerciseZoneText.setText(exerciseZone.get(position));
           holder.exerciseCountText.setText(exerciseCount.get(position));
       }


        return convertView;
    }

    @Override
    public int getCount() {
        return exerciseZone.size();
    }

    public static class ViewHolder{
        TextView exerciseZoneText;
        TextView exerciseCountText;
    }



}
