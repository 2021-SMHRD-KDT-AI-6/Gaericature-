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
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.project.Adapter.Cart2Adapter;
import com.example.project.Adapter.CartAdapter;
import com.example.project.Adapter.DeliveryAdapter;
import com.example.project.Adapter.PurchaseAdapter;
import com.example.project.ExpandableHeightGridView;
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
    CartAdapter cartAdapter;
    Cart2Adapter cart2Adapter;
    Button btnDelivery;

    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        gridViewPurchase = findViewById(R.id.gridViewPurchase);
        gridViewItem = findViewById(R.id.gridViewItem);
        btnDelivery = findViewById(R.id.btnDelivery);

        gridViewPurchase.setSelector(R.drawable.edge);

        RbPreference pref = new RbPreference(this);
        String user_id = pref.getValue("user_id", null);

        Intent get = getIntent();
        int seq = get.getIntExtra("seq",0);
        int check = Integer.parseInt(get.getStringExtra("purchase"));
        int cnt = get.getIntExtra("cnt",0);
        if (check == 1){
            url = "http://192.168.0.115:5000/delivery";
        }else if (check == 2){
            url = "http://192.168.0.115:5000/deliverycart";
        }


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

                    if( check == 2) {
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

                            CartList.add(vo);
                        }
                        cartAdapter = new CartAdapter(getApplicationContext(), R.layout.cartlist, CartList);
                        CartThread cartThread = new CartThread(cartAdapter);
                        cartThread.start();
                    }else if (check == 1){
                        for (int i = 0; i < cartPicArray.length(); i++) {
                            CartVO vo = new CartVO();
                            byte[] b = Base64.decode(cartPicArray.get(i).toString(), Base64.DEFAULT);
                            Bitmap img = BitmapFactory.decodeByteArray(b, 0, b.length);
                            vo.setImgCartThumb(img);

                            vo.setTvItemName((String) cartArray.getJSONArray(i).get(0));
                            vo.setTvItemPrice((Integer) cartArray.getJSONArray(i).get(1));
                            vo.setTvItemCnt(cnt);
                            vo.setItemSeq(String.valueOf(seq));

                            Log.i("name : ",vo.getTvItemName());
                            Log.i("cnt : ",String.valueOf(vo.getTvItemCnt()));

                            CartList.add(vo);
                        }
                        cart2Adapter = new Cart2Adapter(getApplicationContext(), R.layout.cart2list, CartList);
                        Cart2Thread cart2Thread = new Cart2Thread(cart2Adapter);
                        cart2Thread.start();
                    }

                    adapter = new DeliveryAdapter(getApplicationContext(), R.layout.delivery, DeliList);
                    DeliveryThread deliveryThread = new DeliveryThread(adapter);
                    deliveryThread.start();





                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        });

        btnDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PopupActivity.class);
                startActivity(intent);
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


    Handler handler2 = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            gridViewItem.setExpanded(true);
            gridViewItem.setAdapter(cartAdapter);
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
            handler2.sendMessage(message);
        }
    }

    Handler handler3 = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            gridViewItem.setExpanded(true);
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
}