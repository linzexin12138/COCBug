package com.gdou.findCOC;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        String tag = "#dfsdfss";
        tag = "https://fdsf/" + tag.substring(1);
        System.out.println(tag);
        fun(new ArrayList<String>());
        fun2("sfs");
    }

    public static void fun(List<?> list){
        System.out.println("haha");
    }

    public static void fun2(String a){
        System.out.println(a);
    }

}
