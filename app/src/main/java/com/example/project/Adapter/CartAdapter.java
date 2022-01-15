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

public class CartAdapter extends BaseAdapter {

    private ArrayList<CartVO> cart = new ArrayList<>();
    private Context context;
    private int layout;

    public CartAdapter(Context context, int layout, ArrayList<CartVO> cart) {
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
            view = inflater.inflate(R.layout.cartlist, null);
        }

        RbPreference pref = new RbPreference(context.getApplicationContext());
        String user_id = pref.getValue("user_id", null);
        
        ImageView imgCartThumb = view.findViewById(R.id.imgCartThumb);
        TextView tvItemName = view.findViewById(R.id.tvItemName);
        TextView tvItemPrice = view.findViewById(R.id.tvItemPrice);
        TextView tvItemCnt = view.findViewById(R.id.tvItemCnt);
        TextView btnCartPlus = view.findViewById(R.id.btnCartPlus);
        TextView btnCartMinus = view.findViewById(R.id.btnCartMinus);
        TextView btnCartDelete = view.findViewById(R.id.btnCartDelete);

        int price = cart.get(i).getTvItemCnt() * cart.get(i).getTvItemPrice();

        imgCartThumb.setImageBitmap(cart.get(i).getImgCartThumb());
        tvItemName.setText(cart.get(i).getTvItemName());
        tvItemPrice.setText(String.valueOf(price));
        tvItemCnt.setText(String.valueOf(cart.get(i).getTvItemCnt()));

        String item_seq = String.valueOf(cart.get(i).getItemSeq());

        btnCartPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OkHttpClient client = new OkHttpClient();

                RequestBody body = new FormBody.Builder()
                        .add("user_id", user_id)
                        .add("item_seq", item_seq)
                        .build();
                String url = "http://172.30.1.12:5000/cartplus";
                Request request = new Request.Builder().url(url).addHeader("Connection", "close").post(body).build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String result = response.body().string();
                        if (result.equals("1")){
                            int count = Integer.parseInt(tvItemCnt.getText().toString());
                            count = count + 1;
                            tvItemCnt.setText(String.valueOf(count));
                        }
                    }
                });
            }
        });

        btnCartMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int[] count = {Integer.parseInt(tvItemCnt.getText().toString())};

                if (count[0] > 1){
                    OkHttpClient client = new OkHttpClient();

                    RequestBody body = new FormBody.Builder()
                            .add("user_id", user_id)
                            .add("item_seq", item_seq)
                            .build();
                    String url = "http://172.30.1.12:5000/cartminus";
                    Request request = new Request.Builder().url(url).addHeader("Connection", "close").post(body).build();


                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            String result = response.body().string();
                            if (result.equals("1")) {
                                count[0] = count[0] - 1;
                                tvItemCnt.setText(String.valueOf(count[0]));
                            }
                        }
                    });
                }
            }
        });

        btnCartDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int cart_seq = cart.get(i).getCartSeq();

                OkHttpClient client = new OkHttpClient();

                RequestBody body = new FormBody.Builder()
                        .add("cart_seq", String.valueOf(cart_seq))
                        .build();
                String url = "http://172.30.1.12:5000/cartdelete";
                Request request = new Request.Builder().url(url).addHeader("Connection", "close").post(body).build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String result = response.body().string();
                        if (result.equals("1")){
                            cart.remove(i);
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    notifyDataSetChanged();
                                }
                            }, 0);
                        }
                    }
                });
            }
        });
        return view;
    }
}

