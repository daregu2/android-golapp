package com.example.golapp.adapters.week;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.golapp.R;
//import com.example.golapp.adapters.cycle.TopicListAdapter;
import com.example.golapp.adapters.topic.OnDeleteClick;
import com.example.golapp.adapters.topic.TopicListAdapter;
import com.example.golapp.api.RetrofitInstance;
import com.example.golapp.databinding.WeekItemListBinding;
import com.example.golapp.models.Topic;
import com.example.golapp.models.Week;
import com.example.golapp.responses.BaseResponse;
import com.example.golapp.services.WeekService;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class WeekListAdapter extends RecyclerView.Adapter<WeekListAdapter.ViewHolder> {
    private OnDeleteClick onDeleteClick;
    private List<Week> list;
    private List<Topic> topicList;
    WeekService weekService = RetrofitInstance.getRetrofitInstance().create(WeekService.class);

    public WeekListAdapter(List<Week> list, OnDeleteClick onDeleteClick) {
        this.list = list;
        this.topicList = new ArrayList<>();
        this.onDeleteClick = onDeleteClick;
    }

    @NonNull
    @Override
    public WeekListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        WeekItemListBinding binding = WeekItemListBinding.inflate(layoutInflater, parent, false);
        return new WeekListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WeekListAdapter.ViewHolder holder, int position) {
        holder.bindView(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setItems(List<Week> list) {
        this.list = list;
    }

    public void deleteItem(int position) {
        if (list != null && this.getItemCount() > 0) {
            list.remove(position);
        }
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        WeekItemListBinding binding;
        LottieAlertDialog dialog;
        public ViewHolder(@NonNull WeekItemListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindView(Week week) {
            binding.txtWeekName.setText(String.valueOf("Sesión "+ (getAdapterPosition() + 1)));
            binding.txtDate.setText(week.getEvent_date());

            binding.expandableLayout.setVisibility(week.isExpandable() ? View.VISIBLE : View.GONE);

            if (week.isExpandable()){
                binding.imgArrow.setImageResource(R.drawable.ic_arrow_up);
            }else{
                binding.imgArrow.setImageResource(R.drawable.ic_arrow_down);
            }

            binding.weekLayout.setOnLongClickListener(view -> {

                dialog = new LottieAlertDialog.Builder(view.getContext(), DialogTypes.TYPE_WARNING)
                        .setTitle("¿Está seguro de eliminar este registro?")
                        .setDescription("No podra deshacer los cambios luego...")
                        .setPositiveText("Confirmar")
                        .setPositiveListener(lottieAlertDialog -> {
                            dialog.changeDialog(new LottieAlertDialog.Builder(view.getContext(), DialogTypes.TYPE_LOADING)
                                    .setTitle("En proceso")
                            );
                            weekService.delete(week.getId()).enqueue(new Callback<BaseResponse<String>>() {
                                @Override
                                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                                    if (response.isSuccessful() && response.body() !=null) {
                                        Toasty.success(view.getContext(), response.body().getMessage()).show();
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
                return true;
            });

            // EMPIEZA LA MAGIA
            TopicListAdapter topicListAdapter = new TopicListAdapter(topicList,onDeleteClick);
            binding.cyclesRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
//            binding.cyclesRecyclerView.setHasFixedSize(true);
            binding.cyclesRecyclerView.setAdapter(topicListAdapter);

            binding.weekLayout.setOnClickListener(view -> {
                week.setExpandable(!week.isExpandable());
                topicList = week.getTopics();
                notifyItemChanged(getAdapterPosition());
            });


        }
    }
}
