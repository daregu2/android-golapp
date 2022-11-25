package com.example.golapp.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.golapp.databinding.FragmentTutorMainBinding;


public class TutorMainFragment extends Fragment {
    FragmentTutorMainBinding binding;

    public TutorMainFragment() {
        // Required empty public constructor
    }

    public static TutorMainFragment newInstance(String param1, String param2) {
        return new TutorMainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTutorMainBinding.inflate(inflater, container, false);
//        binding.cv.setOnClickListener(view -> startActivity(new Intent(requireContext(), TutorIndexActivity.class)));
//        binding.cvGestionarGrupos.setOnClickListener(view -> startActivity(new Intent(requireContext(), GolIndexActivity.class)));
//        binding.cvGestionarTemas.setOnClickListener(view -> startActivity(new Intent(requireContext(), TopicIndexActivity.class)));
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}