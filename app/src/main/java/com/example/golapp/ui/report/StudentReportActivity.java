package com.example.golapp.ui.report;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.developer.kalert.KAlertDialog;
import com.example.golapp.api.RetrofitInstance;
import com.example.golapp.databinding.ActivityStudentReportBinding;
import com.example.golapp.models.Student;
import com.example.golapp.responses.BaseResponse;
import com.example.golapp.services.ReportService;
import com.example.golapp.services.StudentService;
import com.example.golapp.ui.gol.GolCreateActivity;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.master.validationhelper.ValidationHelper;
import com.rajat.pdfviewer.PdfViewerActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class StudentReportActivity extends AppCompatActivity {
    ActivityStudentReportBinding binding;
    StudentService studentService = RetrofitInstance.getRetrofitInstance().create(StudentService.class);
    ReportService reportService = RetrofitInstance.getRetrofitInstance().create(ReportService.class);
    ValidationHelper validationHelper;
    Student student;
    private ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if (result) {
                new LottieAlertDialog.Builder(StudentReportActivity.this, DialogTypes.TYPE_SUCCESS)
                        .setTitle("Bien!")
                        .build()
                        .show();
            } else {
                new KAlertDialog(StudentReportActivity.this, KAlertDialog.ERROR_TYPE)
                        .setTitleText("Ops")
                        .setContentText("No hay permisos!")
                        .show();
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentReportBinding.inflate(getLayoutInflater());
        validationHelper = new ValidationHelper(null, this, true);
        setContentView(binding.getRoot());

        binding.btnBuscar.setOnClickListener(view -> {
            if (!validateFields()) {
                return;
            }
            binding.loadingView.setVisibility(View.VISIBLE);
            int code = Integer.parseInt(String.valueOf(binding.txtLayoutCodigo.getEditText().getText()));
            studentService.show(code).enqueue(new Callback<BaseResponse<Student>>() {
                @Override
                public void onResponse(Call<BaseResponse<Student>> call, Response<BaseResponse<Student>> response) {
                    binding.loadingView.setVisibility(View.GONE);
                    if (response.isSuccessful() && response.body() != null) {

                        binding.layoutStudentData.setVisibility(View.VISIBLE);
                        binding.btnGuardar.setVisibility(View.VISIBLE);
                        binding.txtNames.setText(response.body().getResult().getNames());
                        binding.txtLastNames.setText(response.body().getResult().getLastNames());
                        binding.txtCodigo.setText(String.valueOf(response.body().getResult().getCode()));
                        binding.txtEmail.setText(response.body().getResult().getEmail());
                        student = response.body().getResult();
                        Toasty.success(StudentReportActivity.this, response.body().getMessage()).show();
                    } else {
                        Converter<ResponseBody, BaseResponse<Student>> converter = RetrofitInstance.getRetrofitInstance().responseBodyConverter(BaseResponse.class, new Annotation[0]);
                        try {
                            BaseResponse<Student> error = converter.convert(Objects.requireNonNull(response.errorBody()));
                            assert error != null;
                            Toasty.error(StudentReportActivity.this, error.getMessage()).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse<Student>> call, Throwable t) {
                    binding.loadingView.setVisibility(View.GONE);
                    System.out.println(t);
                }
            });
        });
        binding.txtLayoutCodigo.setEndIconOnClickListener(view -> {
            binding.layoutStudentData.setVisibility(View.GONE);
            binding.btnGuardar.setVisibility(View.GONE);
            binding.txtLayoutCodigo.getEditText().setText("");
        });

        binding.btnGuardar.setOnClickListener(view -> {
            if (student == null) {
                Toasty.error(StudentReportActivity.this, "No hay estudiante").show();
                return;
            }

            LottieAlertDialog dialog = new LottieAlertDialog.Builder(this, DialogTypes.TYPE_LOADING)
                    .setTitle("En proceso")
                    .build();
            dialog.setCancelable(false);
            dialog.show();
            if (!(ContextCompat.checkSelfPermission(StudentReportActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                activityResultLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                return;
            }
            reportService.studentReport(student.getId()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dialog.dismiss();
                    InputStream input = response.body().byteStream();

                    if (response.isSuccessful() && response.body() != null) {

                        File file = new File(Environment.getExternalStoragePublicDirectory("Download"), student.getCode() + "2022.pdf");
                        try {
                            byte[] bytes = response.body().bytes();
                            FileOutputStream output = new FileOutputStream(file);
                            output.write(bytes);
                            output.close();
                            Toasty.success(StudentReportActivity.this, "El reporte se ha guardado correctamente.").show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Converter<ResponseBody, BaseResponse<String>> converter = RetrofitInstance.getRetrofitInstance().responseBodyConverter(BaseResponse.class, new Annotation[0]);
                        try {
                            BaseResponse<String> error = converter.convert(Objects.requireNonNull(response.errorBody()));
                            assert error != null;
                            Toasty.error(StudentReportActivity.this, error.getMessage()).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dialog.dismiss();
                }
            });
        });
    }

    public boolean validateFields() {
        validationHelper.addRequiredValidation(binding.txtLayoutCodigo, "Ingrese un codigo para buscar.");
        return validationHelper.validateAll();
    }
}