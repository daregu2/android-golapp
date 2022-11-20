package com.example.golapp.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.golapp.R;
import com.example.golapp.databinding.FragmentAdminMainBinding;
import com.example.golapp.databinding.FragmentLoginBinding;
import com.example.golapp.ui.gol.GolIndexActivity;
import com.example.golapp.ui.tutor.TutorIndexActivity;


public class AdminMainFragment extends Fragment {
    FragmentAdminMainBinding binding;

    public AdminMainFragment() {
        // Required empty public constructor
    }

    public static AdminMainFragment newInstance(String param1, String param2) {
        return new AdminMainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdminMainBinding.inflate(inflater, container, false);
        binding.cvGestionarTutores.setOnClickListener(view -> {
            startActivity(new Intent(requireContext(), TutorIndexActivity.class));
        });

        binding.cvGestionarGrupos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireContext(), GolIndexActivity.class));
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}