package com.example.project.VO;

import android.graphics.Bitmap;

public class MyGaericatureVO {
    private Bitmap img;
    private String charNick;

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
