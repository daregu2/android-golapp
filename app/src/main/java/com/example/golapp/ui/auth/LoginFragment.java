package com.example.golapp.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.developer.kalert.KAlertDialog;
import com.example.golapp.api.RetrofitInstance;
import com.example.golapp.databinding.FragmentLoginBinding;
import com.example.golapp.models.UserDetail;
import com.example.golapp.requests.LoginRequest;
import com.example.golapp.responses.BaseResponse;
import com.example.golapp.responses.LoginResponse;
import com.example.golapp.services.AuthService;
import com.example.golapp.ui.MainActivity;
import com.example.golapp.utils.TokenManager;
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

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    TokenManager tokenManager;
    AuthService authService;
    KAlertDialog dialog;
    ValidationHelper validationHelper;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);


        authService = RetrofitInstance.getRetrofitInstance().create(AuthService.class);
        binding.btnLogin.setOnClickListener(view -> {
            String name = String.valueOf(binding.txtUsername.getText());
            String password = String.valueOf(binding.txtPassword.getText());
            loginAndFetchProfile(name, password);

        });
        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        dialog = new KAlertDialog(context, KAlertDialog.PROGRESS_TYPE);
        validationHelper = new ValidationHelper(null,context,false);
        tokenManager = new TokenManager();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void loginAndFetchProfile(String name, String password) {
        if (!validateFields()) {
            return;
        }
        dialog.getProgressHelper().setBarColor(Color.parseColor("#0277bd"));
        dialog.setTitleText("Espere por favor...");
        dialog.setCancelable(false);
        dialog.show();
        authService.login(new LoginRequest(name, password)).enqueue(new Callback<BaseResponse<LoginResponse>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<LoginResponse>> call, @NonNull Response<BaseResponse<LoginResponse>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    tokenManager.saveToken(response.body().getResult().getToken());
                    fetchProfile();
                } else {
                    dialog.hide();
                    Converter<ResponseBody, BaseResponse<String>> converter = RetrofitInstance.getRetrofitInstance().responseBodyConverter(BaseResponse.class, new Annotation[0]);
                    try {
                        BaseResponse<String> error = converter.convert(Objects.requireNonNull(response.errorBody()));
                        assert error != null;
                        Toasty.error(requireContext(), error.getMessage()).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<LoginResponse>> call, @NonNull Throwable t) {
                dialog.hide();
                System.out.println(t.getMessage());
            }
        });

    }

    public void fetchProfile() {
        authService.fetchProfile().enqueue(new Callback<BaseResponse<UserDetail>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<UserDetail>> call, @NonNull Response<BaseResponse<UserDetail>> response) {
                dialog.hide();
                if (response.isSuccessful() && response.body() != null) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("user", response.body().getResult());
                    startActivity(intent);
                    Animatoo.animateSlideUp(requireContext());
                    requireActivity().finish();

                } else {
                    Converter<ResponseBody, BaseResponse<String>> converter = RetrofitInstance.getRetrofitInstance().responseBodyConverter(BaseResponse.class, new Annotation[0]);
                    try {
                        BaseResponse<String> error = converter.convert(Objects.requireNonNull(response.errorBody()));
                        assert error != null;
                        Toasty.error(requireContext(), error.getMessage()).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<UserDetail>> call, @NonNull Throwable t) {
                dialog.hide();
                System.out.println(t.getMessage());
            }
        });
    }

    public boolean validateFields(){
        validationHelper.addRequiredValidation(binding.txtLayoutUsername,"Ingrese un usuario.");
        validationHelper.addRequiredValidation(binding.txtLayoutPassword,"Ingresa una contraseña.");
        return validationHelper.validateAll();
    }
}