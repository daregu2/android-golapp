package com.example.golapp.adapters.eventperson;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.golapp.adapters.topic.OnDeleteClick;
import com.example.golapp.databinding.EventpersonItemListBinding;
import com.example.golapp.models.Event;
import com.example.golapp.models.Person;
import com.example.golapp.ui.event.EventEditActivity;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;

import java.util.List;


public class EventPersonListAdapter extends RecyclerView.Adapter<EventPersonListAdapter.ViewHolder> {

    private List<Person> itemList;
    OnDeleteClick onDeleteClick;
    LottieAlertDialog dialog;

    public EventPersonListAdapter(List<Person> itemList, OnDeleteClick onDeleteClick) {
        this.itemList = itemList;
        this.onDeleteClick = onDeleteClick;
    }

    @NonNull
    @Override
    public EventPersonListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        EventpersonItemListBinding binding = EventpersonItemListBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EventPersonListAdapter.ViewHolder holder, int position) {
        holder.bindView(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setItems(List<Person> itemList) {
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
        EventpersonItemListBinding binding;

        public ViewHolder(@NonNull EventpersonItemListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindView(Person person) {
            binding.txtName.setText(String.format("%s %s", person.getNames(), person.getLastNames()));
        }
    }
}
