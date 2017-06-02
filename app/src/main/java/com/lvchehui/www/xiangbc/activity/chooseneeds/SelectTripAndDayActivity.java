package com.lvchehui.www.xiangbc.activity.chooseneeds;

import android.content.Intent;
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
 * Created by 张灿能 on 2016/6/27.
 * 作用：立即约车--配驾包车
 */
@ContentView(R.layout.activity_select_trip_and_day)
public class SelectTripAndDayActivity extends BaseActivity {

    @ViewInject(R.id.title_view)
    private TitleView m_title_view;

    @ViewInject(R.id.include_1)
    private View m_include_1;

    @ViewInject(R.id.include_2)
    private View m_include_2;

    @ViewInject(R.id.include_3)
    private View m_include_3;

    @ViewInject(R.id.include_4)
    private View m_include_4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(m_title_view, getString(R.string.book_cars));
        init(m_include_1, getString(R.string.one_way_trip_title), getString(R.string.one_way_trip_temp));
        init(m_include_2, getString(R.string.round_trip_title), getString(R.string.round_trip_temp));
        init(m_include_3, getString(R.string.one_day_trip_title), getString(R.string.one_day_trip_temp));
        init(m_include_4, getString(R.string.mutil_day_trip_title), getString(R.string.mutil_day_trip_temp));
    }

    private void init(View v, String title, String des) {
        TextView m_tv_title = (TextView) v.findViewById(R.id.tv_title);
        TextView m_tv_tv_des = (TextView) v.findViewById(R.id.tv_des);
        m_tv_title.setText(title);
        m_tv_tv_des.setText(des);
    }


    @Event({R.id.include_1, R.id.include_2, R.id.include_3, R.id.include_4})
    private void chooseNeedsOnClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.include_1:
                intent = new Intent(SelectTripAndDayActivity.this, RoundTripActivity.class);
                intent.putExtra(RoundTripActivity.IS_GO_AND_BACK, false);
                finish();
                break;
            case R.id.include_2:
                intent = new Intent(SelectTripAndDayActivity.this, RoundTripActivity.class);
                intent.putExtra(RoundTripActivity.IS_GO_AND_BACK, true);
                finish();
                break;
            case R.id.include_3:
                intent = new Intent(SelectTripAndDayActivity.this, OneDayTripActivity.class);
                finish();
                break;
            case R.id.include_4:
                intent = new Intent(SelectTripAndDayActivity.this, MultiDayTripActivity.class);
                finish();
                break;
        }

        startActivity(intent);
    }

    /*private String getString(int resId){
        return getResources().getString(resId);
    }*/
}
