package com.lvchehui.www.xiangbc.bean;

/**
 * Created by 张灿能 on 2016/7/6.
 * 作用：添加收件人列表
 */
public class ExpressInfoAddBean extends BaseBean {
    public ResDataBean resData;

    public static class ResDataBean {
        public String express_info_gid;
        public String users_gid;
        public String consignee;
        public String phone;
        public String address;
    }
}
