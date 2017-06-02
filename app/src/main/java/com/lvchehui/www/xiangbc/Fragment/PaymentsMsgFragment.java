
package com.lvchehui.www.xiangbc.Fragment;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.Request;
import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.base.BaseListFragment;
import com.lvchehui.www.xiangbc.base.BasePageAdapter;
import com.lvchehui.www.xiangbc.bean.PaymentsBean;
import com.lvchehui.www.xiangbc.utils.ConnectionManager;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class PaymentsMsgFragment extends BaseListFragment {

    // 请求数据完进行解析
    @Override
    protected List convertToBeanList(String json) {
        // TODO Auto-generated method stub
        ArrayList<PaymentsBean> arrayList = new ArrayList<>();

        for (int i = 0; i <= 30; i++) {
            PaymentsBean textBean = new PaymentsBean();
            textBean.name = "张灿能";
            textBean.date = "2016-4-25";
            textBean.amount = "25.55";
            textBean.staus = "交易成功";
            arrayList.add(textBean);
        }
        return arrayList;
    }

    @Override
    protected BasePageAdapter initAdapter() {
        // TODO Auto-generated method stub
        return new PaymentsAdapter();
    }

    @Override
    protected boolean isSwipeRefreshLayoutEnabled() {
        // TODO Auto-generated method stub
        return false;
    }

    // 加载页面数
    @Override
    protected int getSizeInPage() {
        // TODO Auto-generated method stub
        return 12;
    }

    // 访问防落请求
    @Override
    protected Request initRequest(int start) {
        // TODO Auto-generated method stub
        return ConnectionManager.getInstance().findSms(getActivity(), "15859254561", this);
    }

    @Override
    protected boolean isPageEnabled() {
        // TODO Auto-generated method stub
        return false;
    }

    // 是否请求道数据
    @Override
    protected boolean isDataGot() {
        // TODO Auto-generated method stub
        return false;
    }

    class PaymentsAdapter extends BasePageAdapter {

        class PaymentsItemViewHolder extends RecyclerView.ViewHolder {

            @ViewInject(R.id.tv_date_time)
            private TextView m_tv_date_time;

            @ViewInject(R.id.et_campany_name)
            private TextView m_tv_name;

            @ViewInject(R.id.tv_money_income)
            private TextView m_tv_money_income;

            public PaymentsItemViewHolder(View itemView) {
                super(itemView);
                x.view().inject(this, itemView);
            }

        }

        @Override
        protected RecyclerView.ViewHolder initViewHolder(ViewGroup viewGroup, int viewType) {
            //final View view = View.inflate(viewGroup.getContext(), R.layout.item_order_list, null);
            View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order_list, viewGroup, false);
            return new PaymentsItemViewHolder(inflate);
        }

        @Override
        public void doBindViewHolder(RecyclerView.ViewHolder viewHoder, int position) {
            if (viewHoder instanceof PaymentsItemViewHolder) {
                PaymentsItemViewHolder relationItemViewHolder = (PaymentsItemViewHolder) viewHoder;
                PaymentsBean bean = (PaymentsBean) mItems.get(position);
                relationItemViewHolder.m_tv_date_time.setText(bean.date);
                relationItemViewHolder.m_tv_name.setText(bean.name);
                relationItemViewHolder.m_tv_money_income.setText(bean.amount);

            }
        }


    }

}
