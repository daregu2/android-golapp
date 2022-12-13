package com.example.golapp.adapters.school;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.golapp.R;
import com.example.golapp.adapters.cycle.CycleListAdapter;
import com.example.golapp.adapters.topic.OnDeleteClick;
import com.example.golapp.databinding.SchoolItemListBinding;
import com.example.golapp.models.Cycle;
import com.example.golapp.models.School;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;


public class SchoolListAdapter extends RecyclerView.Adapter<SchoolListAdapter.ViewHolder> {

    private List<School> list;
    private List<Cycle> cycleList;
    OnDeleteClick onDeleteClick;

    public SchoolListAdapter(List<School> list, OnDeleteClick onDeleteClick) {
        this.list = list;
        this.cycleList = new ArrayList<>();
        this.onDeleteClick = onDeleteClick;
    }

    @NonNull
    @Override
    public SchoolListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        SchoolItemListBinding binding = SchoolItemListBinding.inflate(layoutInflater, parent, false);
        return new SchoolListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SchoolListAdapter.ViewHolder holder, int position) {
        holder.bindView(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setItems(List<School> list) {
        this.list = list;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        SchoolItemListBinding binding;

        public ViewHolder(@NonNull SchoolItemListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindView(School school) {
            binding.txtSchoolName.setText(school.getName());
            binding.expandableLayout.setVisibility(school.isExpandable() ? View.VISIBLE : View.GONE);

            if (school.isExpandable()) {
                binding.imgArrow.setImageResource(R.drawable.ic_arrow_up);
            } else {
                binding.imgArrow.setImageResource(R.drawable.ic_arrow_down);
            }

            // EMPIEZA LA MAGIA
            CycleListAdapter cycleListAdapter = new CycleListAdapter(cycleList, onDeleteClick);
            binding.cyclesRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
//            binding.cyclesRecyclerView.setHasFixedSize(true);
            binding.cyclesRecyclerView.setAdapter(cycleListAdapter);

            binding.schoolLayout.setOnClickListener(view -> {
                school.setExpandable(!school.isExpandable());
                cycleList = school.getCycles();
                notifyItemChanged(getAdapterPosition());
            });


        }
    }
}
