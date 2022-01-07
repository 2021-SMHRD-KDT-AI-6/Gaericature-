package com.example.project.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.project.R;

public class Home extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.fragment_home, container, false);

        TextView mainTitle = fragment.findViewById(R.id.mainTitle);
        mainTitle.bringToFront();

        return fragment;
    }
}