package com.example.golapp.adapters.cycle;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.golapp.api.RetrofitInstance;
import com.example.golapp.databinding.CycleItemListBinding;
import com.example.golapp.models.Cycle;
import com.example.golapp.responses.BaseResponse;
import com.example.golapp.services.GolService;
import com.example.golapp.ui.gol.GolCreateActivity;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class CycleListAdapter extends RecyclerView.Adapter<CycleListAdapter.ViewHolder> {

    private List<Cycle> list;
    GolService golService = RetrofitInstance.getRetrofitInstance().create(GolService.class);
    LottieAlertDialog dialog;

    public CycleListAdapter(List<Cycle> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public CycleListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        CycleItemListBinding binding = CycleItemListBinding.inflate(layoutInflater, parent, false);
        return new CycleListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CycleListAdapter.ViewHolder holder, int position) {
        holder.bindView(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setItems(List<Cycle> list) {
        this.list = list;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CycleItemListBinding binding;

        public ViewHolder(@NonNull CycleItemListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindView(Cycle cycle) {
            binding.txtCycleName.setText(cycle.getName());
            binding.btnAdd.setVisibility(View.GONE);
            binding.btnEdit.setVisibility(View.GONE);
            binding.btnDelete.setVisibility(View.GONE);
            binding.golLayout.setVisibility(View.GONE);
            binding.btnAdd.setOnClickListener(view -> {
                Intent intent = new Intent(view.getContext(), GolCreateActivity.class);
                intent.putExtra("cycleId", cycle.getId());
                view.getContext().startActivity(new Intent(intent));
            });
            binding.btnDelete.setOnClickListener(view -> {
                dialog = new LottieAlertDialog.Builder(view.getContext(), DialogTypes.TYPE_WARNING)
                        .setTitle("¿Está seguro de eliminar este registro?")
                        .setDescription("No podra deshacer los cambios luego...")
                        .setPositiveText("Confirmar")
                        .setPositiveListener(lottieAlertDialog -> {
                            dialog.changeDialog(new LottieAlertDialog.Builder(view.getContext(), DialogTypes.TYPE_LOADING)
                                    .setTitle("En proceso")
                            );
                            golService.delete(cycle.getGol().getId()).enqueue(new Callback<BaseResponse<String>>() {
                                @Override
                                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        Toasty.success(view.getContext(), response.body().getMessage()).show();
                                        binding.btnAdd.setVisibility(View.VISIBLE);
                                        binding.btnEdit.setVisibility(View.GONE);
                                        binding.btnDelete.setVisibility(View.GONE);
                                        binding.golLayout.setVisibility(View.GONE);
                                    } else {
                                        Converter<ResponseBody, BaseResponse<String>> converter = RetrofitInstance.getRetrofitInstance().responseBodyConverter(BaseResponse.class, new Annotation[0]);
                                        try {
                                            BaseResponse<String> error = converter.convert(Objects.requireNonNull(response.errorBody()));
                                            assert error != null;
                                            Toasty.error(view.getContext(), error.getMessage()).show();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    dialog.dismiss();
                                }


                                @Override
                                public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                                    dialog.dismiss();
                                }
                            });


                        })
                        .build();
                dialog.setCancelable(true);
                dialog.show();
            });
            if (cycle.getGol() == null) {
                binding.btnAdd.setVisibility(View.VISIBLE);
                binding.btnEdit.setVisibility(View.GONE);
                binding.btnDelete.setVisibility(View.GONE);
                binding.golLayout.setVisibility(View.GONE);


            } else {
                binding.btnAdd.setVisibility(View.GONE);
                binding.btnEdit.setVisibility(View.VISIBLE);
                binding.btnDelete.setVisibility(View.VISIBLE);
                binding.golLayout.setVisibility(View.VISIBLE);
                binding.txtGolName.setText(cycle.getGol().getName());


            }
        }

    }
}