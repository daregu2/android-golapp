package com.example.golapp.ui.gol;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.golapp.R;
import com.example.golapp.api.RetrofitInstance;
import com.example.golapp.databinding.ActivityGolCreateBinding;
import com.example.golapp.models.School;
import com.example.golapp.responses.BaseResponse;
import com.example.golapp.responses.CollectionResponse;
import com.example.golapp.services.GolService;
import com.example.golapp.services.SchoolService;
import com.example.golapp.ui.tutor.TutorCreateActivity;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.master.validationhelper.ValidationHelper;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
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
    SchoolService schoolService = RetrofitInstance.getRetrofitInstance().create(SchoolService.class);
    ArrayList<String> arraySchool, arrayGrade;
    ArrayAdapter<String> adapterSchool;
    ArrayAdapter<String> adapterGrade;
    HashMap<String, Integer> mapSchools = new HashMap<String, Integer>();
    ValidationHelper validationHelper;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGolCreateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupSchoolSpinner();
        setupGradeSpinner();

        validationHelper = new ValidationHelper(null, GolCreateActivity.this, true);
        binding.btnCancelar.setOnClickListener(view -> finish());
        binding.imgGol.setOnClickListener(view -> ImagePicker.Companion.with(this)
                .crop()
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
            builder.addFormDataPart("school_id", String.valueOf(mapSchools.get(binding.spinSchool.getText().toString())));
            builder.addFormDataPart("cycle", binding.spinCycle.getText().toString());
            if (uri != null) {
                File file = new File(uri.getPath());
                builder.addFormDataPart("photo", file.getName(), RequestBody.create(MultipartBody.FORM, file));
            }
            RequestBody requestBody = builder.build();
            golService.store(requestBody).enqueue(new Callback<BaseResponse<String>>() {
                @Override
                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toasty.success(GolCreateActivity.this, response.body().getMessage()).show();
                        finish();
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

                }
            });
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10) {
            uri = data.getData();
            binding.imgGol.setImageURI(uri);
        }

    }

    public void setupSchoolSpinner() {
        schoolService.fetchSchools().enqueue(new Callback<BaseResponse<CollectionResponse<School>>>() {
            @Override
            public void onResponse(Call<BaseResponse<CollectionResponse<School>>> call, Response<BaseResponse<CollectionResponse<School>>> response) {
                if (response.isSuccessful()) {
                    ArrayList<School> schools = response.body().getResult().getData();
                    arraySchool = new ArrayList<>();
                    for (int i = 0; i < schools.size(); i++) {
                        arraySchool.add(schools.get(i).getName());
                        mapSchools.put(schools.get(i).getName(), schools.get(i).getId());
                    }
                    adapterSchool = new ArrayAdapter<>(GolCreateActivity.this, R.layout.spinner_item, arraySchool);
                    binding.spinSchool.setAdapter(adapterSchool);
                }

            }

            @Override
            public void onFailure(Call<BaseResponse<CollectionResponse<School>>> call, Throwable t) {

            }
        });


    }

    public void setupGradeSpinner() {
        arrayGrade = new ArrayList<>();
        arrayGrade.add("1");
        arrayGrade.add("2");
        arrayGrade.add("3");
        arrayGrade.add("4");
        arrayGrade.add("5");
        arrayGrade.add("6");
        arrayGrade.add("7");
        arrayGrade.add("8");
        arrayGrade.add("9");
        arrayGrade.add("10");
        adapterGrade = new ArrayAdapter<>(this, R.layout.spinner_item, arrayGrade);
        binding.spinCycle.setAdapter(adapterGrade);

    }

    public boolean validateFields() {
        validationHelper.addRequiredValidation(binding.txtSpinSchool, "Este campo es requerido.");
        validationHelper.addRequiredValidation(binding.txtSpinCycle, "Este campo es requerido.");
        validationHelper.addRequiredValidation(binding.txtLayoutNombre, "Este campo es requerido.");
        validationHelper.addRequiredValidation(binding.txtLayoutCanto, "Este campo es requerido.");
        validationHelper.addRequiredValidation(binding.txtLayoutLema, "Este campo es requerido.");
        validationHelper.addRequiredValidation(binding.txtLayoutVersiculo, "Este campo es requerido.");
        return validationHelper.validateAll();
    }
}