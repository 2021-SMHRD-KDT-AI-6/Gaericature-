package com.example.project.VO;

import java.util.Date;

public class DeepImageVO {
    private int deep_seq;
    private String deep_path;
    private Date deep_date;
    private String user_id;

    public int getDeep_seq() {
        return deep_seq;
    }

    public void setDeep_seq(int deep_seq) {
        this.deep_seq = deep_seq;
    }

    public String getDeep_path() {
        return deep_path;
    }

    public void setDeep_path(String deep_path) {
        this.deep_path = deep_path;
    }

    public Date getDeep_date() {
        return deep_date;
    }

    public void setDeep_date(Date deep_date) {
        this.deep_date = deep_date;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
