package com.example.golapp.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.golapp.api.RetrofitInstance;
import com.example.golapp.databinding.FragmentTutorMainBinding;
import com.example.golapp.models.Event;
import com.example.golapp.models.UserDetail;
import com.example.golapp.responses.BaseResponse;
import com.example.golapp.responses.CollectionResponse;
import com.example.golapp.services.EventPersonService;
import com.example.golapp.ui.event.EventIndexActivity;
import com.example.golapp.ui.eventperson.EventPersonActivity;
import com.example.golapp.ui.gol.GolEditActivity;
import com.example.golapp.ui.student.StudentIndexActivity;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class TutorMainFragment extends Fragment {
    FragmentTutorMainBinding binding;
    LottieAlertDialog dialog;
    EventPersonService eventPersonService = RetrofitInstance.getRetrofitInstance().create(EventPersonService.class);

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
        binding = FragmentTutorMainBinding.inflate(inflater, container, false);
//        binding.cv.setOnClickListener(view -> startActivity(new Intent(requireContext(), TutorIndexActivity.class)));
        binding.cvGestionarGol.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(),GolEditActivity.class);
            intent.putExtra("gol",user.getPerson().getCycle().getGol());
            startActivity(intent);
        });
        binding.cvGestionarAlumnos.setOnClickListener(view -> startActivity(new Intent(requireContext(), StudentIndexActivity.class)));
        binding.cvGestionarEventos.setOnClickListener(view -> startActivity(new Intent(requireContext(), EventIndexActivity.class)));
        binding.cvRegistrarAsistencia.setOnClickListener(view -> startActivity(new Intent(requireContext(), EventPersonActivity.class)));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}