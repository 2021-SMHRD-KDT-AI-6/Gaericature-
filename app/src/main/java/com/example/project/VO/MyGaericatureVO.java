package com.example.project.VO;

public class MyGaericatureVO {
    private int img;
    private String tvName;

    public MyGaericatureVO(int img, String tvName) {
        this.img = img;
        this.tvName = tvName;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTvName() {
        return tvName;
    }

    public void setTvName(String tvName) {
        this.tvName = tvName;
    }
}
