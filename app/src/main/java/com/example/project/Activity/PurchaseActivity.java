package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.project.Adapter.Cart2Adapter;
import com.example.project.Adapter.CartAdapter;
import com.example.project.Adapter.DeliveryAdapter;
import com.example.project.ExpandableHeightGridView;
import com.example.project.Loading2;
import com.example.project.R;
import com.example.project.RbPreference;
import com.example.project.VO.CartVO;
import com.example.project.VO.DeliveryVO;

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

public class PurchaseActivity extends AppCompatActivity {

    ExpandableHeightGridView gridViewPurchase, gridViewItem;
    ArrayList<DeliveryVO> DeliList = new ArrayList<>();
    ArrayList<CartVO> CartList = new ArrayList<>();

    DeliveryAdapter adapter;
    Cart2Adapter cart2Adapter;
    Button btnDelivery, btnBuy;
    Loading2 loading2;

    TextView tvAllPrice, tvDeliNull;

    public static Context mContext;
    public int purchaseType;

    String url;
    int price = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        mContext = this;

        gridViewPurchase = findViewById(R.id.gridViewPurchase);
        gridViewItem = findViewById(R.id.gridViewItem);
        btnDelivery = findViewById(R.id.btnDelivery);
        btnBuy = findViewById(R.id.btnBuy);

        tvAllPrice=findViewById(R.id.tvAllPrice);
        tvDeliNull=findViewById(R.id.tvDeliNull);

        tvDeliNull.setVisibility(View.GONE);


        gridViewPurchase.setSelector(R.drawable.edge);

        loading2 = new Loading2(this);
        loading2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading2.setCancelable(false);
        loading2.show();

        RbPreference pref = new RbPreference(this);
        String user_id = pref.getValue("user_id", null);

        Intent get = getIntent();
        int seq = get.getIntExtra("seq",0);

        // Cart에서 왔으면 2 , PurchaseDetail에서 왔으면 1
        purchaseType = Integer.parseInt(get.getStringExtra("purchaseType"));
        if (purchaseType == 1){
            url = "http://172.30.1.12:5000/delivery";
        }else if (purchaseType == 2){
            url = "http://172.30.1.12:5000/deliverycart";
        }

        // PurchaseDetail에서 넘어온 경우, 아이템 갯수를 받아와준다.
        int cnt = get.getIntExtra("cnt",0);

        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormBody.Builder()
                .add("user_id", user_id)
                .add("seq", String.valueOf(seq))
                .add("cnt", String.valueOf(cnt))
                .build();

        Request request = new Request.Builder().url(url).addHeader("Connection","close").post(body).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                DeliList = new ArrayList<>();
                CartList= new ArrayList<>();

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    JSONArray jsonArray = jsonObject.getJSONArray("delivery_list");
                    JSONArray cartPicArray = jsonObject.getJSONArray("cart_pic");
                    JSONArray cartArray = jsonObject.getJSONArray("cart_list");


