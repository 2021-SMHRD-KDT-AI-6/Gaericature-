package com.example.project.VO;

import android.graphics.Bitmap;

public class MyGaericatureVO {
    private Bitmap img;

    public MyGaericatureVO(Bitmap img) {
        this.img = img;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }
}
