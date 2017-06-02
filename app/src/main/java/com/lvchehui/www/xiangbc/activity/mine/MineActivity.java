package com.lvchehui.www.xiangbc.activity.mine;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.OneKeyShare.ShareUtils;
import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.activity.mine.auth.GetAuthInfoActivity;
import com.lvchehui.www.xiangbc.activity.mine.info.MyInfoActivity;
import com.lvchehui.www.xiangbc.app.App;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.bean.AgentApplyBean;
import com.lvchehui.www.xiangbc.bean.AgentApplyDeserializer;
import com.lvchehui.www.xiangbc.bean.UserInfoBean;
import com.lvchehui.www.xiangbc.db.UserInfoTable;
import com.lvchehui.www.xiangbc.utils.ConnectionManager;
import com.lvchehui.www.xiangbc.utils.ConnectionUtil;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.DbUtil;
import com.lvchehui.www.xiangbc.utils.PixelUtil;
import com.lvchehui.www.xiangbc.utils.SPUtil;
import com.lvchehui.www.xiangbc.utils.StringUtils;
import com.lvchehui.www.xiangbc.view.ParallaxScollView;
import com.lvchehui.www.xiangbc.view.TitleView;
import com.lvchehui.www.xiangbc.view.dialog.AgentDialog;

import org.xutils.common.util.DensityUtil;
import org.xutils.ex.DbException;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

/**
 * Created by 张灿能 on 2016/5/19.
 * 作用：我的--首页
 */
@ContentView(R.layout.activity_mine)
public class MineActivity extends BaseActivity implements ConnectionUtil.OnDataLoadEndListener {

    @ViewInject(R.id.title_view)
    private TitleView m_titleView;

    @ViewInject(R.id.iv_mine_bg)
    private ImageView m_iv_mine_bg;

    @ViewInject(R.id.parallaxScollView)
    private ParallaxScollView m_parallaxScollView;

    @ViewInject(R.id.iv_avatar)
    private ImageView m_iv_avatar;

    @ViewInject(R.id.tv_identity)
    private TextView m_tv_identity;

    @ViewInject(R.id.tv_user_nickname)
    private TextView m_tv_author;

    @ViewInject(R.id.tv_user_telephone_num)
    private TextView m_tv_phone;

    @ViewInject(R.id.tv_credit_value)
    private TextView m_tv_credit;


