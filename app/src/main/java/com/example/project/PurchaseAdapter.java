package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PurchaseAdapter extends BaseAdapter {

    private ArrayList<itemVO> item = new ArrayList<>();
    private Context context;
    private int layout;

    public PurchaseAdapter(Context context, int layout, ArrayList<itemVO> item) {
        this.item = item;
        this.context = context;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int i) {
        return item.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.purchaselist, null);
        }

        ImageView imgItem = view.findViewById(R.id.imgItem);

        imgItem.setImageResource(item.get(i).getItem_pic1());

        return view;
    }
}
