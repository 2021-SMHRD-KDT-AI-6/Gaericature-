package com.example.project;

import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

public class MyPage extends Fragment {

    ImageView imgProfile;
    TextView tvPurchaseAll, tvPurchaseAllNum, tvPurchaseDelivering, tvPurchaseDeliveringNum, tvPurchaseComplete, tvPurchaseCompleteNum;
    ExpandableHeightGridView myPageGridView;
    MyGaericatureAdapter adapter;
    ArrayList<MyGaericatureVO> data = new ArrayList<>();
    String url = "http://192.168.0.115:8081/Gaericature/testController";
    View viewPurchaseAll, viewPurchaseDelivering, viewPurchaseComplete;
    String item_seq;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.fragment_mypage, container, false);

        imgProfile = fragment.findViewById(R.id.imgProfile);
        tvPurchaseAll = fragment.findViewById(R.id.tvPurchaseAll);
        tvPurchaseAllNum = fragment.findViewById(R.id.tvPurchaseAllNum);
        tvPurchaseDelivering = fragment.findViewById(R.id.tvPurchaseDelivering);
        tvPurchaseDeliveringNum = fragment.findViewById(R.id.tvPurchaseDeliveringNum);
        tvPurchaseComplete = fragment.findViewById(R.id.tvPurchaseComplete);
        tvPurchaseCompleteNum = fragment.findViewById(R.id.tvPurchaseCompleteNum);

        viewPurchaseAll = fragment.findViewById(R.id.viewPurchaseAll);
        viewPurchaseDelivering = fragment.findViewById(R.id.viewPurchaseDelivering);
        viewPurchaseComplete = fragment.findViewById(R.id.viewPurchaseComplete);

        imgProfile.setBackground(new ShapeDrawable(new OvalShape()));
        imgProfile.setClipToOutline(true);

        tvPurchaseAll.bringToFront();
        tvPurchaseAllNum.bringToFront();
        tvPurchaseDelivering.bringToFront();
        tvPurchaseDeliveringNum.bringToFront();
        tvPurchaseComplete.bringToFront();
        tvPurchaseCompleteNum.bringToFront();



        myPageGridView = fragment.findViewById(R.id.myPageGrid);

        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormBody.Builder().build();

        Request request = new Request.Builder().url(url).post(body).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                 e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    data =  new ArrayList<>();
                    String strJsonOutput = response.body().string();
                    JSONArray jsonOutput = new JSONArray(strJsonOutput);

                    for (int i = 0; i < jsonOutput.length(); i++){
                        item_seq = String.valueOf(jsonOutput.getJSONObject(i).getString("item_seq"));
                        data.add(new MyGaericatureVO(R.drawable.img1, item_seq));
                    }

                    adapter = new MyGaericatureAdapter(getActivity().getApplicationContext(), R.layout.gaericaturelist, data);
                    MyPageThread myPageThread = new MyPageThread(adapter);

                    myPageThread.start();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return fragment;
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            myPageGridView.setExpanded(true);
            myPageGridView.setAdapter(adapter);
        }
    };

    class MyPageThread extends Thread{
        MyGaericatureAdapter adapter;

        public MyPageThread(MyGaericatureAdapter adapter){
            this.adapter = adapter;
        }

        @Override
        public void run() {
            Message message = new Message();
            handler.sendMessage(message);
        }
    }
}
