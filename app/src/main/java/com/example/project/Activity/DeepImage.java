package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.project.BitmapConverter;
import com.example.project.R;

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
    File tempSelectFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_image);

        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnSave);
        imgDeep = findViewById(R.id.imgDeep);

        Intent intent = getIntent();

        byte[] b = intent.getByteArrayExtra("img");
        Bitmap img = BitmapFactory.decodeByteArray(b,0,b.length);

        imgDeep.setImageBitmap(img);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tempSelectFile = new File(Environment.getExternalStorageDirectory(), "temp.jpeg");
                OutputStream out = null;

                try {
                    out = new FileOutputStream(tempSelectFile);
                    Log.i("아웃스트림", "아웃스트림");
                    img.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    Log.i("컴프레스", "컴프레스");
                    //FileUploadUtils.send2Server(tempSelectFile);

                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("file",tempSelectFile.getName(),RequestBody.create(MultipartBody.FORM, tempSelectFile))
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



                        }
                    });

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });

//        String url = "http://192.168.0.115:5000/deepimage";
//        OkHttpClient client = new OkHttpClient();
//        RequestBody body = new FormBody.Builder().build();
//        Request request = new Request.Builder().url(url).post(body).build();
//        client.newCall(request).enqueue(new Callback() {
//
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                try {
//                    Log.d("response", String.valueOf(response));
//                    InputStream inputStream = response.body().byteStream();
//                    deepImage = BitmapFactory.decodeStream(inputStream);
//                    GalleryThread galleryThread = new GalleryThread(deepImage);
//                    galleryThread.start();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    Handler handler = new Handler(){
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            imgDeep.setImageBitmap(deepImage);
//        }
//    };
//
//    class GalleryThread extends Thread{
//        Bitmap deepImage;
//
//        public GalleryThread(Bitmap deepImage){
//            this.deepImage = deepImage;
//        }
//
//        @Override
//        public void run() {
//            Message message = new Message();
//            handler.sendMessage(message);
//        }
    }
}