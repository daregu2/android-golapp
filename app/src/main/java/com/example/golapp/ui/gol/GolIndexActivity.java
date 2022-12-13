package com.example.golapp.ui.gol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.golapp.adapters.school.SchoolListAdapter;
import com.example.golapp.adapters.topic.OnDeleteClick;
import com.example.golapp.api.RetrofitInstance;
import com.example.golapp.databinding.ActivityGolIndexBinding;
import com.example.golapp.models.School;
import com.example.golapp.responses.BaseResponse;
import com.example.golapp.responses.CollectionResponse;
import com.example.golapp.services.GolService;
import com.example.golapp.services.SchoolService;
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

public class GolIndexActivity extends AppCompatActivity implements OnDeleteClick {

    ActivityGolIndexBinding binding;
    SchoolService schoolService = RetrofitInstance.getRetrofitInstance().create(SchoolService.class);
    GolService golService = RetrofitInstance.getRetrofitInstance().create(GolService.class);
    SchoolListAdapter schoolListAdapter;
    private final List<School> schoolList = new ArrayList<>();
    LottieAlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGolIndexBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        schoolListAdapter = new SchoolListAdapter(schoolList, this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(schoolListAdapter);
    }

    @Override
    protected void onResume() {
        initSchoolsList();
        super.onResume();
    }

    private void initSchoolsList() {
        binding.emptyList.setVisibility(View.GONE);
        binding.recyclerView.setVisibility(View.GONE);
        binding.loadingView.setVisibility(View.VISIBLE);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateFade(this);
    }


    public void onDeleteTopicClick(Integer golId) {
        dialog = new LottieAlertDialog.Builder(this, DialogTypes.TYPE_WARNING)
                .setTitle("¿Está seguro de eliminar este registro?")
                .setDescription("No podra deshacer los cambios luego...")
                .setPositiveText("Confirmar")
                .setPositiveListener(lottieAlertDialog -> {
                    dialog.changeDialog(new LottieAlertDialog.Builder(this, DialogTypes.TYPE_LOADING)
                            .setTitle("En proceso")
                    );
                    golService.delete(golId).enqueue(new Callback<BaseResponse<String>>() {
                        @Override
                        public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                Toasty.success(GolIndexActivity.this, response.body().getMessage()).show();
                                initSchoolsList();

                            } else {
                                Converter<ResponseBody, BaseResponse<String>> converter = RetrofitInstance.getRetrofitInstance().responseBodyConverter(BaseResponse.class, new Annotation[0]);
                                try {
                                    BaseResponse<String> error = converter.convert(Objects.requireNonNull(response.errorBody()));
                                    assert error != null;
                                    Toasty.error(GolIndexActivity.this, error.getMessage()).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            dialog.dismiss();
                        }


                        @Override
                        public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                            dialog.dismiss();
                        }
                    });


                })
                .build();
        dialog.setCancelable(true);
        dialog.show();
    }
}