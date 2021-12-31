package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 0;
    ImageView imgGallery;

    BottomNavigationView navView;
    Gallery gallery;
    Home home;
    Purchase purchase;
    MyPage myPage;

    public static int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgGallery = findViewById(R.id.imgGallery);

        navView = findViewById(R.id.navView);
        gallery = new Gallery();
        home = new Home();
        purchase = new Purchase();
        myPage = new MyPage();

        getSupportFragmentManager().beginTransaction().replace(R.id.frame, home).commit();

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ItemGallery:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, gallery).commit();
                        break;
                    case R.id.ItemHome:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, home).commit();
                        break;
                    case R.id.itemPurchase:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, purchase).commit();
                        break;
                    case R.id.ItemMyPage:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, myPage).commit();
                        break;
                }
                return true;
            }
        });


    }


}