package com.example.project.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.project.Activity.PurchaseDetail;
import com.example.project.Adapter.PurchaseAdapter;
import com.example.project.ExpandableHeightGridView;
import com.example.project.R;
import com.example.project.VO.itemVO;
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
    ArrayList<itemVO> data;
    PurchaseAdapter adapter;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.fragment_purchase, container, false);

        tv = fragment.findViewById(R.id.tv);
        gridView = fragment.findViewById(R.id.purchaseGrid);



        String url = "http://192.168.0.115:8081/Gaericature/testController";

        OkHttpClient client = new OkHttpClient();

//        RequestBody body = new FormBody.Builder().add("num", String.valueOf(num)).build();

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

                    data = new ArrayList<>();

                    String strJsonOutput = response.body().string();
                    JSONArray jsonOutput = new JSONArray(strJsonOutput);



//                    a = String.valueOf(jsonOutput.getJSONObject(0).getString("item_seq"));

                    for( int i=0; i < jsonOutput.length(); i++){
                        itemVO vo = new itemVO();
                        vo.setItem_name(String.valueOf(jsonOutput.getJSONObject(i).getString("item_name")));
                        vo.setItem_price(Integer.parseInt(jsonOutput.getJSONObject(i).getString("item_price")));
                        vo.setItem_seq(Integer.parseInt(jsonOutput.getJSONObject(i).getString("item_seq")));
                        vo.setItem_content(String.valueOf(jsonOutput.getJSONObject(i).getString("item_content")));
                        vo.setItem_pic1(R.drawable.img1);
                        data.add(vo);
                    }



                    adapter = new PurchaseAdapter(getActivity().getApplicationContext(), R.layout.purchaselist, data);


//                    MyThread myThread = new MyThread(a);
                    MyThread myThread = new MyThread(adapter);
                    myThread.start();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a_parent, View a_view, int a_position, long a_id) {

                Intent intent = new Intent(getActivity(), PurchaseDetail.class);
                intent.putExtra("list", data);
                startActivity(intent);
            }
        });




        return fragment;
    }

    Handler myHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {



//            tv= (TextView) msg.obj;
//            tv.setText(a);

            gridView.setExpanded(true);
            gridView.setAdapter(adapter);

        }
    };




    class MyThread extends Thread{
//        TextView tv;

//        public MyThread(TextView tv){
//            this.tv=tv;
//        }

        PurchaseAdapter adapter;
        public MyThread(PurchaseAdapter adapter){
            this.adapter=adapter;
        }

        @Override
        public void run() {

//            try {
//                Thread.sleep(1000);

                Message message = new Message();
//                message.obj = tv;

                myHandler.sendMessage(message);
//            } catch (InterruptedException e){
//                e.printStackTrace();
//            }
        }
    }
}