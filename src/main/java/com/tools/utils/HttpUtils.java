package com.tools.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUtils {
    //处理get
    public static String doGet(String url, Map<String,String> headers){
        HttpClient client= HttpClients.createDefault();
        HttpGet get=new HttpGet(url);
        if(headers!=null){
            for (String key : headers.keySet()) {
                get.setHeader(key, headers.get(key));
            }

        }
        try {
            HttpResponse response = client.execute(get);
            return EntityUtils.toString(response.getEntity(),"utf-8");
        } catch (Exception e) {
            System.out.println("get请求报错,url="+url);
            e.printStackTrace();
        }

        return null;

    }

    //处理post
    public static String doPost(String url, Map<String,String> headers,Map<String,Object> para)throws Exception{
        HttpClient client= HttpClients.createDefault();
        HttpPost post=new HttpPost(url);
        //添加参数
        if(para!=null){
            List<NameValuePair> list=new ArrayList<>();
            for (String key : para.keySet()) {
                NameValuePair valuePair=new BasicNameValuePair(key, para.get(key).toString());
                list.add(valuePair);
            }
            HttpEntity entity=new UrlEncodedFormEntity(list);
            post.setEntity(entity);
        }
        //添加header
        if(headers!=null){
            for (String key : headers.keySet()) {
                post.setHeader(key, headers.get(key));
            }
        }
        HttpResponse res = client.execute(post);
        return EntityUtils.toString(res.getEntity(),"utf-8");
    }

}
