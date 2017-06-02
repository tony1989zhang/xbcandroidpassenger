package com.lvchehui.www.xiangbc.activity.itinerary;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.Fragment.FinishListFragment;
import com.lvchehui.www.xiangbc.Fragment.QuoteListFragment;
import com.lvchehui.www.xiangbc.Fragment.ScheduledListFragment;
import com.lvchehui.www.xiangbc.Fragment.SetOffListFragment;
import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.base.BaseFragmentActivity;
import com.lvchehui.www.xiangbc.view.TitleView;
import com.lvchehui.www.xiangbc.view.dragtop.BanSlidingViewPage;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * Created by 张灿能 on 2016/6/2.
 * 作用：我的行程
 */

@ContentView(R.layout.activity_itinerary)
public class ItineraryActivity extends BaseFragmentActivity {

    @ViewInject(R.id.title_view)
    private TitleView m_title_view;
    @ViewInject(R.id.l1)
    private LinearLayout m_l1;
    @ViewInject(R.id.tv_quote)
    private TextView m_tv_quote; //报价中;
    @ViewInject(R.id.tv_scheduled)
    private TextView m_tv_scheduled;//已预定;
    @ViewInject(R.id.tv_setoff)
    private TextView m_tv_setoff; //已出发;
    @ViewInject(R.id.tv_finish)
    private TextView m_tv_finish; //已完成;

    @ViewInject(R.id.banSlidingView)
    private BanSlidingViewPage m_banSlidingView;
    private ArrayList<Fragment> mFragmentsMsg = new ArrayList<>();
    int currentIndex = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitleView(m_title_view, "我的行程");
//        mFragmentsMsg.add(new QuoteListFragment());//QuoteListFragment
//        mFragmentsMsg.add(new ScheduledListFragment());
//        mFragmentsMsg.add(new QuoteListFragment());
        mFragmentsMsg.add(new QuoteListFragment());
        mFragmentsMsg.add(new ScheduledListFragment());
        mFragmentsMsg.add(new SetOffListFragment());
        mFragmentsMsg.add(new FinishListFragment());
//        mFragmentsMsg.add(new FinishListFragment());
        ItineraryFragmentAdapter itineraryFragmentAdapter = new ItineraryFragmentAdapter(getSupportFragmentManager());
        m_banSlidingView.setAdapter(itineraryFragmentAdapter);
        m_banSlidingView.setOnPageChangeListener(new ItineraryOnPageChangeListener());
        m_banSlidingView.setOffscreenPageLimit(0);
        m_banSlidingView.setCurrentItem(currentIndex);
        m_banSlidingView.setScanScroll(true);
        m_tv_quote.setTextColor(ContextCompat.getColor(this,R.color.swipe_refrsh_color3));
    }

    class ItineraryFragmentAdapter extends FragmentPagerAdapter{

        public ItineraryFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentsMsg.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentsMsg.size();
        }
    }
    class ItineraryOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            m_banSlidingView.setCurrentItem(position);
            switch (position){
                case 0:
                    m_tv_quote.setTextColor(ContextCompat.getColor(ItineraryActivity.this, R.color.swipe_refrsh_color3));
                    m_tv_scheduled.setTextColor(ContextCompat.getColor(ItineraryActivity.this, R.color.black));
                    m_tv_setoff.setTextColor(ContextCompat.getColor(ItineraryActivity.this,R.color.black));
                    m_tv_finish.setTextColor(ContextCompat.getColor(ItineraryActivity.this,R.color.black));
                    break;
                case 1:
                    m_tv_quote.setTextColor(ContextCompat.getColor(ItineraryActivity.this,R.color.black));
                    m_tv_scheduled.setTextColor(ContextCompat.getColor(ItineraryActivity.this, R.color.swipe_refrsh_color3));
                    m_tv_setoff.setTextColor(ContextCompat.getColor(ItineraryActivity.this,R.color.black));
                    m_tv_finish.setTextColor(ContextCompat.getColor(ItineraryActivity.this,R.color.black));
                    break;
                case 2:
                    m_tv_quote.setTextColor(ContextCompat.getColor(ItineraryActivity.this, R.color.black));
                    m_tv_scheduled.setTextColor(ContextCompat.getColor(ItineraryActivity.this,R.color.black));
                    m_tv_setoff.setTextColor(ContextCompat.getColor(ItineraryActivity.this, R.color.swipe_refrsh_color3));
                    m_tv_finish.setTextColor(ContextCompat.getColor(ItineraryActivity.this,R.color.black));
                    break;
                case 3:
                    m_tv_quote.setTextColor(ContextCompat.getColor(ItineraryActivity.this,R.color.black));
                    m_tv_scheduled.setTextColor(ContextCompat.getColor(ItineraryActivity.this, R.color.black));
                    m_tv_setoff.setTextColor(ContextCompat.getColor(ItineraryActivity.this,R.color.black));
                    m_tv_finish.setTextColor(ContextCompat.getColor(ItineraryActivity.this, R.color.swipe_refrsh_color3));
                    break;

            }
            currentIndex = position;
        }
        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    @Event({R.id.tv_quote,R.id.tv_scheduled,R.id.tv_setoff,R.id.tv_finish})
    private void ItineraryOnClick(View v){

        switch (v.getId()){
            case R.id.tv_quote:
                currentIndex = 0;
                break;
            case R.id.tv_scheduled:
                currentIndex = 1;
                break;
            case R.id.tv_setoff:
                currentIndex = 2;
                break;
            case R.id.tv_finish:
                currentIndex = 3;
                break;
        }
        m_banSlidingView.setCurrentItem(currentIndex);
    }

}
