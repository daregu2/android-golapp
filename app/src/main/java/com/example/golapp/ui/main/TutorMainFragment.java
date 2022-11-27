package com.example.golapp.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.golapp.databinding.FragmentTutorMainBinding;
import com.example.golapp.models.UserDetail;
import com.example.golapp.ui.gol.GolEditActivity;
import com.example.golapp.ui.student.StudentIndexActivity;

import es.dmoral.toasty.Toasty;


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
        UserDetail user = (UserDetail) getArguments().getSerializable("user");
        Toasty.success(requireContext(),user.getPerson().getCycle().getGol().getName()).show();
        binding = FragmentTutorMainBinding.inflate(inflater, container, false);
//        binding.cv.setOnClickListener(view -> startActivity(new Intent(requireContext(), TutorIndexActivity.class)));
        binding.cvGestionarGol.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(),GolEditActivity.class);
            intent.putExtra("gol",user.getPerson().getCycle().getGol());
            startActivity(intent);
        });
        binding.cvGestionarAlumnos.setOnClickListener(view -> startActivity(new Intent(requireContext(), StudentIndexActivity.class)));
//        binding.cvGestionarTemas.setOnClickListener(view -> startActivity(new Intent(requireContext(), TopicIndexActivity.class)));
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}