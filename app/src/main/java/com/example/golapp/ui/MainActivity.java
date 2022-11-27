package com.example.golapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.developer.kalert.KAlertDialog;
import com.example.golapp.ui.gol.GolCreateActivity;
import com.example.golapp.ui.main.AdminMainFragment;
import com.example.golapp.R;
import com.example.golapp.api.RetrofitInstance;
import com.example.golapp.databinding.ActivityMainBinding;
import com.example.golapp.models.UserDetail;
import com.example.golapp.responses.BaseResponse;
import com.example.golapp.services.AuthService;
import com.example.golapp.ui.auth.AuthActivity;
import com.example.golapp.ui.main.TutorMainFragment;
import com.example.golapp.utils.TokenManager;
import com.google.android.material.navigation.NavigationView;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityMainBinding binding;
    TokenManager tokenManager = new TokenManager();
    AuthService authService = RetrofitInstance.getRetrofitInstance().create(AuthService.class);
    KAlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        binding.navView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.nav_open, R.string.nav_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.navView.setNavigationItemSelectedListener(this);
        dialog = new KAlertDialog(this, KAlertDialog.PROGRESS_TYPE);

        setInitialDataFromServer();
    }

    public void setInitialDataFromServer() {
        Intent i = getIntent();
        UserDetail userDetail = (UserDetail) i.getSerializableExtra("user");
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", userDetail);
        ImageView imageView = (ImageView) binding.navView.getHeaderView(0).findViewById(R.id.headerImg);
        TextView headerNameText = binding.navView.getHeaderView(0).findViewById(R.id.headerNameTxt);
        headerNameText.setText(String.format("%s %s", userDetail.getPerson().getNames(), userDetail.getPerson().getLastNames()));
        Glide.with(this)
                .load(userDetail.getAvatar())
                .fitCenter()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.img_placeholder)
                .into(imageView);
        if (userDetail.getRoles().contains("Administrador")) {
            binding.cardNextEvent.setVisibility(View.GONE);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameContainer, new AdminMainFragment());
            ft.commit();
        }
        if (userDetail.getRoles().contains("Tutor")) {
            TutorMainFragment tutorMainFragment =  new TutorMainFragment();
            tutorMainFragment.setArguments(bundle);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameContainer,tutorMainFragment);
            ft.commit();
        }
    }


    public void logout() {
        dialog.getProgressHelper().setBarColor(Color.parseColor("#0277bd"));
        dialog.setTitleText("Espere por favor...");
        dialog.setCancelable(false);
        dialog.show();
        authService.logout().enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                tokenManager.deleteToken();
                dialog.hide();
                Intent intent = new Intent(MainActivity.this, AuthActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                tokenManager.deleteToken();
                Intent intent = new Intent(MainActivity.this, AuthActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                logout();
                break;
            case R.id.menu_profile:
                Toasty.info(this, "Ver perfil!").show();
                break;
        }
        return true;
    }
}