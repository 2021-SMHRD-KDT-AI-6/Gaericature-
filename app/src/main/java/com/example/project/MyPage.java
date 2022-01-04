package com.example.project;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

public class MyPage extends Fragment {

    ImageView imgProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.fragment_mypage, container, false);

        imgProfile = fragment.findViewById(R.id.imgProfile);

        imgProfile.setBackground(new ShapeDrawable(new OvalShape()));
        imgProfile.setClipToOutline(true);

        return fragment;
    }
}