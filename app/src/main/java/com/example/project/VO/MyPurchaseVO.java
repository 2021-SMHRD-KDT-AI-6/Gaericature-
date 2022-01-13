package com.example.project.VO;

import android.graphics.Bitmap;

public class MyPurchaseVO {

    private String item_name;
    private String item_price;
    private String item_cnt;
    private String deli_yn;
    private Bitmap item_pic1;

    public MyPurchaseVO(String item_name, String item_price, String item_cnt, String deli_yn, Bitmap item_pic1) {
        this.item_name = item_name;
        this.item_price = item_price;
        this.item_cnt = item_cnt;
        this.deli_yn = deli_yn;
        this.item_pic1 = item_pic1;
    }

    public String getDeli_yn() {
        return deli_yn;
    }

    public void setDeli_yn(String deli_yn) {
        this.deli_yn = deli_yn;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getItem_cnt() {
        return item_cnt;
    }

    public void setItem_cnt(String item_cnt) {
        this.item_cnt = item_cnt;
    }

    public Bitmap getItem_pic1() {
        return item_pic1;
    }

    public void setItem_pic1(Bitmap item_pic1) {
        this.item_pic1 = item_pic1;
    }
}
