package com.example.project.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.project.R;

public class MyGaericatureFull extends AppCompatActivity {

    ImageView imgMyGaericatureFull;
    int img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_gaericature_full);

        Intent intent = getIntent();
        imgMyGaericatureFull = findViewById(R.id.imgMyGaericatureFull);
        img = Integer.parseInt(intent.getStringExtra("image"));
        imgMyGaericatureFull.setImageResource(img);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}