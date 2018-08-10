package com.rank.search.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author qiaoshiyong@bshf360.com
 * @since 2018/8/10 16:14
 */
public class HttpUtil {

    private static final String LINE = "\n";

    public static String getPageCode(String str, String encoding) {
        try {
            URL url = new URL(str);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.62 Safari/537.36");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding));
            StringBuffer sb = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                sb.append(str);
                sb.append(LINE);
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "null";
    }
}
