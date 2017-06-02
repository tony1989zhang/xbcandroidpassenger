package com.lvchehui.www.xiangbc.activity.mine.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.app.App;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.bean.GetIdentificationInfoBean;
import com.lvchehui.www.xiangbc.bean.UsersAccountAuthlistBean;
import com.lvchehui.www.xiangbc.utils.ConnectionManager;
import com.lvchehui.www.xiangbc.utils.ConnectionUtil;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.SPUtil;
import com.lvchehui.www.xiangbc.utils.XgoLog;
import com.lvchehui.www.xiangbc.view.TitleView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by 张灿能 on 2016/7/18.
 * 作用：特权认证状态
 */
@ContentView(R.layout.activity_users_account_authlist)
public class GetAuthInfoActivity extends BaseActivity {
    @ViewInject(R.id.title_view)
    private TitleView m_title_view;

    @ViewInject(R.id.tv_title)
    private TextView m_tv_title;

    @ViewInject(R.id.iv_sers_account_status)
    private ImageView m_iv_sers_account_status;

    @ViewInject(R.id.tv_users_account_status)
    private TextView m_tv_users_account_status;

    @ViewInject(R.id.tv_ok)
    private TextView m_tv_ok;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(m_title_view, getString(R.string.privilege_auth));
        m_tv_ok.setText(getString(R.string.submit_auth));
        showProgressDialog();
        ConnectionManager.getInstance().usersAccountAuthlist(this,
                (String) SPUtil.getInstance(this).get(Constants.USER_GID, ""),
                new getAuthInfoOnLoadListener(getAuthInfoOnLoadListener.GET_USERS_ACCOUNT_AUTH_LIST));
    }

    @Event(value = R.id.tv_ok)
    private void usersAccountOnClick(View v) {
        gotoIndentificationListActivity();
    }

    class getAuthInfoOnLoadListener implements ConnectionUtil.OnDataLoadEndListener {

        public int type = 0;
        public static final int GET_USERS_ACCOUNT_AUTH_LIST = 1002;
        public static final int GET_IDENTIFICATION_INFO = 1003;

        public getAuthInfoOnLoadListener(int type) {
            this.type = type;
        }

        @Override
        public void OnLoadEnd(String ret) {
            if (type == GET_USERS_ACCOUNT_AUTH_LIST) onLoadUsersAcc(ret);
            else if (type == GET_IDENTIFICATION_INFO) onLoadIdenti(ret);
        }

        private void onLoadIdenti(String ret) {
            dismissProgressDialog();
            GetIdentificationInfoBean identificationInfoBean = App.getInstance().getBeanFromJson(ret, GetIdentificationInfoBean.class);
            if (identificationInfoBean.errCode == -1) {

            } else {
                m_iv_sers_account_status.setImageResource(R.mipmap.account_succeed);
                m_tv_users_account_status.setText(identificationInfoBean.resData.description);
                m_tv_title.setText(identificationInfoBean.resData.name);
                m_tv_title.setVisibility(View.VISIBLE);
                m_tv_ok.setVisibility(View.INVISIBLE);
            }
        }

        private void onLoadUsersAcc(String ret) {
            dismissProgressDialog();
            UsersAccountAuthlistBean bean = App.getInstance().getBeanFromJson(ret, UsersAccountAuthlistBean.class);
            XgoLog.e("bean:" + bean.toString());
            if (bean.errCode == -1) {
                gotoIndentificationListActivity();
            } else if (bean.resData != null &&bean.resData.size() > 0) {
                if (bean.resData.get(0).auth_status == 1) {
                    m_iv_sers_account_status.setImageResource(R.mipmap.account_ongoing);
                    m_tv_users_account_status.setText(getString(R.string.privilege_auth_going));
                    m_tv_ok.setVisibility(View.INVISIBLE);
                } else if (bean.resData.get(0).auth_status == 2) {
                    showProgressDialog();
                    ConnectionManager.getInstance().getIdentificationInfo(GetAuthInfoActivity.this,
                            (String) SPUtil.getInstance(GetAuthInfoActivity.this).get(Constants.USER_GID, ""),
                            "" + bean.resData.get(0).identification_id, "", new getAuthInfoOnLoadListener(GET_IDENTIFICATION_INFO));

                } else if (bean.resData.get(0).auth_status == 3) {
                    m_iv_sers_account_status.setImageResource(R.mipmap.account_failed);
                    m_tv_users_account_status.setText(bean.resData.get(0).auth_reason);
                    m_tv_ok.setVisibility(View.VISIBLE);
                    m_tv_ok.setText(getString(R.string.privilege_auth_again));
                }
            }
        }
    }

    private void gotoIndentificationListActivity() {
        startActivity(new Intent(GetAuthInfoActivity.this, IdentificationListActivity.class));
        finish();
    }
}
