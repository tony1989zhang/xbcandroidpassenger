package com.lvchehui.www.xiangbc.alipay;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.lvchehui.www.xiangbc.activity.home.HomeActivity;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.view.toast.ToastManager;
import com.lvchehui.www.xiangbc.wxapi.WXPayEntryActivity;

import org.xutils.x;

import java.util.Map;

/**
 * Created by 张灿能 on 2016/9/12.
 * 作用：
 */
public class AliPayHandler extends Handler {
    Activity a;
    public AliPayHandler(Activity a){
        this.a = a;
    }
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case AliPayUtils.SDK_PAY_FLAG: {
                @SuppressWarnings("unchecked")
                PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                /**
                 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
                String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                String resultStatus = payResult.getResultStatus();
                // 判断resultStatus 为9000则代表支付成功
                if (TextUtils.equals(resultStatus, "9000")) {
                    // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                    ToastManager.getManager().show("支付成功");
                    if (null != a) {
                        Intent intent = new Intent(a, WXPayEntryActivity.class);
                        intent.putExtra(Constants.PAYSOURCE.PAY_SOURCE, Constants.PAYSOURCE.PAY_FROM_ALIPAY);
                        a.startActivity(intent);
                    }

                } else {
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    ToastManager.getManager().show("支付失败");
                }
                break;
            }
            default:
                break;
        }
    };
}
