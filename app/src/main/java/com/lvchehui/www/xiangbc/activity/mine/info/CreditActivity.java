package com.lvchehui.www.xiangbc.activity.mine.info;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.view.TitleView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by 张灿能 on 2016/6/2.
 * 作用：信用值显示页面
 */

@ContentView(R.layout.act_credit_value)
public class CreditActivity extends BaseActivity{
    @ViewInject(R.id.title_view)
    private TitleView m_title_view;

    @ViewInject(R.id.tv_credit_value_record)
    private TextView m_tv_credit_value_record;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(m_title_view, getString(R.string.credit_value));
    }

    @Event(R.id.tv_credit_value_record)
    private void recordOnClick(View v){
        showToast("此处应跳转到信用记录页面");
    }
}
