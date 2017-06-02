package com.lvchehui.www.xiangbc.bean;

/**
 * Created by 张灿能 on 2016/5/25.
 * 作用：用户信息
 */
public class UserInfoBean extends BaseBean {
    public ResDataBean resData;

    @Override
    public String toString() {
        return "UserInfoBean{" +
                "resData=" + resData +
                '}';
    }

    public static class ResDataBean {
        public String username;
        public String phone;
        public String email;
        public String head_url = "http://img3.imgtn.bdimg.com/it/u=3690544946,3415742375&fm=21&gp=0.jpg";
        public int sex;
        public int credit_score;
        public String number_name;
        public String nick_name = "";
        public int birthday;
        public String wxpay_account;
        public String alipay_account;
        public IdentificationBean identification;

        @Override
        public String toString() {
            return "ResDataBean{" +
                    "username='" + username + '\'' +
                    ", phone='" + phone + '\'' +
                    ", email='" + email + '\'' +
                    ", head_url='" + head_url + '\'' +
                    ", credit_score=" + credit_score +
                    ", number_name='" + number_name + '\'' +
                    ", nick_name='" + nick_name + '\'' +
                    ", birthday=" + birthday +
                    ", wxpay_account=" + wxpay_account +
                    ", alipay_account=" + alipay_account +
//                    ", identification=" + identification +
                    '}';
        }

        public static class IdentificationBean {
            public String name;
            public double brokerage_rate;
            public double normal_subscription_rate;
            public int special_subscription_rate;

            @Override
            public String toString() {
                return "IdentificationBean{" +
                        "name='" + name + '\'' +
                        ", brokerage_rate=" + brokerage_rate +
                        ", normal_subscription_rate=" + normal_subscription_rate +
                        ", special_subscription_rate=" + special_subscription_rate +
                        '}';
            }
        }
    }

}
