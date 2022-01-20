package com.example.project.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.project.Activity.DeepImage;
import com.example.project.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Home extends Fragment {

    ImageView imgGallery;
    Button btnGallery, btnCamera, btnChange;
    private static final int PICK_IMAGE = 0;
    private static final int PICK_CAMERA = 1;
    File tempSelectFile;
    Bitmap deepImage, bitmap;
    TextView tvGallery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.fragment_home, container, false);


        btnGallery = fragment.findViewById(R.id.btnGallery);
        btnCamera = fragment.findViewById(R.id.btnCamera);
        btnChange = fragment.findViewById(R.id.btnChange);
        imgGallery = fragment.findViewById(R.id.imgGallery);
        tvGallery = fragment.findViewById(R.id.tvGallery);
        btnChange.setVisibility(View.GONE);

        btnGallery.bringToFront();
        btnChange.bringToFront();
        btnCamera.bringToFront();
        imgGallery.bringToFront();


        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, PICK_CAMERA);
            }
        });

        btnChange.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

//                tempSelectFile = new File(Environment.getExternalStorageDirectory(), "temp.jpeg");
                ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
                File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                File tempSelectFile = new File(directory, "temp" + ".jpg");
                Log.i("빈 파일 생성 성공", "빈파일 생성 성공");
                OutputStream out = null;
                Log.i("널 아웃풋스트림", "널 아웃풋스트림");

                try {
                    out = new FileOutputStream(tempSelectFile);
                    Log.i("아웃스트림", "아웃스트림");

                    float scale = (float) (1024/(float)bitmap.getWidth());
                    int image_w = (int) (bitmap.getWidth() * scale);
                    int image_h = (int) (bitmap.getHeight() * scale);
                    Bitmap resize = Bitmap.createScaledBitmap(bitmap,672,896,true);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);






//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    Log.i("컴프레스", "컴프레스");

                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("files",tempSelectFile.getName(),RequestBody.create(MultipartBody.FORM, tempSelectFile))
                            .build();
                    Request request = new Request.Builder().url("http://192.168.0.115:5000/image")
                            .addHeader("Connection","close")
                            .post(requestBody).build();



                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(100, TimeUnit.SECONDS)
                            .readTimeout(100, TimeUnit.SECONDS)
                            .writeTimeout(100, TimeUnit.SECONDS)
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                            InputStream inputStream = response.body().byteStream();
                            deepImage = BitmapFactory.decodeStream(inputStream);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();

                            float scale = (float) (1024/(float)deepImage.getWidth());
                            int image_w = (int) (deepImage.getWidth() * scale);
                            int image_h = (int) (deepImage.getHeight() * scale);
                            Bitmap resize = Bitmap.createScaledBitmap(deepImage,image_w,image_h,true);
                            resize.compress(Bitmap.CompressFormat.JPEG,100,stream);
                            byte[] b = stream.toByteArray();
                            Log.i("byte test :: ", b.toString());
                            Log.i("bytelength test :: ", String.valueOf(b.length));


                            Intent intent = new Intent(getActivity(), DeepImage.class);
                            intent.putExtra("img", b);
                            startActivity(intent);
                        }
                    });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {

            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                imgGallery.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == PICK_CAMERA && resultCode == Activity.RESULT_OK) {

            // Bundle로 데이터를 입력
            Bundle extras = data.getExtras();
            // Bitmap으로 컨버전
            bitmap = (Bitmap) extras.get("data");
            // 이미지뷰에 Bitmap으로 이미지를 입력
            imgGallery.setImageBitmap(bitmap);
        }

        btnGallery.setVisibility(View.GONE);
        btnCamera.setVisibility(View.GONE);
        btnChange.setVisibility(View.VISIBLE);
        tvGallery.setText("변환 버튼을 눌러주세요.");
    }

}