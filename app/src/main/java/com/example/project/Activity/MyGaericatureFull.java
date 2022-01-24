package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.BitmapConverter;
import com.example.project.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyGaericatureFull extends AppCompatActivity {

    ImageView imgMyGaericatureFull, imgCharNickCh;
    TextView tvCharNickCh;
    Button btnCharDel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_gaericature_full);

        imgMyGaericatureFull = findViewById(R.id.imgMyGaericatureFull);
        imgCharNickCh = findViewById(R.id.imgCharNickCh);
        tvCharNickCh = findViewById(R.id.tvCharNickCh);
        btnCharDel = findViewById(R.id.btnCharDel);

        byte[] byteArray = getIntent().getByteArrayExtra("image");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        imgMyGaericatureFull.setImageBitmap(bitmap);

        String nick = getIntent().getStringExtra("nick");
        tvCharNickCh.setText(nick);

        int deep_seq = getIntent().getIntExtra("deep_seq", 0);

        imgCharNickCh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CharNickPopup.class);
                intent.putExtra("deep_seq", deep_seq);
                intent.putExtra("image", byteArray);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        btnCharDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkHttpClient client = new OkHttpClient();

                RequestBody body = new FormBody.Builder()
                        .add("deep_seq", String.valueOf(deep_seq))
                        .build();

                String url = "http://172.30.1.12:5000/deepdel";

                Request request = new Request.Builder().url(url).addHeader("Connection","close").post(body).build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                        Intent intent = new Intent(getApplicationContext(), CartChangeActivity.class);
                        intent.putExtra("ck", 2);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}