package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.project.R;
import com.example.project.RbPreference;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PopupActivity extends AppCompatActivity {

    Button btnAdd;
    EditText edtDname, edtPhone, edtAddr, edtTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        Intent intent = getIntent();

        btnAdd = findViewById(R.id.btnAdd);
        edtDname = findViewById(R.id.edtDname);
        edtAddr = findViewById(R.id.edtAddr);
        edtPhone = findViewById(R.id.edtPhone);
        edtTag = findViewById(R.id.edtTag);

        RbPreference pref = new RbPreference(this);
        String user_id = pref.getValue("user_id", null);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OkHttpClient client = new OkHttpClient.Builder().build();

                RequestBody body = new FormBody.Builder()
                        .add("dname",edtDname.getText().toString())
                        .add("phone",edtPhone.getText().toString())
                        .add("tag",edtTag.getText().toString())
                        .add("addr",edtAddr.getText().toString())
                        .add("user_id",user_id)
                        .build();

                Request request = new Request.Builder().url("http://192.168.0.115:5000/adddelivery")
                        .addHeader("Connection","close").post(body).build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        Intent intent = new Intent(getApplicationContext(), PurchaseActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
            if ( event.getAction() == MotionEvent.ACTION_OUTSIDE){
                return false;
            }
            return true;
        }
}