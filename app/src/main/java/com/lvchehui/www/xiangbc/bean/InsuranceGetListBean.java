package com.lvchehui.www.xiangbc.bean;

import java.util.List;

/**
 * Created by 张灿能 on 2016/9/9.
 * 作用：
 */
public class InsuranceGetListBean extends BaseBean {

    public List<ResDataBean> resData;

    public static class ResDataBean {
        public String insurance_gid;
        public String title;
        public String content;
        public int category;
        public String insurance_company;
        public String selling_price;
    }
}
