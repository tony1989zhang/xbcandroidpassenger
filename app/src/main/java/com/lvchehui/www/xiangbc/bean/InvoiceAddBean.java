package com.lvchehui.www.xiangbc.bean;

/**
 * Created by 张灿能 on 2016/6/13.
 * 作用：添加发票抬头
 */
public class InvoiceAddBean extends BaseBean {

    public ResDataBean resData;
    public static class ResDataBean {
        public String invoice_gid;
        public String users_gid;
        public String invoice_name;

        @Override
        public String toString() {
            return "ResDataBean{" +
                    "invoice_gid='" + invoice_gid + '\'' +
                    ", users_gid='" + users_gid + '\'' +
                    ", invoice_name='" + invoice_name + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "InvoiceAddBean{" +
                "resData=" + resData +
                '}';
    }
}
