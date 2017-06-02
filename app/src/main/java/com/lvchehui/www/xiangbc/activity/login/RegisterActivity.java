package com.lvchehui.www.xiangbc.activity.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.activity.mine.WebActivity;
import com.lvchehui.www.xiangbc.app.App;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.bean.SmsBean;
import com.lvchehui.www.xiangbc.bean.UserBean;
import com.lvchehui.www.xiangbc.utils.ConnectionManager;
import com.lvchehui.www.xiangbc.utils.ConnectionUtil;
import com.lvchehui.www.xiangbc.view.EditText.EditTextForPwd;
import com.lvchehui.www.xiangbc.view.EditText.EditTextWithDel;
import com.lvchehui.www.xiangbc.view.TitleView;
import com.lvchehui.www.xiangbc.view.button.ButtonForCaptcha;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * craete by zcn 2016_5_16
 * 作用：快速注册页面
 */
@ContentView(R.layout.activity_register)
public class RegisterActivity extends BaseActivity {
    private static final int REQUEST_CHECK_USERNAME = 100;
    private static final int SEND_SMS = 101;
    private static final int USER_REGISTER = 104;

    int requestType;

    @ViewInject(R.id.title_view)
    TitleView mTitleView;

    @ViewInject(R.id.captcha_btn)
    ButtonForCaptcha mCaptchaBtn;

    @ViewInject(R.id.account_et)
    private EditTextWithDel mAccountEt;

    @ViewInject(R.id.captcha_et)
    private EditText mCaptchaEt;

    @ViewInject(R.id.password_et)
    private EditTextForPwd mPasswordEt;

    @ViewInject(R.id.instant_register_tv)
    private TextView m_register;

    OnRegisterListener mListener;
    String mResMsg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(mTitleView, "");
        mTitleView.setTitleBackgroundColor(Color.TRANSPARENT);
        //m_register.setBackgroundCompat(getResources().getDrawable(R.drawable.btn_bg_img));
    }

    @Event(value = {R.id.instant_register_tv, R.id.captcha_btn, R.id.agreement_tv}, type = View.OnClickListener.class)
    private void registerOnClick(View v) {
        ConnectionManager cm = ConnectionManager.getInstance();
        switch (v.getId()) {
            case R.id.captcha_btn:
                if (TextUtils.isEmpty(mAccountEt.getText().toString())) {
                    showToast("手机号不能为空");
                    return;
                }
                mListener = new OnRegisterListener(this, REQUEST_CHECK_USERNAME);
                cm.checkUserName(this, mAccountEt.getText().toString(), mListener);
                break;
            case R.id.instant_register_tv:
                if (TextUtils.isEmpty(mAccountEt.getText().toString())
                        || TextUtils.isEmpty(mPasswordEt.getText().toString())
                        || TextUtils.isEmpty(mCaptchaEt.getText().toString())) {
                    showToast("账号、密码或验证码不为空");
                    //setBtnErrorText("账号、密码或验证码不为空");
                    return;
                }

                mListener = new OnRegisterListener(this, USER_REGISTER);
                cm.register(this, mAccountEt.getText().toString(),
                        mPasswordEt.getText().toString(), mCaptchaEt.getText().toString(), mListener);
                //m_register.setProgress(50);
                mCaptchaBtn.setEnabled(false);
                mAccountEt.setEnabled(false);
                mCaptchaEt.setEnabled(false);
                mPasswordEt.setEnabled(false);
                break;
            case R.id.agreement_tv:
                Intent intent = new Intent(this, WebActivity.class);
                intent.putExtra(WebActivity.WEB_EXT_TITLE, getString(R.string.leaguer_agreement));
                intent.putExtra(WebActivity.WEB_EXT_URL, getString(R.string.leaguer_agreement_url));
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCaptchaBtn.cancelCountdown();
    }

    /*@Override
    public void OnLoadEnd(String ret) {
        dismissProgressDialog();
        if (StringUtils.isEmpty(ret)) {
            return;
        }
        if (requestType == SEND_SMS) {
            SmsBean smsBean = App.getInstance().getBeanFromJson(ret, SmsBean.class);
            showToast(smsBean.resMsg);
            if (smsBean.errCode == -1) {
                return;
            }
        } else if (requestType == USER_REGISTER) {
            UserBean registerBean = App.getInstance().getBeanFromJson(ret, UserBean.class);
            showToast(registerBean.resMsg);
            if (registerBean.errCode != -1) {
                //m_register.setProgress(100);
                finish();
            } else {
                //setBtnErrorText(registerBean.resMsg);
                mCaptchaBtn.setEnabled(true);
                mAccountEt.setEnabled(true);
                mPasswordEt.setEnabled(true);
                mCaptchaEt.setEnabled(true);
            }
        }

    }*/

    class OnRegisterListener implements ConnectionUtil.OnDataLoadEndListener{

        public int requestType = 0;
        private Context mContext;

        public OnRegisterListener(Context context, int requestType) {
            mContext = context;
            this.requestType = requestType;
        }

        @Override
        public void OnLoadEnd(String ret) {
            dismissProgressDialog();
            if (TextUtils.isEmpty(ret)){
                return;
            }
            ConnectionManager cm = ConnectionManager.getInstance();
            switch (requestType){
                case REQUEST_CHECK_USERNAME:
                    JSONObject object = null;
                    try{
                        object = new JSONObject(ret);
                        int errCode = object.getInt("errCode");
                        String resMsg = object.getString("resMsg");
                        if (errCode == -1){
                            mResMsg = resMsg;
                            showToast(mResMsg);
                            Log.d("getChapt", mResMsg);
                            return;
                        }
                    } catch (JSONException e){
                        e.printStackTrace();
                    }

                    mCaptchaBtn.startCountdown();
                    showProgressDialog("正发送验证码");
                    requestType = SEND_SMS;
                    cm.sendSms(mContext, mAccountEt.getText().toString(), this);
                    break;
                case SEND_SMS:
                    SmsBean smsBean = App.getInstance().getBeanFromJson(ret, SmsBean.class);
                    showToast(smsBean.resMsg);
                    if (smsBean.errCode == -1) {
                        return;
                    }
                    break;
                case USER_REGISTER:
                    UserBean registerBean = App.getInstance().getBeanFromJson(ret, UserBean.class);
                    showToast(registerBean.resMsg);
                    if (registerBean.errCode != -1) {
                        finish();
                    } else {
                        mCaptchaBtn.setEnabled(true);
                        mAccountEt.setEnabled(true);
                        mPasswordEt.setEnabled(true);
                        mCaptchaEt.setEnabled(true);
                    }
                    break;
            }
        }
    }

    /*private void setBtnErrorText(String errorText) {
        m_register.setProgress(-1);
        m_register.setErrorText(errorText);
        x.task().postDelayed(new Runnable() {
            @Override
            public void run() {
                m_register.setProgress(0);
                m_register.setBackgroundCompat(getResources().getDrawable(R.drawable.btn_bg_img));
            }
        }, 2000);
    }*/

}
