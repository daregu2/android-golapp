package com.example.golapp.ui.gol;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.golapp.api.RetrofitInstance;
import com.example.golapp.databinding.ActivityGolCreateBinding;
import com.example.golapp.responses.BaseResponse;
import com.example.golapp.services.GolService;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.master.validationhelper.ValidationHelper;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class GolCreateActivity extends AppCompatActivity {
    ActivityGolCreateBinding binding;
    GolService golService = RetrofitInstance.getRetrofitInstance().create(GolService.class);
    Integer cycleId;
    ValidationHelper validationHelper;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGolCreateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent i = getIntent();
        cycleId = (Integer) i.getSerializableExtra("cycleId");

        validationHelper = new ValidationHelper(null, GolCreateActivity.this, true);
        binding.btnCancelar.setOnClickListener(view -> {
            finish();
            Animatoo.animateFade(this);
        });
        binding.imgGol.setOnClickListener(view -> ImagePicker.Companion.with(this)
                .crop()
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)
                .start(10));

        binding.btnGuardar.setOnClickListener(view -> {
            if (!validateFields()) {
                return;
            }
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            builder.addFormDataPart("name", String.valueOf(binding.txtLayoutNombre.getEditText().getText()));
            builder.addFormDataPart("chant", String.valueOf(binding.txtLayoutCanto.getEditText().getText()));
            builder.addFormDataPart("motto", String.valueOf(binding.txtLayoutLema.getEditText().getText()));
            builder.addFormDataPart("verse", String.valueOf(binding.txtLayoutVersiculo.getEditText().getText()));
            if (cycleId != null) {
                builder.addFormDataPart("cycle_id", String.valueOf(cycleId));
            }
            if (uri != null) {
                File file = new File(uri.getPath());
                builder.addFormDataPart("photo", file.getName(), RequestBody.create(MultipartBody.FORM, file));
            }
            RequestBody requestBody = builder.build();
            LottieAlertDialog dialog = new LottieAlertDialog.Builder(GolCreateActivity.this, DialogTypes.TYPE_LOADING)
                    .setTitle("En proceso")
                    .build();
            dialog.setCancelable(false);
            dialog.show();

            golService.store(requestBody).enqueue(new Callback<BaseResponse<String>>() {
                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    dialog.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        Toasty.success(GolCreateActivity.this, response.body().getMessage()).show();
                        finish();
                        Animatoo.animateWindmill(GolCreateActivity.this);
                    } else {
                        Converter<ResponseBody, BaseResponse<String>> converter = RetrofitInstance.getRetrofitInstance().responseBodyConverter(BaseResponse.class, new Annotation[0]);
                        try {
                            BaseResponse<String> error = converter.convert(Objects.requireNonNull(response.errorBody()));
                            assert error != null;
                            Toasty.error(GolCreateActivity.this, error.getMessage()).show();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && data != null) {
            uri = data.getData();
            binding.imgGol.setImageURI(uri);
        }
    }

    public boolean validateFields() {
        validationHelper.addRequiredValidation(binding.txtLayoutNombre, "Este campo es requerido.");
        validationHelper.addRequiredValidation(binding.txtLayoutCanto, "Este campo es requerido.");
        validationHelper.addRequiredValidation(binding.txtLayoutLema, "Este campo es requerido.");
        validationHelper.addRequiredValidation(binding.txtLayoutVersiculo, "Este campo es requerido.");
        return validationHelper.validateAll();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateFade(this);
    }
}