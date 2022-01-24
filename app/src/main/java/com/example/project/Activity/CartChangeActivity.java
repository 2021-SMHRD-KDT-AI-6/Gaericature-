package com.example.project.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.project.R;
import com.example.project.RbPreference;

public class CartChangeActivity extends AppCompatActivity {

    Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_change);

        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = (int) (display.getWidth()*0.6);
        getWindow().getAttributes().width=width;
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



        int purchaseType = getIntent().getIntExtra("purchase",0);


        // Cart에서 변경했으면 0, PurchaseActivity에서 변경했으면 1
        int change = getIntent().getIntExtra("ck",0);

        btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // change가 0일 경우 Cart로, change가 1일 경우 PurchaseActivity로 다시 돌아간다.
                // change가 2일 경우 MyPage로 이동.
                if(change == 0 ) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("ck", 1);
                    startActivity(intent);
                    finish();
                }else if(change == 1){
                    Intent intent = new Intent(getApplicationContext(), PurchaseActivity.class);
                    intent.putExtra("purchaseType",String.valueOf(purchaseType));
                    startActivity(intent);
                    finish();
                }else if(change == 2){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("ck", 3);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
    }
}