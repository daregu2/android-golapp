package com.example.golapp.adapters.student;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.golapp.adapters.topic.OnDeleteClick;
import com.example.golapp.api.RetrofitInstance;
import com.example.golapp.databinding.StudentItemListBinding;
import com.example.golapp.models.Student;
import com.example.golapp.responses.BaseResponse;
import com.example.golapp.services.StudentService;
import com.example.golapp.ui.student.StudentEditActivity;
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


public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.ViewHolder> {

    private List<Student> studentList;
    LottieAlertDialog dialog;
    OnDeleteClick onDeleteClick;
    StudentService studentService = RetrofitInstance.getRetrofitInstance().create(StudentService.class);

    public StudentListAdapter(List<Student> studentList, OnDeleteClick onDeleteClick) {
        this.studentList = studentList;
        this.onDeleteClick =onDeleteClick;
    }

    @NonNull
    @Override
    public StudentListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        StudentItemListBinding binding = StudentItemListBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentListAdapter.ViewHolder holder, int position) {
        holder.bindView(studentList.get(position));
    }

    public boolean listHasLider() {
        for (Student student : this.studentList) {
            if (student.getUser() != null) {
                if (student.getUser().isIs_lider()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public void setItems(List<Student> studentList) {
        this.studentList = studentList;
    }

    public void deleteItem(int position) {
        if (studentList != null && this.getItemCount() > 0) {
            studentList.remove(position);
        }
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        StudentItemListBinding binding;

        public ViewHolder(@NonNull StudentItemListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindView(Student student) {
            binding.txtStudentNames.setText(student.getNames());
            binding.txtStudentSchool.setText(student.getCycle().getSchool().getName());
            binding.txtStudentCycle.setText(student.getCycle().getName());

            binding.btnEdit.setOnClickListener(view -> {
                Intent intent = new Intent(view.getContext(), StudentEditActivity.class);
                intent.putExtra("person", student);
                view.getContext().startActivity(new Intent(intent));
            });

            if (student.getUser() != null) {
                if (student.getUser().isIs_lider()) {
                    binding.layoutLider.setVisibility(View.VISIBLE);
                }
            }

            binding.cardStudent.setOnLongClickListener(view -> {
                if (!listHasLider()) {
                    onDeleteClick.onDeleteTopicClick(student.getId());
                } else {
                    Toasty.info(view.getContext(), "Ya hay un lider asignado para esta semana :)").show();
                }
                return true;
            });


            binding.btnDelete.setOnClickListener(view -> {
                dialog = new LottieAlertDialog.Builder(view.getContext(), DialogTypes.TYPE_WARNING)
                        .setTitle("??Est?? seguro de eliminar este registro?")
                        .setDescription("No podra deshacer los cambios luego...")
                        .setPositiveText("Confirmar")
                        .setPositiveListener(lottieAlertDialog -> {
                            dialog.changeDialog(new LottieAlertDialog.Builder(view.getContext(), DialogTypes.TYPE_LOADING)
                                    .setTitle("En proceso")
                            );
                            studentService.delete(student.getId()).enqueue(new Callback<BaseResponse<String>>() {
                                @Override
                                public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                                    if (response.isSuccessful() && response.body() != null) {
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
            });
        }
    }
}
