package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.Adapter.MyGaericatureAdapter;
import com.example.project.R;
import com.example.project.VO.MyGaericatureVO;
import com.example.project.VO.MyPurchaseVO;

import org.json.JSONArray;
import org.json.JSONException;

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

    String url = "http://172.30.1.12:5000/purchaseall";
    ArrayList<MyPurchaseVO> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_all_history);

        Intent intent = getIntent();
        String PurchaseAllNum = intent.getExtras().getString("PurchaseAllNum");
        TextView tvAll = findViewById(R.id.tvAll);
        tvAll.setText(PurchaseAllNum+"개");

        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormBody.Builder().build();

        Request request = new Request.Builder().url(url).post(body).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                
            }
        });

    }
}