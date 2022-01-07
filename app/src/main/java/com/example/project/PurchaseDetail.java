package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class PurchaseDetail extends AppCompatActivity {

    TextView tvName, tvPrice, tvContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_detail);

        tvContent=findViewById(R.id.tvContent);
        tvName=findViewById(R.id.tvName);
        tvPrice=findViewById(R.id.tvPrice);


        Intent intent = getIntent();
        ArrayList<itemVO> list = (ArrayList<itemVO>) intent.getSerializableExtra("list");

        String name = list.get(0).getItem_name();
        String content = list.get(0).getItem_content();
        int price = list.get(0).getItem_price();



        tvContent.setText(content);
        tvPrice.setText(String.valueOf(price));
        tvName.setText(name);



    }
}