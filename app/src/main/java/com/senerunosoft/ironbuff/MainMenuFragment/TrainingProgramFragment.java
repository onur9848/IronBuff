package com.senerunosoft.ironbuff.MainMenuFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.senerunosoft.ironbuff.R;
import com.senerunosoft.ironbuff.databinding.FragmentTrainingProgramBinding;
import com.senerunosoft.ironbuff.table.UserTable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class TrainingProgramFragment extends Fragment {

    FragmentTrainingProgramBinding binding;
    UserTable user = new UserTable();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTrainingProgramBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listview = binding.traningIncludeView.trainingListView;
        ArrayList<ListItemTable> program = new ArrayList<>();
        program.add(new ListItemTable("Gögüs", "3 egz"));
        program.add(new ListItemTable("Sırt", "3 egz"));
        program.add(new ListItemTable("Bacak", "2 egz"));
        ProgramAdapter adapter = new ProgramAdapter(getContext(), program);

        binding.traningIncludeView.trainingListView.setEnabled(false);
        binding.traningIncludeView.trainingListView.setAdapter(adapter);
        binding.traningIncludeView.myCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "position" + view.getTranslationX()+"-" + view.getTranslationY(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void defineVariable() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}

class ListItemTable {
    String EgzersizBolge;
    String EgzersizSayi;

    public ListItemTable(String egzersizBolge, String egzersizSayi) {
        EgzersizBolge = egzersizBolge;
        EgzersizSayi = egzersizSayi;
    }

    public String getEgzersizBolge() {
        return EgzersizBolge;
    }

    public void setEgzersizBolge(String egzersizBolge) {
        EgzersizBolge = egzersizBolge;
    }

    public String getEgzersizSayi() {
        return EgzersizSayi;
    }

    public void setEgzersizSayi(String egzersizSayi) {
        EgzersizSayi = egzersizSayi;
    }
}

class ProgramAdapter extends ArrayAdapter<ListItemTable> {
    public ProgramAdapter(Context context, ArrayList<ListItemTable> itemTables) {
        super(context, 0, itemTables);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItemTable itemTable = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_program, parent, false);
        }
        TextView exercise = convertView.findViewById(R.id.exercise);
        TextView exerciseCount = convertView.findViewById(R.id.exercise_count);

        exercise.setText(itemTable.EgzersizBolge);
        exerciseCount.setText(itemTable.EgzersizSayi);


        return convertView;
    }

}