package com.lvchehui.www.xiangbc.bean;

/**
 * Created by 张灿能 on 2016/7/8.
 * 作用：提交发布需求
 */
public class DemandSubmitRequestBean  {

    public String use_type = "";
    public String begin_time = "";
    public String end_time = "";
    public String pepole_num = "";
    public String car_num = "";

    public String begin_address = "";
    public String end_address = "厦门";

    public String contacts_name = "";
    public String contacts_phone = "";
    public String use_remark = "";

    public String invoice_name = "";
    public String invoice_consignee = "";
    public String invoice_phone = "";
    public String invoice_address = "";
    public String request_start_city = "";

    public String use_trip = "";
    public String car_model = "";
    public String midway_address = "";

    @Override
    public String toString() {
        return "DemandSubmitRequestBean{" +
                "use_type:'" + use_type + '\'' +
                " begin_time:'" + begin_time + '\'' +
                " end_time:'" + end_time + '\'' +
                " pepole_num:'" + pepole_num + '\'' +
                " car_num:'" + car_num + '\'' +
                " begin_address:'" + begin_address + '\'' +
                " end_address:'" + end_address + '\'' +
                " contacts_name:'" + contacts_name + '\'' +
                " contacts_phone:'" + contacts_phone + '\'' +
                " use_remark:'" + use_remark + '\'' +
                " invoice_name:'" + invoice_name + '\'' +
                " invoice_consignee:'" + invoice_consignee + '\'' +
                " invoice_phone:'" + invoice_phone + '\'' +
                " invoice_address:'" + invoice_address + '\'' +
                " request_start_city:'" + request_start_city + '\'' +
                " use_trip:'" + use_trip + '\'' +
                " car_model:'" + car_model + '\'' +
                " midway_address:'" + midway_address + '\'' +
                '}';
    }
}
