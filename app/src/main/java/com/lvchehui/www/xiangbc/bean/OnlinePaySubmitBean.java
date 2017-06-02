package com.lvchehui.www.xiangbc.bean;

/**
 * Created by 张灿能 on 2016/9/10.
 * 作用：
 */
public class OnlinePaySubmitBean extends BaseBean {

    public ResDataBean resData;

    public static class ResDataBean {
        public int add_time;
        public int update_time;
        public String out_trade_no;
        public String total_amount;
        public String send_fee;
        public int online_pay_category;
        public int pay_status;
        public String prepare_pay_gid;
        public String pay_title;
        public String pay_body;
        public String seller_id;
        public String appid;
        public String users_gid;
        public String online_pay_gid;
    }
}
