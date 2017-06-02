package com.lvchehui.www.xiangbc.activity.mine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.OneKeyShare.ShareUtils;
import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.app.App;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.bean.RedpacketShareBean;
import com.lvchehui.www.xiangbc.utils.ConnectionManager;
import com.lvchehui.www.xiangbc.utils.ConnectionUtil;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.SPUtil;
import com.lvchehui.www.xiangbc.utils.StringUtils;
import com.lvchehui.www.xiangbc.view.TitleView;
import com.lvchehui.www.xiangbc.zxing.QRCodeUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by 张灿能 on 2016/6/12.
 * 作用：邀请有奖
 */
@ContentView(R.layout.activity_invite_rew)
public class InviteRewActivity extends BaseActivity implements ConnectionUtil.OnDataLoadEndListener{
    @ViewInject(R.id.title_view)
    private TitleView m_title_view;

    @ViewInject(R.id.tv_invite_num)
    private TextView m_tv_invite_num ;//0;

    @ViewInject(R.id.tv_consumer_num)
    private TextView m_tv_consumer_num ;//0;

    @ViewInject(R.id.share_wx)
    private LinearLayout m_share_wx;

    @ViewInject(R.id.share_cicle)
    private LinearLayout m_share_cicle;

    @ViewInject(R.id.share_msg)
    private LinearLayout m_share_msg;

    @ViewInject(R.id.share_qq)
    private LinearLayout m_share_qq;

    @ViewInject(R.id.share_qzone)
    private LinearLayout m_share_qzone;

    @ViewInject(R.id.share_sina)
    private LinearLayout m_share_sina;

    private String shareText = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(m_title_view, getString(R.string.invite_reward), getString(R.string.red_package));
        showProgressDialog("正获取分享码");
        ConnectionManager.getInstance().redpacket_Share(this, (String) SPUtil.getInstance(this).get(Constants.USER_GID, ""), this);
    }

    @Event(value = R.id.title_right_tv,type = View.OnClickListener.class)
    private void rightTitleOnClick(View v){
        startActivity(new Intent(this,RedpacketListActivity.class));
    }
    @Event(value = {R.id.share_wx,R.id.share_cicle,R.id.share_msg,R.id.share_qq,R.id.share_qzone,R.id.share_sina},type = View.OnClickListener.class)
    private void inviteRewOnClick(View view){
        ShareSDK.initSDK(InviteRewActivity.this);
        String platfrom =null;
        switch (view.getId()){

            case R.id.share_wx:
                platfrom = Wechat.NAME;
                break;
            case R.id.share_cicle:
                platfrom = WechatMoments.NAME;
                break;
            case R.id.share_msg:
                platfrom = ShortMessage.NAME;
                break;
            case R.id.share_qq:
                platfrom = QQ.NAME;
                break;
            case R.id.share_qzone:
                platfrom = QZone.NAME;
                break;
            case R.id.share_sina:
                platfrom = SinaWeibo.NAME;
                break;
        }

        ShareUtils.getInstance().showShare(InviteRewActivity.this, platfrom,shareText);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(this);
    }

    @Override
    public void OnLoadEnd(String ret) {
        if (StringUtils.isEmpty(ret)){
            return;
        }
        RedpacketShareBean redpacketShareBean = App.getInstance().getBeanFromJson(ret, RedpacketShareBean.class);
        showToast(redpacketShareBean.resMsg);
        if (redpacketShareBean.errCode !=-1){
            Bitmap qrCode = QRCodeUtil.getInstance().createQRCode(redpacketShareBean.resData.redPacketCode);
            shareText = redpacketShareBean.resData.redPacketCode;
            dismissProgressDialog();
        }

    }
}
