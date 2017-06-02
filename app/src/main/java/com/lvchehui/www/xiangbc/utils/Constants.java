package com.lvchehui.www.xiangbc.utils;


import android.os.Environment;

/**
 * Created by 张灿能 on 2016/5/10.
 * 作用：保存常量
 */
public interface Constants {

    String WX_APPID = "wx9001cfede52481c0";//微信支付
    String APPID = "648b75ac54b827a4483c7e975e8e8d22"; // 蒲公英上传应用appId
    String IS_FIRST_LAUNCH = "is_frist_launch";
    String SOURCE = "source";
    String USER_GID = "gid";
    String USER_PHONE = "phone";
    String DETAILED_ADDRESS = "detailed_address";
    String EMAIL_CHANGE_TYPE = "email_change_type";
    //
    String AMAP_YUNTU_TABLEID = "5796bb0e7bbf1978ba69246d";
    String AMAP_WEB_API_KEY = "a57d912873a2537d75d609c52c10995e";
    long AMAP_UPLOAD_INTERVAL = 30000;

    interface PAYSOURCE {
        String PAY_SOURCE = "pay_source";
        String PAY_FROM_ALIPAY = "pay_from_alipay";
        String PAY_FROM_WX = "pay_from_wx";
    }
    interface FilePath {
        /**
         * 数据库保存的路径
         */
        String dbPath = Environment.getExternalStorageDirectory().getPath() + "/com.lvchehui.www/";
    }

    /**
     * 认证类型
     */
    interface AuthPutExtra {
        String AUTH_TYPE = "AUTH_TYPE";
        String IDENTIFICATIONBEAN_TYPE = "IDENTIFICATIONBEAN_TYPE";
        int AUTH_ENT = 1000;
        int AUTH_TRAVEL = 1001;
        int AUTH_GOV = 1002;
        int AUTH_STU = 1003;
    }

    interface INTENT_ACTION {
        String UPDATE_DATA = "intent_action_update_data";
        String UPDATE_DATA_EXTRA_LATITUDE = "intent_action_update_data_extra_latitude";
        String UPDATE_DATA_EXTRA_LONGITUDE = "intent_action_update_data_extra_longitude";
        String UPDATE_DATA_EXTRA_CITY = "intent_action_update_data_extra_city ";
    }

}
