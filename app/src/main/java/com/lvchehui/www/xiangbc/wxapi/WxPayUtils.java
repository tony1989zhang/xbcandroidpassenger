package com.lvchehui.www.xiangbc.wxapi;

import android.content.Context;
import android.util.Log;

import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.XgoLog;
import com.lvchehui.www.xiangbc.view.toast.ToastManager;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONObject;

/**
 * Created by 张灿能 on 2016/9/10.
 * 作用：
 */
public class WxPayUtils {

    public static void payOrder(Context context,String content)
    {

        IWXAPI api = WXAPIFactory.createWXAPI(context,null);//wx9001cfede52481c0
        api.registerApp(Constants.WX_APPID);
        boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        if (!isPaySupported)
        {
            ToastManager.getManager().show("未安装微信,或版本过低不支持支付");
        }
        ToastManager.getManager().show("获取订单中...");
        try{
            if (content != null) {
                XgoLog.i(content);
                JSONObject json = new JSONObject(content);
                if(null != json && !json.has("retcode") ){
                    PayReq req = new PayReq();
                    //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                    req.appId			= json.getString("appid");
                    req.partnerId		= json.getString("partnerid");
                    req.prepayId		= json.getString("prepayid");
                    req.nonceStr		= json.getString("noncestr");
                    req.timeStamp		= json.getString("timestamp");
                    req.packageValue	= json.getString("package");
                    req.sign			= json.getString("sign");
                    req.extData			= "app data"; // optional
                    ToastManager.getManager().show("正常调起支付");
                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                    api.sendReq(req);
                }else{
                    Log.d("PAY_GET", "返回错误" + json.getString("retmsg"));
                    ToastManager.getManager().show("返回错误"+json.getString("retmsg"));
                }
            }else{
                Log.d("PAY_GET", "服务器请求错误");
                ToastManager.getManager().show("服务器请求错误");
            }
        }catch(Exception e){
            Log.e("PAY_GET", "异常："+e.getMessage());
            ToastManager.getManager().show("异常："+e.getMessage());
        }
    }

}
