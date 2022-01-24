package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.RbPreference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CharNickPopup extends AppCompatActivity {

    EditText edCharNick;
    Button btnCharNickCom;
    ImageView imgMyGaericatureFull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_char_nick_popup);

        RbPreference pref = new RbPreference(getApplicationContext());
        String url = pref.getValueUrl("url", null);

        int deep_seq = getIntent().getIntExtra("deep_seq", 0);
        byte[] byteArray = getIntent().getByteArrayExtra("image");

        btnCharNickCom = findViewById(R.id.btnCharNickCom);
        edCharNick = findViewById(R.id.edCharNick);
        imgMyGaericatureFull = findViewById(R.id.imgMyGaericatureFull);

        btnCharNickCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String deep_nick = edCharNick.getText().toString();

                if (deep_nick.equals("")) {
                    Toast.makeText(getApplicationContext(), "별명을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("deep_nick", deep_nick)
                            .addFormDataPart("deep_seq", String.valueOf(deep_seq))
                            .build();

                    Request request = new Request.Builder().url(url + "/deepnick")
                            .addHeader("Connection", "close")
                            .post(requestBody).build();

                    OkHttpClient client = new OkHttpClient();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                            Intent intent = new Intent(getApplicationContext(), MyGaericatureFull.class);
                            intent.putExtra("deep_seq", deep_seq);
                            intent.putExtra("nick", deep_nick);
                            intent.putExtra("image", byteArray);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
        });
    }
}