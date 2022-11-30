package com.example.golapp.adapters.event;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.golapp.databinding.EventItemListBinding;
import com.example.golapp.models.Event;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;

import java.util.List;


public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    private List<Event> itemList;
    LottieAlertDialog dialog;

    public EventListAdapter(List<Event> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public EventListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        EventItemListBinding binding = EventItemListBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EventListAdapter.ViewHolder holder, int position) {
        holder.bindView(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setItems(List<Event> itemList) {
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
        EventItemListBinding binding;

        public ViewHolder(@NonNull EventItemListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindView(Event event) {
            binding.txtDate.setText(event.getName());
            binding.btnEdit.setOnClickListener(view -> {
//                Intent intent = new Intent(view.getContext(), GolEditActivity.class);
//                intent.putExtra("person", tutor);
//                view.getContext().startActivity(new Intent(intent));
            });
        }
    }
}
