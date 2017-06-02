package com.lvchehui.www.xiangbc.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.app.App;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.bean.BaseBean;
import com.lvchehui.www.xiangbc.utils.ConnectionManager;
import com.lvchehui.www.xiangbc.utils.ConnectionUtil;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.SPUtil;
import com.lvchehui.www.xiangbc.utils.StringUtils;
import com.lvchehui.www.xiangbc.view.TitleView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by 张灿能 on 2016/6/2.
 * 作用：车队入驻
 */
@ContentView(R.layout.activity_team_settled)
public class TeamSettledActivity extends BaseActivity implements ConnectionUtil.OnDataLoadEndListener {


    @ViewInject(R.id.title_view)
    private TitleView m_title_view;

    @ViewInject(R.id.et_team_name)
    private EditText m_et_team_name;

    @ViewInject(R.id.et_team_phone)
    private EditText m_et_team_phone;

    @ViewInject(R.id.et_team_des)
    private EditText m_et_team_des;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(m_title_view, getString(R.string.fleet_settled));
    }

    @Event(value = R.id.tv_ok, type = View.OnClickListener.class)
    private void btnOkOnClick(View v) {
        if (StringUtils.isEmpty(m_et_team_name.getText().toString())
                || StringUtils.isEmpty(m_et_team_phone.getText().toString())
                || StringUtils.isEmpty(m_et_team_des.getText().toString())
                ) {
            showToast("车队信息不能为空");
            return;
        }

        showProgressDialog();
        ConnectionManager.getInstance().carteamSettledSubmit(this, (String) SPUtil.getInstance(this)
                .get(Constants.USER_GID, ""), m_et_team_name.getText().toString(),
                m_et_team_phone.getText().toString(),
                m_et_team_des.getText().toString(), this);
    }

    @Override
    public void OnLoadEnd(String ret) {
        dismissProgressDialog();
        BaseBean baseBean = App.getInstance().getBeanFromJson(ret, BaseBean.class);
        showToast(baseBean.resMsg);
        if (baseBean.errCode != -1) {
            finish();
        }
    }
}
