package com.senerunosoft.ironbuff.MainMenuFragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.View;
import com.senerunosoft.ironbuff.databinding.FragmentMessageBinding;
import org.jetbrains.annotations.NotNull;


public class MessageFragment extends Fragment {

    FragmentMessageBinding binding;




    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}