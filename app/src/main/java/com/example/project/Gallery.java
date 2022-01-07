package com.example.project;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.FileUtils;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Gallery extends Fragment {

    ImageView imgGallery;
    Button btnGallery, btnCamera;
    private static final int PICK_IMAGE = 0;
    private static final int PICK_CAMERA = 1;
    File tempSelectFile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.fragment_gallery, container, false);

        btnGallery = fragment.findViewById(R.id.btnGallery);
        btnCamera = fragment.findViewById(R.id.btnCamera);
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