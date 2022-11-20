package com.example.golapp.ui.tutor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.golapp.adapters.tutor.TutorListAdapter;
import com.example.golapp.api.RetrofitInstance;
import com.example.golapp.databinding.ActivityTutorIndexBinding;
import com.example.golapp.models.Tutor;
import com.example.golapp.responses.BaseResponse;
import com.example.golapp.responses.CollectionResponse;
import com.example.golapp.services.TutorService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
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
        });
        tutorListAdapter = new TutorListAdapter(tutorList,TutorIndexActivity.this);
//        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(tutorListAdapter);




    }

    @Override
    protected void onResume() {
        binding.emptyList.setVisibility(View.GONE);
        binding.recyclerView.setVisibility(View.GONE);
        binding.loadingView.setVisibility(View.VISIBLE);
        initTutorsList();
        super.onResume();
    }

    private void initTutorsList() {
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
                }

            }

            @Override
            public void onFailure(Call<BaseResponse<CollectionResponse<Tutor>>> call, Throwable t) {

            }
        });
    }
}