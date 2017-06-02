package com.lvchehui.www.xiangbc.activity.pay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.activity.home.HomeActivity;
import com.lvchehui.www.xiangbc.activity.mine.WebActivity;
import com.lvchehui.www.xiangbc.alipay.AliPayHandler;
import com.lvchehui.www.xiangbc.alipay.AliPayUtils;
import com.lvchehui.www.xiangbc.alipay.OnlinePayCreateBean;
import com.lvchehui.www.xiangbc.app.App;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.bean.BaseBean;
import com.lvchehui.www.xiangbc.bean.GetQuotationedListBean;
import com.lvchehui.www.xiangbc.bean.InsuranceDetailSubmitBean;
import com.lvchehui.www.xiangbc.bean.InsuranceGetListBean;
import com.lvchehui.www.xiangbc.bean.OnlinePaySubmitBean;
import com.lvchehui.www.xiangbc.bean.OrderSubmitBean;
import com.lvchehui.www.xiangbc.bean.PreparePaySubmitBean;
import com.lvchehui.www.xiangbc.utils.ConnectionManager;
import com.lvchehui.www.xiangbc.utils.ConnectionUtil;
import com.lvchehui.www.xiangbc.utils.StringUtils;
import com.lvchehui.www.xiangbc.utils.XgoLog;
import com.lvchehui.www.xiangbc.view.QuantityView;
import com.lvchehui.www.xiangbc.view.TitleView;
import com.lvchehui.www.xiangbc.view.dialog.CustomAlertDialog;
import com.lvchehui.www.xiangbc.view.mutilRadioGroup.MyRadioGroup;
import com.lvchehui.www.xiangbc.wxapi.WxPayUtils;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.qqtheme.framework.picker.OptionPicker;

/**
 * Created by 张灿能 on 2016/9/9.
 * 作用：
 */
@ContentView(R.layout.activity_paymentforneeds)
public class PaymentForNeedsActivity extends BaseActivity {
    @ViewInject(R.id.title_view)
    private TitleView m_title_view;

    @ViewInject(R.id.tv_insurance_info)
    private TextView m_tv_insurance_info;

    @ViewInject(R.id.ll_select_insurance_type)
    private LinearLayout m_ll_select_insurance_type;

    @ViewInject(R.id.tv_insurance_type)
    private TextView m_tv_insurance_type;

    @ViewInject(R.id.tv_insurance_cost)
    private TextView m_tv_insurance_cost;
    public double m_selling_price = 0.00;

    @ViewInject(R.id.tv_reselect_insurance_type)
    private TextView m_tv_reselect_insurance_type;

    @ViewInject(R.id.qv_num_select)
    public QuantityView m_qv_num_select;

    @ViewInject(R.id.tv_sum_include_tax)
    public TextView m_tv_sum_include_tax;
    @ViewInject(R.id.tv_deposit)
    public TextView m_tv_deposit;
    @ViewInject(R.id.tv_insurance)
    private TextView m_tv_insurance;

    @ViewInject(R.id.cb_use_public_account_transfer)
    private CheckBox m_cb_use_public_account_transfer;

    @ViewInject(R.id.rg_payment)
    public MyRadioGroup m_rg_payment;
    @ViewInject(R.id.rdoBtn_Alipay)
    public RadioButton m_rdoBtn_Alipay;
    @ViewInject(R.id.rdoBtn_wx)
    public RadioButton m_rdoBtn_wx;
    @ViewInject(R.id.tv_ok)
    public TextView m_tv_ok;
    private int online_pay_category = 1;


