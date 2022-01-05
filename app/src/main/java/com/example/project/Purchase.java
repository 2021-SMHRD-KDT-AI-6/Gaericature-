package com.example.project;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Purchase extends Fragment {

    TextView tv;
    int num = 2;
    String a;
    ExpandableHeightGridView gridView;
    ArrayList<MyGaericatureVO> data;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.fragment_purchase, container, false);

        tv = fragment.findViewById(R.id.tv);
        gridView = fragment.findViewById(R.id.purchaseGrid);



        String url = "http://192.168.0.115:8081/Gaericature/testController";

        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormBody.Builder().add("num", String.valueOf(num)).build();

        Request request = new Request.Builder().url(url).post(body).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {

                    data = new ArrayList<>();

                    String strJsonOutput = response.body().string();
                    JSONArray jsonOutput = new JSONArray(strJsonOutput);
                    a = String.valueOf(jsonOutput.getJSONObject(0).getString("item_seq"));

                    data.add(new MyGaericatureVO(R.drawable.img1, a));
                    data.add(new MyGaericatureVO(R.drawable.img1, a));
                    data.add(new MyGaericatureVO(R.drawable.img1, a));
                    data.add(new MyGaericatureVO(R.drawable.img1, a));
                    data.add(new MyGaericatureVO(R.drawable.img1, a));
                    data.add(new MyGaericatureVO(R.drawable.img1, a));




                    MyThread myThread = new MyThread(tv);
                    myThread.start();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });





//
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                OkHttpClient client = new OkHttpClient();
//
//                RequestBody body = new FormBody.Builder().add("num", String.valueOf(num)).build();
//
//                Request request = new Request.Builder().url(url).post(body).build();
//
//                client.newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                        Gson gson = new Gson();
//                        try {
//                            String strJsonOutput = response.body().string();
//                            JSONArray jsonOutput = new JSONArray(strJsonOutput);
//                            Log.d("tag", String.valueOf(jsonOutput.getJSONObject(0).getString("item_seq")));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//        });
        return fragment;
    }

    Handler myHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {



            tv= (TextView) msg.obj;
            tv.setText(a);
        }
    };

    class MyThread extends Thread{
        TextView tv;

        public MyThread(TextView tv){
            this.tv=tv;
        }

        @Override
        public void run() {

            try {
                Thread.sleep(1000);

                Message message = new Message();
                message.obj = tv;

                myHandler.sendMessage(message);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}