package com.example.project.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.project.Activity.PurchaseDetail;
import com.example.project.Adapter.PurchaseAdapter;
import com.example.project.BitmapConverter;
import com.example.project.ExpandableHeightGridView;
import com.example.project.FileUploadUtils;
import com.example.project.R;
import com.example.project.VO.itemVO;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Purchase extends Fragment {

    TextView tv;
    ExpandableHeightGridView gridView;
    ArrayList<itemVO> data = new ArrayList<>();
    PurchaseAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.fragment_purchase, container, false);

        tv = fragment.findViewById(R.id.tv);
        gridView = fragment.findViewById(R.id.purchaseGrid);


        OkHttpClient client = new OkHttpClient.Builder().build();
        RequestBody body = new FormBody.Builder().build();
        Request request = new Request.Builder().url("http://192.168.0.115:5000/itemlist")
                .addHeader("Connection","close").post(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                data = new ArrayList<>();

                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                    for(int i=0;i<jsonArray.length();i++){
                        byte[] b;
                        b = Base64.decode(jsonArray.get(i).toString(), Base64.DEFAULT);
                        Bitmap img = BitmapFactory.decodeByteArray(b,0,b.length);
                        itemVO vo = new itemVO();
                        vo.setItem_pic1(img);
                        data.add(vo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                adapter = new PurchaseAdapter(getActivity().getApplicationContext(), R.layout.purchaselist,data);

                MyThread myThread = new MyThread(adapter);
                myThread.start();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a_parent, View a_view, int a_position, long a_id) {

                Intent intent = new Intent(getActivity(), PurchaseDetail.class);
                intent.putExtra("seq", a_position);
                startActivity(intent);
            }
        });

        return fragment;
    }

    Handler myHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            gridView.setExpanded(true);
            gridView.setAdapter(adapter);
        }
    };




    class MyThread extends Thread{
        PurchaseAdapter adapter;
        public MyThread(PurchaseAdapter adapter){
            this.adapter=adapter;
        }

        @Override
        public void run() {
            Message message = new Message();
            myHandler.sendMessage(message);
        }
    }
}