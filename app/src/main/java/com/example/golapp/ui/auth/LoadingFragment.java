package com.example.golapp.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.golapp.R;
import com.example.golapp.api.RetrofitInstance;
import com.example.golapp.databinding.FragmentLoadingBinding;
import com.example.golapp.models.UserDetail;
import com.example.golapp.responses.BaseResponse;
import com.example.golapp.services.AuthService;
import com.example.golapp.ui.MainActivity;
import com.example.golapp.utils.TokenManager;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class LoadingFragment extends Fragment {
    TokenManager tokenManager;
    AuthService authService;
    private FragmentLoadingBinding binding;

    public LoadingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoadingBinding.inflate(inflater, container, false);
        authService = RetrofitInstance.getRetrofitInstance().create(AuthService.class);
        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        tokenManager = new TokenManager();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (Objects.equals(tokenManager.getToken(), "")) {
            Navigation.findNavController(view).navigate(R.id.action_loadingFragment_to_loginFragment);
        } else {
            fetchProfile();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void fetchProfile() {
        authService.fetchProfile().enqueue(new Callback<BaseResponse<UserDetail>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<UserDetail>> call, @NonNull Response<BaseResponse<UserDetail>> response) {

                if (response.code()==403 || response.code()==401){
                    redirectLogin();
                    return;
                }

                if (response.isSuccessful() && response.body()!=null) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("user", response.body().getResult());
                    startActivity(intent);
                    requireActivity().finish();

                } else {
                    tokenManager.deleteToken();
                    Converter<ResponseBody, BaseResponse<String>> converter = RetrofitInstance.getRetrofitInstance().responseBodyConverter(BaseResponse.class, new Annotation[0]);
                    try {
                        BaseResponse<String> error = converter.convert(Objects.requireNonNull(response.errorBody()));
                        assert error != null;
                        System.out.println(response.raw());
                        Toasty.error(requireContext(), error.getMessage()).show();
                        requireActivity().finishAndRemoveTask();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<UserDetail>> call, @NonNull Throwable t) {
                tokenManager.deleteToken();
                System.out.println(t.getMessage());

            }
        });

    }

    public void redirectLogin(){
        tokenManager.deleteToken();
        Navigation.findNavController(requireView()).navigate(R.id.action_loadingFragment_to_loginFragment);
    }
}