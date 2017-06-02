package com.lvchehui.www.xiangbc.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.Fragment.ContactFragment;
import com.lvchehui.www.xiangbc.Fragment.InvoiceFragment;
import com.lvchehui.www.xiangbc.R;
        import com.lvchehui.www.xiangbc.activity.AddInvoiceActivity;
import com.lvchehui.www.xiangbc.base.BaseFragmentActivity;
import com.lvchehui.www.xiangbc.utils.XgoLog;
import com.lvchehui.www.xiangbc.view.TitleView;
import com.lvchehui.www.xiangbc.view.dragtop.BanSlidingViewPage;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 作用：常用信息
 */
@ContentView(R.layout.activity_common_info)
public class CommonInfoActivity extends BaseFragmentActivity {

    int currentIndex;
    private static final int CONSTACT_INDEX = 0;
    private static final int INVOICE_INDEX = 1;
    @ViewInject(R.id.banSlidingView)
    BanSlidingViewPage banSlidingView;
    @ViewInject(R.id.title_view)
    TitleView titleView;
    @ViewInject(R.id.tvcontact)
    TextView tvcontact;
    @ViewInject(R.id.tvInvoice)
    TextView tvInvoice;

    ArrayList<Fragment> fragments = new ArrayList<>();
    private ManageMentAdapter mentAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(titleView, getString(R.string.general_info));
        fragments.add(new ContactFragment());// MemberMsgFragment
        fragments.add(new InvoiceFragment());

        mentAdapter = new ManageMentAdapter(getSupportFragmentManager());
        banSlidingView.setOffscreenPageLimit(0);
        banSlidingView.setAdapter(mentAdapter);
        banSlidingView.setOnPageChangeListener(new ManageMentOnPageChangeListener());

        banSlidingView.setCurrentItem(0);
        banSlidingView.setScanScroll(true);
        tvcontact.setTextColor(getResources().getColor(R.color.swipe_refrsh_color3));
        tvInvoice.setTextColor(getResources().getColor(R.color.black));
    }

    @Event(value = { R.id.tv_ok, R.id.tvcontact, R.id.tvInvoice

    }, type = View.OnClickListener.class)
    private void manageMentOnClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ok:
                showToast("添加" + currentIndex);
                if (currentIndex == 0) {
                    startActivityForResult(new Intent(this, AddContactActivity.class), 0);
                } else if (currentIndex == 1) {
                    startActivityForResult(new Intent(this, AddInvoiceActivity.class), 0);
                }
                break;
            case R.id.tvcontact:
                tvcontact.setTextColor(getResources().getColor(R.color.swipe_refrsh_color3));
                tvcontact.setShadowLayer(
                        tvcontact.getShadowRadius(),
                        tvcontact.getShadowDx(),
                        tvcontact.getShadowDy(),
                    R.color.btn_text_shadow_color);
                tvInvoice.setTextColor(getResources().getColor(R.color.black));
                tvInvoice.setShadowLayer(
                        tvInvoice.getShadowRadius(),
                        tvInvoice.getShadowDx(),
                        tvInvoice.getShadowDy(),
                        R.color.tab_text_shadow_color);
                currentIndex = CONSTACT_INDEX;
                banSlidingView.setCurrentItem(currentIndex);
                break;
            case R.id.tvInvoice:
                tvInvoice.setTextColor(getResources().getColor(R.color.swipe_refrsh_color3));
                tvInvoice.setShadowLayer(
                        tvInvoice.getShadowRadius(),
                        tvInvoice.getShadowDx(),
                        tvInvoice.getShadowDy(),
                        R.color.btn_text_shadow_color);
                tvcontact.setTextColor(getResources().getColor(R.color.black));
                tvcontact.setShadowLayer(
                        tvcontact.getShadowRadius(),
                        tvcontact.getShadowDx(),
                        tvcontact.getShadowDy(),
                        R.color.tab_text_shadow_color);
                currentIndex = INVOICE_INDEX;
                banSlidingView.setCurrentItem(currentIndex);
                break;
        }

    }

    class ManageMentAdapter extends FragmentPagerAdapter {

        public ManageMentAdapter(FragmentManager fm) {
            super(fm);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Fragment getItem(int position) {
            // TODO Auto-generated method stub
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return fragments.size();
        }

    }

    class ManageMentOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int position) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int position) {
            banSlidingView.setCurrentItem(position);
            switch (position) {
                case CONSTACT_INDEX:
                    tvcontact.setTextColor(getResources().getColor(R.color.swipe_refrsh_color3));
                    tvInvoice.setTextColor(getResources().getColor(R.color.black));

                    break;
                case INVOICE_INDEX:
                    tvInvoice.setTextColor(getResources().getColor(R.color.swipe_refrsh_color3));
                    tvcontact.setTextColor(getResources().getColor(R.color.black));

                    break;

                default:
                    break;
            }

            currentIndex = position;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        XgoLog.e("onActivity_resutl" + resultCode);
        if (resultCode == RESULT_OK){
            if (currentIndex == 0) {
                ContactFragment fragment = (ContactFragment) fragments.get(currentIndex);
                fragment.onRefresh();
            }else{
                InvoiceFragment fragment = (InvoiceFragment) fragments.get(currentIndex);
                fragment.onRefresh();
            }

        }
    }
}
