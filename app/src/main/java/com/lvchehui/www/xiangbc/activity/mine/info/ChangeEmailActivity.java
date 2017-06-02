package com.lvchehui.www.xiangbc.activity.mine.info;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.view.TextView.EmailAutoCompleteTextView;
import com.lvchehui.www.xiangbc.view.TitleView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by 张灿能 on 2016/5/23.
 * 作用：更换邮箱
 */
@ContentView(R.layout.activity_change_email)
public class ChangeEmailActivity extends BaseActivity {
    @ViewInject(R.id.title_view)
    private TitleView m_titleView;

    @ViewInject(R.id.tv_ok)
    private TextView m_tv_ok;

    @ViewInject(R.id.email_auto_complete_tv)
    private EmailAutoCompleteTextView m_email_auto_complete_tv;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(m_titleView, getString(R.string.email));
        m_tv_ok.setText(getString(R.string.complete));
    }

    @Event(value = {R.id.tv_ok, R.id.email_auto_complete_tv}, type = View.OnClickListener.class)
    private void changeEmailOnClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ok:
                finish();
                break;
            case R.id.email_auto_complete_tv:
                m_email_auto_complete_tv.setCursorVisible(true);
        }
    }

    @Override
    public void finish() {
        setResult(RESULT_FIRST_USER, new Intent().putExtra(Constants.EMAIL_CHANGE_TYPE, m_email_auto_complete_tv.getText().toString()));
        super.finish();
    }
}
