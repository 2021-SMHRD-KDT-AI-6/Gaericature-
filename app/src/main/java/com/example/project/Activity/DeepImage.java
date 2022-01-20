package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.project.BitmapConverter;
import com.example.project.Fragment.MyPage;
import com.example.project.R;
import com.example.project.RbPreference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DeepImage extends AppCompatActivity {

    Button btnCancel, btnSave;
    ImageView imgDeep;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_image);






        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnSave);
        imgDeep = findViewById(R.id.imgDeep);

        RbPreference pref = new RbPreference(this);
        String user_id = pref.getValue("user_id", null);

        Intent intent = getIntent();

        byte[] b = intent.getByteArrayExtra("img");
        Bitmap img = BitmapFactory.decodeByteArray(b,0,b.length);

        imgDeep.setImageBitmap(img);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ContextWrapper cw = new ContextWrapper(getApplicationContext());
                File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                File tempSelectFile = new File(directory, "temp" + ".jpg");

                OutputStream out = null;

                try {
                    out = new FileOutputStream(tempSelectFile);
                    Log.i("아웃스트림", "아웃스트림");
                    img.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    Log.i("컴프레스", "컴프레스");

                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("file",tempSelectFile.getName(),RequestBody.create(MultipartBody.FORM, tempSelectFile))
                            .addFormDataPart("user_id",user_id)
                            .build();
                    Request request = new Request.Builder().url("http://172.30.1.12:5000/saveimage").post(requestBody).build();

                    OkHttpClient client = new OkHttpClient();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);


                        }
                    });

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}