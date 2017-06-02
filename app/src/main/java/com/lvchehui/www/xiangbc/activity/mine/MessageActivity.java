package com.lvchehui.www.xiangbc.activity.mine;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.activity.DepositDetailActivity;
import com.lvchehui.www.xiangbc.app.App;
import com.lvchehui.www.xiangbc.base.BaseListActivity;
import com.lvchehui.www.xiangbc.base.BasePageAdapter;
import com.lvchehui.www.xiangbc.bean.BaseBean;
import com.lvchehui.www.xiangbc.bean.PushDetailGetListBean;
import com.lvchehui.www.xiangbc.bean.PushDetailGetListDataBean;
import com.lvchehui.www.xiangbc.utils.ConnectionManager;
import com.lvchehui.www.xiangbc.utils.ConnectionUtil;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.SPUtil;

import java.util.List;

/**
 * Created by 张灿能 on 2016/5/27.
 * 作用：
 */
public class MessageActivity extends BaseListActivity {
    private boolean mIsDataGot = false;

    @Override
    protected List convertToBeanList(String json) {
        PushDetailGetListBean pushDetailLists = App.getInstance().getBeanFromJson(json, PushDetailGetListBean.class);
        return pushDetailLists.resData;
    }

    @Override
    protected void initViews() {
        super.initViews();
        setTitleView(mTitleView, "消息中心");
    }

    @Override
    protected BasePageAdapter initAdapter() {
        return new MessageListAdapter();
    }

    @Override
    protected boolean isSwipeRefreshLayoutEnabled() {
        return true;
    }

    @Override
    protected int getSizeInPage() {
        return 0;
    }

    @Override
    protected Request initRequest(int start) {
        return ConnectionManager.getInstance()
                .pushDetailGetList(this, "" + SPUtil.getInstance(this).get(Constants.USER_GID, ""), this);
    }

    @Override
    protected boolean isPageEnabled() {
        return false;
    }

    @Override
    protected boolean isDataGot() {
        return mIsDataGot;
    }

    class MessageListAdapter extends BasePageAdapter {

        class MessageItemViewHolder extends RecyclerView.ViewHolder {
            View root;
            TextView tvMsg;
            TextView tvData;

            public MessageItemViewHolder(View itemView) {
                super(itemView);
                root = itemView.findViewById(R.id.root);
                tvMsg = (TextView) itemView.findViewById(R.id.tv_msg);
                tvData = (android.widget.TextView) itemView.findViewById(R.id.tv_data);

            }
        }

        @Override
        protected RecyclerView.ViewHolder initViewHolder(ViewGroup viewGroup, int viewType) {
//            View inflate = View.inflate(viewGroup.getContext(), R.layout.item_message, false);
            View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_message, viewGroup, false);
            return new MessageItemViewHolder(inflate);
        }


        @Override
        public void doBindViewHolder(RecyclerView.ViewHolder viewHoder, int position) {

            if (viewHoder instanceof MessageItemViewHolder) {
                MessageItemViewHolder msgViewHolder = (MessageItemViewHolder) viewHoder;
                PushDetailGetListDataBean pushDetailItem = (PushDetailGetListDataBean) mItems.get(position);
                if (pushDetailItem.push_status == 1) {
//                    msgViewHolder.tvData.setTextColor(getResources().getColor(R.color.text_default_color));
                    msgViewHolder.tvMsg.setTextColor(getResources().getColor(R.color.text_default_color));
                } else if (pushDetailItem.push_status == 3) {
//                    msgViewHolder.tvData.setTextColor(getResources().getColor(R.color.text_default_hintcolor));
                    msgViewHolder.tvMsg.setTextColor(getResources().getColor(R.color.text_default_hintcolor));
                }
                msgViewHolder.tvData.setText("点击查看");
                msgViewHolder.tvMsg.setText(pushDetailItem.content.summary);
                msgViewHolder.root.setOnClickListener(new MessageItemOnclickListener(pushDetailItem));
            }
        }

        class MessageItemOnclickListener implements View.OnClickListener {
            PushDetailGetListDataBean pushDetailItem;

            public MessageItemOnclickListener(PushDetailGetListDataBean pushDetailItem) {
                this.pushDetailItem = pushDetailItem;
            }

            @Override
            public void onClick(View v) {
                ConnectionManager.getInstance().pushDetailSetRead(MessageActivity.this, (String) SPUtil.getInstance(MessageActivity.this).get(Constants.USER_GID, ""), pushDetailItem.push_detail_gid, new ConnectionUtil.OnDataLoadEndListener() {
                    @Override
                    public void OnLoadEnd(String ret) {
                        BaseBean baseBean = App.getInstance().getBeanFromJson(ret, BaseBean.class);
                        if (baseBean.errCode != -1) {
                            onRefresh();
                            startActivity(new Intent(MessageActivity.this, DepositDetailActivity.class));
                        }
                        showToast(baseBean.resMsg);

                    }
                });

            }
        }

    }


}
