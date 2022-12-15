package com.example.golapp.ui.topic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.golapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class TopicBottomSheetFragment extends BottomSheetDialogFragment {

    public TopicBottomSheetFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.layout_topic_dialog, container, false);
        return view;
    }
}
