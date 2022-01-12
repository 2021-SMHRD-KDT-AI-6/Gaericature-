package com.example.project.VO;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Date;

public class itemVO implements Serializable {
    private int item_seq;
    private String item_name;
    private int item_price;
    private String item_content;
    private int item_pic1;
    private Bitmap item_pic2;
    private Bitmap item_pic3;
    private Bitmap item_pic4;
    private Date item_date;
    private String user_id;

    public itemVO(Bitmap item_pic2) {
        this.item_pic2 = item_pic2;
    }

    public int getItem_seq() {
        return item_seq;
    }

    public void setItem_seq(int item_seq) {
        this.item_seq = item_seq;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public int getItem_price() {
        return item_price;
    }

    public void setItem_price(int item_price) {
        this.item_price = item_price;
    }

    public String getItem_content() {
        return item_content;
    }

    public void setItem_content(String item_content) {
        this.item_content = item_content;
    }

    public int getItem_pic1() {
        return item_pic1;
    }

    public void setItem_pic1(int item_pic1) {
        this.item_pic1 = item_pic1;
    }

    public Bitmap getItem_pic2() {
        return item_pic2;
    }

    public void setItem_pic2(Bitmap item_pic2) {
        this.item_pic2 = item_pic2;
    }

    public Bitmap getItem_pic3() {
        return item_pic3;
    }

    public void setItem_pic3(Bitmap item_pic3) {
        this.item_pic3 = item_pic3;
    }

    public Bitmap getItem_pic4() {
        return item_pic4;
    }

    public void setItem_pic4(Bitmap item_pic4) {
        this.item_pic4 = item_pic4;
    }

    public Date getItem_date() {
        return item_date;
    }

    public void setItem_date(Date item_date) {
        this.item_date = item_date;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