    private int color;
    private String titleText = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showProgressDialog();
        getUserInfo();
    }

    private void initView() {
        setTitleView(m_titleView, titleText);
        color = getResources().getColor(R.color.transparent);
        m_titleView.setTitleBackgroundColor(color);
        m_parallaxScollView.setParallaxImageView(m_iv_mine_bg);
        m_parallaxScollView.setOnScrollChangedListener(new ParallaxScollView.OnMyScrollChangedListener() {
            private int maxScrollY = PixelUtil.dp2px(getBaseContext(), 50);

            @Override
            public void onScrollChanged(ScrollView scrollView, int x, int y, int oldx, int oldy) {
                if (y >= maxScrollY) {
                    color = getResources().getColor(R.color.title_bg_color);
                    titleText = "我的";
                } else {
                    titleText = "";
                    color = getResources().getColor(R.color.transparent);
                }
                setTitleView(m_titleView, titleText);
                m_titleView.setTitleBackgroundColor(color);
            }
        });
        m_parallaxScollView.smoothScrollTo(0, 0);
    }

    @Event(R.id.tv_ok)
    private void submitOK(View v){
        startActivity(new Intent(this, AgentActivity.class));
    }
    @Event(value = {
            R.id.ll_make_money,//我要赚钱
            R.id.ll_my_red_package,//我的红包
            R.id.ll_my_msg,//我的消息
            R.id.ll_general_info,//常用信息
            R.id.ll_privilege_auth,//特权认证
            R.id.ll_invite_reward,//邀请有奖
            R.id.ll_feedback,//用户反馈
            R.id.ll_help,//用户反馈
            R.id.ll_fleet_settled,//车队入住
            R.id.ll_setting,//设置
            R.id.rl_my_info//跳转我的
    })
    private void mineOnClick(View v) {
        Class activity = null;
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.rl_my_info:
                PermissionGen.needPermission(this, 200,
                        new String[]{
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        }
                );
                break;
            case R.id.ll_my_msg:
                activity = MessageActivity.class;
                break;
            case R.id.ll_make_money:
                showProgressDialog();
                ConnectionManager.getInstance().agentGetAgentInfo(MineActivity.this,
                        (String) SPUtil.getInstance(MineActivity.this).get(Constants.USER_GID, ""),
                        new MineOnDataLoadEndListener(MineOnDataLoadEndListener.AGENT_GETAGENT_INFO));
                break;
            case R.id.ll_my_red_package:
                activity = RedpacketListActivity.class;
                break;
            case R.id.ll_general_info:
                activity = CommonInfoActivity.class;
                break;
            case R.id.ll_privilege_auth:
                PermissionGen.needPermission(this, 300,
                        new String[]{
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        }
                );

                break;
            case R.id.ll_invite_reward:
//                ShareUtils.getInstance().showShare(this);
                activity = InviteRewActivity.class; //ShareRedpacketActivity.class;
                break;
            case R.id.ll_feedback:
                activity = FeedBackActivity.class;
                break;
            case R.id.ll_help:
                activity = HelpCenterActivity.class;
                /*activity = WebActivity.class;
                intent.putExtra(WebActivity.WEB_EXT_TITLE, getString(R.string.help_center));
                intent.putExtra(WebActivity.WEB_EXT_URL, "http://e.meituan.com/helpnew/artlist/87");*/
                break;
            case R.id.ll_fleet_settled:
                activity = TeamSettledActivity.class;
                break;
            case R.id.ll_setting:
                activity = SettingActivity.class;
                break;
        }
        if (null == activity)
            return;

        intent.setClass(this, activity);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @PermissionSuccess(requestCode = 200)
    public void doSomething() {
        startActivity(new Intent(this, MyInfoActivity.class));
    }

    @PermissionSuccess(requestCode = 300)
    public void doGetAuthInfo(){
        startActivity(new Intent(this, GetAuthInfoActivity.class));
    }

    @PermissionFail(requestCode = 200)
    public void doFailSomething() {
        showToast("获取权限失败");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareUtils.getInstance().stopShow();
    }

    public void getUserInfo() {
        ConnectionManager.getInstance().usersUserInfo(MineActivity.this,
                (String) SPUtil.getInstance(MineActivity.this).get(Constants.USER_GID, ""), MineActivity.this);
    }
    @Override
    public void OnLoadEnd(String ret) {
        dismissProgressDialog();
        try {
            boolean isNull = false;
            UserInfoTable userInfoTable = DbUtil.getInstance().getDbManager().selector(UserInfoTable.class).findFirst();
            //showToast("all:" + userInfoTable);
            UserInfoBean userInfoBean = App.getInstance().getBeanFromJson(ret, UserInfoBean.class);
            if (userInfoBean.errCode != -1) {
                if (null == userInfoTable) {
                    userInfoTable = new UserInfoTable();
                    isNull = true;
                }
                loadUserInfoTable(userInfoTable, userInfoBean);
            }
            setAuthorData(userInfoTable);
            if (!isNull) {
                DbUtil.getInstance().getDbManager().update(userInfoTable, null);
            } else {
                DbUtil.getInstance().getDbManager().save(userInfoTable);
                isNull = false;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void loadUserInfoTable(UserInfoTable userInfoTable, UserInfoBean userInfoBean) {
        if (null == userInfoBean || userInfoBean.resData == null) {
            return;
        }
        userInfoTable.username = "" + userInfoBean.resData.username;
        userInfoTable.sex = userInfoBean.resData.sex;
        userInfoTable.phone = userInfoBean.resData.phone;
        userInfoTable.email = userInfoBean.resData.email;
        userInfoTable.head_url = userInfoBean.resData.head_url;
        userInfoTable.credit_score = userInfoBean.resData.credit_score;
        userInfoTable.number_name = userInfoBean.resData.number_name;
        userInfoTable.nick_name = userInfoBean.resData.nick_name;
        userInfoTable.birthday = userInfoBean.resData.birthday;
        userInfoTable.wxpay_account = userInfoBean.resData.wxpay_account;
        userInfoTable.alipay_account = userInfoBean.resData.alipay_account;
        userInfoTable.name = userInfoBean.resData.identification.name;
        userInfoTable.brokerage_rate = userInfoBean.resData.identification.brokerage_rate;
        userInfoTable.normal_subscription_rate = userInfoBean.resData.identification.normal_subscription_rate;
        userInfoTable.special_subscription_rate = userInfoBean.resData.identification.special_subscription_rate;
        userInfoTable.name = userInfoBean.resData.identification.name;
    }

    private void setAuthorData(UserInfoTable userInfoTable) {
        ImageOptions imageOptions = new ImageOptions.Builder().setSize(DensityUtil.dip2px(120), DensityUtil.dip2px(120))
                .setCircular(true)
                .setRadius(DensityUtil.dip2px(5)).setCrop(true)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP).setLoadingDrawableId(R.mipmap.default_sex_man_icon)
                .setFailureDrawableId(R.mipmap.default_sex_man_icon).build();
        x.image().bind(m_iv_avatar, userInfoTable.head_url, imageOptions);
        m_tv_identity.setText(userInfoTable.name);
        m_tv_author.setText(userInfoTable.nick_name);
        m_tv_phone.setText(StringUtils.accountFormat(userInfoTable.username, true));
        m_tv_credit.setText("信用值:" + userInfoTable.credit_score);
    }

    class MineOnDataLoadEndListener implements ConnectionUtil.OnDataLoadEndListener {
        public int requestType = 0;
        public static final int AGENT_GETAGENT_INFO = 111;

        public MineOnDataLoadEndListener(int requestType) {
            this.requestType = requestType;
        }

        @Override
        public void OnLoadEnd(String ret) {
            dismissProgressDialog();
            switch (requestType) {
                case AGENT_GETAGENT_INFO:
                    AgentApplyBean agentApplyBean = App.getInstance().getBeanFromJson(ret, AgentApplyBean.class, new AgentApplyDeserializer());
                    showToast(agentApplyBean.resMsg);
                    if (agentApplyBean.errCode != -1) {
                        Intent intent = new Intent(MineActivity.this, AgentActivity.class);
                        startActivity(intent);
                    } else {
                        AgentDialog agentDialog = new AgentDialog(MineActivity.this);
                        ArrayList<String> images = new ArrayList<>();
                        images.add("");
                        agentDialog.setImages(images);
                        agentDialog.setAgentExpland(getResources().getString(R.string.agent_description));
                        agentDialog.setAgentDialogListener(new AgentDialog.onAgentDialogListener() {
                            @Override
                            public void tryOnClick() {
                                Intent intent = new Intent(MineActivity.this, RegAgentActivity.class);
                                startActivity(intent);
                            }
                        });
                        agentDialog.show();
                    }
                    break;
            }

        }
    }
}