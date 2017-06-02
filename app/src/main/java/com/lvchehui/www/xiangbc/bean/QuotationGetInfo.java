package com.lvchehui.www.xiangbc.bean;

import java.util.List;

/**
 * Created by 张灿能 on 2016/9/21.
 * 作用：
 */
public class QuotationGetInfo extends BaseBean {

    public ResDataBean resData;

    public static class ResDataBean {
        public int add_time;
        public int update_time;
        public int is_table;
        public int is_lodging;
        public int is_gasoline;
        public int is_road_and_bridge;
        public int is_parking;
        public int is_change_car;
        public int is_daily;
        public String quotation_remark;
        public long start_booking_time;
        public long end_booking_time;
        public int loss_percent;
        public double motorcade_quotation_money;
        public double brokerage_money;
        public double invoice_money;
        public double deposit;
        public int quotation_status;
        public String demand_gid;
        public String users_gid;
        public String users_account_gid;
        public String belong_users_gid;
        public String belong_users_account_gid;
        public String begin_address;
        public String end_address;
        public long begin_time;
        public int use_type;
        public String quotation_gid;
        public List<CarsInfoBean> cars_info;
        public List<MotorcadeInfoBean> motorcade_info;

        public static class CarsInfoBean {
            public int add_time;
            public int update_time;
            public String cars_gid;
            public String plate_number;
            public String vehicle_model;
            public String vehicle_color;
            public int seat_number_just;
            public int seat_number_sum_as;
            public int registration_first;
            public int passanger_premium;
            public String passanger_premium_url;
            public String drive_licence_number;
            public String drive_licence_url;
            public String road_transport_number;
            public String road_transport_url;
            public CarPhotosUrlBean car_photos_url;
            public String car_describe;
            public String motorcade_gid;
            public String motorcade_name;
            public String quotation_gid;
            public int sex;
            public String phone;
            public String quotation_cars_gid;

            public static class CarPhotosUrlBean {
                public List<String> out;
                public List<String> in;
            }
        }

        public static class MotorcadeInfoBean {
            public int add_time;
            public int update_time;
            public int type;
            public String name;
            public String legal_person_name;
            public String address;
            public String province;
            public String city;
            public String area;
            public String business_licence_url;
            public String business_licence_number;
            public String road_manage_licence_url;
            public String road_manage_license_number;
            public String bank_area;
            public String bank;
            public String bank_card;
            public int bank_type;
            public String holder;
            public String users_gid;
            public String users_account_gid;
            public int check_status;
            public String previous_gid;
            public int intergral;
            public int volume;
            public String motorcade_gid;
            public List<String> protocol_url;
            public List<String> check_reason;
        }
    }
}
