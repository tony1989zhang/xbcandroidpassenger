package com.lvchehui.www.xiangbc.OneKeyShare;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.utils.XgoLog;

import cn.sharesdk.framework.Platform;

/**
 * Created by 张灿能 on 2016/6/6.
 * 作用：自定义分享回调页面
 */
public class XiangbcShareCustomizeCallback implements ShareContentCustomizeCallback
{
    private String strText;
    public XiangbcShareCustomizeCallback(String strText){
        this.strText = strText;
    }
    @Override
    public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
        platform.getName();

        XgoLog.e("分享");
        if ("WechatMoments".equals(platform.getName())) {
            // 改写twitter分享内容中的text字段，否则会超长，
            // 因为twitter会将图片地址当作文本的一部分去计算长度
            strText += platform.getContext().getString(R.string.share_to_wechatmoment);
            paramsToShare.setText(strText);
        }else if("SinaWeibo".equals(platform.getName())){
            strText += platform.getContext().getString(R.string.share_to_sina);
            paramsToShare.setText(strText);
        }else if("TencentWeibo".equals(platform.getName())){
            strText += platform.getContext().getString(R.string.share_to_tencent);
            paramsToShare.setText(strText);
        }else if("ShortMessage".equals(platform.getName())){
            strText += platform.getContext().getString(R.string.share_to_sms);
            paramsToShare.setText(strText);

        }
    }
}
