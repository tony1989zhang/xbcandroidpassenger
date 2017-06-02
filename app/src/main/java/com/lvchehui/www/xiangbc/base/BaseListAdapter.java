package com.lvchehui.www.xiangbc.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by 张灿能 on 2016/6/30.
 * 作用：ListView 基类
 */
public abstract class BaseListAdapter extends BaseAdapter {

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
