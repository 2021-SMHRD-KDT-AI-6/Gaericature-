package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.project.Adapter.CartAdapter;
import com.example.project.ExpandableHeightGridView;
import com.example.project.Fragment.Purchase;
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

public class CartActivity extends AppCompatActivity {

    TextView tvCartAll;
    Button btnCartPurchase;
    ExpandableHeightGridView gridViewCart;
    ArrayList<CartVO> data = new ArrayList<>();

    CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        tvCartAll = findViewById(R.id.tvCartAll);
        gridViewCart = findViewById(R.id.gridViewCart);
        btnCartPurchase = findViewById(R.id.btnPurchase);

        Intent intent = getIntent();
        String cnt = intent.getStringExtra("cnt");
        tvCartAll.setText(cnt.toString()+"개");

        RbPreference pref = new RbPreference(this);
        String user_id = pref.getValue("user_id", null);

        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormBody.Builder()
                .add("user_id", user_id)
                .build();
        String url = "http://172.30.1.12:5000/cart";
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
                cartAdapter = new CartAdapter(getApplicationContext(), R.layout.gaericaturelist, data);

                CartThread cartThread = new CartThread(cartAdapter);
                cartThread.start();
            }
        });

        btnCartPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Purchase.class);
                startActivity(intent);
            }
        });

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