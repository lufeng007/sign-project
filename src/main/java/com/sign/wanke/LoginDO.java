package com.sign.wanke;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by sign on 2017/9/29.
 */
public class LoginDO {

    //登录类型
    private Integer LoginType;

    //用户名
    private String AccountName;

    //密码
    private String Password;

    @JSONField(name = "LoginType")
    public Integer getLoginType() {
        return LoginType;
    }

    public void setLoginType(Integer loginType) {
        LoginType = loginType;
    }

    @JSONField(name = "AccountName")
    public String getAccountName() {
        return AccountName;
    }

    public void setAccountName(String accountName) {
        AccountName = accountName;
    }

    @JSONField(name = "Password")
    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