    private List<InsuranceGetListBean.ResDataBean> resData;
    private List<String> asinsuranceGidList = new ArrayList<>();
    //    ArrayList<String> insuranceDetails = new ArrayList<>();
    public GetQuotationedListBean.ResDataBean dataBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(m_title_view, getString(R.string.pro_deposit));
        EventBus.getDefault().registerSticky(this);
        //  List<String> dataset = new LinkedList<>(Arrays.asList("不购买保险x0", "长安意外旅游保险单价x10元", "长安意外旅游保险单价x20元"));
        showProgressDialog();
        ConnectionManager.getInstance().insuranceGetList(this, new InsuranceOnDataLoadEndListener());
        m_qv_num_select.setOnQuantityChangeListener(new QuantityChangeListener());
    }

    @Event(value = R.id.rg_payment,type = MyRadioGroup.OnCheckedChangeListener.class)
    private void myOnCheckedChanged(MyRadioGroup group, int checkedId){
        checkedChanged(group,checkedId);
    }

    public void checkedChanged(MyRadioGroup group, int checkedId){
        XgoLog.i("dataBean.deposit:" + dataBean.deposit);
        if (dataBean != null && StringUtils.isNotEmpty(dataBean.deposit)) {
            double total = Double.valueOf(dataBean.deposit) + m_qv_num_select.getQuantity() * m_selling_price;
            if (checkedId == m_rdoBtn_wx.getId()) {
                m_tv_ok.setText("微信付款" + total + "元");
            } else if (checkedId == m_rdoBtn_Alipay.getId()) {
                m_tv_ok.setText("支付宝" + total + "元");
            }
        }
    }


    @Event({R.id.tv_insurance_info,
            R.id.ll_select_insurance_type,
            R.id.tv_reselect_insurance_type,
            R.id.cb_use_public_account_transfer,
            R.id.tv_accept_license})
    private void payMentOnClick(View v) {
        switch (v.getId()) {
            case R.id.tv_reselect_insurance_type:
                if (null == resData || resData.size() == 0) {
                    return;
                }
                final String[] asinsuranceGids = new String[resData.size()];
                final String[] insuranceTypesCost = new String[resData.size()];// getResources().getStringArray(R.array.insurance_types_with_costs);
                final String[] insuranceTypes = new String[resData.size()]; //getResources().getStringArray(R.array.insurance_types);
                final String[] insuranceCosts = new String[resData.size()];//getResources().getStringArray(R.array.insurance_costs);
                for (int i = 0; i < resData.size(); i++) {
                    InsuranceGetListBean.ResDataBean bean = resData.get(i);
                    insuranceTypes[i] = bean.title;
                    insuranceCosts[i] = bean.selling_price;
                    insuranceTypesCost[i] = bean.title + "单价:" + bean.selling_price;
                    asinsuranceGids[i] = bean.insurance_gid;
                }


                OptionPicker picker = new OptionPicker(this, insuranceTypesCost);
                picker.setSelectedIndex(0);
                picker.setTextSize(16);
                picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(int position, String option) {
                        m_tv_insurance_type.setText(insuranceTypes[position]);
                        m_tv_insurance_cost.setText("￥"+ insuranceCosts[position]);
                        Double selling_price = Double.valueOf(insuranceCosts[position]);
                        m_selling_price = selling_price;
                        m_tv_insurance.setText("￥" + selling_price*m_qv_num_select.getQuantity());
                        asinsuranceGidList.clear();
                        asinsuranceGidList.add(asinsuranceGids[position]);
                    }
                });
                picker.show();
                break;
            case R.id.tv_insurance_info:
                showToast("此处应显示意外险页面");
                break;
            case R.id.cb_use_public_account_transfer:
                // 使用对公转账
                if (m_cb_use_public_account_transfer.isChecked()) {
                    CustomAlertDialog dialog = new CustomAlertDialog(this);
                    dialog.builder().setTitle("公账转账须知")
                            .setMsg(getString(R.string.public_account_transfer_content))
                            .setNegativeButton("关闭", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                    dialog.show();
                }
                break;
            case R.id.tv_accept_license:
                Intent intent = new Intent(this, WebActivity.class);
                intent.putExtra(WebActivity.WEB_EXT_TITLE, getString(R.string.leaguer_agreement));
                intent.putExtra(WebActivity.WEB_EXT_URL, getString(R.string.leaguer_agreement_url));
                startActivity(intent);
                break;
        }
    }

    @Subscriber
    public void getQuotationInfo(GetQuotationedListBean.ResDataBean dataBean) {
        XgoLog.i("getQuotationInfo:" + dataBean.toString());
        this.dataBean = dataBean;
        m_tv_sum_include_tax.setText("￥" + dataBean.sum_money);
        m_tv_deposit.setText("￥" + dataBean.deposit);
        int checkedRadioButtonId = m_rg_payment.getCheckedRadioButtonId();
        String total = dataBean.deposit + m_qv_num_select.getQuantity() * m_selling_price;
        if (checkedRadioButtonId == m_rdoBtn_wx.getId()) {
            m_tv_ok.setText("微信付款" + total + "元");
        } else if (checkedRadioButtonId == m_rdoBtn_Alipay.getId()) {
            m_tv_ok.setText("支付宝" + total + "元");
        }
    }

    @Event(R.id.tv_ok)
    private void submitOk(View v) {
        showProgressDialog("提交保险");
        ConnectionManager.getInstance().insuranceDetailSubmit(
                this,
                asinsuranceGidList.get(0),
                "" + m_qv_num_select.getQuantity(),
                new InsuranceDetailSubmitLoadEndListener());
    }

    //提交订单事件
    class OrderSubmitOnDataLoadEndListener implements ConnectionUtil.OnDataLoadEndListener {

        @Override
        public void OnLoadEnd(String ret) {
            dismissProgressDialog();
            OrderSubmitBean orderSubmitBean = App.getInstance().getBeanFromJson(ret, OrderSubmitBean.class);
            if (orderSubmitBean.errCode != -1) {
                showProgressDialog("提交订单");
              /* List<ArrayList<Integer>> pay_handle = new ArrayList<>();//pay_handler
                ArrayList<Integer> handle_type = new ArrayList<>();//预付定金
                handle_type.add(1);
                handle_type.add(-1);
                pay_handle.add(handle_type);
                String s = new Gson().toJson(pay_handle);*/
            //    List<HashMap<String,Integer>> params = new ArrayList<>();
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("pay_handle[0][1]", "-1");
                hashMap.put("pay_handle[0][0]","1");
               // params.add(hashMap);
                //XgoLog.i(params.toString());
                ConnectionManager.getInstance().preparePaySubmit(PaymentForNeedsActivity.this,
                        hashMap,orderSubmitBean.resData.order_gid, null, null, new PreparePaySubmitOnDataLoadEndListener(orderSubmitBean.resData));
            }
        }
    }

    //提交预支付订单
    class PreparePaySubmitOnDataLoadEndListener implements ConnectionUtil.OnDataLoadEndListener {

        private OrderSubmitBean.ResDataBean orderSubmitBeanResData;
        public PreparePaySubmitOnDataLoadEndListener(OrderSubmitBean.ResDataBean orderSubmitBeanResData){
            this.orderSubmitBeanResData = orderSubmitBeanResData;
        }

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
//                //调取提交在线支付
                showProgressDialog();

                ConnectionManager.getInstance().onlinePaySubmit(
                        PaymentForNeedsActivity.this,
                        "" + online_pay_category,
                        orderSubmitBeanResData.order_amount,
                        paySubmitBean.prepare_pay_gid,
                        orderSubmitBeanResData.order_name,
                        orderSubmitBeanResData.order_describe,
                        new OnlinePaySubmitOnDataLoadEndListener()
                );
                ConnectionManager.getInstance().PreparePayPay(PaymentForNeedsActivity.this,
                        paySubmitBean.prepare_pay_gid, new PreparePayPayOnDataLoadEndListener());
            }
        }
    }

    class PreparePayPayOnDataLoadEndListener implements ConnectionUtil.OnDataLoadEndListener{

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
                        PaymentForNeedsActivity.this,
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
                //判断调取支付宝或则微信
                if (online_pay_category == 1) {
                    showToast("正在调取支付宝支付");
                    AliPayUtils.pay(PaymentForNeedsActivity.this, onlinePayCreate.resData, new AliPayHandler(PaymentForNeedsActivity.this));
                }
                else if(online_pay_category == 2) {
                    showToast("正在调取微信支付");
                    WxPayUtils.payOrder(PaymentForNeedsActivity.this, onlinePayCreate.resData);
                }
                else
                    showToast("调取支付失败");

            }
            //提交预支付订单
