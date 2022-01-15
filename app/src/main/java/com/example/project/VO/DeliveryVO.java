package com.example.project.VO;

public class DeliveryVO {

    private String tvDname;
    private String tvAddr;
    private String tvPhone;
    private String Deli_seq;

    public String getDeli_seq() {
        return Deli_seq;
    }

    public void setDeli_seq(String deli_seq) {
        Deli_seq = deli_seq;
    }

    public String getTvDname() {
        return tvDname;
    }

    public void setTvDname(String tvDname) {
        this.tvDname = tvDname;
    }

    public String getTvAddr() {
        return tvAddr;
    }

    public void setTvAddr(String tvAddr) {
        this.tvAddr = tvAddr;
    }

    public String getTvPhone() {
        return tvPhone;
    }

    public void setTvPhone(String tvPhone) {
        this.tvPhone = tvPhone;
    }
}
