package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.project.Adapter.DeliveryAdapter;
import com.example.project.Adapter.PurchaseAdapter;
import com.example.project.ExpandableHeightGridView;
import com.example.project.R;
import com.example.project.RbPreference;
import com.example.project.VO.DeliveryVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PurchaseActivity extends AppCompatActivity {

    ExpandableHeightGridView gridViewPurchase;
    ArrayList<DeliveryVO> data = new ArrayList<>();
    DeliveryAdapter adapter;
    Button btnDelivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        gridViewPurchase = findViewById(R.id.gridViewPurchase);
        btnDelivery = findViewById(R.id.btnDelivery);

        gridViewPurchase.setSelector(R.drawable.edge);

        RbPreference pref = new RbPreference(this);
        String user_id = pref.getValue("user_id", null);

        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormBody.Builder()
                .add("user_id",user_id)
                .build();

        String url = "http://192.168.0.115:5000/delivery";
        Request request = new Request.Builder().url(url).addHeader("Connection","close").post(body).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                data = new ArrayList<>();

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    JSONArray jsonArray = jsonObject.getJSONArray("delivery_list");

                    for(int i=0;i<jsonArray.length();i++){
                        DeliveryVO vo = new DeliveryVO();
                        vo.setTvDname((String) jsonArray.getJSONArray(i).get(0));
                        vo.setTvAddr((String) jsonArray.getJSONArray(i).get(2));
                        vo.setTvPhone((String) jsonArray.getJSONArray(i).get(1));
                        vo.setDeli_seq(String.valueOf(jsonArray.getJSONArray(i).get(3)) );



                        Log.i("name :: ",vo.getTvDname());
                        Log.i("addr :: ",vo.getTvAddr());
                        Log.i("phone :: ",vo.getTvPhone());
                        Log.i("seq :: ",vo.getDeli_seq());

                        data.add(vo);
                    }

                    adapter = new DeliveryAdapter(getApplicationContext(), R.layout.delivery, data);
                    DeliveryThread deliveryThread = new DeliveryThread(adapter);
                    deliveryThread.start();



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        });

        btnDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PopupActivity.class);
                startActivity(intent);
            }
        });


    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            gridViewPurchase.setExpanded(true);
            gridViewPurchase.setAdapter(adapter);
        }
    };

    class DeliveryThread extends Thread{
        DeliveryAdapter adapter;

        public DeliveryThread(DeliveryAdapter adapter){
            this.adapter=adapter;
        }

        @Override
        public void run() {
            Message message = new Message();
            handler.sendMessage(message);
        }
    }
}