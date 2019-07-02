package com.tools.utils;

import java.io.File;
import java.io.FileWriter;

public class FileUtils {

    public static boolean saveFile(String dir,String path,String content)throws Exception{
        File d=new File("dir");
        if(!d.exists()){
            d.mkdirs();
        }
        FileWriter  fw=new FileWriter(path);
        fw.write(content);
        fw.flush();
        fw.close();
        return true;
    }
}
