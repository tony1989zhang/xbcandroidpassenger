package com.lvchehui.www.xiangbc.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.utils.StringUtils;
import com.lvchehui.www.xiangbc.view.EditText.EditTextWithDel;
import com.lvchehui.www.xiangbc.view.TitleView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by 张灿能 on 2016/5/26.
 * 作用：
 */
@ContentView(R.layout.activity_change_phone)
public class ChangePhoneActivity extends BaseActivity {
    @ViewInject(R.id.title_view)
    private TitleView m_titleView;

    @ViewInject(R.id.account_et)
    private EditTextWithDel m_account_et;

    @ViewInject(R.id.captcha_et)
    private EditText m_captcha_et;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(m_titleView,"更改手机");
    }
    /*@Event(value = R.id.captcha_btn,type = View.OnClickListener.class)
    private void btnCaptcha(View v){

        showToast("发送验证码");
    }*/

    @Event(value = R.id.btn_ok,type = View.OnClickListener.class)
    private void btnOK(View v){

        String msg = "";
        if (StringUtils.isEmpty(m_account_et.getText().toString())){
            msg = "手机号不能为空";
        }
        if (StringUtils.isEmpty(m_captcha_et.getText().toString())){
            msg = "密码不能为空";
        }
        showToast(msg);
        if (StringUtils.isEmpty(msg)){
            finish();
        }
    }
}
