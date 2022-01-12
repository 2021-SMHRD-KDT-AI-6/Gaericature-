package com.example.project.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.R;
import com.example.project.VO.MyPurchaseVO;

import java.util.ArrayList;

public class MyPurchaseAdapter extends BaseAdapter {

    private ArrayList<MyPurchaseVO> myPurchase = new ArrayList<>();
    private Context context;
    private int layout;

    public MyPurchaseAdapter(Context context, int layout, ArrayList<MyPurchaseVO> myPurchase) {
        this.myPurchase = myPurchase;
        this.context = context;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.mypurchaselist, null);
        }

        TextView tvMyPurItemState = view.findViewById(R.id.tvMyPurItemState);
        TextView tvMyPurItemName = view.findViewById(R.id.tvMyPurItemName);
        TextView tvMyPurItemPrice = view.findViewById(R.id.tvMyPurItemPrice);
        ImageView imgMyPurThumb = view.findViewById(R.id.imgMyPurThumb);

        tvMyPurItemState.setText(myPurchase.get(i).getDeli_yn());
        tvMyPurItemName.setText(myPurchase.get(i).getItem_name());
        tvMyPurItemPrice.setText(myPurchase.get(i).getItem_price());
        imgMyPurThumb.setImageBitmap(myPurchase.get(i).getItem_pic1());

        return view;
    }
}
