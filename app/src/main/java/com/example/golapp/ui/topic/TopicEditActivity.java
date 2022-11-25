package com.example.golapp.ui.topic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.golapp.api.RetrofitInstance;
import com.example.golapp.databinding.ActivityTopicEditBinding;
import com.example.golapp.models.Topic;
import com.example.golapp.requests.topic.TopicRequest;
import com.example.golapp.responses.BaseResponse;
import com.example.golapp.services.TopicService;
import com.master.validationhelper.ValidationHelper;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class TopicEditActivity extends AppCompatActivity {
    ActivityTopicEditBinding binding;
    ValidationHelper validationHelper;
    Topic topic;
    TopicService topicService = RetrofitInstance.getRetrofitInstance().create(TopicService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTopicEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnCancelar.setOnClickListener(view -> finish());
        setupEditableTopic();
        validationHelper = new ValidationHelper(null, this, true);

        binding.btnGuardar.setOnClickListener(view -> {
            if (!validateFields()) {
                return;
            }

            TopicRequest request = new TopicRequest();
            request.setName(String.valueOf(binding.txtLayoutNombre.getEditText().getText()));
            request.setDescription(String.valueOf(binding.txtLayoutDescripcion.getEditText().getText()));
            request.setResource_link(String.valueOf(binding.txtLayoutLink.getEditText().getText()));
            topicService.update(topic.getId(), request).enqueue(new Callback<BaseResponse<String>>() {
                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toasty.success(TopicEditActivity.this, response.body().getMessage()).show();
                        finish();
                    } else {
                        Converter<ResponseBody, BaseResponse<String>> converter = RetrofitInstance.getRetrofitInstance().responseBodyConverter(BaseResponse.class, new Annotation[0]);
                        try {
                            BaseResponse<String> error = converter.convert(Objects.requireNonNull(response.errorBody()));
                            assert error != null;
                            Toasty.error(TopicEditActivity.this, error.getMessage()).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                    Toasty.error(TopicEditActivity.this, "ERROR").show();
                }
            });
        });
    }

    public void setupEditableTopic() {
        Intent i = getIntent();
        topic = (Topic) i.getSerializableExtra("topic");
        binding.txtLayoutNombre.getEditText().setText(topic.getName());
        binding.txtLayoutDescripcion.getEditText().setText(topic.getDescription());
        binding.txtLayoutLink.getEditText().setText(topic.getResource_link());
    }

    public boolean validateFields() {
        validationHelper.addRequiredValidation(binding.txtLayoutNombre, "Este campo es requerido.");
        validationHelper.addRequiredValidation(binding.txtLayoutLink, "Este campo es requerido.");
        return validationHelper.validateAll();
    }
}