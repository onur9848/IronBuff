package com.senerunosoft.ironbuff.AdminPage.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.senerunosoft.ironbuff.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> list;
    private String selecTedExerciseZone;
    TextView textView;
    private ArrayList<String> img;

    public GridViewAdapter(Context context, ArrayList<String> exerciseZone,TextView textView) {
        this.context = context;
        this.list = exerciseZone;
        this.textView = textView;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    public void setSelecTedExerciseZone(String selecTedExerciseZone) {
        this.selecTedExerciseZone = selecTedExerciseZone;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.my_grid_view, viewGroup, false);


        }
        Uri uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/iron-buff-b521e.appspot.com/o/ironbuff%2Firon%20buff-3%20(2).png?alt=media&token=49244278-d5bb-43c7-b06e-3987b61ea2c8");
        TextView exerciseZoneName = (TextView) view.findViewById(R.id.exercise_zone_textview_gridview);
        ImageView exerciseImg = (ImageView) view.findViewById(R.id.exercise_zone_image);
        exerciseZoneName.setText(list.get(i));
        Picasso.get().load(uri).into(exerciseImg);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelecTedExerciseZone(list.get(i));
                viewGroup.setVisibility(View.GONE);
                textView.setText(list.get(i));

            }
        });

        return view;
    }
}
