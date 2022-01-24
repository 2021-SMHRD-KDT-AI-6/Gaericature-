package com.example.project.Adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.R;
import com.example.project.VO.MyGaericatureVO;

import java.util.ArrayList;

public class CharAdapter extends BaseAdapter {

    private ArrayList<MyGaericatureVO> gaericature = new ArrayList<>();
    private Context context;
    private int layout;

    public CharAdapter(Context context, int layout, ArrayList<MyGaericatureVO> gaericature){
        this.gaericature=gaericature;
        this.context=context;
        this.layout=layout;
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
            view = inflater.inflate(R.layout.characterlist, null);
        }

        ImageView imgMyChar = view.findViewById(R.id.imgMyChar);
        imgMyChar.setImageBitmap(gaericature.get(i).getImg());

        TextView tvCharNick = view.findViewById(R.id.tvCharNick);
        tvCharNick.setText(gaericature.get(i).getCharNick());

        return view;
    }
}
