package com.lvchehui.www.xiangbc.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.FrameLayout;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.view.TitleView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by 张灿能 on 2016/6/15.
 * 作用：只加载一个Fragment的Activity
 */
@ContentView(R.layout.activity_for_fragment_normal)
public class ActivityForFragmentNormal extends BaseFragmentActivity{
    @ViewInject(R.id.title_view)
    public TitleView m_title_view;
    public Fragment mFragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews(){
        mFragment = initFragment();
        if (null != this.getIntent() && null != this.getIntent().getExtras()){
            mFragment.setArguments(this.getIntent().getExtras());
        }
        this.getSupportFragmentManager().beginTransaction().add(R.id.main_container,mFragment).commit();
    }

    public Fragment initFragment(){
        return null;
    }


}
