package com.lvchehui.www.xiangbc.bean;

/**
 * Created by 张灿能 on 2016/9/21.
 * 作用：
 */
public class OrderInfo extends BaseBean{
    public ResDataBean resData;
    public static class ResDataBean {
        public long add_time;
        public long update_time;
        public String status;
        public String order_serial;
        public String order_name;
        public String order_describe;
        public int order_expire;
        public double order_amount;
        public double order_paid_money;
        public double insurance_total_money;
        public String motorcade_users_gid;
        public String passenger_users_gid;
        public int order_category;
        public int is_public;
        public String demand_gid;
        public String quotation_gid;
        public String order_gid;
    }
}
