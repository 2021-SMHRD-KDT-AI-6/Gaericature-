package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MyPagePurchaseAllHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_all_history);

        Intent intent = getIntent();
        String PurchaseAllNum = intent.getExtras().getString("PurchaseAllNum");
        TextView tvAll = findViewById(R.id.tvAll);
        tvAll.setText(PurchaseAllNum);
    }
}