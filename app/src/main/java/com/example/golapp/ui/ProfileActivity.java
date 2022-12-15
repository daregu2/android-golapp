package com.example.golapp.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.golapp.R;
import com.example.golapp.api.RetrofitInstance;
import com.example.golapp.databinding.ActivityProfileBinding;
import com.example.golapp.models.UserDetail;
import com.example.golapp.requests.ResetPasswordRequest;
import com.example.golapp.responses.BaseResponse;
import com.example.golapp.services.ProfileService;
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

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding binding;
    Uri uri;
    ProfileService profileService = RetrofitInstance.getRetrofitInstance().create(ProfileService.class);
    ValidationHelper validationHelper;
    UserDetail user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUserFromActivity();
        validationHelper = new ValidationHelper(null, this, true);

        binding.btnCancelar.setOnClickListener(view -> {
            finish();
            Animatoo.animateFade(this);
        });
        binding.imgAvatar.setOnClickListener(view -> ImagePicker.Companion.with(this)
                .crop()
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)
                .start(10));

        binding.btnGuardar.setOnClickListener(view -> {
            if (!validateFields()) {
                return;
            }

            LottieAlertDialog dialog = new LottieAlertDialog.Builder(this, DialogTypes.TYPE_LOADING)
                    .setTitle("En proceso")
                    .build();
            dialog.setCancelable(false);
            dialog.show();
            ResetPasswordRequest request = new ResetPasswordRequest();
            request.setPassword(String.valueOf(binding.txtLayoutPassword.getEditText().getText()));
            profileService.resetPassword(request).enqueue(new Callback<BaseResponse<String>>() {
                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    dialog.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        Toasty.success(ProfileActivity.this, response.body().getMessage()).show();
                        finish();
                        Animatoo.animateFade(ProfileActivity.this);
                    } else {
                        Converter<ResponseBody, BaseResponse<String>> converter = RetrofitInstance.getRetrofitInstance().responseBodyConverter(BaseResponse.class, new Annotation[0]);
                        try {
                            BaseResponse<String> error = converter.convert(Objects.requireNonNull(response.errorBody()));
                            assert error != null;
                            Toasty.error(ProfileActivity.this, error.getMessage()).show();
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

    private void initUserFromActivity() {
        Intent i = getIntent();
        user = (UserDetail) i.getSerializableExtra("user");
        if (!user.getAvatar().equals("")) {
            Glide.with(this)
                    .load(user.getAvatar())
                    .fitCenter()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.img_placeholder)
                    .into(binding.imgAvatar);
        }
        if (user.getPerson().getCycle().getGol() != null) {
            Glide.with(this)
                    .load(user.getPerson().getCycle().getGol().getPhoto())
                    .fitCenter()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.img_placeholder)
                    .into(binding.imgGol);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && data != null) {
            uri = data.getData();
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            if (uri != null) {
                File file = new File(uri.getPath());
                builder.addFormDataPart("avatar", file.getName(), RequestBody.create(MultipartBody.FORM, file));
                RequestBody requestBody = builder.build();
                LottieAlertDialog dialog = new LottieAlertDialog.Builder(this, DialogTypes.TYPE_LOADING)
                        .setTitle("En proceso")
                        .build();
                dialog.setCancelable(false);
                dialog.show();
                profileService.uploadAvatar(requestBody).enqueue(new Callback<BaseResponse<String>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            Toasty.success(ProfileActivity.this, response.body().getMessage()).show();
                            startActivity(new Intent(ProfileActivity.this, AuthActivity.class));
                            finishAffinity();
                        } else {
                            Converter<ResponseBody, BaseResponse<String>> converter = RetrofitInstance.getRetrofitInstance().responseBodyConverter(BaseResponse.class, new Annotation[0]);
                            try {
                                BaseResponse<String> error = converter.convert(Objects.requireNonNull(response.errorBody()));
                                assert error != null;
                                Toasty.error(ProfileActivity.this, error.getMessage()).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                        System.out.println(t.getMessage());
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    public boolean validateFields() {
        validationHelper.addRequiredValidation(binding.txtLayoutPassword, "Este campo es requerido.");
        validationHelper.addConfirmPasswordValidation(binding.txtLayoutPassword, binding.txtLayoutConfirmPassword, "Las contraseñas no coinciden");
        return validationHelper.validateAll();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateFade(this);
    }
}