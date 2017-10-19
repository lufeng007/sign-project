package com.sign.xiaopiqi;

import com.sign.common.HttpRequest;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by sign on 2017/10/3.
 *
 * 小啤汽公众号签到程序
 * result:
 *  error:0 表示成功
 *  error:4 表示重复签到
 */
public class XiaoPiQiSignExecute {
    public static Logger logger = LoggerFactory.getLogger(XiaoPiQiSignExecute.class);

    public static void main(String[] args) {
        PropertyConfigurator.configure(XiaoPiQiSignExecute.class.getResource("/log4j.xml"));
        String url = "http://interface.gd.sina.com.cn/gdif/xiaopiqi201706/xpqcardadd.html";

        Map<String,String> openidMap = new HashMap<String, String>();
        openidMap.put("oZkNpuD4fH7laXR_-Z4BzLMq2coM", "xiaofengzi");
        openidMap.put("oZkNpuI8KcjteoH3N7e3RXd35eG8", "memory");
        openidMap.put("oZkNpuGGLzb8jUs70HEQ1jtkOepk", "laoshi");
        openidMap.put("oZkNpuC_kYBhltQICKhY8rOnO-AA", "007");
        openidMap.put("oZkNpuOYq_lwqL5Uq7t4AD6yoml0", "fengzhiye");
        openidMap.put("oZkNpuGNlGslQqvU5X741u0YkEQM", "fengzhiye0571");
        openidMap.put("oZkNpuF6NMXWa3DLNBrgCfBL46Ks", "fengzhiye3465");
        //指定执行时间点
        String executeHour = "09";
        //签到成功
        String error = "0";

        Long time1;
        Long time2;
        logger.info("------------------------小啤汽签到程序启动成功，共有" + openidMap.size() + "个微信");
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

                Set set = openidMap.keySet();
                for (Iterator iter = set.iterator(); iter.hasNext();) {
                    String key = (String) iter.next();
                    String value = openidMap.get(key);
                    int nextSignTime = random.nextInt(100000);
                    logger.info("------------------------{}毫秒后号码{}开始签到", nextSignTime, value);
                    Thread.sleep(nextSignTime);

                    time1 = System.currentTimeMillis();
                    Thread.sleep(random.nextInt(100)*100);
                    time2 = System.currentTimeMillis();
                    //发送 GET 请求
                    String result= HttpRequest.sendGet(url,
                            "openid=" + key + "&_=" + time2 +"&callback=Zepto" + time1);
                    if (result.contains("success")) {
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
