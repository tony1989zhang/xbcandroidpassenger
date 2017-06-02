package com.lvchehui.www.xiangbc.activity.pay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.activity.home.HomeActivity;
import com.lvchehui.www.xiangbc.alipay.AliPayHandler;
import com.lvchehui.www.xiangbc.alipay.AliPayUtils;
import com.lvchehui.www.xiangbc.alipay.OnlinePayCreateBean;
import com.lvchehui.www.xiangbc.app.App;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.bean.BaseBean;
import com.lvchehui.www.xiangbc.bean.DemandSubmitDataBean;
import com.lvchehui.www.xiangbc.bean.OnlinePaySubmitBean;
import com.lvchehui.www.xiangbc.bean.OrderInfo;
import com.lvchehui.www.xiangbc.bean.OrderSubmitBean;
import com.lvchehui.www.xiangbc.bean.PreparePaySubmitBean;
import com.lvchehui.www.xiangbc.bean.QuotationGetInfo;
import com.lvchehui.www.xiangbc.utils.ConnectionManager;
import com.lvchehui.www.xiangbc.utils.ConnectionUtil;
import com.lvchehui.www.xiangbc.utils.XgoLog;
import com.lvchehui.www.xiangbc.view.TitleView;
import com.lvchehui.www.xiangbc.view.mutilRadioGroup.MyRadioGroup;
import com.lvchehui.www.xiangbc.wxapi.WxPayUtils;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;

/**
 * Created by tendoasan on 2016/9/7.
 * 作用：发布需求--打赏司机
 */

@ContentView(R.layout.activity_pay_for_trip)
public class PayForTripActivity extends BaseActivity {

    @ViewInject(R.id.title_view)
    private TitleView m_title_view;

    @ViewInject(R.id.tv_select_redpackage)
    private TextView m_tv_select_redpackage; // 暂无红包可用
    /*@ViewInject(R.id.radiogroup_feed_rmb)
    private MyRadioGroup m_radiogroup_feed_rmb; // 打赏司机

    @ViewInject(R.id.radio_feed_5_yuan)
    private RadioButton m_radio_feed_5_yuan;

    @ViewInject(R.id.radio_feed_10_yuan)
    private RadioButton m_radio_feed_10_yuan;

    @ViewInject(R.id.radio_feed_20_yuan)
    private RadioButton m_radio_feed_20_yuan;

    @ViewInject(R.id.radio_feed_other)
    private RadioButton m_radio_feed_other;*/

    @ViewInject(R.id.tv_sum_include_tax)
    private TextView m_tv_sum_include_tax; // 含税总额
    @ViewInject(R.id.tv_already_pay_off)
    private TextView m_tv_already_pay_off; // 已付
    @ViewInject(R.id.tv_deposit)
    private TextView m_tv_deposit; // 订金
    @ViewInject(R.id.tv_insurance)
    private TextView m_tv_insurance; // 保险
    @ViewInject(R.id.include_pay_ok)
    private LinearLayout m_include_pay_ok; // 支付宝付款2410元
    @ViewInject(R.id.tv_remain_pay_off)
    private TextView m_tv_remain_pay_off;

    @ViewInject(R.id.tv_ok)
    private TextView m_tv_ok;

    @ViewInject(R.id.rg_payment)
    private MyRadioGroup m_rg_payment;

    @ViewInject(R.id.rdoBtn_Alipay)
    private RadioButton m_rdoBtn_Alipay;

    @ViewInject(R.id.rdoBtn_wx)
    private RadioButton m_rdoBtn_wx;

