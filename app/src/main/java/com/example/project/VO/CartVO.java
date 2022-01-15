package com.example.project.VO;

import android.graphics.Bitmap;

public class CartVO {

    private Bitmap imgCartThumb;
    private String tvItemName;
    private int tvItemPrice;
    private int tvItemCnt;
    private int cartSeq;
    private String itemSeq;

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

    public int getCartSeq() {
        return cartSeq;
    }

    public void setCartSeq(int cartSeq) {
        this.cartSeq = cartSeq;
    }

    public String getItemSeq() {
        return itemSeq;
    }

    public void setItemSeq(String itemSeq) {
        this.itemSeq = itemSeq;
    }
}
