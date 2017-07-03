package com.tshm;


import android.support.annotation.NonNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class MD5 {
    //MD5 šifriranje gesla
    @NonNull
    String md5(String password) throws NoSuchAlgorithmException {

        StringBuilder hexString = new StringBuilder();

        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
        digest.update(password.getBytes());
        byte messageDigest[] = digest.digest();

        for (byte aMessageDigest : messageDigest)
            hexString.append(Integer.toHexString(0xFF & aMessageDigest));
        return hexString.toString();
    }
}
