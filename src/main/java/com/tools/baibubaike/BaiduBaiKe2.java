package com.tools.baibubaike;

import com.alibaba.fastjson.JSON;
import com.tools.utils.HttpUtils;
import com.tools.utils.RegexUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BaiduBaiKe2 {
    private static String userAgent="Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36";
    private static String Origin="https://baike.baidu.com";

    private static String playurl="https://baike.baidu.com/api/wikisecond/playurl";
    public static Map<String,String> headers=new HashMap<>();
    static{
        headers.put("User-Agent", userAgent);
        headers.put("Origin", userAgent);
    }
    //public static String url="https://baike.baidu.com/item/%E6%9C%9B%E6%B4%9E%E5%BA%AD/4491310";
    public static String url="https://baike.baidu.com/item/秒懂百科";
    //https://baike.baidu.com/item/%E7%A7%92%E6%87%82%E7%99%BE%E7%A7%91
    public static void main(String[] args) throws Exception{
        Scanner sc=new Scanner(System.in);
        System.out.println("请输入词条地址:");
        url=sc.nextLine().trim();
        //Referer 就是 url
        if(RegexUtil.getOne(url, "[\u4e00-\u9fa5]+")!=null){
            url=url.replace(RegexUtil.getOne(url, "[\u4e00-\u9fa5]+"), URLEncoder.encode(RegexUtil.getOne(url, "[\u4e00-\u9fa5]+"), "utf-8"));
        }
        headers.put("Referer", url);
        String html=HttpUtils.doGet(url,headers);
        //FileUtils.saveFile("E:\\html", "E:\\html\\"+key+".html", html);
        //找的secondId
        Set<String> set= RegexUtil.findStr(html, "secondId\":[0-9]+", 10, 0,true );
        //findId(html);
        Iterator<String> it = set.iterator();
        while (it.hasNext()){
            String sid=it.next();
            Map<String,Object> map=new HashMap<>();
            map.put("secondId", sid);
            //请求playurl 后获取 一个json 得到 m3u8 跟 mp4 的两个地址
            String result= HttpUtils.doPost(playurl, headers, map);
            BaiduBaikeVO bv=JSON.parseObject(result, BaiduBaikeVO.class);
            //这里使用 m3u8视频
            String data=bv.getList().get(0).get("hlsUrl");
            //https://baikevideo.cdn.bcebos.com/media/mda-O92JIgc0fVwWQ4bv/243ee47e5b647e2154e63239bfc008ea.m3u8
            //请求 之后，返回一个类似文本的结果，显示出各个 分辨率的 m3u8地址。
            String res = HttpUtils.doGet(data, headers);
            // demo： 243ee47e5b647e2154e63239bfc008ea_1280x720_1708000.m3u8
            String str=RegexUtil.getOne(res, "\\S+_1280x720_\\S+\\.m3u8");
            // 拼接请求url
            String temp_url=data.substring(0, data.lastIndexOf("/")+1)+str;
            //请求 1280*720的url 获取 分片的 视频流
            String ssss =HttpUtils.doGet(temp_url,headers);
            List<String> lll = RegexUtil.findStr(ssss, "\\S+\\.m3u8\\S.+");

            List<String> target = lll.stream().map(stemp -> {
                // 拼接请求url
                return data.substring(0, data.lastIndexOf("/") + 1) + stemp;
            }).collect(Collectors.toList());
            saveFile(target);

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



}
