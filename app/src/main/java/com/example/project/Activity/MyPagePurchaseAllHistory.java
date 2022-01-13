package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.Adapter.MyGaericatureAdapter;
import com.example.project.Adapter.MyPurchaseAdapter;
import com.example.project.ExpandableHeightGridView;
import com.example.project.R;
import com.example.project.RbPreference;
import com.example.project.VO.MyGaericatureVO;
import com.example.project.VO.MyPurchaseVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyPagePurchaseAllHistory extends AppCompatActivity {


    ArrayList<MyPurchaseVO> data;
    JSONArray jsonArray1, jsonArray2;
    MyPurchaseAdapter adapter;
    ExpandableHeightGridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_all_history);

        gridView = findViewById(R.id.gridView);

        Intent intent = getIntent();
        String PurchaseAllNum = intent.getExtras().getString("PurchaseAllNum");
        TextView tvAll = findViewById(R.id.tvAll);
        tvAll.setText(PurchaseAllNum+"개");

        // 세션에서 아이디 가져오기
        RbPreference pref = new RbPreference(this);
        String user_id = pref.getValue("user_id", null);

        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormBody.Builder()
                                       .add("user_id", user_id)
                                       .build();
        String url = "http://172.30.1.12:5000/purchaseall";
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

                    byte[] b = Base64.decode(jsonArray2.getString(0), Base64.DEFAULT);
                    Bitmap img = BitmapFactory.decodeByteArray(b, 0, b.length);

                    data.add(new MyPurchaseVO(jsonArray1.getJSONArray(0).getString(0), jsonArray1.getJSONArray(0).getString(1),
                            jsonArray1.getJSONArray(0).getString(3), jsonArray1.getJSONArray(0).getString(4), img));
                    Log.d("data", data.get(0).getItem_cnt());

                    adapter = new MyPurchaseAdapter(getApplicationContext(), R.layout.mypurchaselist, data);
                    MyPurThread myPurThread = new MyPurThread(adapter);
                    myPurThread.start();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
//            gridView.setExpanded(true);
            MyPurchaseAdapter adapter = (MyPurchaseAdapter) msg.obj;
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
            message.obj = adapter;
            handler.sendMessage(message);
        }
    }
}