                    if(jsonArray.length() == 0){
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                tvDeliNull.setVisibility(View.VISIBLE);
                            }
                        }, 0);
                    }
                    for(int i=0;i<jsonArray.length();i++){
                        DeliveryVO vo = new DeliveryVO();
                        vo.setTvDname((String) jsonArray.getJSONArray(i).get(0));
                        vo.setTvAddr((String) jsonArray.getJSONArray(i).get(2));
                        vo.setTvPhone((String) jsonArray.getJSONArray(i).get(1));
                        vo.setDeli_seq(String.valueOf(jsonArray.getJSONArray(i).get(3)) );

                        Log.i("name :: ",vo.getTvDname());
                        Log.i("addr :: ",vo.getTvAddr());
                        Log.i("phone :: ",vo.getTvPhone());
                        Log.i("seq :: ",vo.getDeli_seq());

                        DeliList.add(vo);
                    }

                    if( purchaseType == 2) {
                        for (int i = 0; i < cartPicArray.length(); i++) {
                            CartVO vo = new CartVO();
                            byte[] b = Base64.decode(cartPicArray.get(i).toString(), Base64.DEFAULT);
                            Bitmap img = BitmapFactory.decodeByteArray(b, 0, b.length);
                            vo.setImgCartThumb(img);

                            vo.setTvItemName((String) cartArray.getJSONArray(i).get(0));
                            vo.setTvItemPrice((Integer) cartArray.getJSONArray(i).get(1));
                            vo.setTvItemCnt((Integer) cartArray.getJSONArray(i).get(3));
                            vo.setCartSeq((Integer) cartArray.getJSONArray(i).get(4));
                            vo.setItemSeq(String.valueOf(cartArray.getJSONArray(i).get(5)));
                            price += (Integer) cartArray.getJSONArray(i).get(1) * (Integer) cartArray.getJSONArray(i).get(3);

                            CartList.add(vo);
                        }

                    }else if (purchaseType == 1){
                        for (int i = 0; i < cartPicArray.length(); i++) {
                            CartVO vo = new CartVO();
                            byte[] b = Base64.decode(cartPicArray.get(i).toString(), Base64.DEFAULT);
                            Bitmap img = BitmapFactory.decodeByteArray(b, 0, b.length);
                            vo.setImgCartThumb(img);

                            vo.setTvItemName((String) cartArray.getJSONArray(i).get(0));
                            vo.setTvItemPrice((Integer) cartArray.getJSONArray(i).get(1));
                            vo.setTvItemCnt(cnt);
                            vo.setItemSeq(String.valueOf(seq));

                            price +=(Integer) cartArray.getJSONArray(i).get(1);

                            Log.i("name : ",vo.getTvItemName());
                            Log.i("cnt : ",String.valueOf(vo.getTvItemCnt()));

                            CartList.add(vo);
                        }

                    }

                    cart2Adapter = new Cart2Adapter(getApplicationContext(), R.layout.cart2list, CartList);
                    Cart2Thread cart2Thread = new Cart2Thread(cart2Adapter);
                    cart2Thread.start();

                    adapter = new DeliveryAdapter(getApplicationContext(), R.layout.delivery, DeliList);
                    DeliveryThread deliveryThread = new DeliveryThread(adapter);
                    deliveryThread.start();
                    loading2.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        });

        btnDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PopupActivity.class);
                intent.putExtra("purchase", String.valueOf(purchaseType));
                startActivity(intent);
            }
        });

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestBody body = new FormBody.Builder()
                        .add("user_id", user_id)
                        .add("check", String.valueOf(purchaseType))
                        .add("seq", String.valueOf(seq))
                        .add("cnt", String.valueOf(cnt))
                        .build();

                Request request = new Request.Builder().url("http://192.168.0.115:5000/buy").addHeader("Connection","close").post(body).build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        Intent intent = new Intent(getApplicationContext(), MyPagePurchaseAllHistory.class);
                        intent.putExtra("PurchaseAllNum", response.body().string());
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });


    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            gridViewPurchase.setExpanded(true);
            gridViewPurchase.setAdapter(adapter);
        }
    };


    class DeliveryThread extends Thread{
        DeliveryAdapter adapter;

        public DeliveryThread(DeliveryAdapter adapter){
            this.adapter=adapter;
        }

        @Override
        public void run() {
            Message message = new Message();
            handler.sendMessage(message);
        }
    }

    Handler handler3 = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            gridViewItem.setExpanded(true);
            tvAllPrice.setText(price+"원");
            gridViewItem.setAdapter(cart2Adapter);
        }
    };

    class Cart2Thread extends Thread{

        Cart2Adapter cart2Adapter;

        public Cart2Thread(Cart2Adapter cart2Adapter) {
            this.cart2Adapter = cart2Adapter;
        }

        @Override
        public void run() {
            Message message = new Message();
            handler3.sendMessage(message);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        overridePendingTransition(0,0);
    }
}