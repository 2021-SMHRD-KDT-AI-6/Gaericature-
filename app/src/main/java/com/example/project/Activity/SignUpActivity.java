package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.project.Adapter.MyGaericatureAdapter;
import com.example.project.BitmapConverter;
import com.example.project.FileUploadUtils;
import com.example.project.R;
import com.example.project.VO.MyGaericatureVO;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUpActivity extends AppCompatActivity {

    ImageView imgSignUp;
    EditText edSignUpId, edSignUpPw, edSignUpNick;
    Button btnSignUpBack, btnSignUpComplete;
    ImageView btnSignUpGallery;

    int PICK_IMAGE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edSignUpId = findViewById(R.id.edSignUpId);
        edSignUpPw = findViewById(R.id.edSignUpPw);
        edSignUpNick = findViewById(R.id.edSignUpNick);
        btnSignUpGallery = findViewById(R.id.btnSignUpGallery);
        btnSignUpBack = findViewById(R.id.btnSignUpBack);
        btnSignUpComplete = findViewById(R.id.btnSignUpComplete);
        imgSignUp = findViewById(R.id.imgSignUp);

        imgSignUp.setBackground(new ShapeDrawable(new OvalShape()));
        imgSignUp.setClipToOutline(true);

        btnSignUpGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        btnSignUpBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnSignUpComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_id = edSignUpId.getText().toString();
                String user_pw = edSignUpPw.getText().toString();
                String user_nick = edSignUpNick.getText().toString();

                BitmapDrawable img = (BitmapDrawable) imgSignUp.getDrawable();
                Bitmap bitmap = img.getBitmap();

                String url = "http://172.30.1.12:5000/signup";

                File tempSelectFile = new File(Environment.getExternalStorageDirectory(), "temp.jpeg");
                OutputStream out = null;
                try {
                    out = new FileOutputStream(tempSelectFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                OkHttpClient client = new OkHttpClient();

                RequestBody body = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("user_id", user_id)
                        .addFormDataPart("user_pw", user_pw)
                        .addFormDataPart("user_nick", user_nick)
                        .addFormDataPart("user_profile",tempSelectFile.getName(),RequestBody.create(MultipartBody.FORM, tempSelectFile))
                        .build();

                Request request = new Request.Builder().url(url).post(body).build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        try {
                            Handler handler = new Handler(Looper.getMainLooper());
                            String result = response.body().string();
                            if (result.equals("2")){
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "이미 사용중인 아이디입니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }, 0);
                            }else if (result.equals("3")) {
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "이미 사용중인 닉네임입니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }, 0);
                            }else if (result.equals("1")) {
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "회원가입 성공!", Toast.LENGTH_SHORT).show();
                                    }
                                }, 0);
                                Intent intent = new Intent(getApplicationContext(), BeginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imgSignUp.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}