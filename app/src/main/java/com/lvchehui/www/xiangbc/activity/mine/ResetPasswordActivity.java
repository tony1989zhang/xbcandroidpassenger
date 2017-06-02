package com.lvchehui.www.xiangbc.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.activity.login.ForgetActivity;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.StringUtils;
import com.lvchehui.www.xiangbc.view.EditText.EditTextForPwd;
import com.lvchehui.www.xiangbc.view.TitleView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by 张灿能 on 2016/5/17.
 * 作用：修改密码
 */
@ContentView(R.layout.activity_reset_password)
public class ResetPasswordActivity extends BaseActivity {

    @ViewInject(R.id.title_view)
    private TitleView m_titleView;

    @ViewInject(R.id.password_old_et)
    private EditTextForPwd m_password_old_et;

    @ViewInject(R.id.password_new_et)
    private EditTextForPwd m_password_new_et;

    @ViewInject(R.id.btn_ok)
    private TextView m_btn_ok;

    private String m_source;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(m_titleView, getString(R.string.reset_password));
        Intent intent = getIntent();
        m_source = intent.getStringExtra(Constants.SOURCE);
        if (ForgetActivity.class.getSimpleName().equals(m_source)) {
            m_password_old_et.setVisibility(View.GONE);
        } else if (SettingActivity.class.getSimpleName().equals(m_source)) {
            m_password_old_et.setVisibility(View.VISIBLE);
        }

    }

    @Event(value = R.id.btn_ok, type = View.OnClickListener.class)
    private void resetPWD(View v) {
        if (SettingActivity.class.getSimpleName().equals(m_source) && StringUtils.isEmpty(m_password_old_et.getText().toString())) {
            showToast("原密码不能为空");
        } else if (StringUtils.isEmpty(m_password_new_et.getText().toString())) {
            showToast("新密码不能为空");
        }

        showToast("更改密码成功");
    }
}
