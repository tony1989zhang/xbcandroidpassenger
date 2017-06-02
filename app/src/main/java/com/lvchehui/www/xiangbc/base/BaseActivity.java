package com.lvchehui.www.xiangbc.base;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.activity.home.CityActivity;
import com.lvchehui.www.xiangbc.app.App;
import com.lvchehui.www.xiangbc.utils.XgoLog;
import com.lvchehui.www.xiangbc.view.SystemBarTintManager;
import com.lvchehui.www.xiangbc.view.TitleView;
import com.lvchehui.www.xiangbc.view.dialog.CustomProgressDialog;

import org.simple.eventbus.EventBus;
import org.xutils.view.annotation.Event;
import org.xutils.x;

import java.lang.ref.WeakReference;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

/**
 * 作用：Activity基类
 * 完成载入进度条，Toast和标题栏视图的设置
 */
public class BaseActivity extends Activity {

    protected final static String LAST_ACTIVITY_NAME = "LAST_ACTIVITY_NAME";
    private CustomProgressDialog mProgress;
    private View mToastView;
    private Toast mToast;

    public WeakReference<Activity> WriActivity = new WeakReference<Activity>(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {

        XgoLog.e("onCreate");
        // setTheme(R.style.FullBleedTheme);
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        App.getInstance().aliveActivitys.add(WriActivity);

        // 设置沉浸式状态栏？
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(getResources().getColor(R.color.title_bg_color));

        mProgress = CustomProgressDialog.createDialog(this);
        mToastView = LayoutInflater.from(this).inflate(R.layout.toast_view, null);
        mToast = Toast.makeText(this, null, Toast.LENGTH_LONG);
        mToast.setView(mToastView);

    }

/*
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }*/
    public void showProgressDialog() {
        showProgressDialog(getResources().getString(R.string.wait_a_moment));
    }

    public void showProgressDialog(String strMessage) {
        showProgressDialog(strMessage, false);
    }

    public void showProgressDialog(String strMessage, boolean isCancelAble) {

        if (null == mProgress) {
            return;
        }

        if (isCancelAble) {
            mProgress.setCancelable(true);
            mProgress.setOnCancelListener(new OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    mProgress.dismiss();
                }
            });
        }

        mProgress.setMessage(strMessage);
        mProgress.show();
    }

    public void dismissProgressDialog() {
        if (null != mProgress && mProgress.isShowing()) {
            mProgress.dismiss();
        }
    }

    public void showToast(String hint) {
        showToast(hint, Toast.LENGTH_LONG);
    }

    public void showToast(String hint, int duration) {
        if (null == mToast || null == mToastView)
            return;

        TextView tv = (TextView) mToastView.findViewById(R.id.toastMessage);
        tv.setText(hint);
        mToast.setDuration(duration);
        mToast.show();
    }

    @Event(value = {R.id.title_back_iv, R.id.title_right_tv}, type = View.OnClickListener.class)
    private void titleClick(View v) {
        switch (v.getId()) {
            case R.id.title_back_iv:
                finish();
                break;
            case R.id.title_right_tv:
                break;

            default:
                break;
        }
    }

    public void setTitleView(TitleView title_view, String title) {
        setTitleView(title_view, title, null);
    }


    public void setTitleView(TitleView title_view, String title, String rightText) {
        if (title_view == null)
            return;

        title_view.setTitle(title);
        title_view.setTitleBackVisibility(View.VISIBLE);
        title_view.setTitleRightText(rightText);
    }

    @Override
    public void startActivity(Intent intent) {
        if (null != intent) {
            intent.putExtra(LAST_ACTIVITY_NAME, this.getClass().getName());
        }
        super.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setContentView(R.layout.view_null);
        mProgress = null;
        App.getInstance().aliveActivitys.remove(WriActivity);
//        App.getInstance().getRefWatcher(this).watch(this);
        EventBus.getDefault().unregister(this);
    }
}
