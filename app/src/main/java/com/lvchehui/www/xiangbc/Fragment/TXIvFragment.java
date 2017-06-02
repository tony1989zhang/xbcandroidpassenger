package com.lvchehui.www.xiangbc.Fragment;

import android.view.View;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.base.BaseFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by 张灿能 on 2016/5/23.
 * 作用：申请提现
 */
@ContentView(R.layout.layout_tx_iv)
public class TXIvFragment extends BaseFragment {
    @ViewInject(R.id.tv_total_money)
    private TextView m_tv_total_money; //35;

    @ViewInject(R.id.tv_withdrawal)
    private TextView m_tv_withdrawal; //提 现 ;

    @Event(value = R.id.tv_withdrawal)
    private void txOnClick(View v){
        showToast("提现");
    }
}
