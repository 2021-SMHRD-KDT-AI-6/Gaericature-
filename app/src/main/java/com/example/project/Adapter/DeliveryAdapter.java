package com.example.project.Adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.project.R;
import com.example.project.VO.DeliveryVO;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DeliveryAdapter extends BaseAdapter {

    private ArrayList<DeliveryVO> vo = new ArrayList<>();
    private Context context;
    private int layout;

    public DeliveryAdapter(Context context, int layout, ArrayList<DeliveryVO> vo ){
        this.vo = vo;
        this.context = context;
        this.layout = layout;
    }


    @Override
    public int getCount() {
        return vo.size();
    }

    @Override
    public Object getItem(int i) {
        return vo.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.delivery, null);
        }

        TextView tvDname = view.findViewById(R.id.tvDname);
        TextView tvPhone = view.findViewById(R.id.tvPhone);
        TextView tvAddr = view.findViewById(R.id.tvAddr);
        TextView btnDeliDel = view.findViewById(R.id.btnDeliDel);

        tvDname.setText(vo.get(i).getTvDname());
        tvAddr.setText(vo.get(i).getTvAddr());
        tvPhone.setText(vo.get(i).getTvPhone());

        btnDeliDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String deli_seq = vo.get(i).getDeli_seq();

                OkHttpClient client = new OkHttpClient();

                RequestBody body = new FormBody.Builder()
                        .add("deli_seq", deli_seq)
                        .build();
                String url = "http://192.168.0.115:5000/delidel";
                Request request = new Request.Builder().url(url).addHeader("Connection", "close").post(body).build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                        String result = response.body().string();
                        if(result.equals("1")){
                            vo.remove(i);
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    notifyDataSetChanged();
                                }
                            }, 0);





                        }
                    }
                });
            }
        });


        return view;
    }
}