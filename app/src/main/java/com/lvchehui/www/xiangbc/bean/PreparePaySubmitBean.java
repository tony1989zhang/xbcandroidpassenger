package com.lvchehui.www.xiangbc.bean;

import java.util.List;

/**
 * Created by 张灿能 on 2016/9/10.
 * 作用：
 */
public class PreparePaySubmitBean extends BaseBean {
    public ResDataBean resData;

    public static class ResDataBean {
        public int pay_money;
        public String order_gid;
        public int update_time;
        public int add_time;
        public String status;
        public String pay_serial;
        public String prepare_pay_gid;
//        public List<List<Integer>> pay_channel;
//        public List<List<Integer>> pay_handle;

        @Override
        public String toString() {
            return "ResDataBean{" +
                    "pay_money=" + pay_money +
                    ", order_gid='" + order_gid + '\'' +
                    ", update_time=" + update_time +
                    ", add_time=" + add_time +
                    ", status='" + status + '\'' +
                    ", pay_serial='" + pay_serial + '\'' +
                    ", prepare_pay_gid='" + prepare_pay_gid + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "PreparePaySubmitBean{" +
                "resData=" + resData +
                '}';
    }

    /*
    public ResDataBean resData;

    public static class ResDataBean {
        public int pay_money;
        public String order_gid;
        public int update_time;
        public int add_time;
        public String status;
        public String pay_serial;
        public String prepare_pay_gid;
        public List<List<Integer>> pay_channel;
        public List<Integer> pay_handle;
    }
*/

//    public ResDataBean resData;

/*
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
    }*/
}
