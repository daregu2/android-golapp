package com.example.golapp.ui.tutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.golapp.R;
import com.example.golapp.api.RetrofitInstance;
import com.example.golapp.databinding.ActivityTutorCreateBinding;
import com.example.golapp.databinding.ActivityTutorEditBinding;
import com.example.golapp.models.School;
import com.example.golapp.models.Tutor;
import com.example.golapp.models.UserDetail;
import com.example.golapp.requests.tutor.TutorStoreRequest;
import com.example.golapp.responses.BaseResponse;
import com.example.golapp.responses.CollectionResponse;
import com.example.golapp.services.SchoolService;
import com.example.golapp.services.TutorService;
import com.master.validationhelper.ValidationHelper;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class TutorEditActivity extends AppCompatActivity {

    ActivityTutorEditBinding binding;
    ArrayList<String> arraySchool, arrayGrade;
    ArrayAdapter<String> adapterSchool;
    ArrayAdapter<String> adapterGrade;
    HashMap<String, Integer> mapSchools = new HashMap<String, Integer>();
    ValidationHelper validationHelper;
    Tutor tutor;
    TutorService tutorService = RetrofitInstance.getRetrofitInstance().create(TutorService.class);
    SchoolService schoolService = RetrofitInstance.getRetrofitInstance().create(SchoolService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTutorEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        validationHelper = new ValidationHelper(null, TutorEditActivity.this, true);
        binding.btnCancelar.setOnClickListener(view -> {
            finish();
            Animatoo.animateFade(this);
        });
        setupSchoolSpinner();
        setupGradeSpinner();
        setupEditablePerson();

        binding.btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateFields()) {
                    return;
                }

                TutorStoreRequest request = new TutorStoreRequest();
                request.setNames(String.valueOf(binding.txtLayoutNombres.getEditText().getText()));
                request.setLast_names(String.valueOf(binding.txtLayoutApellidos.getEditText().getText()));
                request.setCode(Integer.parseInt(String.valueOf(binding.txtLayoutCodigo.getEditText().getText())));
                request.setEmail(String.valueOf(binding.txtLayoutEmail.getEditText().getText()));
                request.setPhone(Integer.parseInt(String.valueOf(binding.txtLayouTelefono.getEditText().getText())));
                request.setSchool_id(mapSchools.get(binding.spinSchool.getText().toString()));
                request.setCycle(binding.spinCycle.getText().toString());
                if (binding.rbSexoMasculino.isChecked()) {
                    request.setGender(String.valueOf(binding.rbSexoMasculino.getText()));
                } else {
                    request.setGender(String.valueOf(binding.rbSexoFemenino.getText()));
                }
                tutorService.update(tutor.getId(), request).enqueue(new Callback<BaseResponse<String>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Toasty.success(TutorEditActivity.this, response.body().getMessage()).show();
                            Animatoo.animateWindmill(TutorEditActivity.this);
                            finish();
                        } else {
                            Converter<ResponseBody, BaseResponse<String>> converter = RetrofitInstance.getRetrofitInstance().responseBodyConverter(BaseResponse.class, new Annotation[0]);
                            try {
                                BaseResponse<String> error = converter.convert(Objects.requireNonNull(response.errorBody()));
                                assert error != null;
                                Toasty.error(TutorEditActivity.this, error.getMessage()).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                        Toasty.error(TutorEditActivity.this, "ERROR").show();
                    }
                });
            }
        });
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
                    adapterSchool = new ArrayAdapter<>(TutorEditActivity.this, R.layout.spinner_item, arraySchool);
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

    public void setupEditablePerson() {
        Intent i = getIntent();
        tutor = (Tutor) i.getSerializableExtra("person");
        binding.spinSchool.setText(tutor.getCycle().getSchool().getName(), false);
        binding.spinCycle.setText(tutor.getCycle().getName(), false);
        binding.txtLayoutNombres.getEditText().setText(tutor.getNames());
        binding.txtLayoutApellidos.getEditText().setText(tutor.getLastNames());
        binding.txtLayoutCodigo.getEditText().setText(String.valueOf(tutor.getCode()));
        binding.txtLayoutEmail.getEditText().setText(tutor.getEmail());
        binding.txtLayouTelefono.getEditText().setText(String.valueOf(tutor.getPhone()));
        if (tutor.getGender().equals("Masculino")) {
            binding.rbSexoMasculino.setChecked(true);
        } else {
            binding.rbSexoFemenino.setChecked(true);
        }

    }

    public boolean validateFields() {
        validationHelper.addRequiredValidation(binding.txtLayoutNombres, "Este campo es requerido.");
        validationHelper.addRequiredValidation(binding.txtLayoutApellidos, "Este campo es requerido.");
        validationHelper.addRequiredValidation(binding.txtLayoutCodigo, "Este campo es requerido.");
        validationHelper.addEmailValidation(binding.txtLayoutEmail, "Este campo es requerido.", "El campo debe ser un email válido", true);
        validationHelper.addRequiredValidation(binding.txtLayouTelefono, "Este campo es requerido.");
        validationHelper.addRequiredValidation(binding.txtSpinSchool, "Este campo es requerido.");
        validationHelper.addRequiredValidation(binding.txtSpinCycle, "Este campo es requerido.");
        validationHelper.addCustomLogicValidation(() -> {
            if (binding.rbSexoFemenino.isChecked() || binding.rbSexoMasculino.isChecked()) {
                return true;
            } else {
                Toasty.error(this, "Debe seleccionar un Sexo.").show();
                return false;
            }
        });
        return validationHelper.validateAll();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateFade(this);
    }
}