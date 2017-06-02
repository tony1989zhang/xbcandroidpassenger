package com.lvchehui.www.xiangbc.bean;

import java.util.List;

/**
 * Created by 张灿能 on 2016/7/18.
 * 作用：
 */
public class UsersAccountAuthlistBean extends BaseBean{


    public List<ResDataBean> resData;

    public static class ResDataBean {
        public String users_account_auth_log_gid;
        public int add_time;
        public int identification_id;
        public String name;
        public String address;
        public String owner;
        public int industry_type;
        public String credential_number;
        public String credential_photo_url;
        public String sign_in_person_name;
        public String sign_in_preson_id_card_no;
        public String sign_in_person_id_card_photo_url;
        public int auth_status;
        public String auth_reason;
        public String users_gid;

        @Override
        public String toString() {
            return "ResDataBean{" +
                    "users_account_auth_log_gid='" + users_account_auth_log_gid + '\'' +
                    ", add_time=" + add_time +
                    ", identification_id=" + identification_id +
                    ", name='" + name + '\'' +
                    ", address='" + address + '\'' +
                    ", owner='" + owner + '\'' +
                    ", industry_type=" + industry_type +
                    ", credential_number='" + credential_number + '\'' +
                    ", credential_photo_url='" + credential_photo_url + '\'' +
                    ", sign_in_person_name='" + sign_in_person_name + '\'' +
                    ", sign_in_preson_id_card_no='" + sign_in_preson_id_card_no + '\'' +
                    ", sign_in_person_id_card_photo_url='" + sign_in_person_id_card_photo_url + '\'' +
                    ", auth_status=" + auth_status +
                    ", users_gid='" + users_gid + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "errCode=" + errCode +
                ", resMsg='" + resMsg + '\'' +
                '}'+"UsersAccountAuthlistBean{" +
                "resData=" + resData +
                '}';
    }
}
