package com.senerunosoft.ironbuff.MainMenuFragment;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.service.autofill.Dataset;
import android.util.Log;
import android.util.TypedValue;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.mikephil.charting.buffer.BarBuffer;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.*;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.*;
import com.github.mikephil.charting.highlight.ChartHighlighter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.senerunosoft.ironbuff.R;
import com.senerunosoft.ironbuff.databinding.FragmentStatisticBinding;
import com.senerunosoft.ironbuff.table.MessageTable;
import com.senerunosoft.ironbuff.table.UserMeasurementTable;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class StatisticFragment extends Fragment {

    FragmentStatisticBinding binding;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    TypedValue value;
    ValueFormatter formatter;
    private List<UserMeasurementTable> MeasurementList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStatisticBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        fireStoreData();

    }

    private void fireStoreData() {
        firestore.collection("userTable").document(auth.getUid()).collection("bodyMeasurement").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                MeasurementList = task.getResult().toObjects(UserMeasurementTable.class);

                getColors();
                setWeightTable();
                setMeasurementTable();
            }
        });
//        firestore.collection("userTable").document(auth.getUid()).collection();


    }

    private void getColors() {
        value = new TypedValue();
        Resources.Theme theme = getContext().getTheme();
        theme.resolveAttribute(com.google.android.material.R.attr.colorPrimary, value, true);
    }

    @SuppressLint("ResourceAsColor")
    private void setWeightTable() {
        List<Entry> entryList = new ArrayList<>();
        List<String> dateLabel = new ArrayList<>();
        SimpleDateFormat myformat = new SimpleDateFormat("dd/MM/yy");
        int i = 0;
        Collections.sort(MeasurementList);
        LineChart chart = binding.lineChart;
        for (UserMeasurementTable table : MeasurementList) {
            float weight = Float.parseFloat(table.getWeight());
            dateLabel.add(myformat.format(table.getDate()));
            entryList.add(new Entry(i, weight));
            i++;
        }
        formatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value >= MeasurementList.size())
                    return super.getFormattedValue(value);
                else
                    return dateLabel.get((int) value);

            }
        };
        LineDataSet dataSet = new LineDataSet(entryList, "Ağırlık");
        dataSet.setColor(Color.YELLOW);
        dataSet.setValueTextSize(12f);
        dataSet.setFormSize(15);
        dataSet.setForm(Legend.LegendForm.CIRCLE);
        dataSet.setCircleRadius(10);
        dataSet.setCircleHoleRadius(6);
        dataSet.setLineWidth(5);
        dataSet.setDrawFilled(true);
        dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);


        LineData data = new LineData(dataSet);

        chartSetStyle(chart);
        chart.setData(data);
    }

    private LineData data;

    private void setMeasurementTable() {
        List<String> spinnerList = new ArrayList<>();
        spinnerList.add("Chest"); //0
        spinnerList.add("Hips"); //1
        spinnerList.add("Arms"); //2
        spinnerList.add("Waist"); //3
        spinnerList.add("Legs"); //4
        SpinnerAdapter adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerList);
        data = new LineData();
        binding.statisticSelectZone.setAdapter(adapter);
        final LineChart chart = binding.statisticMeasurementChart;
        data.setHighlightEnabled(true);
        chartSetStyle(chart);

        binding.statisticSelectZone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(), "select: " + i, Toast.LENGTH_SHORT).show();
                getdata(i);
                chart.setData(data);
                chart.invalidate();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }

    private void getdata(int i) {

        int j = 0;

        if (i == 0) {
            List<Entry> chestEntries = new ArrayList<>();
            for (UserMeasurementTable table : MeasurementList) {
                float chest = Float.parseFloat(table.getChest());
                chestEntries.add(new Entry(j++, chest));
            }
            LineDataSet chestDataSet = new LineDataSet(chestEntries, getString(R.string.chest));
            dataSetStyle(chestDataSet);

            data = new LineData(chestDataSet);

        } else if (i == 1) {
            float hips;
            List<Entry> hipsEntries = new ArrayList<>();
            for (UserMeasurementTable table : MeasurementList) {
                hips = Float.parseFloat(table.getHips());
                hipsEntries.add(new Entry(j++, hips));
            }
            LineDataSet hipsDataset = new LineDataSet(hipsEntries, getString(R.string.hips));

            dataSetStyle(hipsDataset);
            data = new LineData(hipsDataset);

        } else if (i == 2) {
            float leftarm, rightarm;
            List<Entry> leftArmEntries = new ArrayList<>();
            List<Entry> rightArmEntries = new ArrayList<>();
            for (UserMeasurementTable table : MeasurementList) {
                leftarm = Float.parseFloat(table.getLeftArm());
                rightarm = Float.parseFloat(table.getRightArm());
                rightArmEntries.add(new Entry(j, rightarm));
                leftArmEntries.add(new Entry(j++, leftarm));
            }
            LineDataSet leftArmDataset = new LineDataSet(leftArmEntries, getString(R.string.left_arm));
            LineDataSet rightArmDataset = new LineDataSet(rightArmEntries, getString(R.string.right_arm));


            dataSetStyle(leftArmDataset);
            dataSetStyle(rightArmDataset);

            leftArmDataset.setColor(Color.RED);
            rightArmDataset.setColor(Color.BLUE);

            data = new LineData();
            data.addDataSet(leftArmDataset);
            data.addDataSet(rightArmDataset);
        } else if (i == 3) {
            float waist;
            List<Entry> waistEntries = new ArrayList<>();
            for (UserMeasurementTable table:MeasurementList){
                waist = Float.parseFloat(table.getWaist());
                waistEntries.add(new Entry(j++,waist));
            }
            LineDataSet waistDataSet = new LineDataSet(waistEntries,getString(R.string.waist));
            dataSetStyle(waistDataSet);

            data = new LineData(waistDataSet);

        } else if (i == 4) {
            float rightCalf,rightThigh,leftCalf,leftThigh;
            List<Entry> rightCalfEntries =new ArrayList<>();
            List<Entry> rightThighEntries =new ArrayList<>();
            List<Entry> leftCalfEntries =new ArrayList<>();
            List<Entry> leftThighEntries =new ArrayList<>();
            for (UserMeasurementTable table:MeasurementList){
                rightCalf = Float.parseFloat(table.getRightCalf());
                rightThigh = Float.parseFloat(table.getRightThigh());
                leftCalf = Float.parseFloat(table.getLeftCalf());
                leftThigh = Float.parseFloat(table.getLeftThigh());
                rightCalfEntries.add(new Entry(j,rightCalf));
                rightThighEntries.add(new Entry(j,rightThigh));
                leftCalfEntries.add(new Entry(j,leftCalf));
                leftThighEntries.add(new Entry(j++,leftThigh));
            }
            LineDataSet rightCalfDataset = new LineDataSet(rightCalfEntries,getString(R.string.right_calf));
            rightCalfDataset.setColor(Color.BLUE);
            LineDataSet rightThighDataset = new LineDataSet(rightThighEntries,getString(R.string.right_thigh));
            rightThighDataset.setColor(Color.RED);
            LineDataSet leftCalfDataset = new LineDataSet(leftCalfEntries,getString(R.string.left_calf));
            leftCalfDataset.setColor(Color.YELLOW);
            LineDataSet leftThighDataset = new LineDataSet(leftThighEntries,getString(R.string.left_thigh));
            leftThighDataset.setColor(Color.LTGRAY);

            dataSetStyle(rightCalfDataset);dataSetStyle(rightThighDataset);
            dataSetStyle(leftCalfDataset);dataSetStyle(leftThighDataset);

            data = new LineData();
            data.addDataSet(rightCalfDataset);
            data.addDataSet(rightThighDataset);
            data.addDataSet(leftCalfDataset);
            data.addDataSet(leftThighDataset);

        }
    }

    private void dataSetStyle(LineDataSet set) {
        set.setValueTextSize(10);
        set.setFormSize(15);
        set.setForm(Legend.LegendForm.CIRCLE);
        set.setCircleRadius(10);
        set.setCircleHoleRadius(6);
        set.setLineWidth(5);
        set.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        set.setDrawValues(true);
        set.setHighlightEnabled(true);
        set.setHighLightColor(Color.RED);


    }

    private void chartSetStyle(LineChart chart){
        Legend legend = chart.getLegend();
        legend.setXEntrySpace(5f);
        legend.setWordWrapEnabled(true);
        legend.setTextSize(12f);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setXEntrySpace(10);

        XAxis xAxis = chart.getXAxis();
        xAxis.setTextSize(14);
        xAxis.setLabelCount(4);
        xAxis.setAxisMaximum(MeasurementList.size());
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawLabels(true);
        xAxis.setGranularity(1);
        xAxis.setYOffset(10);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(60);
        xAxis.setValueFormatter(formatter);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setTextSize(14);
        yAxis.setXOffset(15);

        ViewPortHandler handler = chart.getViewPortHandler();
        handler.setDragOffsetX(20);

        chart.setExtraTopOffset(10);
        chart.getAxisRight().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.setVisibleXRangeMaximum(5.1f);
        chart.setBackgroundColor(value.data);
    }



}