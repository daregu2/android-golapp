package com.example.golapp.ui.gol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.golapp.adapters.school.SchoolListAdapter;
import com.example.golapp.api.RetrofitInstance;
import com.example.golapp.databinding.ActivityGolIndexBinding;
import com.example.golapp.models.School;
import com.example.golapp.responses.BaseResponse;
import com.example.golapp.responses.CollectionResponse;
import com.example.golapp.services.SchoolService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GolIndexActivity extends AppCompatActivity {

    ActivityGolIndexBinding binding;
    SchoolService schoolService = RetrofitInstance.getRetrofitInstance().create(SchoolService.class);
    SchoolListAdapter schoolListAdapter;
    private final List<School> schoolList  = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGolIndexBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        schoolListAdapter = new SchoolListAdapter(schoolList);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(schoolListAdapter);
    }

    @Override
    protected void onResume() {
        binding.emptyList.setVisibility(View.GONE);
        binding.recyclerView.setVisibility(View.GONE);
        binding.loadingView.setVisibility(View.VISIBLE);
        initSchoolsList();
        super.onResume();
    }

    private void initSchoolsList() {
        schoolService.fetchSchools().enqueue(new Callback<BaseResponse<CollectionResponse<School>>>() {
            @Override
            public void onResponse(Call<BaseResponse<CollectionResponse<School>>> call, Response<BaseResponse<CollectionResponse<School>>> response) {
                binding.loadingView.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().getResult().getData().isEmpty()) {
                        binding.emptyList.setVisibility(View.VISIBLE);
                        binding.recyclerView.setVisibility(View.GONE);
                    } else {
                        schoolList.clear();
                        schoolList.addAll(response.body().getResult().getData());
                        schoolListAdapter.setItems(schoolList);
                        schoolListAdapter.notifyDataSetChanged();
                        binding.emptyList.setVisibility(View.GONE);
                        binding.recyclerView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<CollectionResponse<School>>> call, Throwable t) {

            }
        });
    }
}