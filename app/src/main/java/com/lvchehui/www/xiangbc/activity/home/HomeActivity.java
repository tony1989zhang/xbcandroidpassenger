package com.lvchehui.www.xiangbc.activity.home;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.igexin.sdk.PushManager;
import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.activity.chooseneeds.FinishOrderActivity;
import com.lvchehui.www.xiangbc.activity.chooseneeds.SelectTripAndDayActivity;
import com.lvchehui.www.xiangbc.activity.freeride.FreeRideActivity;
import com.lvchehui.www.xiangbc.activity.itinerary.ItineraryActivity;
import com.lvchehui.www.xiangbc.activity.mine.AgentActivity;
import com.lvchehui.www.xiangbc.activity.mine.MessageActivity;
import com.lvchehui.www.xiangbc.activity.mine.MineActivity;
import com.lvchehui.www.xiangbc.activity.mine.RegAgentActivity;
import com.lvchehui.www.xiangbc.app.App;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.bean.AgentApplyBean;
import com.lvchehui.www.xiangbc.bean.AgentApplyDeserializer;
import com.lvchehui.www.xiangbc.bean.PushCountUnreadBean;
import com.lvchehui.www.xiangbc.db.UserInfoTable;
import com.lvchehui.www.xiangbc.receiver.AMapReceiver;
import com.lvchehui.www.xiangbc.service.LocationService;
import com.lvchehui.www.xiangbc.utils.ConnectionManager;
import com.lvchehui.www.xiangbc.utils.ConnectionUtil;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.DbUtil;
import com.lvchehui.www.xiangbc.utils.SPUtil;
import com.lvchehui.www.xiangbc.utils.StringUtils;
import com.lvchehui.www.xiangbc.utils.XgoLog;
import com.lvchehui.www.xiangbc.utils.evenbus.CityEvent;
import com.lvchehui.www.xiangbc.view.ImageCycleView;
import com.lvchehui.www.xiangbc.view.TitleView;
import com.lvchehui.www.xiangbc.view.dialog.AgentDialog;
import com.lvchehui.www.xiangbc.wxapi.WXPayEntryActivity;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

/**
 * Created by 张灿能 on 2016/5/13.
 * 作用：主页，展示地址与消息，行程，旋风车，我的，立即约车入口
 */

@ContentView(R.layout.activity_home)
public class HomeActivity extends BaseActivity implements ImageCycleView.ImageCycleViewListener, ConnectionUtil.OnDataLoadEndListener {
    @ViewInject(R.id.image_cv)
    ImageCycleView m_image_cv;

    @ViewInject(R.id.ll_home)
    private LinearLayout m_ll_home;
    @ViewInject(R.id.title_view)
    private TitleView m_titleView;
    //    @ViewInject(R.id.small_ll)
//    private LinearLayout m_small_ll;
    @ViewInject(R.id.iv_ride)
    private ImageView m_iv_ride;
    @ViewInject(R.id.tv_ride)
    private TextView m_tv_ride;
    @ViewInject(R.id.iv_mine)
    private ImageView m_iv_mine;
    @ViewInject(R.id.tv_mine)
    private TextView m_tv_mine;
    @ViewInject(R.id.iv_requirements)
    private ImageView m_iv_requirements;
    @ViewInject(R.id.rl_requirements)
    private RelativeLayout m_rl_requirements;
    @ViewInject(R.id.rl_ride)
    private RelativeLayout m_rl_ride;
    @ViewInject(R.id.rl_mine)
    private RelativeLayout m_rl_mine;
    private Request request;
    private AMapReceiver aMapReceiver = new AMapReceiver();
    private boolean hasUserInfo = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushManager.getInstance().initialize(this.getApplicationContext());

