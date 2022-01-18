package com.example.project.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import com.example.project.Activity.PurchaseActivity;
import com.example.project.Activity.PurchaseDetail;
import com.example.project.Adapter.CartAdapter;
import com.example.project.ExpandableHeightGridView;
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

public class Cart extends Fragment {

    Button btnCartPurchase;
    ExpandableHeightGridView gridViewCart;
    ArrayList<CartVO> data = new ArrayList<>();

    CartAdapter cartAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.fragment_cart, container, false);

        gridViewCart = fragment.findViewById(R.id.gridViewCart);
        btnCartPurchase = fragment.findViewById(R.id.btnPurchase);

        RbPreference pref = new RbPreference(getActivity().getApplicationContext());
        String user_id = pref.getValue("user_id", null);

        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormBody.Builder()
                .add("user_id", user_id)
                .build();
        String url = "http://192.168.0.115:5000/cart";
        Request request = new Request.Builder().url(url).addHeader("Connection", "close").post(body).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    JSONArray jsonArray1 = jsonObject.getJSONArray("cart_pic");
                    JSONArray jsonArray2 = jsonObject.getJSONArray("cart_list");
                    for(int i = 0; i < jsonArray1.length(); i++) {
                        CartVO cartVO = new CartVO();
                        byte[] b;
                        b = Base64.decode(jsonArray1.get(i).toString(), Base64.DEFAULT);
                        Bitmap img = BitmapFactory.decodeByteArray(b, 0, b.length);
                        cartVO.setImgCartThumb(img);

                        cartVO.setTvItemName((String) jsonArray2.getJSONArray(i).get(0));
                        cartVO.setTvItemPrice((Integer) jsonArray2.getJSONArray(i).get(1));
                        cartVO.setTvItemCnt((Integer) jsonArray2.getJSONArray(i).get(3));
                        cartVO.setCartSeq((Integer) jsonArray2.getJSONArray(i).get(4));
                        cartVO.setItemSeq(String.valueOf(jsonArray2.getJSONArray(i).get(5)));

                        data.add(cartVO);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                cartAdapter = new CartAdapter(getActivity().getApplicationContext(), R.layout.cartlist, data);

                CartThread cartThread = new CartThread(cartAdapter);
                cartThread.start();
            }
        });

        btnCartPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), PurchaseActivity.class);
                intent.putExtra("purchase","2");
                startActivity(intent);
            }
        });

        gridViewCart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity().getApplicationContext(), PurchaseDetail.class);
                intent.putExtra("seq", Integer.parseInt(data.get(i).getItemSeq()));
                startActivity(intent);
            }
        });

        return fragment;
    }


    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            gridViewCart.setAdapter(cartAdapter);
        }
    };

    class CartThread extends Thread{

        CartAdapter cartAdapter;

        public CartThread(CartAdapter cartAdapter) {
            this.cartAdapter = cartAdapter;
        }

        @Override
        public void run() {
            Message message = new Message();
            handler.sendMessage(message);
        }
    }


}