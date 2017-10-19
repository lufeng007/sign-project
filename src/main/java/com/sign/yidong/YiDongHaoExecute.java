package com.sign.yidong;

import com.sign.common.HttpRequest;
import com.sign.common.HttpRequestDO;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by sign on 2017/10/3.
 *
 * 中国移动公众号签到程序
 *
 */
public class YiDongHaoExecute {
    public static Logger logger = LoggerFactory.getLogger(YiDongHaoExecute.class);

    public static void main(String[] args) {

        PropertyConfigurator.configure(YiDongHaoExecute.class.getResource("/log4j.xml"));
        String url = "http://wap.zj.10086.cn/wact/marketplat/signin/yxsignActivity/sign.do?rd=";
        String param = "";
        String userAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 10_0_2 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) Mobile/14A456 MicroMessenger/6.5.18 NetType/WIFI Language/zh_CN";
        String refere = "http://wap.zj.10086.cn/wact/deploy/";

        Map<String,String> platfromIdMap = new HashMap<String, String>();
        platfromIdMap.put("PL20170317192942", "AC20170331194255");
        Map<String,String> phoneMap = new HashMap<String, String>();
        phoneMap.put("PL20170317192942", "15068116897");

        //指定执行时间点
        String executeHour = "09";
        //签到成功
        String errorCode = "4";

        Long time1;
        logger.info("------------------------浙江移动公众号签到程序启动成功，共有" + platfromIdMap.size() + "个账号");
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

                Set set = platfromIdMap.keySet();
                for (Iterator iter = set.iterator(); iter.hasNext();) {
                    String key = (String) iter.next();
                    String value = platfromIdMap.get(key);
//                    int nextSignTime = random.nextInt(100000);
//                    logger.info("------------------------{}毫秒后号码{}开始签到", nextSignTime, value);
//                    Thread.sleep(nextSignTime);

                    //获取电话号码
                    Set phoneSet = phoneMap.keySet();
                    for (Iterator phoneIter = phoneSet.iterator(); phoneIter.hasNext();) {
                        String phoneValue = phoneMap.get(key);
                        if (phoneValue != null && phoneValue != "") {
                            HttpRequestDO httpRequestDO = new HttpRequestDO();
                            httpRequestDO.setUserAgent(userAgent);
                            time1 = System.currentTimeMillis();
                            String newUrl = url + time1;
                            httpRequestDO.setUrl(newUrl);
                            param = "platfromId=" + key +"&acId=" + value;
                            httpRequestDO.setParam(param);
                            String newReferer = refere + key + "/lottery/" + value + "/sign_1.html?channel=2&num=" + phoneValue;
                            httpRequestDO.setRefere(newReferer);
                            String result = HttpRequest.sendPost(httpRequestDO);
                            if (result != null && !result.equals("0")) {
                                logger.info(value + "签到成功");
                            } else {
                                logger.info(value + "签到失败");
                                logger.info(result);
                                //TODO 发送邮件提醒
                            }
                        }
                        break;
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
