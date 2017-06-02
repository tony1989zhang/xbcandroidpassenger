package com.lvchehui.www.xiangbc.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import com.igexin.sdk.PushConsts;
import com.lvchehui.www.xiangbc.app.App;
import com.lvchehui.www.xiangbc.bean.BaseBean;
import com.lvchehui.www.xiangbc.bean.PushDetailGetListDataBean;
import com.lvchehui.www.xiangbc.utils.ConnectionManager;
import com.lvchehui.www.xiangbc.utils.ConnectionUtil;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.SPUtil;
import com.lvchehui.www.xiangbc.utils.XgoLog;
import com.lvchehui.www.xiangbc.view.notification.ResidentNotificationHelper;
import com.lvchehui.www.xiangbc.view.toast.ToastManager;

/**
 * Created by 张灿能 on 2016/5/27.
 * 作用：自定义个推接收，可自定义对数据进行处理
 */
public class XbcPushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d("GetuiSdkDemo", "onReceive() action=" + bundle.getInt("action"));

        switch (bundle.getInt(PushConsts.CMD_ACTION)) {
            case PushConsts.GET_CLIENTID:

                String cid = bundle.getString("clientid");
                // TODO:处理cid返回
                ToastManager.getManager().show("cid:" + cid);
                XgoLog.e("clientid:" + cid);
                ConnectionManager.getInstance().usersSaveClientId(context, (String) SPUtil.getInstance(context).get(Constants.USER_GID, ""), cid, "1", "1", new ConnectionUtil.OnDataLoadEndListener() {
                    @Override
                    public void OnLoadEnd(String ret) {
                        BaseBean bean = App.getInstance().getBeanFromJson(ret, BaseBean.class);
                        ToastManager.getManager().show(bean.resMsg);
                    }
                });
                break;
            case PushConsts.GET_MSG_DATA:

                XgoLog.e("GET_MSG_DATA:" + PushConsts.GET_MSG_DATA);
                String taskid = bundle.getString("taskid");
                String messageid = bundle.getString("messageid");
                byte[] payload = bundle.getByteArray("payload");
                if (payload != null) {
                    String data = new String(payload);
                    // TODO:接收处理透传（payload）数据
                    XgoLog.e("接收data:" + data);
                    ToastManager.getManager().show("data:" + data);
                    PushDetailGetListDataBean.ContentBean pushDetails = App.getInstance().getBeanFromJson(data, PushDetailGetListDataBean.ContentBean.class);
                    Bitmap bitmap = ConnectionUtil.getInstance().loadDrawableBitmap(pushDetails.logo_url);
                    XgoLog.e("bitmap:" +bitmap);
                    String NotifiTitle = "" + pushDetails.title;
                    String NotifiSummary = "" + pushDetails.summary;
                    ResidentNotificationHelper.sendResidentNoticeType(context,
                            NotifiTitle,
                            NotifiSummary,
                            bitmap);
                    //暂放，加判断跳转对应的页面或则打开URL
                }
                break;
            default:
                break;
        }
    }
}
