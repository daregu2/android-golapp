package com.example.golapp.ui.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.golapp.adapters.student.StudentListAdapter;
import com.example.golapp.adapters.topic.OnDeleteClick;
import com.example.golapp.api.RetrofitInstance;
import com.example.golapp.databinding.ActivityStudentIndexBinding;
import com.example.golapp.models.Student;
import com.example.golapp.responses.BaseResponse;
import com.example.golapp.responses.CollectionResponse;
import com.example.golapp.services.StudentService;
import com.example.golapp.ui.topic.TopicIndexActivity;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;

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

public class StudentIndexActivity extends AppCompatActivity implements OnDeleteClick {
    ActivityStudentIndexBinding binding;
    StudentService studentService = RetrofitInstance.getRetrofitInstance().create(StudentService.class);
    StudentListAdapter studentListAdapter;
    private final List<Student> studentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentIndexBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnAdd.setOnClickListener(view -> startActivity(new Intent(StudentIndexActivity.this, StudentCreateActivity.class)));
        studentListAdapter = new StudentListAdapter(studentList, this);
//        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(studentListAdapter);


    }

    @Override
    protected void onResume() {
        initStudentsList();
        super.onResume();
    }

    private void initStudentsList() {
        binding.emptyList.setVisibility(View.GONE);
        binding.recyclerView.setVisibility(View.GONE);
        binding.loadingView.setVisibility(View.VISIBLE);
        studentService.index().enqueue(new Callback<BaseResponse<CollectionResponse<Student>>>() {
            @Override
            public void onResponse(Call<BaseResponse<CollectionResponse<Student>>> call, Response<BaseResponse<CollectionResponse<Student>>> response) {
                binding.loadingView.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().getResult().getData().isEmpty()) {
                        binding.emptyList.setVisibility(View.VISIBLE);
                        binding.recyclerView.setVisibility(View.GONE);
                    } else {
                        studentList.clear();
                        studentList.addAll(response.body().getResult().getData());
                        studentListAdapter.setItems(studentList);
                        studentListAdapter.notifyDataSetChanged();
                        binding.emptyList.setVisibility(View.GONE);
                        binding.recyclerView.setVisibility(View.VISIBLE);
                    }
                } else {
                    Converter<ResponseBody, BaseResponse<String>> converter = RetrofitInstance.getRetrofitInstance().responseBodyConverter(BaseResponse.class, new Annotation[0]);
                    try {
                        BaseResponse<String> error = converter.convert(Objects.requireNonNull(response.errorBody()));
                        assert error != null;
                        Toasty.error(StudentIndexActivity.this, error.getMessage()).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<BaseResponse<CollectionResponse<Student>>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onDeleteTopicClick(Integer id) {
        LottieAlertDialog dialog =  new LottieAlertDialog.Builder(this, DialogTypes.TYPE_LOADING)
                .setTitle("En proceso").build();
        dialog.show();
        studentService.setLider(id).enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    Toasty.success(StudentIndexActivity.this, response.body().getMessage()).show();
                    //TODO: XDXD
                    initStudentsList();
                } else {
                    Converter<ResponseBody, BaseResponse<String>> converter = RetrofitInstance.getRetrofitInstance().responseBodyConverter(BaseResponse.class, new Annotation[0]);
                    try {
                        BaseResponse<String> error = converter.convert(Objects.requireNonNull(response.errorBody()));
                        assert error != null;
                        Toasty.error(StudentIndexActivity.this, error.getMessage()).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }
}