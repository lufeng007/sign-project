package com.sign.wanke;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sign.common.HttpRequest;
import com.sign.common.HttpRequestDO;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by sign on 2017/9/29.
 *
 * errCode:10000 表示登陆成功
 *
 * 万科签到程序
 */
public class WankeSignExecute {

    public static Logger logger = LoggerFactory.getLogger(WankeSignExecute.class);

    public static void main(String[] args) {

        PropertyConfigurator.configure(WankeSignExecute.class.getResource("/log4j.xml"));
        String url = "https://union.vanke.com/api/Member/Login";
        String jsonString = "";
        //用户名号码密文明文集合
        Map<String,String> phoneMap = new HashMap<String, String>();
        phoneMap.put("MTUwNjgxMTY4OTc=", "15068116897");
        phoneMap.put("MTczNjk2MzA1NzE=", "17369630571");
        phoneMap.put("MTU4NTgxMDUyNDQ=", "15858105244");
        phoneMap.put("MTUxNTgxMDQ5Nzg=", "15158104978");
        phoneMap.put("MTgyNTgyOTkxMjU=", "18258299125");
        phoneMap.put("MTUxNjcxMDEwMjI=", "15167101022");
        phoneMap.put("MTMxNDg0ODg0ODE=", "13148488481");
        phoneMap.put("MTM2NTY2NzY3MjU=", "13656676725");
        phoneMap.put("MTUwNjcxNTQxMzY=", "15067154136");
        phoneMap.put("MTg4Njg3MzM3MDY=", "18868733706");
        phoneMap.put("MTU5NjgxNzE0NTI=", "15968171452");
        phoneMap.put("MTUzMDU1NjgwOTI=", "15305568092");

        //密码
        String password = "Mzk0NTQ4";
        String password0571 = "MTM3MDU4NTQ4OTFx";//13705854891q
        String password8092 = "YWI3Nzg4OTk=";
        //指定执行时间点
        String executeHour = "09";
        //表示登陆成功
        String errCode = "10000";

        logger.info("------------------------万科签到程序启动成功，共有" + phoneMap.size() + "个号码");
        while (true) {
            //是否执行签到标志
            boolean haveSign = false;
            try {
                Random random = new Random();
                Date date = new Date();
                String hour = new SimpleDateFormat("HH").format(date);
                logger.info("------------------------当前hour:{}", hour);
                //每天固定某个时间点开始跑程序
                if (!executeHour.equals(hour)) {
                    logger.info("------------------------未到指定时间点（{}），程序睡眠一个小时", executeHour);
                    //每隔一个小时监测一次
                    Thread.sleep(1000*60*60);
                    continue;
                }
                //在当前与2.78（10000/60/60）小时随机获取执行时间
                int delayTime = random.nextInt(10000)*1000;
                logger.info("------------------------{}毫秒后开始签到", delayTime);
                Thread.sleep(delayTime);

                Set set = phoneMap.keySet();
                for (Iterator iter = set.iterator(); iter.hasNext();) {
                    String key = (String) iter.next();
                    String value = phoneMap.get(key);
                    int nextSignTime = random.nextInt(100000);
                    logger.info("------------------------{}毫秒后号码{}开始签到", nextSignTime, value);
                    Thread.sleep(nextSignTime);

                    HttpRequestDO httpRequestDO = new HttpRequestDO();
                    httpRequestDO.setUrl(url);
                    httpRequestDO.setMethod("POST");
                    httpRequestDO.setConnectType("application/json");
                    BodyDO bodyDO = new BodyDO();
                    LoginDO loginDO = new LoginDO();
                    loginDO.setLoginType(1);
                    loginDO.setAccountName(key);
                    loginDO.setPassword(password);
                    if ("MTUwNjgxMTY4OTc=".equals(key)) {
                        loginDO.setPassword(password0571);
                    } else if ("MTUzMDU1NjgwOTI=".equals(key) || "MTUxNjcxMDEwMjI=".equals(key)) {
                        loginDO.setPassword(password8092);
                    }
                    bodyDO.setBody(loginDO);
                    jsonString = HttpRequest.object2Json(bodyDO);
                    httpRequestDO.setParam(jsonString);
                    String result = HttpRequest.sendPost(httpRequestDO);
                    JSONObject jsonObject = JSON.parseObject(result);
                    if (errCode.equals(jsonObject.getJSONObject("state").get("errCode").toString())) {
                        logger.info(value + "签到成功");
                    } else {
                        logger.info(value + "签到失败");
                        logger.info(result);
                        //TODO 发送邮件提醒
                    }
                }
                logger.info("============================================全部签到完成");
                haveSign = true;
            } catch (Exception e) {
                logger.error("------------------------签到异常,{}", e);
                //TODO 需要发邮件或短信提醒
            } finally {
                if (haveSign) {
                    //执行完再睡眠1个小时，避免之前睡眠时间太短导致多次循环签到
                    try {
                        Thread.sleep(1000*60*60);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
