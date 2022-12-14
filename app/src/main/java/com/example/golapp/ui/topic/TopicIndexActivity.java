package com.example.golapp.ui.topic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.golapp.adapters.topic.OnDeleteClick;
import com.example.golapp.adapters.week.WeekListAdapter;
import com.example.golapp.api.RetrofitInstance;
import com.example.golapp.databinding.ActivityTopicIndexBinding;
import com.example.golapp.models.Week;
import com.example.golapp.responses.BaseResponse;
import com.example.golapp.responses.CollectionResponse;
import com.example.golapp.services.TopicService;
import com.example.golapp.services.WeekService;
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

public class TopicIndexActivity extends AppCompatActivity implements OnDeleteClick {
    ActivityTopicIndexBinding binding;
    WeekService weekService = RetrofitInstance.getRetrofitInstance().create(WeekService.class);
    TopicService topicService = RetrofitInstance.getRetrofitInstance().create(TopicService.class);
    WeekListAdapter weekListAdapter;
    private List<Week> weekList = new ArrayList<>();
    LottieAlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTopicIndexBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnAdd.setOnClickListener(view -> {
            LottieAlertDialog dialog =  new LottieAlertDialog.Builder(TopicIndexActivity.this, DialogTypes.TYPE_LOADING)
                    .setTitle("En proceso").build();
            dialog.show();
            weekService.store().enqueue(new Callback<BaseResponse<String>>() {
                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    dialog.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        Toasty.success(TopicIndexActivity.this, response.body().getMessage()).show();
                        //TODO: XDXD
                        initWeeksList();
                    } else {
                        Converter<ResponseBody, BaseResponse<String>> converter = RetrofitInstance.getRetrofitInstance().responseBodyConverter(BaseResponse.class, new Annotation[0]);
                        try {
                            BaseResponse<String> error = converter.convert(Objects.requireNonNull(response.errorBody()));
                            assert error != null;
                            Toasty.error(TopicIndexActivity.this, error.getMessage()).show();
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
        });
    }
    @Override
    protected void onResume() {
        weekListAdapter = new WeekListAdapter(weekList, this);
//        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(weekListAdapter);

        initWeeksList();
        super.onResume();
    }

    private void initWeeksList() {
        binding.emptyList.setVisibility(View.GONE);
        binding.recyclerView.setVisibility(View.GONE);
        binding.loadingView.setVisibility(View.VISIBLE);
        weekService.index().enqueue(new Callback<BaseResponse<CollectionResponse<Week>>>() {
            @Override
            public void onResponse(Call<BaseResponse<CollectionResponse<Week>>> call, Response<BaseResponse<CollectionResponse<Week>>> response) {
                binding.loadingView.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null){
                    if (response.body().getResult().getData().isEmpty()){
                        binding.emptyList.setVisibility(View.VISIBLE);
                        binding.recyclerView.setVisibility(View.GONE);
                    }else{
                        weekList.clear();
                        weekList.addAll(response.body().getResult().getData());
                        weekListAdapter.setItems(weekList);
                        weekListAdapter.notifyDataSetChanged();
                        binding.emptyList.setVisibility(View.GONE);
                        binding.recyclerView.setVisibility(View.VISIBLE);
                    }
                } else {
                    Converter<ResponseBody, BaseResponse<String>> converter = RetrofitInstance.getRetrofitInstance().responseBodyConverter(BaseResponse.class, new Annotation[0]);
                    try {
                        BaseResponse<String> error = converter.convert(Objects.requireNonNull(response.errorBody()));
                        assert error != null;
                        Toasty.error(TopicIndexActivity.this, error.getMessage()).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<CollectionResponse<Week>>> call, Throwable t) {

            }
        });
    }


    @Override
    public void onDeleteTopicClick(Integer id) {
        dialog = new LottieAlertDialog.Builder(this, DialogTypes.TYPE_WARNING)
                .setTitle("¿Está seguro de eliminar este registro?")
                .setDescription("No podra deshacer los cambios luego...")
                .setPositiveText("Confirmar")
                .setPositiveListener(lottieAlertDialog -> {
                    dialog.changeDialog(new LottieAlertDialog.Builder(TopicIndexActivity.this, DialogTypes.TYPE_LOADING)
                            .setTitle("En proceso")
                    );
                    topicService.delete(id).enqueue(new Callback<BaseResponse<String>>() {
                        @Override
                        public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                            if (response.isSuccessful() && response.body() !=null) {
                                Toasty.success(TopicIndexActivity.this, response.body().getMessage()).show();
                                initWeeksList();

                            } else {
                                Converter<ResponseBody, BaseResponse<String>> converter = RetrofitInstance.getRetrofitInstance().responseBodyConverter(BaseResponse.class, new Annotation[0]);
                                try {
                                    BaseResponse<String> error = converter.convert(Objects.requireNonNull(response.errorBody()));
                                    assert error != null;
                                    Toasty.error(TopicIndexActivity.this, error.getMessage()).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateFade(this);
    }
}