package com.lvchehui.www.xiangbc.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by 张灿能 on 2016/7/20.
 * 作用：
 */
@Table(name = "user_info")
public class UserInfoTable {
    @Column(name = "id",isId = true)
    public int id;
    @Column(name = "username")
    public String username;
    @Column(name = "sex")
    public int sex;
    @Column(name = "phone")
    public String phone;
    @Column(name = "email")
    public String email;
    @Column(name = "head_url")
    public String head_url;
    @Column(name = "credit_score")
    public int credit_score;
    @Column(name = "number_name")
    public String number_name;
    @Column(name = "nick_name")
    public String nick_name;
    @Column(name = "birthday")
    public int birthday;
    @Column(name = "wxpay_account")
    public String wxpay_account;
    @Column(name = "alipay_account")
    public String alipay_account;

    @Column(name = "name")
    public String name;
    @Column(name = "brokerage_rate")
    public double brokerage_rate;
    @Column(name = "normal_subscription_rate")
    public double normal_subscription_rate;
    @Column(name = "special_subscription_rate")
    public int special_subscription_rate;

    @Column(name = "choose_city")
    public String choose_city;

    @Override
    public String toString() {
        return "UserInfoTable{" +
                "username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", head_url='" + head_url + '\'' +
                ", credit_score=" + credit_score +
                ", number_name='" + number_name + '\'' +
                ", nick_name='" + nick_name + '\'' +
                ", birthday=" + birthday +
                ", wxpay_account='" + wxpay_account + '\'' +
                ", alipay_account='" + alipay_account + '\'' +
                ", name='" + name + '\'' +
                ", brokerage_rate=" + brokerage_rate +
                ", normal_subscription_rate=" + normal_subscription_rate +
                ", special_subscription_rate=" + special_subscription_rate +
                '}';
    }
}
