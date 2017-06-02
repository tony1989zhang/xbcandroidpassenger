package com.lvchehui.www.xiangbc.activity.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.activity.home.HomeActivity;
import com.lvchehui.www.xiangbc.app.App;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.bean.SmsBean;
import com.lvchehui.www.xiangbc.bean.UserBean;
import com.lvchehui.www.xiangbc.utils.ConnectionManager;
import com.lvchehui.www.xiangbc.utils.ConnectionUtil;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.SPUtil;
import com.lvchehui.www.xiangbc.utils.StringUtils;
import com.lvchehui.www.xiangbc.utils.XgoLog;
import com.lvchehui.www.xiangbc.view.EditText.EditTextWithDel;
import com.lvchehui.www.xiangbc.view.EditText.EditTextForPwd;
import com.lvchehui.www.xiangbc.view.TitleView;
import com.lvchehui.www.xiangbc.view.button.ButtonForCaptcha;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by 张灿能 on 2016/5/17.
 * 作用：忘记密码，重新设置密码页面
 */
@ContentView(R.layout.activity_forget)
public class ForgetActivity extends BaseActivity {
    private int forget_type;
    private static final int FORGET_TYPE_CAPTCHA = 1;
    private static final int FORGET_TYPE_OK = 2;
    private static final int REQUEST_CHECK_USERNAME = 100;
    private static final int SEND_SMS = 101;
    private static final int REQUEST_RESETPWD_AND_LOGIN = 102;

    @ViewInject(R.id.title_view)
    private TitleView mTitleView;

    @ViewInject(R.id.account_et)
    private EditTextWithDel mAccountEt;

    @ViewInject(R.id.captcha_et)
    private EditText mCaptchaEt;

    @ViewInject(R.id.password_new_et)
    private EditTextForPwd m_password_new_et;

    @ViewInject(R.id.captcha_btn)
    private ButtonForCaptcha m_btn_captcha;

    OnForgetPwdListener mListener;

    @Event(value = {R.id.captcha_btn, R.id.btn_ok}, type = View.OnClickListener.class)
    private void forgetOnClick(View v) {
        ConnectionManager cm = ConnectionManager.getInstance();
        switch (v.getId()) {
            case R.id.captcha_btn:
                if(TextUtils.isEmpty(mAccountEt.getText().toString())){
                    showToast("手机号不能空");
                    return;
                }
                mListener = new OnForgetPwdListener(this, REQUEST_CHECK_USERNAME);
                cm.checkUserName(this, mAccountEt.getText().toString(), mListener);
                //cm.sendSms(this, mAccountEt.getText().toString(), mListener);
                /*m_btn_captcha.startCountdown();
                forget_type = FORGET_TYPE_CAPTCHA;*/
                break;
            case R.id.btn_ok:
                showProgressDialog("提交验证信息中。。。");
                if (StringUtils.isEmpty(mCaptchaEt.getText())) {
                    showToast("验证码不能为空");
                    dismissProgressDialog();
                    return;
                }

                if (StringUtils.isEmpty(m_password_new_et.getText().toString())) {
                    showToast("新密码不能为空");
                    return;
                }
                mListener = new OnForgetPwdListener(this, REQUEST_RESETPWD_AND_LOGIN);
                cm.resetPassword(ForgetActivity.this, mAccountEt.getText().toString()
                        , m_password_new_et.getText().toString(), mCaptchaEt.getText().toString(), mListener);
                forget_type = FORGET_TYPE_OK;
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(mTitleView, "");
        mTitleView.setTitleBackgroundColor(Color.TRANSPARENT);
    }

    /*@Override
    public void OnLoadEnd(String ret) {
        switch (forget_type) {
            case FORGET_TYPE_CAPTCHA:
                SmsBean smsBean = App.getInstance().getBeanFromJson(ret, SmsBean.class);
                String resMsg = smsBean.resMsg;
                showToast(smsBean.resMsg);
                if (smsBean.errCode == -1 && TextUtils.equals(resMsg, "用户已经存在")) {
                }
                break;
            case FORGET_TYPE_OK:
                UserBean userBean = App.getInstance().getBeanFromJson(ret, UserBean.class);
                showToast(userBean.resMsg);
                if (userBean.errCode == -1) {
                    finish();
                }
                break;
        }


    }*/

    class OnForgetPwdListener implements ConnectionUtil.OnDataLoadEndListener{

        public int requestType = 0;
        private Context mContext;

        public OnForgetPwdListener(Context context, int requestType) {
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
                        if (errCode == -1 && TextUtils.equals(resMsg, "手机格式不正确")){
                            showToast(resMsg);
                            Log.d("getChapt", resMsg);
                            break;
                        }
                    } catch (JSONException e){
                        e.printStackTrace();
                    }

                    m_btn_captcha.startCountdown();
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
                case REQUEST_RESETPWD_AND_LOGIN:
                    UserBean userBean = App.getInstance().getBeanFromJson(ret, UserBean.class);
                    XgoLog.e("userBean:" + userBean.resData.getGid() + "userName:" + userBean.resData.getUsername());

                    SPUtil.getInstance(mContext).save(Constants.USER_GID, userBean.resData.getGid());
                    SPUtil.getInstance(mContext).save(Constants.USER_PHONE, userBean.resData.getUsername());
                    if (userBean.errCode != -1) {
                        startActivity(new Intent(mContext, HomeActivity.class));
                        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                        finish();
                    }

                    showToast(userBean.resMsg);

                    break;
            }
        }
    }
}
