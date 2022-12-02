package com.senerunosoft.ironbuff.MainMenuFragment.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.senerunosoft.ironbuff.MainMenuFragment.MainMenuFragment;
import com.senerunosoft.ironbuff.MainMenuFragment.addMeasurementDialogFragment;
import com.senerunosoft.ironbuff.R;
import com.senerunosoft.ironbuff.table.UserMeasurementTable;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.List;

public class BodyMeasurementPageAdapter extends RecyclerView.Adapter<BodyMeasurementPageAdapter.ViewHolder> {

    private final List<UserMeasurementTable> table;
    ViewPager2 viewPager2;
    LayoutInflater layoutInflater;
    FragmentManager manager;

    public BodyMeasurementPageAdapter(Context context, List<UserMeasurementTable> table, ViewPager2 viewPager2, FragmentManager manager) {
        this.table = table;
        this.viewPager2 = viewPager2;
        this.layoutInflater = LayoutInflater.from(context);
        this.manager = manager;

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
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
        holder.chest.setText(table.get(position).getChest() + " cm");
        holder.leftArm.setText(table.get(position).getLeftArm() + " cm");
        holder.rightArm.setText(table.get(position).getRightArm() + " cm");
        holder.waist.setText(table.get(position).getWaist() + " cm");
        holder.hips.setText(table.get(position).getHips() + " cm");
        holder.leftThigh.setText(table.get(position).getLeftThigh() + " cm");
        holder.rightThigh.setText(table.get(position).getRightThigh() + " cm");
        holder.leftCalf.setText(table.get(position).getLeftCalf() + " cm");
        holder.rightCalf.setText(table.get(position).getRightCalf() + " cm");
        holder.weight.setText(table.get(position).getWeight() + " kg");
        holder.date.setText(myFormat.format(table.get(position).getDate()));

        holder.addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addMeasurementDialogFragment dialogFragment = new addMeasurementDialogFragment();
                dialogFragment.show(manager,"DialogFragment");
            }
        });

    }

    @Override
    public int getItemCount() {
        return table.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView chest, leftArm, rightArm, waist, hips, leftThigh, rightThigh, leftCalf, rightCalf, weight, date;
        ImageView addImage;
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
            date = itemView.findViewById(R.id.measurement_date);
            addImage = itemView.findViewById(R.id.add_measurement_button);

        }
    }
}