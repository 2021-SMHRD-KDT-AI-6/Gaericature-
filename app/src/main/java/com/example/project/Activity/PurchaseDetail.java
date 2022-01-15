package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.R;
import com.example.project.VO.itemVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PurchaseDetail extends AppCompatActivity {

    TextView tvName, tvPrice, tvContent;
    Button btnPurchase, btnCart;
    ImageView imgPurchase, imgDetail;
    itemVO item = new itemVO();
    Bitmap img1,img2;
    int seq;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_detail);

        tvContent=findViewById(R.id.tvContent);
        tvName=findViewById(R.id.tvName);
        tvPrice=findViewById(R.id.tvPrice);
        btnPurchase=findViewById(R.id.btnPurchase);
        btnCart=findViewById(R.id.btnCart);
        imgPurchase=findViewById(R.id.imgPurchase);
        imgDetail=findViewById(R.id.imgDetail);


        // intent로 상품번호 가져오기
        Intent intent = getIntent();

        seq = intent.getIntExtra("seq", 0);
        seq+=1;

        OkHttpClient client = new OkHttpClient.Builder().build();


        RequestBody body = new FormBody.Builder()
                .add("seq",String.valueOf(seq))
                .build();

        Request request = new Request.Builder().url("http://192.168.0.115:5000/itemdetail")
                .addHeader("Connection","close").post(body).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray jsonArray1 = jsonObject.getJSONArray("string");
                    JSONArray jsonArray2 = jsonObject.getJSONArray("img");

                    Log.i("price :: ",String.valueOf((int) jsonArray1.get(1)));
                    Log.i("name :: ",(String) jsonArray1.get(0));
                    Log.i("content :: ",(String) jsonArray1.get(2));


                    byte[] b = Base64.decode(jsonArray2.get(0).toString(), Base64.DEFAULT);
                    img1 = BitmapFactory.decodeByteArray(b,0,b.length);

                    byte[] b1 = Base64.decode(jsonArray2.get(1).toString(), Base64.DEFAULT);
                    img2 = BitmapFactory.decodeByteArray(b1,0,b.length);


                    item.setItem_price((int)jsonArray1.get(1));
                    item.setItem_name((String)jsonArray1.get(0));
                    item.setItem_content((String)jsonArray1.get(2));
                    item.setItem_pic1(img1);
                    item.setItem_pic2(img2);

                    Log.i("2price :: ",String.valueOf(item.getItem_price()));
                    Log.i("2name :: ",item.getItem_name());
                    Log.i("2content :: ",item.getItem_content());



//                    tvName.setText(item.getItem_name());
//                    tvContent.setText(item.getItem_content());
//                    tvPrice.setText(String.valueOf(item.getItem_price()));




                    MyThread myThread = new MyThread(tvName, tvPrice,tvContent,img1,img2);
                    myThread.start();


                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });


        btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PurchaseActivity.class);
                intent.putExtra("seq",seq);
                startActivity(intent);
            }
        });
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            tvName.setText(item.getItem_name());
            tvContent.setText(item.getItem_content());
            tvPrice.setText(String.valueOf(item.getItem_price()));
            imgPurchase.setImageBitmap(img1);
            imgDetail.setImageBitmap(img2);

        }
    };

    class MyThread extends Thread {
        TextView tvName, tvPrice, tvContent;
        Bitmap img1,img2;

        public MyThread(TextView tvName, TextView tvPrice, TextView tvContent, Bitmap img1, Bitmap img2){
            this.tvName = tvName;
            this.tvContent = tvContent;
            this.tvPrice = tvPrice;
            this.img1=img1;
            this.img2=img2;


        }

        @Override
        public void run() {
            Message message = new Message();
            handler.sendMessage(message);
        }

    }
}