package com.senerunosoft.ironbuff.AdminPage;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.senerunosoft.ironbuff.R;
import com.senerunosoft.ironbuff.databinding.FragmentAdminUserAddProgramBinding;
import org.jetbrains.annotations.NotNull;

import java.util.zip.Inflater;

public class AdminUserAddProgramFragment extends Fragment {

    FragmentAdminUserAddProgramBinding binding;
    private String DOC_ID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdminUserAddProgramBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DOC_ID = getArguments().getString("DocId");
        binding.denemeTaht.setText(DOC_ID);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}