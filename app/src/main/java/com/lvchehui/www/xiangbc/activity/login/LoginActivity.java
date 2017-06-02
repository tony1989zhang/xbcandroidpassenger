package com.lvchehui.www.xiangbc.activity.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.activity.home.HomeActivity;
import com.lvchehui.www.xiangbc.app.App;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.bean.UserBean;
import com.lvchehui.www.xiangbc.bean.UserDataBean;
import com.lvchehui.www.xiangbc.utils.ConnectionManager;
import com.lvchehui.www.xiangbc.utils.ConnectionUtil;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.DbUtil;
import com.lvchehui.www.xiangbc.utils.MD5Util;
import com.lvchehui.www.xiangbc.utils.SPUtil;
import com.lvchehui.www.xiangbc.utils.StringUtils;
import com.lvchehui.www.xiangbc.utils.XgoLog;
import com.lvchehui.www.xiangbc.view.EditText.EditTextWithDel;
import com.lvchehui.www.xiangbc.view.EditText.EditTextForPwd;
import com.lvchehui.www.xiangbc.view.TitleView;
import com.lvchehui.www.xiangbc.view.dragtop.DragTopLayout;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by zcn on 2016/5/16.
 * 作用：注册页面
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity implements ConnectionUtil.OnDataLoadEndListener {

    /**
     * 考虑用smooth-app-bar-layout代替
     */
    @ViewInject(R.id.drag_top_view)
    DragTopLayout dragLayout;

    @ViewInject(R.id.title_view)
    TitleView titleView;

    @ViewInject(R.id.account_et)
    EditTextWithDel m_account_et;

    @ViewInject(R.id.password_et)
    private EditTextForPwd m_password_et;

    UserDataBean userInfo;

    private boolean showTopView = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showProgressDialog();
        x.task().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismissProgressDialog();
            }
        }, 1000);

        dragLayout.setOverDrag(!dragLayout.isOverDrag());
        setTitleView(titleView, "");
        titleView.setTitleBackgroundColor(Color.TRANSPARENT);
        titleView.setTitleBackVisibility(View.GONE);

        DbManager db = DbUtil.getInstance().getDbManager();
        try {
            userInfo = db.selector(UserDataBean.class).findFirst();
            if (null == userInfo) {
                showToast("未登陆");
            } else {
                m_account_et.setText(userInfo.getUsername());
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Event(value = {R.id.title_right_tv, R.id.telephone_number_login_tv, R.id.login_tv,
            R.id.instant_register_tv, R.id.top_view, R.id.drag_content_view,
            R.id.forget_pwd_tv_02, R.id.forget_pwd_tv},
            type = View.OnClickListener.class)
    private void loginOnClick(View v) {
        switch (v.getId()) {
            case R.id.title_right_tv:
                backTopView();
                break;
            case R.id.telephone_number_login_tv:
                changeTitle(true, titleView, "", getResources().getColor(R.color.title_bg_color));
                showTopView = false;
                break;
            case R.id.login_tv:
                if (StringUtils.isEmpty(m_password_et.getText().toString()) || StringUtils.isEmpty(m_account_et.getText().toString())) {
                    showToast("账号或密码不能为空");
                    return;
                }
                showProgressDialog("正在登录");
                String md5PWD = MD5Util.getMD5(m_password_et.getText().toString());
                XgoLog.e("md5PWD:" + md5PWD);
                ConnectionManager.getInstance().login(this, m_account_et.getText().toString(), md5PWD, this);
                break;
            case R.id.instant_register_tv:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.top_view:
                break;
            case R.id.drag_content_view:
                break;
            case R.id.forget_pwd_tv:
            case R.id.forget_pwd_tv_02:
                startActivity(new Intent(LoginActivity.this, ForgetActivity.class));
                break;
        }

    }

    /**
     * 返回上级页面
     */
    private void backTopView() {
        showTopView = !showTopView;
        changeTitle(showTopView, titleView, "", Color.TRANSPARENT);
        /**
         * 取消该页面EditText 占焦点
         */
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != imm) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    /**
     *
     * @param touchMode
     * @param titleView
     * @param cannel
     * @param color
     */
    private void changeTitle(boolean touchMode, TitleView titleView, String cannel, int color) {
        // showTopView = !showTopView;
        dragLayout.toggleTopView(touchMode);
        setTitleView(titleView, "", cannel);
        // mTitleView.setTitleBackgroundColor(color);
    }

    @Override
    public void OnLoadEnd(String ret) {

        dismissProgressDialog();

        if (StringUtils.isEmpty(ret))
            return;

        UserBean userBean = App.getInstance().getBeanFromJson(ret, UserBean.class);
        XgoLog.e("userBean:" + userBean.resData.getGid() + "userName:" + userBean.resData.getUsername());

        SPUtil.getInstance(this).save(Constants.USER_GID, userBean.resData.getGid());
        SPUtil.getInstance(this).save(Constants.USER_PHONE, userBean.resData.getUsername());
        if (userBean.errCode != -1) {
            startActivity(new Intent(this, HomeActivity.class));
            overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            finish();
        }

        showToast(userBean.resMsg);
    }

    @Override
    public void finish() {
        if (showTopView) {
            super.finish();
        } else {
            backTopView();
        }
    }
}
