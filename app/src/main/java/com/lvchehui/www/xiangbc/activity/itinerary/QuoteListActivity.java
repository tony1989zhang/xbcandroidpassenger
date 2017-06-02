package com.lvchehui.www.xiangbc.activity.itinerary;

import com.android.volley.Request;
import com.lvchehui.www.xiangbc.base.BaseListActivity;
import com.lvchehui.www.xiangbc.base.BasePageAdapter;
import com.lvchehui.www.xiangbc.bean.BaseBean;

import java.util.List;

/**
 * Created by 张灿能 on 2016/6/3.
 * 作用：
 */
public class QuoteListActivity extends BaseListActivity {
    @Override
    protected List<BaseBean> convertToBeanList(String json) {
        return null;
    }

    @Override
    protected BasePageAdapter initAdapter() {
        return null;
    }

    @Override
    protected boolean isSwipeRefreshLayoutEnabled() {
        return false;
    }

    @Override
    protected int getSizeInPage() {
        return 0;
    }

    @Override
    protected Request initRequest(int start) {
        return null;
    }

    @Override
    protected boolean isPageEnabled() {
        return false;
    }

    @Override
    protected boolean isDataGot() {
        return false;
    }
}
