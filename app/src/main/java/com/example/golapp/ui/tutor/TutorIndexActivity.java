package com.example.golapp.ui.tutor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.golapp.adapters.tutor.TutorListAdapter;
import com.example.golapp.api.RetrofitInstance;
import com.example.golapp.databinding.ActivityTutorIndexBinding;
import com.example.golapp.models.Tutor;
import com.example.golapp.responses.BaseResponse;
import com.example.golapp.responses.CollectionResponse;
import com.example.golapp.services.TutorService;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class TutorIndexActivity extends AppCompatActivity {
    ActivityTutorIndexBinding binding;
    TutorService tutorService = RetrofitInstance.getRetrofitInstance().create(TutorService.class);
    TutorListAdapter tutorListAdapter;
    private List<Tutor> tutorList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTutorIndexBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnAddTutor.setOnClickListener(view -> {
            startActivity(new Intent(TutorIndexActivity.this, TutorCreateActivity.class));
            Animatoo.animateZoom(this);
        });
        tutorListAdapter = new TutorListAdapter(tutorList,TutorIndexActivity.this);
//        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(tutorListAdapter);
    }

    @Override
    protected void onResume() {
        initTutorsList();
        super.onResume();
    }

    private void initTutorsList() {
        binding.emptyList.setVisibility(View.GONE);
        binding.recyclerView.setVisibility(View.GONE);
        binding.loadingView.setVisibility(View.VISIBLE);
        tutorService.index().enqueue(new Callback<BaseResponse<CollectionResponse<Tutor>>>() {
            @Override
            public void onResponse(Call<BaseResponse<CollectionResponse<Tutor>>> call, Response<BaseResponse<CollectionResponse<Tutor>>> response) {
                binding.loadingView.setVisibility(View.GONE);
                if (response.isSuccessful()){
                    if (response.body().getResult().getData().isEmpty()){
                        binding.emptyList.setVisibility(View.VISIBLE);
                        binding.recyclerView.setVisibility(View.GONE);
                    }else{
                        tutorList.clear();
                        tutorList.addAll(response.body().getResult().getData());
                        tutorListAdapter.setItems(tutorList);
                        tutorListAdapter.notifyDataSetChanged();
                        binding.emptyList.setVisibility(View.GONE);
                        binding.recyclerView.setVisibility(View.VISIBLE);
                    }
                } else {
                    Converter<ResponseBody, BaseResponse<String>> converter = RetrofitInstance.getRetrofitInstance().responseBodyConverter(BaseResponse.class, new Annotation[0]);
                    try {
                        BaseResponse<String> error = converter.convert(Objects.requireNonNull(response.errorBody()));
                        assert error != null;
                        Toasty.error(TutorIndexActivity.this, error.getMessage()).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<BaseResponse<CollectionResponse<Tutor>>> call, Throwable t) {
                System.err.println(t);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateFade(this);
    }
}