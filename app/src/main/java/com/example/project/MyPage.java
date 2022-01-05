package com.example.project;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class MyPage extends Fragment {

    ImageView imgProfile;
    TextView tvPurchaseAll, tvPurchaseAllNum, tvPurchaseDelivering, tvPurchaseDeliveringNum, tvPurchaseComplete, tvPurchaseCompleteNum;
    ExpandableHeightGridView gridView;
    MyGaericatureAdapter adapter;
    ArrayList<MyGaericatureVO> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.fragment_mypage, container, false);

        imgProfile = fragment.findViewById(R.id.imgProfile);
        tvPurchaseAll = fragment.findViewById(R.id.tvPurchaseAll);
        tvPurchaseAllNum = fragment.findViewById(R.id.tvPurchaseAllNum);
        tvPurchaseDelivering = fragment.findViewById(R.id.tvPurchaseDelivering);
        tvPurchaseDeliveringNum = fragment.findViewById(R.id.tvPurchaseDeliveringNum);
        tvPurchaseComplete = fragment.findViewById(R.id.tvPurchaseComplete);
        tvPurchaseCompleteNum = fragment.findViewById(R.id.tvPurchaseCompleteNum);

        imgProfile.setBackground(new ShapeDrawable(new OvalShape()));
        imgProfile.setClipToOutline(true);

        tvPurchaseAll.bringToFront();
        tvPurchaseAllNum.bringToFront();
        tvPurchaseDelivering.bringToFront();
        tvPurchaseDeliveringNum.bringToFront();
        tvPurchaseComplete.bringToFront();
        tvPurchaseCompleteNum.bringToFront();

        gridView = fragment.findViewById(R.id.myPageGrid);

        data = new ArrayList<>();
        data.add(new MyGaericatureVO(R.drawable.img1, "이미지1"));
        data.add(new MyGaericatureVO(R.drawable.img2, "이미지2"));
        data.add(new MyGaericatureVO(R.drawable.img3, "이미지3"));
        data.add(new MyGaericatureVO(R.drawable.img1, "이미지1"));
        data.add(new MyGaericatureVO(R.drawable.img2, "이미지2"));
        data.add(new MyGaericatureVO(R.drawable.img3, "이미지3"));
        data.add(new MyGaericatureVO(R.drawable.img1, "이미지1"));
        data.add(new MyGaericatureVO(R.drawable.img2, "이미지2"));
        data.add(new MyGaericatureVO(R.drawable.img3, "이미지3"));
        data.add(new MyGaericatureVO(R.drawable.img1, "이미지1"));
        data.add(new MyGaericatureVO(R.drawable.img2, "이미지2"));
        data.add(new MyGaericatureVO(R.drawable.img3, "이미지3"));
        data.add(new MyGaericatureVO(R.drawable.img1, "이미지1"));
        data.add(new MyGaericatureVO(R.drawable.img2, "이미지2"));
        data.add(new MyGaericatureVO(R.drawable.img3, "이미지3"));
        data.add(new MyGaericatureVO(R.drawable.img1, "이미지1"));
        data.add(new MyGaericatureVO(R.drawable.img2, "이미지2"));
        data.add(new MyGaericatureVO(R.drawable.img3, "이미지3"));

        adapter = new MyGaericatureAdapter(getActivity().getApplicationContext(), R.layout.gaericaturelist, data);
        gridView.setExpanded(true);
        gridView.setAdapter(adapter);

        return fragment;
    }
}
