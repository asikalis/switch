package com.switchapp.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringEncrypt {
    public static String SHA256(String strSrc) {
        if (null == strSrc) {
            return null;
        }
        MessageDigest md = null;
        String strDes = null;
        try {
            byte[] bt = strSrc.getBytes(StandardCharsets.UTF_8);
            md = MessageDigest.getInstance("SHA-256");
            md.update(bt);
            byte[] bts = md.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : bts) {
                hexString.append(String.format("%02x", b));
            }
            strDes = hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
        return strDes;
    }

    public static void main(String[] args) {
        String s = StringEncrypt.SHA256("Bl@ckb0ard");
        System.out.println(s);
    }
}
