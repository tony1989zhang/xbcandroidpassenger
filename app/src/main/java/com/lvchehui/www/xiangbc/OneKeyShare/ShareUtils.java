package com.lvchehui.www.xiangbc.OneKeyShare;

import android.content.Context;

import com.lvchehui.www.xiangbc.R;

import cn.sharesdk.framework.ShareSDK;

/**
 * Created by 张灿能 on 2016/6/3.
 * 作用：一键分享 集成shareSDK
 */
public class ShareUtils  {

    private Context context;
    private static  ShareUtils shareUtils = null;
    private ShareUtils(){


    }

    public static ShareUtils getInstance(){
        if (null == shareUtils)
        {
            synchronized (ShareUtils.class) {
                if(null == shareUtils) {
                    shareUtils = new ShareUtils();
                }
            }
        }

        return shareUtils;
    }

    public void showShare(Context context,String text){

        this.context = context;
        ShareSDK.initSDK(context);

        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
          // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(context.getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText(text);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(context.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
      // 启动分享GUI
        oks.show(context);
    }
    //快捷分享的文档：http://wiki.mob.com/Android_%E5%BF%AB%E6%8D%B7%E5%88%86%E4%BA%AB
    public void showShare(Context context,String platform,String strtext){
        final OnekeyShare oks = new OnekeyShare();
//        oks.setNotification(R.mipmap.ic_launcher, context.getString(R.string.app_name));
        //不同平台的分享参数，请看文档
        //http://wiki.mob.com/Android_%E4%B8%8D%E5%90%8C%E5%B9%B3%E5%8F%B0%E5%88%86%E4%BA%AB%E5%86%85%E5%AE%B9%E7%9A%84%E8%AF%A6%E7%BB%86%E8%AF%B4%E6%98%8E
     //   String text = context.getString(R.string.share_title) + "http://www.mob.com";
        oks.setTitle("share title");
        oks.setText("测试中");
        //oks.setSilent(silent);
        oks.setDialogMode();
        oks.disableSSOWhenAuthorize();
        if (platform != null) {
            oks.setPlatform(platform);
        }
        // 去自定义不同平台的字段内容
        // http://wiki.mob.com/Android_%E5%BF%AB%E6%8D%B7%E5%88%86%E4%BA%AB#.E4.B8.BA.E4.B8.8D.E5.90.8C.E5.B9.B3.E5.8F.B0.E5.AE.9A.E4.B9.89.E5.B7.AE.E5.88.AB.E5.8C.96.E5.88.86.E4.BA.AB.E5.86.85.E5.AE.B9
        oks.setShareContentCustomizeCallback(new XiangbcShareCustomizeCallback(strtext));
        oks.show(context);
    }
    public void stopShow(){
        if (null != context) {
            ShareSDK.stopSDK(context);
        }
    }
}
