package com.example.project.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.R;
import com.example.project.VO.MyGaericatureVO;

import java.util.ArrayList;

public class MyGaericatureAdapter extends BaseAdapter {

    private ArrayList<MyGaericatureVO> gaericature = new ArrayList<>();
    private Context context;
    private int layout;

    public MyGaericatureAdapter(Context context, int layout, ArrayList<MyGaericatureVO> gaericature) {
        this.gaericature = gaericature;
        this.context = context;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return gaericature.size();
    }

    @Override
    public Object getItem(int i) {
        return gaericature.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.gaericaturelist, null);
        }

        ImageView imgMyGaericature = view.findViewById(R.id.imgMyGaericature);
        imgMyGaericature.setImageBitmap(gaericature.get(i).getImg());

        return view;
    }
}
