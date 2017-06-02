package com.lvchehui.www.xiangbc.activity.mine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.Fragment.MemberIvFragment;
import com.lvchehui.www.xiangbc.Fragment.MemberMsgFragment;
import com.lvchehui.www.xiangbc.Fragment.PaymentsIvFragmet;
import com.lvchehui.www.xiangbc.Fragment.PaymentsMsgFragment;
import com.lvchehui.www.xiangbc.Fragment.TXIvFragment;
import com.lvchehui.www.xiangbc.Fragment.TXMsgFragment;
import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.base.BaseFragmentActivity;
import com.lvchehui.www.xiangbc.view.TitleView;
import com.lvchehui.www.xiangbc.view.dragtop.BanSlidingViewPage;
import com.lvchehui.www.xiangbc.view.dragtop.DragTopLayout;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 作用：代理商界面
 */
@ContentView(R.layout.activity_agent)
public class AgentActivity extends BaseFragmentActivity {

    @ViewInject(R.id.title_view)
    private TitleView titleView;

    @ViewInject(R.id.drag_layout)
    private DragTopLayout dragLayout;

    @ViewInject(R.id.banSlidingViewPage_iv)
    private BanSlidingViewPage ivViewPager;

    @ViewInject(R.id.banSlidingViewPage_msg)
    private BanSlidingViewPage msgViewPager;


    @ViewInject(R.id.tv_member)
    private TextView memberTv;

    @ViewInject(R.id.tv_payments)
    private TextView paymentsTV;

    @ViewInject(R.id.tv_tx)
    private TextView txTv;

    private ArrayList<Fragment> mFragmentsIV = new ArrayList<>();
    private ArrayList<Fragment> mFragmentsMsg = new ArrayList<>();


    int currentIndex = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(titleView, getString(R.string.agent));
        showProgressDailog();
        initViewPager();
     //   dragLayout.setOverDrag(true);//dragLayout.isOverDrag()
    }

    private void initViewPager() {
        mFragmentsIV.add(new PaymentsIvFragmet());
        mFragmentsIV.add(new MemberIvFragment());
        mFragmentsIV.add(new TXIvFragment());

        mFragmentsMsg.add(new PaymentsMsgFragment());
        mFragmentsMsg.add(new MemberMsgFragment());
        mFragmentsMsg.add(new TXMsgFragment());

        IVFragmentAdapter ivAdapter = new IVFragmentAdapter(getSupportFragmentManager());
        ivViewPager.setAdapter(ivAdapter);
        ivViewPager.setOffscreenPageLimit(0);

        MSGFragmentAdapter msgAdapter = new MSGFragmentAdapter(getSupportFragmentManager());
        msgViewPager.setAdapter(msgAdapter);
        msgViewPager.setOffscreenPageLimit(0);

        ivViewPager.setOnPageChangeListener(new AgentOnPageChangeListener());
        msgViewPager.setOnPageChangeListener(new AgentOnPageChangeListener());

        msgViewPager.setCurrentItem(0);
        ivViewPager.setCurrentItem(0);
        ivViewPager.setScanScroll(true);
        msgViewPager.setScanScroll(true);
        paymentsTV.setTextColor(getResources().getColor(R.color.swipe_refrsh_color3));
        dragLayout.setOverDrag(false);
        x.task().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismissProgressDialog();
            }
        },1000);

    }


    @Event(value = {R.id.title_right_tv, R.id.tv_member, R.id.tv_payments, R.id.tv_tx}, type = View.OnClickListener.class)
    private void AgentOnClick(View v) {
        switch (v.getId()) {
            case R.id.tv_payments:
                currentIndex = 0;
                break;
            case R.id.tv_member:
                currentIndex = 1;

                break;

            case R.id.tv_tx:
                currentIndex = 2;
                break;

            case R.id.title_right_tv:
                break;

            default:
                break;
        }
        ivViewPager.setCurrentItem(currentIndex);
        msgViewPager.setCurrentItem(currentIndex);

    }

    class IVFragmentAdapter extends FragmentPagerAdapter {

        public IVFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentsIV.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentsIV.size();
        }
    }

    class MSGFragmentAdapter extends FragmentPagerAdapter {

        public MSGFragmentAdapter(FragmentManager fm) {
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

    class AgentOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int position) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            ivViewPager.setCurrentItem(position);
            msgViewPager.setCurrentItem(position);
            switch (position) {
                case 0:
                    paymentsTV.setTextColor(getResources().getColor(R.color.swipe_refrsh_color3));
                    memberTv.setTextColor(getResources().getColor(R.color.text_default_color));
                    txTv.setTextColor(getResources().getColor(R.color.text_default_color));

                    paymentsTV.getPaint().setFakeBoldText(true);
                    memberTv.getPaint().setFakeBoldText(false);
                    txTv.getPaint().setFakeBoldText(false);
                    break;
                case 1:
                    memberTv.setTextColor(getResources().getColor(R.color.swipe_refrsh_color3));
                    paymentsTV.setTextColor(getResources().getColor(R.color.text_default_color));
                    txTv.setTextColor(getResources().getColor(R.color.text_default_color));

                    memberTv.getPaint().setFakeBoldText(true);
                    paymentsTV.getPaint().setFakeBoldText(false);
                    txTv.getPaint().setFakeBoldText(false);
                    break;
                case 2:
                    paymentsTV.setTextColor(getResources().getColor(R.color.text_default_color));
                    memberTv.setTextColor(getResources().getColor(R.color.text_default_color));
                    txTv.setTextColor(getResources().getColor(R.color.swipe_refrsh_color3));

                    memberTv.getPaint().setFakeBoldText(false);
                    paymentsTV.getPaint().setFakeBoldText(false);
                    txTv.getPaint().setFakeBoldText(true);
                    break;

                default:
                    break;
            }

            currentIndex = position;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
