package com.senerunosoft.ironbuff.AdminPage;

import android.os.Binder;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.senerunosoft.ironbuff.R;
import com.senerunosoft.ironbuff.databinding.FragmentAdminUpdateUserprogramBinding;
import org.jetbrains.annotations.NotNull;


public class AdminUpdateUserprogramFragment extends Fragment {

    FragmentAdminUpdateUserprogramBinding binding;
    private String DOC_ID_USER;
    private String DOC_ID_TRAINING;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAdminUpdateUserprogramBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DOC_ID_USER = getArguments().getString("docIDUser");
        DOC_ID_TRAINING = getArguments().getString("docIDTraining");


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}