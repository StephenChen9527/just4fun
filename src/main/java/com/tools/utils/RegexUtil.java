package com.tools.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

    public static String getOne(String src,String regex){
        Matcher matcher= Pattern.compile(regex).matcher(src);
        if (matcher.find()){
            return src.substring(matcher.start(), matcher.end());
        }
        return null;
    }
    public static String getOne(String src,String regex,int startIndex,int endIndex){
        Matcher matcher= Pattern.compile(regex).matcher(src);
        if (matcher.find()){
            return src.substring(matcher.start()+startIndex, matcher.end()+endIndex);
        }
        return null;
    }

    public static List<String> findStr(String src,String regex){
        List<String> result=new ArrayList<>();
        Matcher matcher= Pattern.compile(regex).matcher(src);
        while (matcher.find()){
            result.add(src.substring(matcher.start(), matcher.end()));
        }
        return result;
    }
    public static List<String> findStr(String src,String regex,int startIndex,int endIndex){
        List<String> result=new ArrayList<>();
        Matcher matcher= Pattern.compile(regex).matcher(src);
        while (matcher.find()){
            result.add(src.substring(matcher.start()+startIndex, matcher.end()+endIndex));
        }
        return result;
    }
    public static Set<String> findStr(String src, String regex,int startIndex,int endIndex,boolean isSet){
        Set<String> result=new HashSet<>();
        Matcher matcher= Pattern.compile(regex).matcher(src);
        while (matcher.find()){
            result.add(src.substring(matcher.start()+startIndex, matcher.end()+endIndex));
        }
        return result;
    }
    public static Set<String> findStr(String src, String regex,boolean isSet){
        Set<String> result=new HashSet<>();
        Matcher matcher= Pattern.compile(regex).matcher(src);
        while (matcher.find()){
            result.add(src.substring(matcher.start(), matcher.end()));
        }
        return result;
    }
}
