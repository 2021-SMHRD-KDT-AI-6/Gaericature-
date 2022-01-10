package com.example.project.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.FileUtils;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.project.Activity.DeepImage;
import com.example.project.Adapter.MyGaericatureAdapter;
import com.example.project.BitmapConverter;
import com.example.project.FileUploadUtils;
import com.example.project.R;
import com.example.project.VO.MyGaericatureVO;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Gallery extends Fragment {

    ImageView imgGallery;
    Button btnGallery, btnCamera, btnChange;
    private static final int PICK_IMAGE = 0;
    private static final int PICK_CAMERA = 1;
    File tempSelectFile;
    Bitmap deepImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.fragment_gallery, container, false);

        btnGallery = fragment.findViewById(R.id.btnGallery);
        btnCamera = fragment.findViewById(R.id.btnCamera);
        btnChange = fragment.findViewById(R.id.btnChange);
        imgGallery = fragment.findViewById(R.id.imgGallery);



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

//            String url = "http://192.168.0.115:5000/image";
//            OkHttpClient client = new OkHttpClient();

            BitmapDrawable img = (BitmapDrawable) imgGallery.getDrawable();
            Bitmap bitmap = img.getBitmap();

            @Override
            public void onClick(View view) {


                tempSelectFile = new File(Environment.getExternalStorageDirectory(), "temp.jpeg");


                Log.i("빈 파일 생성 성공", "빈파일 생성 성공");
                OutputStream out = null;
                Log.i("널 아웃풋스트림", "널 아웃풋스트림");

                try {
                    out = new FileOutputStream(tempSelectFile);
                    Log.i("아웃스트림", "아웃스트림");
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    Log.i("컴프레스", "컴프레스");
                    //FileUploadUtils.send2Server(tempSelectFile);

                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("files",tempSelectFile.getName(),RequestBody.create(MultipartBody.FORM, tempSelectFile))
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

                            InputStream inputStream = response.body().byteStream();
                            deepImage = BitmapFactory.decodeStream(inputStream);
                            String deepImage2 = BitmapConverter.BitmapToString(deepImage);

                            Intent intent = new Intent(getActivity().getApplicationContext(), DeepImage.class);
                            intent.putExtra("img", deepImage2);
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
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                imgGallery.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == PICK_CAMERA && resultCode == Activity.RESULT_OK) {


            // Bundle로 데이터를 입력
            Bundle extras = data.getExtras();
            // Bitmap으로 컨버전
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            // 이미지뷰에 Bitmap으로 이미지를 입력
            imgGallery.setImageBitmap(imageBitmap);


            String imgconvert = BitmapConverter.BitmapToString(imageBitmap);
            Log.i("test : ",imgconvert);


//            String date = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date());
            tempSelectFile = new File(Environment.getExternalStorageDirectory(), "temp.jpeg");



            Log.i("빈 파일 생성 성공", "빈파일 생성 성공");
            OutputStream out = null;
            Log.i("널 아웃풋스트림", "널 아웃풋스트림");

            try {
                out = new FileOutputStream(tempSelectFile);
                Log.i("아웃스트림", "아웃스트림");
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                Log.i("컴프레스", "컴프레스");
                FileUploadUtils.send2Server(tempSelectFile);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


}