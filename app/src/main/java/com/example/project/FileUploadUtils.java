package com.example.project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FileUploadUtils {
    public static void send2Server(File file){
                    RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                .addFormDataPart("files",file.getName(),RequestBody.create(MultipartBody.FORM, file))
                    .build();
            Request request = new Request.Builder().url("http://192.168.0.115:5000/image").post(requestBody).build();



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


    }

    public static Bitmap test(int i){

        final Bitmap[] img = new Bitmap[1];
        RequestBody requestBody = new FormBody.Builder().add("cnt",String.valueOf(i)).build();
        Request request = new Request.Builder().url("http://192.168.0.115:5000/test2").post(requestBody).build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                InputStream inputStream = response.body().byteStream();
                img[0] = BitmapFactory.decodeStream(inputStream);








            }


        });

        return img[0];
    }
}
