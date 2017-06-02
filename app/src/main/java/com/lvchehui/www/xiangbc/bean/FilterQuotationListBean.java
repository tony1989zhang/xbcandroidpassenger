package com.lvchehui.www.xiangbc.bean;

import java.util.List;

/**
 * Created by 张灿能 on 2016/9/12.
 * 作用：
 */
public class FilterQuotationListBean extends BaseBean {

    public List<ResDataBean> resData;

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
        public String motorcade_quotation_money;
        public String brokerage_money;
        public String invoice_money;
        public String deposit;
        public int quotation_status;
        public String users_gid;
        public String users_account_gid;
        public String belong_users_gid;
        public String belong_users_account_gid;
        public String begin_address;
        public String end_address;
        public long begin_time;
        public int use_type;
        public String quotation_gid;
        public List<List<String>> address;
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
            public int manage_scope;
            public CarPhotosUrlBean car_photos_url;
            public String car_describe;
            public String motorcade_gid;
            public String motorcade_name;
            public String quotation_gid;
            public String quotation_cars_gid;

            @Override
            public String toString() {
                return "CarsInfoBean{" +
                        "add_time=" + add_time +
                        ", update_time=" + update_time +
                        ", cars_gid='" + cars_gid + '\'' +
                        ", plate_number='" + plate_number + '\'' +
                        ", vehicle_model='" + vehicle_model + '\'' +
                        ", vehicle_color='" + vehicle_color + '\'' +
                        ", seat_number_just=" + seat_number_just +
                        ", seat_number_sum_as=" + seat_number_sum_as +
                        ", registration_first=" + registration_first +
                        ", passanger_premium=" + passanger_premium +
                        ", passanger_premium_url='" + passanger_premium_url + '\'' +
                        ", drive_licence_number='" + drive_licence_number + '\'' +
                        ", drive_licence_url='" + drive_licence_url + '\'' +
                        ", road_transport_number='" + road_transport_number + '\'' +
                        ", road_transport_url='" + road_transport_url + '\'' +
                        ", manage_scope=" + manage_scope +
                        ", car_photos_url=" + car_photos_url +
                        ", car_describe='" + car_describe + '\'' +
                        ", motorcade_gid='" + motorcade_gid + '\'' +
                        ", motorcade_name='" + motorcade_name + '\'' +
                        ", quotation_gid='" + quotation_gid + '\'' +
                        ", quotation_cars_gid='" + quotation_cars_gid + '\'' +
                        '}';
            }

            public static class CarPhotosUrlBean {
                public List<String> in;
                public List<String> out;

                @Override
                public String toString() {
                    return "CarPhotosUrlBean{" +
                            "in=" + in +
                            ", out=" + out +
                            '}';
                }
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
            public int intergral;
            public int volume;
            public String motorcade_gid;
            public List<String> protocol_url;
            public List<String> check_reason;

            @Override
            public String toString() {
                return "MotorcadeInfoBean{" +
                        "add_time=" + add_time +
                        ", update_time=" + update_time +
                        ", type=" + type +
                        ", name='" + name + '\'' +
                        ", legal_person_name='" + legal_person_name + '\'' +
                        ", address='" + address + '\'' +
                        ", province='" + province + '\'' +
                        ", city='" + city + '\'' +
                        ", area='" + area + '\'' +
                        ", business_licence_url='" + business_licence_url + '\'' +
                        ", business_licence_number='" + business_licence_number + '\'' +
                        ", road_manage_licence_url='" + road_manage_licence_url + '\'' +
                        ", road_manage_license_number='" + road_manage_license_number + '\'' +
                        ", bank_area='" + bank_area + '\'' +
                        ", bank='" + bank + '\'' +
                        ", bank_card='" + bank_card + '\'' +
                        ", bank_type=" + bank_type +
                        ", holder='" + holder + '\'' +
                        ", users_gid='" + users_gid + '\'' +
                        ", users_account_gid='" + users_account_gid + '\'' +
                        ", check_status=" + check_status +
                        ", intergral=" + intergral +
                        ", volume=" + volume +
                        ", motorcade_gid='" + motorcade_gid + '\'' +
                        ", protocol_url=" + protocol_url +
                        ", check_reason=" + check_reason +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "ResDataBean{" +
                    "add_time=" + add_time +
                    ", update_time=" + update_time +
                    ", is_table=" + is_table +
                    ", is_lodging=" + is_lodging +
                    ", is_gasoline=" + is_gasoline +
                    ", is_road_and_bridge=" + is_road_and_bridge +
                    ", is_parking=" + is_parking +
                    ", is_change_car=" + is_change_car +
                    ", is_daily=" + is_daily +
                    ", quotation_remark='" + quotation_remark + '\'' +
                    ", start_booking_time=" + start_booking_time +
                    ", end_booking_time=" + end_booking_time +
                    ", loss_percent=" + loss_percent +
                    ", motorcade_quotation_money='" + motorcade_quotation_money + '\'' +
                    ", brokerage_money='" + brokerage_money + '\'' +
                    ", invoice_money='" + invoice_money + '\'' +
                    ", deposit='" + deposit + '\'' +
                    ", quotation_status=" + quotation_status +
                    ", users_gid='" + users_gid + '\'' +
                    ", users_account_gid='" + users_account_gid + '\'' +
                    ", belong_users_gid='" + belong_users_gid + '\'' +
                    ", belong_users_account_gid='" + belong_users_account_gid + '\'' +
                    ", begin_address='" + begin_address + '\'' +
                    ", end_address='" + end_address + '\'' +
                    ", begin_time=" + begin_time +
                    ", use_type=" + use_type +
                    ", quotation_gid='" + quotation_gid + '\'' +
                    ", address=" + address +
                    ", cars_info=" + cars_info +
                    ", motorcade_info=" + motorcade_info +
                    '}';
        }
    }
}
