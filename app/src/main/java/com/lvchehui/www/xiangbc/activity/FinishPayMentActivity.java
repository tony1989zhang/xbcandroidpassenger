package com.lvchehui.www.xiangbc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.activity.home.HomeActivity;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.StringUtils;
import com.lvchehui.www.xiangbc.view.TitleView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by 张灿能 on 2016/6/14.
 * 作用：完成订单
 */
@ContentView(R.layout.activity_finish_payment)
public class FinishPayMentActivity extends BaseActivity {
    @ViewInject(R.id.title_view)
    private TitleView m_title_view;

    @ViewInject(R.id.iv_gif)
    private ImageView m_iv_gif;

    @ViewInject(R.id.tv_finish_said)
    private TextView m_tv_finish_said; //订金支付成功  客服将协助您完成对公账户结算 ;

    @ViewInject(R.id.tv_ok)
    private Button m_tv_ok; //查看我的行程;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(m_title_view, "支付结果");
      /*  Intent intent = getIntent();
        String strSource = intent.getStringExtra(Constants.SOURCE);
        if (StringUtils.isEmpty(strSource)){
            return;
        }
        if (strSource.equals(ConfirmOrderActivity.class.getSimpleName())){
            m_tv_finish_said.setText(getString(R.string.send_needs_wait_offers));
        }*/
    }

    @Event(value = R.id.tv_ok)
    private void finishPayOnClick(View v){
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(Constants.SOURCE,FinishPayMentActivity.class.getSimpleName());
        startActivity(intent);
    }
}
