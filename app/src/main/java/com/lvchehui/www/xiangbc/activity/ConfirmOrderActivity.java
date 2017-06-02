package com.lvchehui.www.xiangbc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.activity.chooseneeds.FinishOrderActivity;
import com.lvchehui.www.xiangbc.app.App;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.base.BaseReqActivity;
import com.lvchehui.www.xiangbc.bean.BaseBean;
import com.lvchehui.www.xiangbc.bean.DemandIssueBean;
import com.lvchehui.www.xiangbc.bean.DemandSubmitDataBean;
import com.lvchehui.www.xiangbc.utils.ConnectionManager;
import com.lvchehui.www.xiangbc.utils.ConnectionUtil;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.SPUtil;
import com.lvchehui.www.xiangbc.utils.StringUtils;
import com.lvchehui.www.xiangbc.utils.XgoLog;
import com.lvchehui.www.xiangbc.view.TitleView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by 张灿能 on 2016/6/22.
 * 作用：确认订单
 */
@ContentView(R.layout.activity_confirm_order)
public class ConfirmOrderActivity extends BaseActivity implements ConnectionUtil.OnDataLoadEndListener{

    public static final String DEMAND_SUBMITBEAN_TYPE = "DEMAND_SUBMITBEAN_TYPE";
    public static final String CONFIRM_ORDER_STRING_TRAVEL = "confirm_order_string_travel_info";
    public static final String CONFIRM_ORDER_STRING_PASSENGER = "confirm_order_string_passenger_info";

    @ViewInject(R.id.title_view)
    private TitleView m_title_view;

    @ViewInject(R.id.ll_order_detail_trip_info)
    private LinearLayout m_ll_order_detail_trip_info;

    @ViewInject(R.id.ll_order_detail_passenger_info)
    private LinearLayout m_ll_order_detail_passenger_info;


    @ViewInject(R.id.tv_explanation)
    private TextView m_tv_explanation; //点击付款既已阅读并接受《巴士用户条款》与《用户指南》;

    @ViewInject(R.id.tv_ok)
    private TextView m_tv_ok;
    private  DemandSubmitDataBean demandSubmitDataBean;
    String travelInfoStr_type1 = "";
    String traveLInfoStr_type2 ="";
    String travelInfoStr_type3 = "";
    String travelInfoStr_type4 = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(m_title_view, "订单详情");
        Intent intent = getIntent();
        String source = intent.getStringExtra(Constants.SOURCE);
        if (!StringUtils.isEmpty(source)&&source.equals(BaseReqActivity.class.getSimpleName())){
            demandSubmitDataBean = (DemandSubmitDataBean) intent.getSerializableExtra(DEMAND_SUBMITBEAN_TYPE);
            XgoLog.e("demandSubmitDataBean:" + demandSubmitDataBean);
        }
//        String travelStr = intent.getStringExtra(CONFIRM_ORDER_STRING_TRAVEL);
//        String passengerStr = intent.getStringExtra(CONFIRM_ORDER_STRING_PASSENGER);
//        m_tv_travel_info.setText("" + travelStr);
//        m_tv_passenger_info.setText("" + passengerStr);
        m_tv_explanation.setVisibility(View.GONE);
        m_tv_ok.setText("发布需求");
    }

    @Event(value = {R.id.tv_ok},type = View.OnClickListener.class)
    private void confirmOrderOnClick(View v){
        showProgressDialog();
        ConnectionManager.getInstance().demandIssue(this,(String) SPUtil.getInstance(this).get(Constants.USER_GID,""),demandSubmitDataBean.demand_gid,this);
    }

    @Override
    public void OnLoadEnd(String ret) {

        dismissProgressDialog();
        DemandIssueBean demandIssueBean = App.getInstance().getBeanFromJson(ret, DemandIssueBean.class);
        if (demandIssueBean.errCode != -1){

            ConnectionManager.getInstance().demandDealPush(this, demandSubmitDataBean.demand_gid, new ConnectionUtil.OnDataLoadEndListener() {
                @Override
                public void OnLoadEnd(String ret) {
                    BaseBean baseBean = App.getInstance().getBeanFromJson(ret, BaseBean.class);
                    showToast(baseBean.resMsg);
                }
            });

            Intent intent = new Intent(this, FinishOrderActivity.class);
            intent.putExtra(Constants.SOURCE,ConfirmOrderActivity.class.getSimpleName());
            startActivity(intent);
            App.getInstance().aliveActivitys.get(App.getInstance().aliveActivitys.size()-2).get().finish();//关闭本页面下面的Activity
            finish();
        }

        showToast(demandIssueBean.resMsg);

    }
}
