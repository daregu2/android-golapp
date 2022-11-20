package com.example.golapp.ui.gol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.golapp.adapters.gol.GolListAdapter;
import com.example.golapp.api.RetrofitInstance;
import com.example.golapp.databinding.ActivityGolIndexBinding;
import com.example.golapp.responses.BaseResponse;
import com.example.golapp.responses.CollectionResponse;
import com.example.golapp.responses.GolResponse;
import com.example.golapp.services.GolService;
import com.example.golapp.ui.tutor.TutorCreateActivity;
import com.example.golapp.ui.tutor.TutorIndexActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GolIndexActivity extends AppCompatActivity {

    ActivityGolIndexBinding binding;
    GolService golService = RetrofitInstance.getRetrofitInstance().create(GolService.class);
    GolListAdapter golListAdapter;
    private final List<GolResponse> golList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGolIndexBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnAdd.setOnClickListener(view -> {
            startActivity(new Intent(this, GolCreateActivity.class));
        });

        golListAdapter = new GolListAdapter(golList, GolIndexActivity.this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(golListAdapter);
    }

    @Override
    protected void onResume() {
        binding.emptyList.setVisibility(View.GONE);
        binding.recyclerView.setVisibility(View.GONE);
        binding.loadingView.setVisibility(View.VISIBLE);
        initGolsList();
        super.onResume();
    }

    private void initGolsList() {
        golService.index().enqueue(new Callback<BaseResponse<CollectionResponse<GolResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponse<CollectionResponse<GolResponse>>> call, Response<BaseResponse<CollectionResponse<GolResponse>>> response) {
                binding.loadingView.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().getResult().getData().isEmpty()) {
                        binding.emptyList.setVisibility(View.VISIBLE);
                        binding.recyclerView.setVisibility(View.GONE);
                    } else {
                        golList.clear();
                        golList.addAll(response.body().getResult().getData());
                        golListAdapter.setItems(golList);
                        golListAdapter.notifyDataSetChanged();
                        binding.emptyList.setVisibility(View.GONE);
                        binding.recyclerView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<CollectionResponse<GolResponse>>> call, Throwable t) {
                System.out.println(t);
            }
        });
    }
}