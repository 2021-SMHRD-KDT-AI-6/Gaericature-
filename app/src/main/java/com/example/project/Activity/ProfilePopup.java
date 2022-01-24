package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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

public class ProfilePopup extends AppCompatActivity {

    ImageView imgChange, imgGalleryChange;
    EditText edNickChange;
    Button btnChangeCom;
    private static final int PICK_IMAGE = 0;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_popup);

        imgChange = findViewById(R.id.imgChange);
        edNickChange = findViewById(R.id.edNickChange);
        imgGalleryChange = findViewById(R.id.imgGalleryChange);
        btnChangeCom = findViewById(R.id.btnChangeCom);

        Drawable drawable = imgChange.getDrawable();
        bitmap = ((BitmapDrawable) drawable).getBitmap();

        RbPreference pref = new RbPreference(this);
        String user_id = pref.getValue("user_id", null);

        imgChange.setBackground(new ShapeDrawable(new OvalShape()));
        imgChange.setClipToOutline(true);

        imgGalleryChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        btnChangeCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nick = edNickChange.getText().toString();

                if (nick.equals("")){
                    Toast.makeText(getApplicationContext(), "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    ContextWrapper cw = new ContextWrapper(getApplicationContext());
                    File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                    File tempSelectFile = new File(directory, "temp" + ".jpg");
                    OutputStream out = null;

                    try {
                        out = new FileOutputStream(tempSelectFile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);

                        RequestBody requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("files", tempSelectFile.getName(),RequestBody.create(MultipartBody.FORM, tempSelectFile))
                                .addFormDataPart("nick", nick)
                                .addFormDataPart("user_id", user_id)
                                .build();

                        Request request = new Request.Builder().url("http://172.30.1.12:5000/profilecorr")
                                .addHeader("Connection","close")
                                .post(requestBody).build();

                        OkHttpClient client = new OkHttpClient();

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.putExtra("ck", 3);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imgChange.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
    }
}