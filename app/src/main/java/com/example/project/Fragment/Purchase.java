package com.example.project.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.project.Activity.PurchaseDetail;
import com.example.project.Adapter.PurchaseAdapter;
import com.example.project.ExpandableHeightGridView;
import com.example.project.Loading2;
import com.example.project.R;
import com.example.project.RbPreference;
import com.example.project.VO.itemVO;

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

public class Purchase extends Fragment {

    TextView tv;
    ExpandableHeightGridView gridView;
    ArrayList<itemVO> data = new ArrayList<>();
    PurchaseAdapter adapter;
    Loading2 loading2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragment = inflater.inflate(R.layout.fragment_purchase, container, false);

        tv = fragment.findViewById(R.id.tv);
        gridView = fragment.findViewById(R.id.purchaseGrid);

        loading2 = new Loading2(fragment.getContext());
        loading2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading2.setCancelable(false);
        loading2.show();

        RbPreference pref = new RbPreference(getActivity().getApplicationContext());
        String url = pref.getValueUrl("url", null);

        OkHttpClient client = new OkHttpClient.Builder().build();
        RequestBody body = new FormBody.Builder().build();
        Request request = new Request.Builder().url(url + "/itemlist")
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
                loading2.dismiss();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a_parent, View a_view, int a_position, long a_id) {

                Intent intent = new Intent(getActivity(), PurchaseDetail.class);
                intent.putExtra("seq", a_position + 1);
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