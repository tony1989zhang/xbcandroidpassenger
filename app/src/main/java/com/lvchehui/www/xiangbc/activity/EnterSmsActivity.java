package com.lvchehui.www.xiangbc.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.view.TitleView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by 张灿能 on 2016/7/16.
 * 作用：填写发送短信验证信息
 */
@ContentView(R.layout.activity_enter_sms)
public class EnterSmsActivity extends BaseActivity {

    @ViewInject(R.id.title_view)
    private TitleView m_title_view;
    @ViewInject(R.id.tv_explanation)
    private TextView m_tv_explanation;
    @ViewInject(R.id.tv_ok)
    private TextView m_tv_ok;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(m_title_view,"填写验证码");
        m_tv_ok.setText("验证");
    }
}
