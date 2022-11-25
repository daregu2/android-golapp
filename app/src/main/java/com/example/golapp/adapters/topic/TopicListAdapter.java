package com.example.golapp.adapters.topic;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.golapp.adapters.cycle.CycleListAdapter;
import com.example.golapp.api.RetrofitInstance;
import com.example.golapp.databinding.CycleItemListBinding;
import com.example.golapp.databinding.TopicItemListBinding;
import com.example.golapp.models.Cycle;
import com.example.golapp.models.Topic;
import com.example.golapp.services.TopicService;
import com.example.golapp.ui.gol.GolEditActivity;
import com.example.golapp.ui.topic.TopicCreateActivity;
import com.example.golapp.ui.topic.TopicEditActivity;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;

import java.util.List;

public class TopicListAdapter extends RecyclerView.Adapter<TopicListAdapter.ViewHolder> {

    private List<Topic> list;
    OnDeleteClick onDeleteClick;
    TopicService topicService = RetrofitInstance.getRetrofitInstance().create(TopicService.class);
    LottieAlertDialog dialog;

    public TopicListAdapter(List<Topic> list,OnDeleteClick onDeleteClick) {
        this.list = list;
        this.onDeleteClick = onDeleteClick;
    }

    @NonNull
    @Override
    public TopicListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        TopicItemListBinding binding = TopicItemListBinding.inflate(layoutInflater, parent, false);
        return new TopicListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicListAdapter.ViewHolder holder, int position) {
        holder.bindView(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setItems(List<Topic> list) {
        this.list = list;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TopicItemListBinding binding;

        public ViewHolder(@NonNull TopicItemListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindView(Topic topic) {
            binding.txtCycleName.setText(String.valueOf(topic.getGrade()+"° "));
            if (!topic.isIs_active()) {
                binding.btnAdd.setVisibility(View.VISIBLE);
                binding.btnEdit.setVisibility(View.GONE);
                binding.btnDelete.setVisibility(View.GONE);
//                binding.topicLayout.setVisibility(View.GONE);
            } else {
                binding.btnAdd.setVisibility(View.GONE);
                binding.btnEdit.setVisibility(View.VISIBLE);
                binding.btnDelete.setVisibility(View.VISIBLE);
//                binding.topicLayout.setVisibility(View.VISIBLE);
            }

            binding.btnAdd.setOnClickListener(view -> {
                Intent intent = new Intent(view.getContext(), TopicCreateActivity.class);
                intent.putExtra("topic", topic);
                view.getContext().startActivity(new Intent(intent));
            });

            binding.btnEdit.setOnClickListener(view -> {
                Intent intent = new Intent(view.getContext(), TopicEditActivity.class);
                intent.putExtra("topic", topic);
                view.getContext().startActivity(new Intent(intent));
            });

            binding.btnDelete.setOnClickListener(view -> onDeleteClick.onDeleteTopicClick(topic.getId()));
        }
    }

}
