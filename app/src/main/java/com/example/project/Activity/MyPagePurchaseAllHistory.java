package com.example.project.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.R;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyPagePurchaseAllHistory extends AppCompatActivity {

    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_all_history);

        Intent intent = getIntent();
        String PurchaseAllNum = intent.getExtras().getString("PurchaseAllNum");
        TextView tvAll = findViewById(R.id.tvAll);
        tvAll.setText(PurchaseAllNum);
        ImageView img = findViewById(R.id.img);

        Thread thread = new Thread(){
            @Override
            public void run() {
                try{
                    URL url = new URL("http://172.30.1.12/C:/Users/smhrd/project2/고양이.jpg");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setDoInput(true);

                    conn.connect();

                    InputStream inputStream = conn.getInputStream();

                    bitmap = BitmapFactory.decodeStream(inputStream);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        thread.start();

        try {
            thread.join();
            img.setImageBitmap(bitmap);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}