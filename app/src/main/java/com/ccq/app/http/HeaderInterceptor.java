package com.ccq.app.http;

import android.os.SystemClock;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/12/9.
 */

public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        builder.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.108 Safari/537.36 2345Explorer/8.0.0.13547");
        builder.addHeader("Cache-Control", "max-age=0");
        builder.addHeader("Upgrade-Insecure-Requests", "1");
        builder.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");

        builder.addHeader("user", "web");
        builder.addHeader("pass", "web");
        String stemp = String.valueOf(SystemClock.currentThreadTimeMillis());
        builder.addHeader("time", stemp);
        builder.addHeader("auth", getMd5("webweb" + stemp));

        return chain.proceed(builder.build());
    }


    String getMd5(String... strings) {
        if (strings.length == 0) return null;
        else {
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < strings.length; i++) {
                stringBuffer.append(strings[i]);
            }
            String step1 = stringBuffer.toString();
            MessageDigest md5 = null;
            try {
                md5 = MessageDigest.getInstance("MD5");
                byte[] bytes = md5.digest(step1.getBytes());
                String result = "";
                for (byte b : bytes) {
                    String temp = Integer.toHexString(b & 0xff);
                    if (temp.length() == 1) {
                        temp = "0" + temp;
                    }
                    result += temp;
                }
                return result;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
