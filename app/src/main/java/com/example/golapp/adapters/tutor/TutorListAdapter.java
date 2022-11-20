package com.example.golapp.adapters.tutor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.golapp.api.RetrofitInstance;
import com.example.golapp.databinding.TutorItemListBinding;
import com.example.golapp.models.Tutor;
import com.example.golapp.responses.BaseResponse;
import com.example.golapp.services.TutorService;
import com.example.golapp.ui.tutor.TutorEditActivity;
import com.labters.lottiealertdialoglibrary.ClickListener;
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


public class TutorListAdapter extends RecyclerView.Adapter<TutorListAdapter.ViewHolder> {

    private List<Tutor> tutorList;
    LottieAlertDialog dialog;
    TutorService tutorService = RetrofitInstance.getRetrofitInstance().create(TutorService.class);

    public TutorListAdapter(List<Tutor> tutorList, Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        this.tutorList = tutorList;
    }

    @NonNull
    @Override
    public TutorListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        TutorItemListBinding binding = TutorItemListBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TutorListAdapter.ViewHolder holder, int position) {
        holder.bindView(tutorList.get(position));
    }

    @Override
    public int getItemCount() {
        return tutorList.size();
    }

    public void setItems(List<Tutor> tutorList) {
        this.tutorList = tutorList;
    }
    public void deleteItem(int position) {
        if (tutorList != null && this.getItemCount() >0){
            tutorList.remove(position);
        }
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TutorItemListBinding binding;

        public ViewHolder(@NonNull TutorItemListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindView(Tutor tutor) {
            binding.txtTutorNames.setText(tutor.getNames());
            binding.txtTutorSchool.setText(tutor.getSchool());
            binding.txtTutorCycle.setText(tutor.getCycle());
            binding.btnEdit.setOnClickListener(view -> {
                Intent intent = new Intent(view.getContext(), TutorEditActivity.class);
                view.getContext().startActivity(new Intent(intent));
            });

            binding.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog = new LottieAlertDialog.Builder(view.getContext(), DialogTypes.TYPE_WARNING)
                            .setTitle("¿Está seguro de eliminar este registro?")
                            .setDescription("No podra deshacer los cambios luego...")
                            .setPositiveText("Confirmar")
                            .setPositiveListener(lottieAlertDialog -> {
                                dialog.changeDialog(new LottieAlertDialog.Builder(view.getContext(), DialogTypes.TYPE_LOADING)
                                        .setTitle("En proceso")
                                );
                                tutorService.delete(tutor.getId()).enqueue(new Callback<BaseResponse<String>>() {
                                    @Override
                                    public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                                        if (response.isSuccessful()) {
                                            Toasty.success(view.getContext(), "Tutor eliminado correctamente!").show();
                                            deleteItem(getAdapterPosition());

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
                }
            });
        }
    }
}
