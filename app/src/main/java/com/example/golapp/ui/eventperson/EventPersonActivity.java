package com.example.golapp.ui.eventperson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.golapp.adapters.event.EventListAdapter;
import com.example.golapp.adapters.eventperson.EventPersonListAdapter;
import com.example.golapp.adapters.eventperson.OnEventPersonClick;
import com.example.golapp.adapters.topic.OnDeleteClick;
import com.example.golapp.api.RetrofitInstance;
import com.example.golapp.databinding.ActivityEventIndexBinding;
import com.example.golapp.databinding.ActivityEventPersonBinding;
import com.example.golapp.models.Event;
import com.example.golapp.models.Person;
import com.example.golapp.requests.eventperson.EventPersonUpdateRequest;
import com.example.golapp.responses.BaseResponse;
import com.example.golapp.responses.CollectionResponse;
import com.example.golapp.services.EventPersonService;
import com.example.golapp.services.EventService;
import com.example.golapp.ui.event.EventIndexActivity;
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

public class EventPersonActivity extends AppCompatActivity implements OnEventPersonClick {
    ActivityEventPersonBinding binding;
    EventPersonService eventPersonService = RetrofitInstance.getRetrofitInstance().create(EventPersonService.class);
    EventPersonListAdapter eventListAdapter;
    private final List<Person> people = new ArrayList<>();
    LottieAlertDialog dialog;
    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventPersonBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        eventListAdapter = new EventPersonListAdapter(people, this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(eventListAdapter);
    }

    @Override
    protected void onResume() {
        initPersonEventList();
        super.onResume();
    }

    private void initPersonEventList() {
        binding.emptyList.setVisibility(View.GONE);
        binding.recyclerView.setVisibility(View.GONE);
        binding.loadingView.setVisibility(View.VISIBLE);
        eventPersonService.index().enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                binding.loadingView.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    System.out.println(response.body());
                    event = response.body();
                    if (response.body().getPeople().isEmpty()) {
                        System.out.println(response.body().getPeople().get(1).getNames());
                        binding.emptyList.setVisibility(View.VISIBLE);
                        binding.recyclerView.setVisibility(View.GONE);
                    } else {
                        people.clear();
                        people.addAll(response.body().getPeople());
                        eventListAdapter.setItems(people);
                        eventListAdapter.notifyDataSetChanged();
                        binding.emptyList.setVisibility(View.GONE);
                        binding.recyclerView.setVisibility(View.VISIBLE);
                    }
                } else {
                    Converter<ResponseBody, BaseResponse<String>> converter = RetrofitInstance.getRetrofitInstance().responseBodyConverter(BaseResponse.class, new Annotation[0]);
                    try {
                        BaseResponse<String> error = converter.convert(Objects.requireNonNull(response.errorBody()));
                        assert error != null;
                        Toasty.error(EventPersonActivity.this, error.getMessage()).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }


    @Override
    public void onPresentClick(Integer personId, Boolean isPresent) {
        if (event != null) {
            LottieAlertDialog dialog =  new LottieAlertDialog.Builder(this, DialogTypes.TYPE_LOADING)
                    .setTitle("En proceso").build();
            dialog.show();
            eventPersonService.update(event.getId(), new EventPersonUpdateRequest(personId, isPresent)).enqueue(new Callback<BaseResponse<String>>() {
                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    dialog.dismiss();
                    if (response.isSuccessful() && response.body() !=null){
                        Toasty.success(EventPersonActivity.this,response.body().getMessage()).show();
                        initPersonEventList();
                    } else {
                        Converter<ResponseBody, BaseResponse<String>> converter = RetrofitInstance.getRetrofitInstance().responseBodyConverter(BaseResponse.class, new Annotation[0]);
                        try {
                            BaseResponse<String> error = converter.convert(Objects.requireNonNull(response.errorBody()));
                            assert error != null;
                            Toasty.error(EventPersonActivity.this, error.getMessage()).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                    dialog.dismiss();
                    System.out.println(t.getMessage());
                }
            });
        }else{
            Toasty.error(this,"No hay evento").show();
        }
    }
}