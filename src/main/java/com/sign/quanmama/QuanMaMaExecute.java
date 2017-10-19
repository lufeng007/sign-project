package com.sign.quanmama;

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
 * Created by lufeng on 2017/10/6.
 *
 * 券妈妈签到程序
 */
public class QuanMaMaExecute {

    public static Logger logger = LoggerFactory.getLogger(QuanMaMaExecute.class);

    public static void main(String[] args) {

        PropertyConfigurator.configure(QuanMaMaExecute.class.getResource("/log4j.xml"));
        String url = "http://ios.quanmama.com/ajax/qiandao.ashx";
        String param = "";
        String userAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0_2 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) Mobile/14A456 APP_USER/quanmama AliApp(BC/2.1) tae_sdk_ios_2.1 havana";
        //用户名号码密文明文集合
        Map<String,String> accountMap = new HashMap<String, String>();
        accountMap.put("oEU34sv2sFKn_98W9FcmQ7S-Uw88%40weixin.cn", "xiaofengzi");
        accountMap.put("oEU34shyJTo2uWlvym82w1JJmWMw%40weixin.cn", "memory");
        accountMap.put("oEU34slPuaYKUSmKCt0l7qPZ3BRE%40weixin.cn", "fengzhiye");
        accountMap.put("oEU34spC20zkDUztSL81uUEyjwQg%40weixin.cn", "fengzhiye0571");
        accountMap.put("oEU34srmEA6stymIwuc19aft8lyw%40weixin.cn", "fengzhiye3465");

        //指定执行时间点
        String executeHour = "09";
        //表示签到成功
        String msgTitle = "签到成功";

        logger.info("------------------------券妈妈签到程序启动成功，共有" + accountMap.size() + "个账号");
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

                Set set = accountMap.keySet();
                for (Iterator iter = set.iterator(); iter.hasNext();) {
                    String key = (String) iter.next();
                    String value = accountMap.get(key);
                    int nextSignTime = random.nextInt(100000);
                    logger.info("------------------------{}毫秒后号码{}开始签到", nextSignTime, value);
                    Thread.sleep(nextSignTime);

                    HttpRequestDO httpRequestDO = new HttpRequestDO();
                    httpRequestDO.setUrl(url);
                    httpRequestDO.setUserAgent(userAgent);
                    param ="action=checkin&email=" + key;
                    httpRequestDO.setParam(param);
                    String result = HttpRequest.sendPost(httpRequestDO);
                    JSONObject jsonObject = JSON.parseObject(result);
                    if (msgTitle.equals(jsonObject.get("MsgTitle").toString())) {
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
