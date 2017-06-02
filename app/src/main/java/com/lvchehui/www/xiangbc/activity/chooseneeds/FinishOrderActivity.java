package com.lvchehui.www.xiangbc.activity.chooseneeds;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.activity.home.HomeActivity;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.view.TitleView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by 张灿能 on 2016/9/13.
 * 作用：
 */
@ContentView(R.layout.activity_finish_order)
public class FinishOrderActivity extends BaseActivity {
    @ViewInject(R.id.title_view)
    private TitleView m_title_view;

    @ViewInject(R.id.tv_finish_said)
    private TextView m_tv_finish_said; //@string/order_finish;

    @ViewInject(R.id.tv_ok)
    private TextView m_tv_ok; //查看我的行程;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(m_title_view,"完成支付");
    }
    @Event(value = R.id.tv_ok)
    private void finishPayOnClick(View v){
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(Constants.SOURCE, FinishOrderActivity.class.getSimpleName());
        startActivity(intent);
    }
}
