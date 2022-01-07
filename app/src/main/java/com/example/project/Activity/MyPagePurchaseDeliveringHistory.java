package com.example.project.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.project.R;

public class MyPagePurchaseDeliveringHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page_purchase_delivering_history);

        Intent intent = getIntent();
        String PurchaseDeliveringNum = intent.getExtras().getString("PurchaseDeliveringNum");
        TextView tvDeli = findViewById(R.id.tvDeli);
        tvDeli.setText(PurchaseDeliveringNum);

    }
}