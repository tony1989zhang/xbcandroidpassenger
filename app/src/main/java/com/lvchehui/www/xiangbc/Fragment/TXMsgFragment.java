package com.lvchehui.www.xiangbc.Fragment;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.activity.SetPayPassWordActivity;
import com.lvchehui.www.xiangbc.base.BaseListFragment;
import com.lvchehui.www.xiangbc.base.BasePageAdapter;
import com.lvchehui.www.xiangbc.bean.PaymentsBean;
import com.lvchehui.www.xiangbc.utils.ConnectionManager;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张灿能 on 2016/5/23.
 * 作用：设置提现的账号
 */
public class TXMsgFragment extends BaseListFragment {
    private View m_layout_no_card;

    @Override
    protected void initViews(View root) {
        super.initViews(root);
        m_layout_no_card = root.findViewById(R.id.layout_no_card);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_tx_msg_list;
    }

    @Override
    protected List convertToBeanList(String json) {
        ArrayList<PaymentsBean> arrayList = new ArrayList<>();
//        for (int i = 0;i <= 30;i++){
//            PaymentsBean paymentsBean = new PaymentsBean();
//            paymentsBean.name = "张上哪";
//            arrayList.add(paymentsBean);
//        }
        return arrayList;
    }

    @Override
    protected BasePageAdapter initAdapter() {
        return new TxAdapter();
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
        return  ConnectionManager.getInstance().findSms(getActivity(), "15859254561", this);
    }

    @Override
    public void showEmpty() {
        super.showEmpty();
        mEmptyTips.setVisibility(View.GONE);
        m_layout_no_card.setVisibility(View.VISIBLE);
        m_layout_no_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("跳转支付页面");
                startActivity(new Intent(getActivity(), SetPayPassWordActivity.class));
            }
        });
    }

    @Override
    public void hideEmpty() {
        super.hideEmpty();
        m_layout_no_card.setVisibility(View.GONE);
    }

    @Override
    protected boolean isPageEnabled() {
        return false;
    }

    @Override
    protected boolean isDataGot() {
        return false;
    }

    class TxAdapter extends BasePageAdapter implements View.OnClickListener{

        class TxItemViewHolder extends RecyclerView.ViewHolder{

            @ViewInject(R.id.et_campany_name)
            private TextView m_tv_name;

            @ViewInject(R.id.tv_other)
            private TextView m_tv_other;
            public TxItemViewHolder(View itemView) {
                super(itemView);
                x.view().inject(this,itemView);
            }
        }

        class HeadItemViewHolder extends RecyclerView.ViewHolder{
            @ViewInject(R.id.root)
            private View m_root;
            @ViewInject(R.id.iv_pay_logo)
            private ImageView m_iv_pay_logo;
            @ViewInject(R.id.et_campany_name)
            private TextView m_tv_name;

            public HeadItemViewHolder(View itemView){
                super(itemView);
                x.view().inject(this,itemView);
            }
        }
        @Override
        protected RecyclerView.ViewHolder initViewHolder(ViewGroup viewGroup, int viewType) {
            View inflate = null;
            if (viewType == 0){
                inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tx_head,viewGroup,false);
                return new HeadItemViewHolder(inflate);
            }
                inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tx, viewGroup, false);

            return new TxItemViewHolder(inflate);
        }

        @Override
        public void doBindViewHolder(RecyclerView.ViewHolder viewHoder, int position) {

            if (getItemViewType(position) != 0&&viewHoder instanceof TxItemViewHolder){
                TxItemViewHolder txItemViewHolder = (TxItemViewHolder) viewHoder;
                PaymentsBean bean = (PaymentsBean) mItems.get(position);
                txItemViewHolder.m_tv_name.setText(bean.name);
            }else if(getItemViewType(position) == 0 &&viewHoder instanceof HeadItemViewHolder){
                HeadItemViewHolder headItemViewHolder = (HeadItemViewHolder)viewHoder;
                headItemViewHolder.m_root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showToast("跳转账号更改");
                    }
                });
            }
        }


        @Override
        public int getItemViewType(int position) {
            if (position == 0){
                return 0;
            }
            return 1;
        }

        @Override
        public void onClick(View v) {

        }
    }
}
