package com.example.golapp.ui.event;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.golapp.R;
import com.example.golapp.api.RetrofitInstance;
import com.example.golapp.databinding.ActivityEventEditBinding;
import com.example.golapp.databinding.ActivityGolEditBinding;
import com.example.golapp.models.Event;
import com.example.golapp.models.Gol;
import com.example.golapp.responses.BaseResponse;
import com.example.golapp.services.EventService;
import com.example.golapp.services.GolService;
import com.example.golapp.ui.gol.GolEditActivity;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.master.validationhelper.ValidationHelper;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Calendar;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class EventEditActivity extends AppCompatActivity {
    ActivityEventEditBinding binding;
    Event event;
    ValidationHelper validationHelper;
    EventService eventService = RetrofitInstance.getRetrofitInstance().create(EventService.class);
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        validationHelper = new ValidationHelper(null, this, true);


        binding.imgBanner.setOnClickListener(view -> ImagePicker.Companion.with(this)
                .crop()
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .galleryOnly()
                .start(10));
        binding.btnCancelar.setOnClickListener(view -> finish());
        initEventEdit();

        binding.txtLayoutStart.setStartIconOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int hour = cldr.get(Calendar.HOUR_OF_DAY);
            int minutes = cldr.get(Calendar.MINUTE);
            // time picker dialog
            TimePickerDialog picker = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                            binding.txtLayoutStart.getEditText().setText(String.format("%02d:%02d", sHour, sMinute));

                        }
                    }, hour, minutes, true);
            picker.show();
        });
        binding.txtLayoutEnd.setStartIconOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int hour = cldr.get(Calendar.HOUR_OF_DAY);
            int minutes = cldr.get(Calendar.MINUTE);
            // time picker dialog
            TimePickerDialog picker = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                            binding.txtLayoutEnd.getEditText().setText(String.format("%02d:%02d", sHour, sMinute));
                        }
                    }, hour, minutes, true);
            picker.show();
        });

        binding.btnGuardar.setOnClickListener(view -> {
            if (!validateFields()) {
                return;
            }

            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            builder.addFormDataPart("name", String.valueOf(binding.txtLayoutNombre.getEditText().getText()));
            builder.addFormDataPart("start_at", String.valueOf(binding.txtLayoutStart.getEditText().getText()));
            builder.addFormDataPart("end_at", String.valueOf(binding.txtLayoutEnd.getEditText().getText()));
            builder.addFormDataPart("_method", "PUT");

            if (uri != null) {
                File file = new File(uri.getPath());
                builder.addFormDataPart("banner", file.getName(), RequestBody.create(MultipartBody.FORM, file));
            }
            RequestBody requestBody = builder.build();
            LottieAlertDialog dialog = new LottieAlertDialog.Builder(this, DialogTypes.TYPE_LOADING)
                    .setTitle("En proceso")
                    .build();
            dialog.setCancelable(false);
            dialog.show();
            eventService.update(event.getId(), requestBody).enqueue(new Callback<BaseResponse<String>>() {
                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    dialog.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        Toasty.success(EventEditActivity.this, response.body().getMessage()).show();
                        finish();
                    } else {
                        Converter<ResponseBody, BaseResponse<String>> converter = RetrofitInstance.getRetrofitInstance().responseBodyConverter(BaseResponse.class, new Annotation[0]);
                        try {
                            BaseResponse<String> error = converter.convert(Objects.requireNonNull(response.errorBody()));
                            assert error != null;
                            Toasty.error(EventEditActivity.this, error.getMessage()).show();
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

    private void initEventEdit() {
        Intent i = getIntent();
        event = (Event) i.getSerializableExtra("event");
        if (!event.getBanner().equals("")) {
            Glide.with(this)
                    .load(event.getBanner())
                    .fitCenter()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.img_placeholder)
                    .into(binding.imgBanner);
        }
        binding.txtLayoutNombre.getEditText().setText(event.getName());
        binding.txtLayoutStart.getEditText().setText(event.getStart_at());
        binding.txtLayoutEnd.getEditText().setText(event.getEnd_at());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && data != null) {
            uri = data.getData();
            binding.imgBanner.setImageURI(uri);
        }
    }

    public boolean validateFields() {
        validationHelper.addRequiredValidation(binding.txtLayoutNombre, "Este campo es requerido.");
        validationHelper.addRequiredValidation(binding.txtLayoutStart, "Este campo es requerido.");
        validationHelper.addRequiredValidation(binding.txtLayoutEnd, "Este campo es requerido.");
        return validationHelper.validateAll();
    }
}