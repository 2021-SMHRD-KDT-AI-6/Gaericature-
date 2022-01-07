package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MyPagePurchaseCompleteHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page_purchase_complete_history);

        Intent intent = getIntent();
        String PurchaseCompleteNum = intent.getExtras().getString("PurchaseCompleteNum");
        TextView tvCom = findViewById(R.id.tvCom);
        tvCom.setText(PurchaseCompleteNum);

    }
}