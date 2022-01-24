package com.example.project.VO;

import android.graphics.Bitmap;

public class MyGaericatureVO {
    private Bitmap img;
    private String charNick;
    private int deep_seq;

    public int getDeep_seq() {
        return deep_seq;
    }

    public void setDeep_seq(int deep_seq) {
        this.deep_seq = deep_seq;
    }

    public String getCharNick() {
        return charNick;
    }

    public void setCharNick(String charNick) {
        this.charNick = charNick;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }
}
