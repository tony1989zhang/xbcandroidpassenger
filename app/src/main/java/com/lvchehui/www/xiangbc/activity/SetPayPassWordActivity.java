package com.lvchehui.www.xiangbc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.jungly.gridpasswordview.GridPasswordView;
import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.app.App;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.bean.BaseBean;
import com.lvchehui.www.xiangbc.utils.ConnectionManager;
import com.lvchehui.www.xiangbc.utils.ConnectionUtil;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.SPUtil;
import com.lvchehui.www.xiangbc.utils.XgoLog;
import com.lvchehui.www.xiangbc.view.TitleView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by 张灿能 on 2016/7/15.
 * 作用：设置支付密码
 */
@ContentView(R.layout.activity_set_pay_password)
public class SetPayPassWordActivity extends BaseActivity implements  GridPasswordView.OnPasswordChangedListener,ConnectionUtil.OnDataLoadEndListener{

    @ViewInject(R.id.title_view)
    private TitleView m_title_view;
    @ViewInject(R.id.gv_pay_password)
    private GridPasswordView m_gv_pay_password;
    @ViewInject(R.id.tv_title)
    private TextView m_tv_title;
    @ViewInject(R.id.tv_title_smail)
    private TextView m_tv_title_smail; 
    private boolean isFirst = true;
    private String firstPwd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(m_title_view, "闪电提现");
        m_tv_title.setText("请输入提现密码");
        m_gv_pay_password.setOnPasswordChangedListener(this);
    }

    @Override
    public void onTextChanged(String psw) {
    }

    @Override
    public void onInputFinish(String psw) {
        if (isFirst){
            m_gv_pay_password.clearPassword();
            isFirst = false;
            firstPwd = psw;
            m_tv_title.setText("请再次输入一次密码");
        }else if(!isFirst){
            if (psw.equals(firstPwd)){
                showToast("设置密码成功");
                ConnectionManager.getInstance().userSetPayPassword(this,(String) SPUtil.getInstance(this).get(Constants.USER_GID,""),psw,this);
            }else{
                showToast("密码错误");
                m_gv_pay_password.clearPassword();
                isFirst = true;
            }
        }
    }

    @Override
    public void OnLoadEnd(String ret) {
        BaseBean baseBean = App.getInstance().getBeanFromJson(ret, BaseBean.class);
        XgoLog.e("密码:" + baseBean.errCode);
        showToast(baseBean.resMsg);
        if (baseBean.errCode == -1)
        {
            startActivity(new Intent(this,PayAccountActivity.class));
        }
    }
}
