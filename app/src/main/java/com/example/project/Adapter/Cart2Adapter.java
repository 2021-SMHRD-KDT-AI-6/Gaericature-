package com.example.project.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.project.R;
import com.example.project.RbPreference;
import com.example.project.VO.CartVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Cart2Adapter extends BaseAdapter {

    private ArrayList<CartVO> cart = new ArrayList<>();
    private Context context;
    private int layout;

    public Cart2Adapter(Context context, int layout, ArrayList<CartVO> cart) {
        this.cart = cart;
        this.context = context;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return cart.size();
    }

    @Override
    public Object getItem(int i) {
        return cart.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.cart2list, null);
        }

        RbPreference pref = new RbPreference(context.getApplicationContext());
        String user_id = pref.getValue("user_id", null);

        ImageView imgCartThumb = view.findViewById(R.id.imgCartThumb);
        TextView tvItemName = view.findViewById(R.id.tvItemName);
        TextView tvItemPrice = view.findViewById(R.id.tvItemPrice);
        TextView tvItemCnt = view.findViewById(R.id.tvItemCnt);

        int price = cart.get(i).getTvItemCnt() * cart.get(i).getTvItemPrice();

        imgCartThumb.setImageBitmap(cart.get(i).getImgCartThumb());
        tvItemName.setText(cart.get(i).getTvItemName());
        tvItemPrice.setText(price+"원");
        tvItemCnt.setText(cart.get(i).getTvItemCnt()+"개");

        String item_seq = String.valueOf(cart.get(i).getItemSeq());


        return view;
    }
}

