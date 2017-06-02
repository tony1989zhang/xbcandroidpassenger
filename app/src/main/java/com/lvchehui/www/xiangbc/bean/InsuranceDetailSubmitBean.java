package com.lvchehui.www.xiangbc.bean;

/**
 * Created by 张灿能 on 2016/9/10.
 * 作用：
 */
public class InsuranceDetailSubmitBean extends  BaseBean{
    public ResDataBean resData;
    public static class ResDataBean {
        public String users_gid;
        public String insurance_gid;
        public String quantity;
        public int add_time;
        public String gid;
        public String title;
        public String content;
        public int category;
        public String charge;
        public String insurance_company;
        public String insurance_detail_gid;
    }
}
