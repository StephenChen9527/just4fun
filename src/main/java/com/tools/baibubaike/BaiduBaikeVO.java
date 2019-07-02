package com.tools.baibubaike;

import java.util.ArrayList;
import java.util.Map;

public class BaiduBaikeVO {

    private Integer errno;
    private String errmsg;
    private ArrayList<Map<String,String>> list;

    public Integer getErrno() {
        return errno;
    }

    public void setErrno(Integer errno) {
        this.errno = errno;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public ArrayList<Map<String,String>> getList() {
        return list;
    }

    public void setList(ArrayList<Map<String,String>> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "BaiduBaikeVO{" +
                "errno=" + errno +
                ", errmsg='" + errmsg + '\'' +
                ", list=" + list +
                '}';
    }
}
