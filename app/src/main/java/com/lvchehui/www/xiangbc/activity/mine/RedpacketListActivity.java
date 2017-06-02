package com.lvchehui.www.xiangbc.activity.mine;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.base.BaseListActivity;
import com.lvchehui.www.xiangbc.base.BasePageAdapter;
import com.lvchehui.www.xiangbc.bean.RedPacketListBean;
import com.lvchehui.www.xiangbc.bean.RedPacketListDataBean;
import com.lvchehui.www.xiangbc.utils.ConnectionManager;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.SPUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张灿能 on 2016/6/6.
 * 作用：我的红包列表
 */
@ContentView(R.layout.act_redpacketlist)
public class RedpacketListActivity extends BaseListActivity{
    @Override
    protected void initViews() {
        super.initViews();
        setTitleView(mTitleView,"我的红包");
    }

    @Override
    protected List convertToBeanList(String json) {
//        RedPacketListBean beanFromJson = App.getInstance().getBeanFromJson(json, RedPacketListBean.class);
        RedPacketListBean beanFromJson = new RedPacketListBean();
        showToast("" + beanFromJson.resMsg);
        textData(beanFromJson);
        return beanFromJson.resData;
    }

    private void textData( RedPacketListBean beanFromJson ){
        beanFromJson.resData = new ArrayList<>();
        for (int i = 0;i < 44;i++) {
            RedPacketListDataBean redPacketListDataBean = new RedPacketListDataBean();
            redPacketListDataBean.title = "标题" + i;
            redPacketListDataBean.description = "描述" + i;
            redPacketListDataBean.begin_time = "2016-06-06";
            beanFromJson.resData.add(redPacketListDataBean);
        }
    }
    @Event(R.id.tv_ok)
    private void submitOK(View v){
        startActivity(new Intent(this, AgentActivity.class));
    }
    @Override
    protected BasePageAdapter initAdapter() {
        return new RedpacketListAdapter();
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
        return ConnectionManager.getInstance().redPacketList(this, "" + SPUtil.getInstance(this).get(Constants.USER_GID,"R1OoDF7XODuGR2H81iRe9Q99j712QVx1"),"" + 3,this);
    }

    @Override
    protected boolean isPageEnabled() {
        return false;
    }

    @Override
    protected boolean isDataGot() {
        return false;
    }
    class RedpacketListAdapter extends BasePageAdapter{
        @ViewInject(R.id.tv_red_total)
        private TextView m_tv_red_total; //50;

        @ViewInject(R.id.tv_red_title)
        private TextView m_tv_red_title; //标题;

        @ViewInject(R.id.tv_red_description)
        private TextView m_tv_red_description; //描述;

        class RedpacketItemViewHolder extends RecyclerView.ViewHolder {

            public RedpacketItemViewHolder(View itemView) {
                super(itemView);
                x.view().inject(itemView);
            }
        }
        @Override
        protected RecyclerView.ViewHolder initViewHolder(ViewGroup viewGroup, int viewType) {
            final View view = View.inflate(viewGroup.getContext(), R.layout.item_red_packe,null);
            return new RedpacketItemViewHolder(view);
        }

        @Override
        public void doBindViewHolder(RecyclerView.ViewHolder viewHoder, int position) {
            if (viewHoder instanceof RedpacketItemViewHolder){
                RedpacketItemViewHolder holder = (RedpacketItemViewHolder) viewHoder;
            }

        }
    }
}
