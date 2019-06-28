package com.tools.one;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OneMain {
    private static Map<String,String> map=new HashMap<String, String>();
    public static void main(String[] args) throws Exception{
        //href="/code/12fb24d47ec745ce8970b44b2ac44919.html"
        String regex="code/\\S{32}\\.html";
        Pattern pattern = Pattern.compile(regex);
        String str="http://www.oneyuanma.com/code/default/java/";
        for(int i=1;i<12;i++){
            String uri=str+i+".html";
            HttpClient client= HttpClients.createDefault();
            HttpGet get=new HttpGet(uri);
            HttpResponse execute = client.execute(get);
            String result= EntityUtils.toString(execute.getEntity()) ;
            Matcher matcher = pattern.matcher(result);
            //找到id
            while (matcher.find()){
                String id=result.substring(matcher.start()+5,matcher.end()-5).replace("homeById.do?id=", "");
                if(map.get(id)==null){
                    get(id);
                }
                map.put(id, id);

            }
        }


    }

    //得到网站
    public static void get(String id)throws Exception{
        String uri="http://www.oneyuanma.com/web/system/getFileUrlT.do?id="+id+"&fileType=04";
        HttpClient client= HttpClients.createDefault();
        HttpGet get=new HttpGet(uri);
        HttpResponse execute = client.execute(get);
        //获得了 下载的url
        String result= EntityUtils.toString(execute.getEntity());
        //涉及中文 ，需要进行转码。
        String path=result.substring(0, result.lastIndexOf("/")+1)
                +URLEncoder.encode(result.substring(result.lastIndexOf("/")+1, result.length()), "utf-8");
        String filePath="D:\\download\\"+result.substring(result.lastIndexOf("/")+1, result.length());
        download(path,filePath);
    }


    private static void download (String url,String filepath)throws Exception{

        new Thread(() -> {
            try {
                File dir=new File("D:\\download");
                if(!dir.exists()){
                    dir.mkdir();
                }
                Thread.sleep(new Random().nextInt(5000)+2000);
                HttpClient client= HttpClients.createDefault();
                HttpGet get=new HttpGet(url);
                HttpResponse execute = client.execute(get);
                HttpEntity entity = execute.getEntity();
                InputStream in = entity.getContent();
                byte[] buffer=new byte[4096];
                int readLength = 0;
                FileOutputStream file=new FileOutputStream(new File(filepath));
                while ((readLength=in.read(buffer)) > 0) {
                    file.write(buffer, 0, readLength);
                }
                file.flush();
            } catch (Exception e) {
                System.out.println("下载出错,");
                System.out.println(url);
                e.printStackTrace();
            }

        }).start();

    }

}
