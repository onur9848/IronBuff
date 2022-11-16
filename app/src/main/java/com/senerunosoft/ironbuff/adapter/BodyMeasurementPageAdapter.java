package com.senerunosoft.ironbuff.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.senerunosoft.ironbuff.MainMenuFragment.MainMenuFragment;
import com.senerunosoft.ironbuff.R;
import com.senerunosoft.ironbuff.table.userMeasurementTable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BodyMeasurementPageAdapter extends RecyclerView.Adapter<BodyMeasurementPageAdapter.ViewHolder> {

    private final List<userMeasurementTable> table;
    ViewPager2 viewPager2;
    LayoutInflater layoutInflater;

    public BodyMeasurementPageAdapter(Context context, List<userMeasurementTable> table, ViewPager2 viewPager2) {
        this.table = table;
        this.viewPager2 = viewPager2;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.body_measurement_view, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.chest.setText(table.get(position).getChest()+" cm");
        holder.leftArm.setText(table.get(position).getLeftArm()+" cm");
        holder.rightArm.setText(table.get(position).getRightArm()+" cm");
        holder.waist.setText(table.get(position).getWaist()+" cm");
        holder.hips.setText(table.get(position).getHips()+" cm");
        holder.leftThigh.setText(table.get(position).getLeftThigh()+" cm");
        holder.rightThigh.setText(table.get(position).getRightThigh()+" cm");
        holder.leftCalf.setText(table.get(position).getLeftCalf()+" cm");
        holder.rightCalf.setText(table.get(position).getRightCalf()+" cm");
        holder.weight.setText(table.get(position).getWeight()+" kg");

    }

    @Override
    public int getItemCount() {
        return table.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView chest, leftArm, rightArm, waist, hips, leftThigh, rightThigh, leftCalf, rightCalf,weight;
        LinearLayout linearLayout;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.container);
            chest = itemView.findViewById(R.id.measurement_chest);
            leftArm = itemView.findViewById(R.id.measurement_left_arm);
            rightArm = itemView.findViewById(R.id.measurement_right_arm);
            waist = itemView.findViewById(R.id.measurement_waist);
            hips = itemView.findViewById(R.id.measurement_hips);
            leftThigh = itemView.findViewById(R.id.measurement_left_thigh);
            rightThigh = itemView.findViewById(R.id.measurement_right_thigh);
            leftCalf = itemView.findViewById(R.id.measurement_left_calf);
            rightCalf = itemView.findViewById(R.id.measurement_right_calf);
            weight = itemView.findViewById(R.id.measurement_weight);

        }
    }
}