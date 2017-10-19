package com.sign.yidong;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sign.common.HttpRequest;
import com.sign.common.HttpRequestDO;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by sign on 2017/10/3.
 *
 * 中国移动app签到程序
 *
 */
public class YiDongAppExecute {
    public static Logger logger = LoggerFactory.getLogger(YiDongAppExecute.class);

    public static void main(String[] args) {

        PropertyConfigurator.configure(YiDongAppExecute.class.getResource("/log4j.xml"));
        String url = "http://wap.zj.10086.cn/mobileStore/sign/signIn.do?r=";
        String referer = "";
        String userAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0_2 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) Mobile/14A456 sjyyt/3.6.1";

        Map<String,String> phoneMap = new HashMap<String, String>();
        phoneMap.put("1001", "15068116897");//15068116897
        //指定执行时间点
        String executeHour = "09";
        //签到成功
        String errorCode = "0";
        //当月签到次数
        String times = "";

        logger.info("------------------------中国移动app签到程序启动成功，共有" + phoneMap.size() + "个账号");
        while (true) {
            //是否执行签到标志
            boolean haveSign = false;
            try {
                Random random = new Random();
//                Date date = new Date();
//                String hour = new SimpleDateFormat("HH").format(date);
//                logger.info("------------------------当前hour:{}", hour);
//                //每天固定某个时间点开始跑程序
//                if (!executeHour.equals(hour)) {
//                    logger.info("------------------------未到指定时间点（{}），程序睡眠一个小时", executeHour);
//                    //每隔一个小时监测一次
//                    Thread.sleep(1000*60*60);
//                    continue;
//                }
//                //在当前与2.78（10000/60/60）小时随机获取执行时间
//                int delayTime = random.nextInt(10000)*1000;
//                logger.info("------------------------{}毫秒后开始签到", delayTime);
//                Thread.sleep(delayTime);

                Set set = phoneMap.keySet();
                for (Iterator iter = set.iterator(); iter.hasNext();) {
                    String key = (String) iter.next();
                    String value = phoneMap.get(key);
//                    int nextSignTime = random.nextInt(100000);
//                    logger.info("------------------------{}毫秒后号码{}开始签到", nextSignTime, value);
//                    Thread.sleep(nextSignTime);

                    HttpRequestDO httpRequestDO = new HttpRequestDO();
                    httpRequestDO.setUserAgent(userAgent);
                    httpRequestDO.setAccept("application/json");
                    String newUrl = url + Math.random();
                    httpRequestDO.setUrl(newUrl);
                    referer = "http://wap.zj.10086.cn/mobileStore/sign/index.do?cf=" + key + "&phone=" + value + "&session=H8MUM8QXVNQIZMV6QH6E0XYR&nonce=144Gn8V0y3f0ZS5u&encpn=24ba042346eb2cc5a83a080c67af4a72&num=15068116897&version=3.6.1&businssId=QDYL&WT.mc_id=20170629STKDJZTC-QDYL";
                    String result = HttpRequest.sendPost(httpRequestDO);
                    JSONObject jsonObject = JSON.parseObject(result);
                    if (errorCode.equals(jsonObject.get("code").toString())) {
                        logger.info(value + "签到成功");
                        times = jsonObject.get("times").toString();
                        logger.info("本月已签到" + times + "次");
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
