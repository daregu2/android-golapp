package com.example.golapp.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.golapp.databinding.FragmentLiderMainBinding;
import com.example.golapp.databinding.LayoutTopicDialogBinding;
import com.example.golapp.models.UserDetail;
import com.example.golapp.ui.event.EventEditActivity;
import com.example.golapp.ui.event.EventIndexActivity;
import com.example.golapp.ui.eventperson.EventPersonActivity;
import com.example.golapp.ui.gol.GolEditActivity;
import com.example.golapp.ui.student.StudentIndexActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;


import es.dmoral.toasty.Toasty;

public class LiderMainFragment extends Fragment {
    FragmentLiderMainBinding binding;
    LottieAlertDialog dialog;

    public LiderMainFragment() {
        // Required empty public constructor
    }

    public static LiderMainFragment newInstance(String param1, String param2) {
        return new LiderMainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        UserDetail user = (UserDetail) getArguments().getSerializable("user");
        binding = FragmentLiderMainBinding.inflate(inflater, container, false);

        binding.cvGestionarGol.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(), GolEditActivity.class);
            intent.putExtra("gol", user.getPerson().getCycle().getGol());
            startActivity(intent);
            Animatoo.animateSlideUp(requireContext());
        });
        binding.cvGestionarAlumnos.setOnClickListener(view -> {
            if (user.getEvent() == null) {
                Toasty.error(requireContext(), "No hay evento.").show();
            } else {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
                LayoutTopicDialogBinding bin = LayoutTopicDialogBinding.inflate(getLayoutInflater());
                bottomSheetDialog.setContentView(bin.getRoot());
                bin.txtTopicName.setText(user.getEvent().getTopic().getName());
                bin.txtTopicLink.setText(user.getEvent().getTopic().getResource_link());
                bottomSheetDialog.show();
            }
        });
        binding.cvGestionarEventos.setOnClickListener(view -> {
            if (user.getEvent() == null) {
                Toasty.error(requireContext(), "No hay evento.").show();
            } else {
                Intent intent = new Intent(requireContext(), EventEditActivity.class);
                intent.putExtra("event", user.getEvent());
                startActivity(intent);
                Animatoo.animateSlideUp(requireContext());
            }
        });
        binding.cvRegistrarAsistencia.setOnClickListener(view -> {
            if (user.getEvent() == null) {
                Toasty.error(requireContext(), "No hay evento o el evento ha finalizado :(").show();
            } else {
                startActivity(new Intent(requireContext(), EventPersonActivity.class));
                Animatoo.animateSlideUp(requireContext());
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