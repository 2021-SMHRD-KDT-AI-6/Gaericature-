package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.BitmapConverter;
import com.example.project.R;
import com.example.project.VO.itemVO;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class PurchaseDetail extends AppCompatActivity {

    TextView tvName, tvPrice, tvContent;
    Button btnPurchase;
    Bitmap test;
    ImageView imgPurchase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_detail);

        tvContent=findViewById(R.id.tvContent);
        tvName=findViewById(R.id.tvName);
        tvPrice=findViewById(R.id.tvPrice);
        btnPurchase=findViewById(R.id.btnPurchase);
        imgPurchase=findViewById(R.id.imgPurchase);


        Intent intent = getIntent();
        ArrayList<itemVO> list = (ArrayList<itemVO>) intent.getSerializableExtra("list");

        String name = list.get(0).getItem_name();
        String content = list.get(0).getItem_content();
        int price = list.get(0).getItem_price();



        tvContent.setText(content);
        tvPrice.setText(String.valueOf(price));
        tvName.setText(name);


        btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OkHttpClient client = new OkHttpClient();
                RequestBody body = new FormBody.Builder().build();
                Request request = new Request.Builder().url("http://192.168.0.115:5000/loadimage").post(body).build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        try{

                            ResponseBody in = response.body();


                            InputStream inputStream = in.byteStream();
                            BufferedInputStream buffer = new BufferedInputStream(inputStream);


                            test = BitmapFactory.decodeStream(buffer);


                            imgPurchase.setImageBitmap(test);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });


    }
}