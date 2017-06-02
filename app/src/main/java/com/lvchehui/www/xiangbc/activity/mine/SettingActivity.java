package com.lvchehui.www.xiangbc.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.activity.ChangePhoneActivity;
import com.lvchehui.www.xiangbc.activity.login.LoginActivity;
import com.lvchehui.www.xiangbc.app.App;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.impl.OnOperationListener;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.DbUtil;
import com.lvchehui.www.xiangbc.utils.MarketUtils;
import com.lvchehui.www.xiangbc.utils.SPUtil;
import com.lvchehui.www.xiangbc.view.TitleView;
import com.lvchehui.www.xiangbc.view.dialog.CustomDialog;
import com.lvchehui.www.xiangbc.view.dialog.EmailDialog;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by 张灿能 on 2016/5/26.
 * 作用：设置
 */
@ContentView(R.layout.activity_setting)
public class SettingActivity extends BaseActivity {

    @ViewInject(R.id.title_view)
    private TitleView m_title_view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(m_title_view, getString(R.string.setting));
    }

    @Event(value = {R.id.ll_reset_phone, R.id.ll_reset_password, R.id.ll_about_us,
            R.id.ll_app_eva, R.id.ll_clear_cache, R.id.ll_logout},
            type = View.OnClickListener.class)
    private void setTingOnClick(View v) {
        Class activity = null;
        final Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.ll_reset_phone:
                //resetPhone();
                activity = ChangePhoneActivity.class;

                break;
            case R.id.ll_reset_password:
                activity = ResetPasswordActivity.class;
                intent.putExtra(Constants.SOURCE, SettingActivity.class.getSimpleName());
                break;
            case R.id.ll_about_us:
                activity = WebActivity.class;
                intent.putExtra(WebActivity.WEB_EXT_TITLE, getString(R.string.about_us));
                intent.putExtra(WebActivity.WEB_EXT_URL, getString(R.string.about_us_url));
                break;
            case R.id.ll_app_eva:
//                PgyFeedbackShakeManager.register(SettingActivity.this, Constants.APPID);
//                try
//                {
//                    testCrashReport();
//                }
//                catch (Exception e)
//                {
//                    PgyFeedbackShakeManager.register(SettingActivity.this, Constants.APPID);
//                }
                MarketUtils.launchAppDetail("com.loftsex.www", "com.qihoo.appstore");
                break;
            case R.id.ll_clear_cache:
                // 清除缓存 or 检查版本
                PgyUpdateManager.register(SettingActivity.this, Constants.APPID, new UpdateManagerListener() {
                    @Override
                    public void onUpdateAvailable() {
                        Toast.makeText(SettingActivity.this, "有新的版本", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNoUpdateAvailable() {
                        Toast.makeText(SettingActivity.this, "已经是最新的版本", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.ll_logout:
                final CustomDialog customDialog = new CustomDialog(this);
                customDialog.setTitle(getString(R.string.logout));
                customDialog.setMessage("您是否确认退出账户？");
                customDialog.setButtonsText("取消", "确认");
                customDialog.setOperationListener(new OnOperationListener() {
                    @Override
                    public void onLeftClick() {
                        customDialog.cancel();
                    }

                    @Override
                    public void onRightClick() {
                        customDialog.cancel();
                        SPUtil.getInstance(SettingActivity.this).save(Constants.USER_GID, "");
                        try {
                            DbUtil.getInstance().getDbManager().dropDb();
                        } catch (DbException e) {
                            e.printStackTrace();
                        }

                        startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                        SettingActivity.this.overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

                        App.getInstance().finishAllActivity();


                    }
                });
                customDialog.show();
                break;
        }
        if (null == activity) {
            return;
        }
        intent.setClass(this, activity);
        startActivity(intent);
    }

    /**
     * 更换手机号，页面不对？？？
     */
    private void resetPhone() {
        EmailDialog emailDialog = new EmailDialog(this);

        emailDialog.setSmailTitle(getString(R.string.reset_phone_warn));
        emailDialog.setEmailParamsAndOnClick(new EmailDialog.EmailOnClickListener() {
            @Override
            public void btnOnClick() {

                startActivity(new Intent(SettingActivity.this, ChangePhoneActivity.class));
            }

            @Override
            public void setEmailParams(ImageView iv, android.widget.TextView emailAc) {
                emailAc.setText("15859254561");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void testCrashReport() {
        throw new RuntimeException("这是个测试bug!");
    }
}
