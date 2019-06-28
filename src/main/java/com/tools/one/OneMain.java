package com.tools.one;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OneMain {
    private static Map<String,String> map=new HashMap<String, String>();
    public static void main1(String[] args) throws Exception{
        //href="/code/12fb24d47ec745ce8970b44b2ac44919.html"
        String regex="code/\\S{32}\\.html";
        Pattern pattern = Pattern.compile(regex);
        String str="http://www.oneyuanma.com/code/default/java/";
        boolean isNoData=false;
        for(int i=1;i<2;i++){
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
        Thread.sleep(5000);
        String uri="http://www.oneyuanma.com/web/system/getFileUrlT.do?id="+id+"&fileType=04";
        HttpClient client= HttpClients.createDefault();
        HttpGet get=new HttpGet(uri);
        HttpResponse execute = client.execute(get);
        String result= EntityUtils.toString(execute.getEntity()) ;
        System.out.println(result);
        String filePath="D:\\download\\"+result.substring(result.lastIndexOf("/"), result.length());
        download(result,filePath);
    }

    public static void main(String[] args) throws Exception{
        String filePath="D:\\download\\OneManage管理系统V2.1.zip";
        download("http://qiniu.oneyuanma.com/"+URLEncoder.encode("OneManage管理系统V2.1.zip", "UTF-8"),filePath);
    }
    private static void download (String url,String filepath)throws Exception{
        //url= URLEncoder.encode(url, "gbk");
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
    }

}
