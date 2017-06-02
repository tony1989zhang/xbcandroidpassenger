package com.lvchehui.www.xiangbc.bean;

/**
 * Created by 张灿能 on 2016/9/10.
 * 作用：
 */
public class OrderSubmitBean extends BaseBean {
    public ResDataBean resData;
    public static class ResDataBean {
        public int add_time;
        public String order_serial;
        public String order_name;
        public String order_describe;
        public int order_expire;
        public String order_amount;
        public String order_paid_money;
        public String insurance_total_money;
        public String motorcade_users_gid;
        public String passenger_users_gid;
        public int order_category;
        public int is_public;
        public String demand_gid;
        public String quotation_gid;
        public String order_gid;
    }
}
