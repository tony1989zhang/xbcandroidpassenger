package com.lvchehui.www.xiangbc.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 张灿能 on 2016/7/12.
 * 作用：
 */
public class DemandSubmitDataBean implements Serializable {

        public int add_time;
        public int update_time;
        public long begin_time;
        public long end_time;
        public int pepole_num;
        public int car_num;
        public String begin_address;
        public String end_address;
        public int use_type;
        public String contacts_name;
        public String contacts_phone;
        public int is_need_invoice;
        public String use_remark;
        public String invoice_name;
        public String invoice_phone;
        public String invoice_address;
        public String invoice_consignee;
        public String users_gid;
        public int request_status;
        public String request_start_time;
        public String request_end_time;
        public String request_start_city;
        public String request_serial_number;
        public String request_order;
        public String order_gid;
        public String cancel_reason;
        public String demand_gid;
        public List<Integer> car_model;
        public List<String> midway_address;
//        public ArrayList<HashMap<String,String>> use_trip;
        public String quotation_gid;

        @Override
        public String toString() {
            return "ResDataBean{" +
                    "add_time=" + add_time +
                    ", update_time=" + update_time +
                    ", begin_time=" + begin_time +
                    ", end_time=" + end_time +
                    ", pepole_num=" + pepole_num +
                    ", car_num=" + car_num +
                    ", begin_address='" + begin_address + '\'' +
                    ", end_address='" + end_address + '\'' +
                    ", use_type=" + use_type +
                    ", contacts_name='" + contacts_name + '\'' +
                    ", contacts_phone='" + contacts_phone + '\'' +
                    ", is_need_invoice=" + is_need_invoice +
                    ", use_remark='" + use_remark + '\'' +
                    ", invoice_name='" + invoice_name + '\'' +
                    ", invoice_phone='" + invoice_phone + '\'' +
                    ", invoice_address='" + invoice_address + '\'' +
                    ", invoice_consignee='" + invoice_consignee + '\'' +
                    ", users_gid='" + users_gid + '\'' +
                    ", request_status=" + request_status +
                    ", request_start_time=" + request_start_time +
                    ", request_end_time=" + request_end_time +
                    ", request_start_city='" + request_start_city + '\'' +
                    ", request_serial_number='" + request_serial_number + '\'' +
                    ", request_order='" + request_order + '\'' +
                    ", order_gid=" + order_gid +
                    ", cancel_reason=" + cancel_reason +
                    ", demand_gid='" + demand_gid + '\'' +
                    ", car_model=" + car_model +
                    ", midway_address=" + midway_address +
//                    ", use_trip=" + use_trip +
                    '}';
        }
}
