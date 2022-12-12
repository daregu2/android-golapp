package com.example.golapp.ui.event;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.golapp.adapters.event.EventListAdapter;
import com.example.golapp.adapters.topic.OnDeleteClick;
import com.example.golapp.api.RetrofitInstance;
import com.example.golapp.databinding.ActivityEventIndexBinding;
import com.example.golapp.models.Event;
import com.example.golapp.responses.BaseResponse;
import com.example.golapp.responses.CollectionResponse;
import com.example.golapp.services.EventService;
import com.example.golapp.ui.auth.AuthActivity;
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

public class EventIndexActivity extends AppCompatActivity implements OnDeleteClick {
    ActivityEventIndexBinding binding;
    EventService eventService = RetrofitInstance.getRetrofitInstance().create(EventService.class);
    EventListAdapter eventListAdapter;
    private final List<Event> eventList = new ArrayList<>();
    LottieAlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventIndexBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        binding.btnAdd.setOnClickListener(view -> startActivity(new Intent(EventIndexActivity.this, EventCreateActivity.class)));
        eventListAdapter = new EventListAdapter(eventList,this);
//        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(eventListAdapter);

        binding.btnAdd.setOnClickListener(view -> startActivity(new Intent(this, EventCreateActivity.class)));

    }

    @Override
    protected void onResume() {
        initEventsList();
        super.onResume();
    }

    private void initEventsList() {
        binding.emptyList.setVisibility(View.GONE);
        binding.recyclerView.setVisibility(View.GONE);
        binding.loadingView.setVisibility(View.VISIBLE);
        eventService.index().enqueue(new Callback<BaseResponse<CollectionResponse<Event>>>() {
            @Override
            public void onResponse(Call<BaseResponse<CollectionResponse<Event>>> call, Response<BaseResponse<CollectionResponse<Event>>> response) {
                binding.loadingView.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getResult().getData().isEmpty()) {
                        binding.emptyList.setVisibility(View.VISIBLE);
                        binding.recyclerView.setVisibility(View.GONE);
                    } else {
                        eventList.clear();
                        eventList.addAll(response.body().getResult().getData());
                        eventListAdapter.setItems(eventList);
                        eventListAdapter.notifyDataSetChanged();
                        binding.emptyList.setVisibility(View.GONE);
                        binding.recyclerView.setVisibility(View.VISIBLE);
                    }
                } else {
                    Converter<ResponseBody, BaseResponse<String>> converter = RetrofitInstance.getRetrofitInstance().responseBodyConverter(BaseResponse.class, new Annotation[0]);
                    try {
                        BaseResponse<String> error = converter.convert(Objects.requireNonNull(response.errorBody()));
                        assert error != null;
                        Toasty.error(EventIndexActivity.this, error.getMessage()).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<BaseResponse<CollectionResponse<Event>>> call, Throwable t) {

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
                    dialog.changeDialog(new LottieAlertDialog.Builder(this, DialogTypes.TYPE_LOADING)
                            .setTitle("En proceso")
                    );
                    eventService.delete(id).enqueue(new Callback<BaseResponse<String>>() {
                        @Override
                        public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                            if (response.isSuccessful() && response.body() !=null) {
                                Toasty.success(EventIndexActivity.this, response.body().getMessage()).show();
                                startActivity(new Intent(EventIndexActivity.this, AuthActivity.class));
                                finishAffinity();

                            } else {
                                Converter<ResponseBody, BaseResponse<String>> converter = RetrofitInstance.getRetrofitInstance().responseBodyConverter(BaseResponse.class, new Annotation[0]);
                                try {
                                    BaseResponse<String> error = converter.convert(Objects.requireNonNull(response.errorBody()));
                                    assert error != null;
                                    Toasty.error(EventIndexActivity.this, error.getMessage()).show();
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