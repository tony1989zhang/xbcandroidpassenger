package com.lvchehui.www.xiangbc.bean;

import java.io.Serializable;

/**
 * Created by 张灿能 on 2016/7/18.
 * 作用：查询认证特权项目
 */
public class IdentificationInfoBean implements Serializable {
    public String name;
    public String description;
    public String brokerage_rate;
    public String normal_subscription_rate;
    public String special_subscription_rate;
    public String identification_gid;
    public String identification_id;
    public String is_default;
}
