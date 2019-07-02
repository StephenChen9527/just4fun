package com.tools.baibubaike;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tools.utils.FileUtils;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaiduBaiKe {
    public static String url="https://baike.baidu.com/item/D/%E6%9C%9B%E6%B4%9E%E5%BA%A4491310";
    private static String header="Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36";
    public static void main(String[] args) throws Exception{
        Scanner sc=new Scanner(System.in);
        //System.out.println("请输入词条:");
        //String key=sc.nextLine();
        //String key="洞庭湖";

        String html=doGet(url);
        //FileUtils.saveFile("E:\\html", "E:\\html\\"+key+".html", html);
        //找的id
        Set<String> set=findId(html);
        Iterator<String> it = set.iterator();
        while (it.hasNext()){
            String sid=it.next();
            Map<String,Object> map=new HashMap<>();
            map.put("secondId", sid);
            //请求id
            String result=doPost("https://baike.baidu.com/api/wikisecond/playurl", map);
            BaiduBaikeVO bv=JSON.parseObject(result, BaiduBaikeVO.class);

            String data=bv.getList().get(0).get("hlsUrl");
            //dopost  找到所有的分片视频
            List<String> vurl = findVURL(data, url);
            for (String s : vurl) {

                List<String> target=findAllm38u(find1280(s),data);
                //_1280x720_ 抓取
                saveFile(target);
            }
            //System.out.println(data);
        }
    }
    public static void saveFile(List<String> target)throws Exception{
        FileOutputStream fos=new FileOutputStream("E:\\html\\"+System.currentTimeMillis()+".mp4", true);
        for (String s : target) {
            HttpClient client=HttpClients.createDefault();
            HttpGet get= new HttpGet(s);
            get.setHeader("Referer", url);
            get.setHeader("Origin", "https://baike.baidu.com");
            HttpResponse execute = client.execute(get);
            InputStream is = execute.getEntity().getContent();
            byte[] bytes=new byte[4096];
            int len=0;
            while((len=is.read(bytes))>0){
                fos.write(bytes,0 , len);
            }
            fos.flush();
        }
        fos.flush();
        fos.close();

    }


    public static List<String> findAllm38u(String url,String data)throws Exception{
        url=data.substring(0, data.lastIndexOf("/")+1)+url;
        List<String> target=new ArrayList<>();
        List<String> list =findVURL(url,"https://baike.baidu.com/item/%E6%9C%9B%E6%B4%9E%E5%BA%AD/4491310");
        String str=list.get(0);
        //64d0e9cf60eda5c7d4517d73cd88645e.m3u8.0.0.ts
        Matcher matcher=Pattern.compile("\\S+\\.m3u8\\S.+").matcher(str);
        while (matcher.find()){
            target.add(data.substring(0, data.lastIndexOf("/")+1)+str.substring(matcher.start(),matcher.end()));
        }
        return target;
    }

    public static String find1280(String str){
        Pattern p=Pattern.compile("\\S+_1280x720_\\S+\\.m3u8");
        Matcher matcher=p.matcher(str);
        if(matcher.find()){
            System.out.println("find");
            str=str.substring(matcher.start(), matcher.end());
        }
        return str;
    }

    public static List<String> findVURL(String url,String referer)throws Exception{
        List<String> list=new ArrayList<>();
        HttpClient client = HttpClients.createDefault();
        HttpGet get=new HttpGet(url);
        get.setHeader("Referer", referer);
        get.setHeader("Origin", "https://baike.baidu.com");
        HttpResponse res = client.execute(get);
        String r=EntityUtils.toString(res.getEntity());
        list.add(r);
        return list;
    }

//https://baikevideo.cdn.bcebos.com/media/mda-O92JIgc0fVwWQ4bv/

    public static Set<String> findId(String html){
        Set<String> set=new HashSet<>();
        String reg="secondId\":[0-9]+";
        Pattern p=Pattern.compile(reg);
        Matcher matcher=p.matcher(html);
        while(matcher.find()){
            set.add(html.substring(matcher.start()+10, matcher.end()));
        }
        return set;
    }
    //请求获取页面
    public static String doGet (String url)throws Exception{
        HttpClient client= HttpClients.createDefault();
        HttpGet get=new HttpGet(url);
        get.setHeader("User-Agent", header);
        HttpResponse res = client.execute(get);
        return EntityUtils.toString(res.getEntity(),"utf-8");
    }

    public static String doPost(String url, Map<String,Object> para)throws Exception{
        HttpClient client= HttpClients.createDefault();
        HttpPost post=new HttpPost(url);
        post.setHeader("User-Agent", header);
        List<NameValuePair> list=new ArrayList<>();
        for (String key : para.keySet()) {
            NameValuePair valuePair=new BasicNameValuePair(key, para.get(key).toString());
            list.add(valuePair);
        }

        HttpEntity entity=new UrlEncodedFormEntity(list);
        post.setEntity(entity);
        HttpResponse res = client.execute(post);
        return EntityUtils.toString(res.getEntity(),"utf-8");
    }
}
