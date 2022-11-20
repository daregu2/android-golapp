package com.example.golapp.adapters.gol;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.golapp.R;
import com.example.golapp.api.RetrofitInstance;
import com.example.golapp.databinding.GolItemListBinding;
import com.example.golapp.models.Gol;
import com.example.golapp.models.Tutor;
import com.example.golapp.responses.BaseResponse;
import com.example.golapp.responses.GolResponse;
import com.example.golapp.services.GolService;
import com.example.golapp.services.TutorService;
import com.example.golapp.ui.tutor.TutorEditActivity;
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


public class GolListAdapter extends RecyclerView.Adapter<GolListAdapter.ViewHolder> {

    private List<GolResponse> itemList;
    LottieAlertDialog dialog;
    GolService golService = RetrofitInstance.getRetrofitInstance().create(GolService.class);

    public GolListAdapter(List<GolResponse> itemList, Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public GolListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        GolItemListBinding binding = GolItemListBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GolListAdapter.ViewHolder holder, int position) {
        holder.bindView(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setItems(List<GolResponse> itemList) {
        this.itemList = itemList;
    }

    public void deleteItem(int position) {
        if (itemList != null && this.getItemCount() > 0) {
            itemList.remove(position);
        }
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        GolItemListBinding binding;

        public ViewHolder(@NonNull GolItemListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindView(GolResponse gol) {
            binding.txtGolName.setText(gol.getName());
            binding.txtGolChant.setText(String.format("Canto: %s",gol.getChant()));
            binding.txtGolMotto.setText(String.format("Lema: %s",gol.getMotto()));
            binding.txtGolVerse.setText(String.format("Versiculo: %s",gol.getVerse()));
            Glide.with(itemView.getContext())
                    .load(gol.getPhoto())
                    .fitCenter()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.img_placeholder)
                    .into(binding.imgGol);
            binding.btnEdit.setOnClickListener(view -> {
//                Intent intent = new Intent(view.getContext(), GolEditActivity.class);
//                intent.putExtra("person", tutor);
//                view.getContext().startActivity(new Intent(intent));
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
                                // TODO: Falta el delete deGol service


                            })
                            .build();
                    dialog.setCancelable(true);
                    dialog.show();
                }
            });
        }
    }
}
