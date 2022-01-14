package com.example.project.VO;

import android.graphics.Bitmap;

public class CartVO {

    private Bitmap imgCartThumb;
    private String tvItemName;
    private int tvItemPrice;
    private int tvItemCnt;

    public Bitmap getImgCartThumb() {
        return imgCartThumb;
    }

    public void setImgCartThumb(Bitmap imgCartThumb) {
        this.imgCartThumb = imgCartThumb;
    }

    public String getTvItemName() {
        return tvItemName;
    }

    public void setTvItemName(String tvItemName) {
        this.tvItemName = tvItemName;
    }

    public int getTvItemPrice() {
        return tvItemPrice;
    }

    public void setTvItemPrice(int tvItemPrice) {
        this.tvItemPrice = tvItemPrice;
    }

    public int getTvItemCnt() {
        return tvItemCnt;
    }

    public void setTvItemCnt(int tvItemCnt) {
        this.tvItemCnt = tvItemCnt;
    }
}
