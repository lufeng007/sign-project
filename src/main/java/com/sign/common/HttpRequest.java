package com.sign.common;

import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * Created by sign on 2017/9/29.
 */
public class HttpRequest {

    /**
     * 对象转json串
     * @param object
     * @return
     */
    public static String object2Json(Object object) {
        return JSONObject.toJSONString(object);
    }

    /**
     * 向指定URL发送GET方法的请求（非json请求）
     *
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0_2 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) Mobile/14A456 MicroMessenger/6.5.18 NetType/WIFI Language/zh_CN");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
//            for (String key : map.keySet()) {
//                System.out.println(key + "--->" + map.get(key));
//            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    //通用的post请求
    public static String sendPost(HttpRequestDO httpRequestDO) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(httpRequestDO.getUrl());
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            String accept = httpRequestDO.getAccept();
            if (accept != null && accept != "") {
                conn.setRequestProperty("accept", accept);
            } else {
                conn.setRequestProperty("accept", "*/*");
            }
            String connection = httpRequestDO.getConnection();
            if (connection != null && connection != "") {
                conn.setRequestProperty("connection", connection);
            } else {
                conn.setRequestProperty("connection", "Keep-Alive");
            }
            String connectType = httpRequestDO.getConnectType();
            if (connectType != null && connectType != "") {
                conn.setRequestProperty("Content-Type", connectType);
            }
            String userAgent = httpRequestDO.getUserAgent();
            if (userAgent != null && userAgent != "") {
                conn.setRequestProperty("user-agent", userAgent);
            }
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            String param = httpRequestDO.getParam();
            if (param != null && param != "") {
                out.print(param);
            }
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }


    /**
     * 发送post请求（json请求）
     * @param url
     * @param jsonParam json串
     * @return
     */
//    public static String sendPostJson(String url, String jsonParam) {
//        OutputStreamWriter out = null;
//        BufferedReader reader = null;
//        String result = "";
//        try {
//            URL httpUrl = new URL(url);
//            // 打开和URL之间的连接
//            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
//            // 设置通用的请求属性
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Content-Type", "application/json");
//            conn.setRequestProperty("connection", "Keep-Alive");
//            conn.setUseCaches(false);//设置不要缓存
//            conn.setInstanceFollowRedirects(true);
//            // 发送POST请求必须设置如下两行
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            // 获取URLConnection对象对应的输出流
//            out = new OutputStreamWriter(conn.getOutputStream());
//            // 发送请求参数
//            out.write(jsonParam);
//            // flush输出流的缓冲
//            out.flush();
//            // 定义BufferedReader输入流来读取URL的响应
//            reader = new BufferedReader(
//                    new InputStreamReader(conn.getInputStream()));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                result += line;
//            }
//        } catch (Exception e) {
//            System.out.println("发送 POST 请求出现异常！"+e);
//            e.printStackTrace();
//        }
//        //使用finally块来关闭输出流、输入流
//        finally{
//            try{
//                if(out!=null){
//                    out.close();
//                }
//                if(reader!=null){
//                    reader.close();
//                }
//            }
//            catch(IOException ex){
//                ex.printStackTrace();
//            }
//        }
//        return result;
//    }

}