//            startActivity(new Intent(PaymentForNeedsActivity.this, FinishPayMentActivity.class));
//
        }
    }
    // 查询保险列表事件
    class InsuranceOnDataLoadEndListener implements ConnectionUtil.OnDataLoadEndListener {

        @Override
        public void OnLoadEnd(String ret) {

            InsuranceGetListBean beanFromJson = App.getInstance().getBeanFromJson(ret, InsuranceGetListBean.class);
            if (beanFromJson.errCode != -1) {
                asinsuranceGidList.clear();
                resData = beanFromJson.resData;
                m_tv_insurance_type.setText(beanFromJson.resData.get(0).title);
                m_tv_insurance_cost.setText("￥" +beanFromJson.resData.get(0).selling_price);
                Double selling_price = Double.valueOf(beanFromJson.resData.get(0).selling_price);
                m_selling_price = selling_price;
                m_tv_insurance.setText("￥" + selling_price*m_qv_num_select.getQuantity());
                asinsuranceGidList.add(beanFromJson.resData.get(0).insurance_gid);
                m_qv_num_select.getQuantity();
            }
            dismissProgressDialog();
        }
    }


    /**
     * 提交保险
     */
    class InsuranceDetailSubmitLoadEndListener implements ConnectionUtil.OnDataLoadEndListener {

        @Override
        public void OnLoadEnd(String ret) {
            InsuranceDetailSubmitBean baseBean = App.getInstance().getBeanFromJson(ret, InsuranceDetailSubmitBean.class);
            dismissProgressDialog();
            if (baseBean.errCode != -1) {
                int is_public = 0;
                if (m_cb_use_public_account_transfer.isChecked()) {
                    is_public = 1;
                } else {
                    is_public = 0;
                }
                asinsuranceGidList.clear();
                asinsuranceGidList.add(baseBean.resData.insurance_detail_gid);

                showProgressDialog("提交保险中");
                ConnectionManager.getInstance().
                        orderSubmit(PaymentForNeedsActivity.this,
                                dataBean.demand_gid,
                                dataBean.quotation_gid,
                                asinsuranceGidList,
                                "1",
                                "" + is_public,
                                new OrderSubmitOnDataLoadEndListener());
            }
        }
    }


    class QuantityChangeListener implements QuantityView.OnQuantityChangeListener{
        @Override
        public void onQuantityChanged(int newQuantity, boolean programmatically) {
            m_tv_insurance.setText("￥" + m_selling_price*m_qv_num_select.getQuantity());
//            m_tv_deposit.setText("￥" + quotationInfo.deposit);
//            int checkedRadioButtonId = m_rg_payment.getCheckedRadioButtonId();
//            double total = Double.valueOf(quotationInfo.deposit) + m_qv_num_select.getQuantity() * m_selling_price;
//            if (checkedRadioButtonId == m_rdoBtn_wx.getId()) {
//                m_tv_ok.setText("支付宝付款" + total + "元");
//            } else if (checkedRadioButtonId == m_rdoBtn_Alipay.getId()) {
//                m_tv_ok.setText("微信付款" + total + "元");
//            }
        }

        @Override
        public void onLimitReached() {
            m_tv_insurance.setText("￥" + m_selling_price*m_qv_num_select.getQuantity());
//            m_tv_deposit.setText("￥" + quotationInfo.deposit);
//            int checkedRadioButtonId = m_rg_payment.getCheckedRadioButtonId();
//            double total = Double.valueOf(quotationInfo.deposit) + m_qv_num_select.getQuantity() * m_selling_price;
//            if (checkedRadioButtonId == m_rdoBtn_wx.getId()) {
//                m_tv_ok.setText("支付宝付款" + total + "元");
//            } else if (checkedRadioButtonId == m_rdoBtn_Alipay.getId()) {
//                m_tv_ok.setText("微信付款" + total + "元");
//            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().removeStickyEvent(GetQuotationedListBean.ResDataBean.class);
        EventBus.getDefault().unregister(this);
    }
}