    @ViewInject(R.id.tv_accept_license)
    private TextView m_tv_accept_license; //@string/accept_license;
    private int online_pay_category = 1;
    private QuotationGetInfo.ResDataBean quotationInfo;
    private DemandSubmitDataBean mDemandInfo;
    private OrderInfo.ResDataBean mOrderInfo;
    private double tobePaid;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(m_title_view, "单程用车");
        EventBus.getDefault().registerSticky(this);
    }


    @Event(value = R.id.rg_payment,type = MyRadioGroup.OnCheckedChangeListener.class)
    private void myRadioGroupChecked(MyRadioGroup group,int checkedId){
        if (checkedId == m_rdoBtn_Alipay.getId()){
            online_pay_category = 1;
            m_tv_ok.setText("支付宝支付" + tobePaid);
        }else if(checkedId == m_rdoBtn_wx.getId()){
            online_pay_category = 2;
            m_tv_ok.setText("微信支付" + tobePaid);
        }
    }
    @Event({R.id.tv_select_redpackage, R.id.radio_feed_other, R.id.tv_ok})
    private void payForTripOnClick(View v){
        switch (v.getId()){
            case R.id.tv_select_redpackage:
                showToast("暂无红包可用");
                break;
            case R.id.radio_feed_other:
                showToast("请输入打赏金额");
                break;
            case R.id.tv_ok:
                //提交在线支付，创建在线支付宝
                HashMap<String, String> hashMap = new HashMap<>();
               /* hashMap.put("pay_handle[0][1]", "1");
                hashMap.put("pay_handle[0][0]", "0");*/

                hashMap.put("pay_handle[0][1]","-1");
                hashMap.put("pay_handle[0][0]", "4");
                // params.add(hashMap);
                //XgoLog.i(params.toString());
                ConnectionManager.getInstance().preparePaySubmit(PayForTripActivity.this,
                        hashMap,mOrderInfo.order_gid,
                        null,
                        null,
                        new PreparePaySubmitOnDataLoadEndListener());
                break;
        }
    }


    @Subscriber
    public void getDemandInfo(DemandSubmitDataBean demandInfo){
        XgoLog.i("mDemandInfo:" + demandInfo.toString());
        ConnectionManager
                .getInstance()
                .orderGetinfo(this
                        , demandInfo.order_gid
                        , new OrderGetInfoOnDataLoadEndListener());
    }
    @Subscriber
    public void getQuotationInfo(QuotationGetInfo.ResDataBean dataBean) {
        this.quotationInfo = dataBean;
        m_tv_deposit.setText("￥" + dataBean.deposit);

    }

    class OrderGetInfoOnDataLoadEndListener implements ConnectionUtil.OnDataLoadEndListener {


        @Override
        public void OnLoadEnd(String ret) {
            OrderInfo orderInfo = App.getInstance().getBeanFromJson(ret, OrderInfo.class);
            if (orderInfo.errCode != -1)
            {
                OrderInfo.ResDataBean resData = orderInfo.resData;
                mOrderInfo = resData;
                double order_amount = resData.order_amount;
                m_tv_sum_include_tax.setText("¥" + order_amount);
                m_tv_already_pay_off.setText("¥" + resData.order_paid_money);
                m_tv_insurance.setText("¥" + resData.insurance_total_money);
                 tobePaid = order_amount - (resData.order_paid_money + resData.insurance_total_money);
                m_tv_remain_pay_off.setText("¥" + (order_amount - (resData.order_paid_money+resData.insurance_total_money)));
                int radioButtonId = m_rg_payment.getCheckedRadioButtonId();
                if (m_rdoBtn_Alipay.getId() == radioButtonId)
                    m_tv_ok.setText("支付宝支付" + tobePaid);
                else m_tv_ok.setText("微信支付" + tobePaid);

            }

        }
    }

    //提交预支付订单
    class PreparePaySubmitOnDataLoadEndListener implements ConnectionUtil.OnDataLoadEndListener {

      //  private OrderSubmitBean.ResDataBean orderSubmitBeanResData;

        @Override
        public void OnLoadEnd(String ret) {
            dismissProgressDialog();
            PreparePaySubmitBean baseBean = App.getInstance().getBeanFromJson(ret, PreparePaySubmitBean.class);
            if (baseBean.errCode != -1) {
                int checkedRadioButtonId = m_rg_payment.getCheckedRadioButtonId();
                if (checkedRadioButtonId == m_rdoBtn_wx.getId()) {
                    online_pay_category = 2;
                } else if (checkedRadioButtonId == m_rdoBtn_Alipay.getId()) {
                    online_pay_category = 1;
                }
                PreparePaySubmitBean.ResDataBean paySubmitBean = baseBean.resData;
                if (paySubmitBean == null)
                {
                    showToast("订单状态有误");
                    return;
                }
                XgoLog.d("paySubmitBean" + paySubmitBean.toString());
                //调取提交在线支付
                showProgressDialog();
                ConnectionManager.getInstance().onlinePaySubmit(
                        PayForTripActivity.this,
                        "" + online_pay_category,
                        "" + tobePaid,
                        paySubmitBean.prepare_pay_gid,
                        mOrderInfo.order_name,
                        mOrderInfo.order_describe,
                        new OnlinePaySubmitOnDataLoadEndListener()
                );

                ConnectionManager.getInstance().PreparePayPay(PayForTripActivity.this,
                        paySubmitBean.prepare_pay_gid,new PreparePayPayOnDataLoadEndListener());
            }
        }
    }

    class  PreparePayPayOnDataLoadEndListener implements ConnectionUtil.OnDataLoadEndListener{

        @Override
        public void OnLoadEnd(String ret) {
            XgoLog.d("PreparePayPayOnDataLoadEndListener:" + ret);
            BaseBean baseBean = App.getInstance().getBeanFromJson(ret, BaseBean.class);
            showToast(baseBean.resMsg);
        }
    }

    //提交在线支付
    class OnlinePaySubmitOnDataLoadEndListener implements ConnectionUtil.OnDataLoadEndListener{
        @Override
        public void OnLoadEnd(String ret) {
            dismissProgressDialog();
            OnlinePaySubmitBean onLinePaySubmitBean = App.getInstance().getBeanFromJson(ret, OnlinePaySubmitBean.class);
            if (onLinePaySubmitBean.errCode != -1)
            {
                showProgressDialog("创建在线支付订单");
                //在线支付订单创建
                ConnectionManager.getInstance().onlinePayCreate(
                        PayForTripActivity.this,
                        onLinePaySubmitBean.resData.online_pay_gid,
                        new OnlinePayCreateOndataLoadEndListener()
                );
            }
        }
    }

    //提交在线付款
    class OnlinePayCreateOndataLoadEndListener implements ConnectionUtil.OnDataLoadEndListener{

        @Override
        public void OnLoadEnd(String ret) {
            dismissProgressDialog();
            OnlinePayCreateBean onlinePayCreate = App.getInstance().getBeanFromJson(ret, OnlinePayCreateBean.class);
            if (onlinePayCreate.errCode != -1){
             //   startActivity(new Intent(PayForTripActivity.this, HomeActivity.class));
                //判断调取支付宝或则微信
                if (online_pay_category == 1) {
                    showToast("正在调取支付宝支付");

                    AliPayUtils.pay(PayForTripActivity.this, onlinePayCreate.resData, new AliPayHandler(PayForTripActivity.this));
                }
                else if(online_pay_category == 2) {
                    showToast("正在调取微信支付");
                    WxPayUtils.payOrder(PayForTripActivity.this, onlinePayCreate.resData);
                }
                else
                    showToast("调取支付失败");

            }
            //提交预支付订单
//            startActivity(new Intent(PaymentForNeedsActivity.this, FinishPayMentActivity.class));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
