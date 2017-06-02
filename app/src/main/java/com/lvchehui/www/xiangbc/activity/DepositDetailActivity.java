package com.lvchehui.www.xiangbc.activity;

import android.os.Bundle;
import android.widget.ScrollView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.view.NodeListView.CustomNodeListView;
import com.lvchehui.www.xiangbc.view.NodeListView.LogisticsAdapter;
import com.lvchehui.www.xiangbc.view.NodeListView.LogisticsInfo;
import com.lvchehui.www.xiangbc.view.TitleView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by 张灿能 on 2016/5/27.
 * 作用： 订金详情
 */
@ContentView(R.layout.activity_deposit_detail)
public class DepositDetailActivity extends BaseActivity{
    @ViewInject(R.id.title_view)
    private TitleView m_title_view;

    @ViewInject(R.id.content_scroll_view)
    ScrollView m_scroll_view;
    @ViewInject(R.id.tv_des)
    private android.widget.TextView m_tv_des;

    @ViewInject(R.id.custom_listview)
    private CustomNodeListView m_custom_listview;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(m_title_view, "订金详情");
        m_tv_des.setText("   对于您本次取消订单，扣除您一半的订金作为车队赔偿金，另一半将退返到您支付账户");
        ArrayList<LogisticsInfo> arrayLists = new ArrayList<>();
        for (int i = 0;i <= 30 ;i ++){
            LogisticsInfo logisticsInfo = new LogisticsInfo();
            logisticsInfo.ftime = "2016-12-12";
            logisticsInfo.time = "17:39";
            logisticsInfo.context = "订金退款中，请等待银行处理";
            arrayLists.add(logisticsInfo);
        }
        m_custom_listview.setAdapter(new LogisticsAdapter(arrayLists, this));
        x.task().postDelayed(new Runnable() {
            @Override
            public void run() {
                m_scroll_view.scrollTo(0,0);
            }
        },200);

    }
}
