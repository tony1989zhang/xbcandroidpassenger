package com.lvchehui.www.xiangbc.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.XgoLog;
import com.lvchehui.www.xiangbc.view.toast.ToastManager;

/**
 * Created by 张灿能 on 2016/7/26.
 * 作用：接收服务的经纬度。
 */
public class AMapReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent == null)
            return;
        if (Constants.INTENT_ACTION.UPDATE_DATA.equals(intent.getAction())){
            //暂时性不做处理
            String lat = intent.getStringExtra(Constants.INTENT_ACTION.UPDATE_DATA_EXTRA_LATITUDE);
            String lon = intent.getStringExtra(Constants.INTENT_ACTION.UPDATE_DATA_EXTRA_LONGITUDE);
            String city = intent.getStringExtra(Constants.INTENT_ACTION.UPDATE_DATA_EXTRA_CITY);
         //   ToastManager.getManager().show("lat:" + lat + "lon:" + lon + "city:" + city);
           // XgoLog.e("lat:" + lat + "lon:" + lon + "city:" + city);

        }
    }
}
