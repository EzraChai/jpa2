package com.bjpowernode.springboot.blog.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

    /**
     * MD5 加密
     * @param str
     * @return
     */
    public static String code(String str){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes(StandardCharsets.UTF_8));
            byte[] byteDigest = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < byteDigest.length; offset++) {
                i = byteDigest[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            //32位加密
            return buf.toString();
            //16位加密
            //return buf.toString().subString(8,24);
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }return null;
    }

    /*public static void main(String[] args) {
        System.out.println(MD5Utils.code("Chloe"));
    }*/
}
