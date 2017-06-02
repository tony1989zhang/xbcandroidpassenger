package com.lvchehui.www.xiangbc.activity.itinerary;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.Fragment.QuotedCarListFragment;
import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.base.ActivityForFragmentNormal;
import com.lvchehui.www.xiangbc.utils.ConnectionManager;
import com.lvchehui.www.xiangbc.utils.XgoLog;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by 张灿能 on 2016/6/16.
 * 作用：报价列表
 */
@ContentView(R.layout.activity_quoted_car_list)
public class QuotationedListActivity extends ActivityForFragmentNormal {


    @ViewInject(R.id.tv_price_sort)
    private TextView m_tv_price_sort;

    @ViewInject(R.id.tv_volume_sort)
    private TextView m_tv_volume_sort;

    @ViewInject(R.id.tv_eva_sort)
    private TextView m_tv_eva_sort;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(m_title_view, "我的行程");
        EventBus.getDefault().registerSticky(this);
    }



    @Event(value = {R.id.tv_price_sort,R.id.tv_volume_sort,R.id.tv_eva_sort})
    private void quotedCarOnClick(View v){
        MyTripQuotedCarListFragment  quotedCarListFragment = null;
        if (mFragment instanceof  MyTripQuotedCarListFragment) {
            quotedCarListFragment = (MyTripQuotedCarListFragment) mFragment;
        }else{
            return;
        }
        switch (v.getId()){
            case R.id.tv_price_sort:
                quotedCarListFragment.setSortQuotedCar(MyTripQuotedCarListFragment.PRICE_SORT_TYPE);
                m_tv_price_sort.setTextColor(getResources().getColor(R.color.swipe_refrsh_color3));
                m_tv_volume_sort.setTextColor(getResources().getColor(R.color.text_black));
                m_tv_eva_sort.setTextColor(getResources().getColor(R.color.text_black));
                break;
            case R.id.tv_volume_sort:
                quotedCarListFragment.setSortQuotedCar(MyTripQuotedCarListFragment.VOLUME_SORT_TYPE);
                m_tv_price_sort.setTextColor(getResources().getColor(R.color.text_black));
                m_tv_volume_sort.setTextColor(getResources().getColor(R.color.swipe_refrsh_color3));
                m_tv_eva_sort.setTextColor(getResources().getColor(R.color.text_black));
                break;
            case R.id.tv_eva_sort:
                quotedCarListFragment.setSortQuotedCar(MyTripQuotedCarListFragment.EVA_SORT_TYPE);
                m_tv_price_sort.setTextColor(getResources().getColor(R.color.text_black));
                m_tv_volume_sort.setTextColor(getResources().getColor(R.color.text_black));
                m_tv_eva_sort.setTextColor(getResources().getColor(R.color.swipe_refrsh_color3));
                break;
        }

        quotedCarListFragment.onRefresh();
    }

    @Override
    public Fragment initFragment() {
        return new MyTripQuotedCarListFragment();
    }


    @Subscriber
    public void getDemandGid(String demandGid){
        showToast("demandGid:" + demandGid);
        XgoLog.e("demandGid:" + demandGid);
        MyTripQuotedCarListFragment  quotedCarListFragment = null;
        if (mFragment instanceof  MyTripQuotedCarListFragment) {
            quotedCarListFragment = (MyTripQuotedCarListFragment) mFragment;
            quotedCarListFragment.setmDemandGid(demandGid);
        }
        EventBus.getDefault().removeStickyEvent(demandGid.getClass());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