        EventBus.getDefault().register(this);
        setTitle();
        initAD();
        getUserInfo();
    }

    @Override
    protected void onStart() {
        super.onStart();
        boolean networkConnected = ConnectionUtil.getInstance().isNetworkConnected(this);
        if (hasUserInfo && networkConnected) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Constants.INTENT_ACTION.UPDATE_DATA);
            registerReceiver(aMapReceiver, intentFilter);
            Intent startService = new Intent(this, LocationService.class);
            startService(startService);
        }
    }

    /**
     * 下订单 变化时候跳转
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String intentExtra = intent.getStringExtra(Constants.SOURCE);
        if (StringUtils.isNotEmpty(intentExtra) && (intentExtra.equals(WXPayEntryActivity.class.getSimpleName()) ||
                intentExtra.equals(FinishOrderActivity.class.getSimpleName()))) {
            gotoItinearary();
        }
    }

    private void setTitle() {
        m_titleView.setTitle(getResources().getString(R.string.app_name));
        m_titleView.setTitleLeftText(getResources().getString(R.string.city));
        m_titleView.setTitleRightIv(R.mipmap.home_message_icon);
        request = ConnectionManager.getInstance().pushDetailCountUnread(this, (String) SPUtil.getInstance(this).get(Constants.USER_GID, ""), this);

    }

    private void initAD() {
        ArrayList<String> picLists = new ArrayList<>();
        picLists.add("http://7xlh8k.com1.z0.glb.clouddn.com/group3@3x.png");
        m_image_cv.setImageResources(picLists, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConnectionManager.getInstance().pushDetailCountUnread(this, (String) SPUtil.getInstance(this).get(Constants.USER_GID, ""), this);
    }

    @Override
    public void onImageClick(int position, View imageView) {
        showToast(getResources().getString(R.string.hello_everyone));
    }

    @Event(value = {R.id.rl_ride,
            R.id.rl_mine,
            R.id.iv_requirements,
            R.id.title_left_tv,
            R.id.title_right_iv,
            R.id.rl_itinerary,
            R.id.rl_make_money
    })
    private void homeOnClick(View v) {

        switch (v.getId()) {
            case R.id.rl_ride:
                startActivity(new Intent(this, FreeRideActivity.class));
                break;
            case R.id.rl_mine:
                startActivity(new Intent(this, MineActivity.class));
                break;
            case R.id.iv_requirements:
                startActivity(new Intent(this, SelectTripAndDayActivity.class));
//                chooseNeeds();
                break;
            case R.id.title_left_tv:
                PermissionGen.needPermission(this, 100,
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
                                Manifest.permission.CHANGE_WIFI_STATE
                        }
                );

                break;
            case R.id.title_right_iv:
                startActivity(new Intent(this, MessageActivity.class));
                break;
            case R.id.rl_itinerary:
                gotoItinearary();
                break;
            case R.id.rl_make_money:
                boolean networkConnected = ConnectionUtil.getInstance().isNetworkConnected(this);
                if (networkConnected) {
                    showProgressDialog();
                    ConnectionManager.getInstance().agentGetAgentInfo(this,
                            (String) SPUtil.getInstance(this).get(Constants.USER_GID, ""),
                            new MakeMoneyOnDataLoadEndListener(MakeMoneyOnDataLoadEndListener.AGENT_GETAGENT_INFO));
                } else {
                    showToast(getString(R.string.not_connected));
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @PermissionSuccess(requestCode = 100)
    public void doSomething() {
        showToast("获取到GPS定位权限");
        startActivity(new Intent(HomeActivity.this, CityActivity.class));
    }

    @PermissionFail(requestCode = 100)
    public void doFailSomething() {
        showToast("获取权限失败");
    }


    public void gotoItinearary() {
        startActivity(new Intent(this, ItineraryActivity.class));
    }

    class MakeMoneyOnDataLoadEndListener implements ConnectionUtil.OnDataLoadEndListener {
        public int requestType = 0;
        public static final int AGENT_GETAGENT_INFO = 111;

        public MakeMoneyOnDataLoadEndListener(int requestType) {
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
                        Intent intent = new Intent(HomeActivity.this, AgentActivity.class);
                        startActivity(intent);
                    } else {
                        AgentDialog agentDialog = new AgentDialog(HomeActivity.this);
                        ArrayList<String> images = new ArrayList<>();
                        images.add("");
                        agentDialog.setImages(images);
                        agentDialog.setAgentExpland(getResources().getString(R.string.agent_description));
                        agentDialog.setAgentDialogListener(new AgentDialog.onAgentDialogListener() {
                            @Override
                            public void tryOnClick() {
                                Intent intent = new Intent(HomeActivity.this, RegAgentActivity.class);
                                startActivity(intent);
                            }
                        });
                        agentDialog.show();
                    }
                    break;
            }

        }
    }

    @Override
    public void OnLoadEnd(String ret) {
        PushCountUnreadBean pushCountBean = App.getInstance().getBeanFromJson(ret, PushCountUnreadBean.class);
        if (pushCountBean.errCode != -1) {
            m_titleView.setEmCount(pushCountBean.resData);
        } else {

        }
    }

    @Subscriber(mode = ThreadMode.MAIN)
    public void onReceiveStickyEvent(CityEvent event) {
        String city = event.getCity();
        //XgoLog.e("city:" + city);
        //showToast("event:" + city);
        m_titleView.setTitleLeftText(city);
        try {
            UserInfoTable userInfoTable = DbUtil.getInstance().getDbManager().findFirst(UserInfoTable.class);
            if (null == userInfoTable) {
                userInfoTable = new UserInfoTable();
            }
            userInfoTable.choose_city = city;
            DbUtil.getInstance().getDbManager().saveOrUpdate(userInfoTable);
            hasUserInfo = true;
            List<UserInfoTable> all = DbUtil.getInstance().getDbManager().findAll(UserInfoTable.class);
          //  showToast("all:" + all);
            //XgoLog.e("userINfoTable:" + all);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (aMapReceiver != null) {
            unregisterReceiver(aMapReceiver);
        }
        stopService(new Intent(this, LocationService.class));
        EventBus.getDefault().unregister(this);
        if (request != null) {
            request = null;
        }
    }

    private void getUserInfo() {
        try {
            UserInfoTable userInfoTable = DbUtil.getInstance().getDbManager().findFirst(UserInfoTable.class);
            if (null != userInfoTable) {
                m_titleView.setTitleLeftText(userInfoTable.choose_city);
                hasUserInfo = true;
            } else {
                PermissionGen.needPermission(this, 100,
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
                                Manifest.permission.CHANGE_WIFI_STATE
                        }
                );
                //startActivity(new Intent(this, CityActivity.class));
                hasUserInfo = false;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

}
