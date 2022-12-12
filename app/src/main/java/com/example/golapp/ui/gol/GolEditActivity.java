package com.example.golapp.ui.gol;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.golapp.R;
import com.example.golapp.api.RetrofitInstance;
import com.example.golapp.databinding.ActivityGolEditBinding;
import com.example.golapp.models.Gol;
import com.example.golapp.responses.BaseResponse;
import com.example.golapp.services.GolService;
import com.example.golapp.ui.auth.AuthActivity;
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

public class GolEditActivity extends AppCompatActivity {
    ActivityGolEditBinding binding;
    Gol gol;
    ValidationHelper validationHelper;
    GolService golService = RetrofitInstance.getRetrofitInstance().create(GolService.class);
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGolEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        validationHelper = new ValidationHelper(null, GolEditActivity.this, true);

        binding.imgGol.setOnClickListener(view -> ImagePicker.Companion.with(this)
                .crop()
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)
                .start(10));
        binding.btnCancelar.setOnClickListener(view -> finish());
        initGolFromActivity();

        binding.btnGuardar.setOnClickListener(view -> {
            if (!validateFields()) {
                return;
            }

            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            builder.addFormDataPart("name", String.valueOf(binding.txtLayoutNombre.getEditText().getText()));
            builder.addFormDataPart("chant", String.valueOf(binding.txtLayoutCanto.getEditText().getText()));
            builder.addFormDataPart("motto", String.valueOf(binding.txtLayoutLema.getEditText().getText()));
            builder.addFormDataPart("verse", String.valueOf(binding.txtLayoutVersiculo.getEditText().getText()));
            builder.addFormDataPart("_method", "PUT");

            if (uri != null) {
                File file = new File(uri.getPath());
                builder.addFormDataPart("photo", file.getName(), RequestBody.create(MultipartBody.FORM, file));
            }
            RequestBody requestBody = builder.build();
            LottieAlertDialog dialog = new LottieAlertDialog.Builder(GolEditActivity.this, DialogTypes.TYPE_LOADING)
                    .setTitle("En proceso")
                    .build();
            dialog.setCancelable(false);
            dialog.show();
            golService.update(gol.getId(), requestBody).enqueue(new Callback<BaseResponse<String>>() {
                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    dialog.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        Toasty.success(GolEditActivity.this, response.body().getMessage()).show();
                        startActivity(new Intent(GolEditActivity.this, AuthActivity.class));
                        finishAffinity();
                    } else {
                        Converter<ResponseBody, BaseResponse<String>> converter = RetrofitInstance.getRetrofitInstance().responseBodyConverter(BaseResponse.class, new Annotation[0]);
                        try {
                            BaseResponse<String> error = converter.convert(Objects.requireNonNull(response.errorBody()));
                            assert error != null;
                            Toasty.error(GolEditActivity.this, error.getMessage()).show();
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

    private void initGolFromActivity() {
        Intent i = getIntent();
        gol = (Gol) i.getSerializableExtra("gol");
        if (!gol.getPhoto().equals("")) {
            Glide.with(this)
                    .load(gol.getPhoto())
                    .fitCenter()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.img_placeholder)
                    .into(binding.imgGol);
        }
        binding.txtLayoutNombre.getEditText().setText(gol.getName());
        binding.txtLayoutCanto.getEditText().setText(gol.getChant());
        binding.txtLayoutLema.getEditText().setText(gol.getMotto());
        binding.txtLayoutVersiculo.getEditText().setText(gol.getVerse());

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
}