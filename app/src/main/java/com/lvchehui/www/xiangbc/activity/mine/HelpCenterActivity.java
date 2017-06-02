package com.lvchehui.www.xiangbc.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.view.TitleView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by tendoasan on 2016/9/3.
 * 作用帮助中心
 */
@ContentView(R.layout.activity_help_center)
public class HelpCenterActivity extends BaseActivity {
    @ViewInject(R.id.title_view)
    private TitleView m_title_view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(m_title_view, getString(R.string.help_center));
    }

    @Event(value = {R.id.ll_ride_guide, R.id.ll_faq, R.id.ll_over_journey_cost_ref,
            R.id.ll_leaguer_agreement, R.id.ll_privacy_provision, R.id.ll_accident_insurance_plan},
            type = View.OnClickListener.class)
    private void setLinearLayoutOnClick(View v) {
        Class activity = null;
        final Intent intent = new Intent();
        switch (v.getId()){
            case R.id.ll_ride_guide:
                activity = WebActivity.class;
                intent.putExtra(WebActivity.WEB_EXT_TITLE, getString(R.string.ride_guide));
                intent.putExtra(WebActivity.WEB_EXT_URL, getString(R.string.ride_guide_url));
                break;
            case R.id.ll_faq:
                activity = WebActivity.class;
                intent.putExtra(WebActivity.WEB_EXT_TITLE, getString(R.string.faq));
                intent.putExtra(WebActivity.WEB_EXT_URL, getString(R.string.faq_url));
                break;
            case R.id.ll_over_journey_cost_ref:
                activity = WebActivity.class;
                intent.putExtra(WebActivity.WEB_EXT_TITLE, getString(R.string.over_journey_cost_ref));
                intent.putExtra(WebActivity.WEB_EXT_URL, getString(R.string.over_journey_cost_ref_url));
                break;
            case R.id.ll_leaguer_agreement:
                activity = WebActivity.class;
                intent.putExtra(WebActivity.WEB_EXT_TITLE, getString(R.string.leaguer_agreement));
                intent.putExtra(WebActivity.WEB_EXT_URL, getString(R.string.leaguer_agreement_url));
                break;
            case R.id.ll_privacy_provision:
                activity = WebActivity.class;
                intent.putExtra(WebActivity.WEB_EXT_TITLE, getString(R.string.privacy_provision));
                intent.putExtra(WebActivity.WEB_EXT_URL, getString(R.string.privacy_provision_url));
                break;
            case R.id.ll_accident_insurance_plan:
                activity = WebActivity.class;
                intent.putExtra(WebActivity.WEB_EXT_TITLE, getString(R.string.accident_insurance_plan));
                intent.putExtra(WebActivity.WEB_EXT_URL, getString(R.string.accident_insurance_plan_url));
                break;
        }

        intent.setClass(this, activity);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
