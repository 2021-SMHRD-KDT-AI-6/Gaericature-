package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import com.example.project.Adapter.MyPurchaseAdapter;
import com.example.project.ExpandableHeightGridView;
import com.example.project.Loading2;
import com.example.project.R;
import com.example.project.RbPreference;
import com.example.project.VO.MyPurchaseVO;

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

public class MyPagePurchaseCompleteHistory extends AppCompatActivity {


    ArrayList<MyPurchaseVO> data;
    JSONArray jsonArray1, jsonArray2;
    MyPurchaseAdapter adapter;
    ExpandableHeightGridView gridView;
    Loading2 loading2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page_purchase_complete_history);

        gridView = findViewById(R.id.gridView3);

        loading2 = new Loading2(this);
        loading2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading2.setCancelable(false);
        loading2.show();

        Intent intent = getIntent();
        String PurchaseCompleteNum = intent.getExtras().getString("PurchaseCompleteNum");
        TextView tvAll = findViewById(R.id.tvAll3);
        tvAll.setText(PurchaseCompleteNum+"개");

        // 세션에서 아이디 가져오기
        RbPreference pref = new RbPreference(this);
        String user_id = pref.getValue("user_id", null);

        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormBody.Builder()
                .add("user_id", user_id)
                .build();
        String url = "http://192.168.0.115:5000/purchasecom";
        Request request = new Request.Builder().url(url).addHeader("Connection", "close").post(body).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                data = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    jsonArray1 = jsonObject.getJSONArray("purchase_list");
                    jsonArray2 = jsonObject.getJSONArray("purchase_pic");

                    for (int i = 0; i < jsonArray1.length(); i++){
                        MyPurchaseVO vo = new MyPurchaseVO();
                        byte[] b = Base64.decode(jsonArray2.getString(i), Base64.DEFAULT);
                        Bitmap img = BitmapFactory.decodeByteArray(b, 0, b.length);
                        vo.setItem_name(jsonArray1.getJSONArray(i).getString(0));
                        vo.setItem_price(jsonArray1.getJSONArray(i).getString(1));
                        vo.setItem_cnt(jsonArray1.getJSONArray(i).getString(3));
                        if (jsonArray1.getJSONArray(i).getString(4).equals("N")) {
                            vo.setDeli_yn("배송중");
                        }else {
                            vo.setDeli_yn("배송 완료");
                        }
                        vo.setItem_pic1(img);
                        data.add(vo);
                    }

                    Log.d("data", String.valueOf(data));

                    adapter = new MyPurchaseAdapter(getApplicationContext(), R.layout.mypurchaselist, data);
                    MyPurThread myPurThread = new MyPurThread(adapter);
                    myPurThread.start();
                    loading2.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            gridView.setExpanded(true);
            gridView.setAdapter(adapter);
        }
    };

    class MyPurThread extends Thread{
        MyPurchaseAdapter adapter;
        public MyPurThread(MyPurchaseAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public void run() {
            Message message = new Message();
            handler.sendMessage(message);
        }
    }
}