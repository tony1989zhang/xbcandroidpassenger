package com.lvchehui.www.xiangbc.activity.pay;

import android.os.Bundle;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.bean.DemandSubmitDataBean;
import com.lvchehui.www.xiangbc.bean.FilterQuotationListBean;
import com.lvchehui.www.xiangbc.bean.GetQuotationedListBean;
import com.lvchehui.www.xiangbc.utils.DateUtil;
import com.lvchehui.www.xiangbc.utils.StringUtils;
import com.lvchehui.www.xiangbc.utils.XgoLog;
import com.lvchehui.www.xiangbc.view.QuantityView;
import com.lvchehui.www.xiangbc.view.mutilRadioGroup.MyRadioGroup;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by 张灿能 on 2016/9/19.
 * 作用：顺风车预付定金
 */
@ContentView(R.layout.activity_pay_for_ride)
public class PaymentForRideActivity extends PaymentForNeedsActivity {


    @ViewInject(R.id.tv_day)
    private TextView m_tv_day; //03月23日;

    @ViewInject(R.id.address)
    private TextView m_address; //厦门～福州;

    @ViewInject(R.id.et_campany_name)
    private TextView m_et_campany_name; //王先生;

    @ViewInject(R.id.tv_user_telephone_num)
    private TextView m_tv_user_telephone_num; //15859254561;

    @ViewInject(R.id.tv_passenger_num)
    private TextView m_tv_passenger_num; //43人;

    @ViewInject(R.id.tv_car)
    private TextView m_tv_car; //考斯特54座;

    @ViewInject(R.id.tv_compand)
    private TextView m_tv_compand; //盛世文化传媒有限公司;

    @ViewInject(R.id.tv_invoice_contact)
    private TextView m_tv_invoice_contact; //张小姐  13055787933;

    @ViewInject(R.id.tv_address)
    private TextView m_tv_address; //地址：福建省厦门市集美区商务运营中心603栋4楼;

    // 保险种类
    @ViewInject(R.id.tv_insurance_type)
    private TextView m_tv_insurance_type;

    // 保险金额
    @ViewInject(R.id.tv_insurance_cost)
    private TextView m_tv_insurance_cost;

    // 保险数量
    @ViewInject(R.id.qv_num_select)
    private QuantityView m_qv_num_select;

    @ViewInject(R.id.tv_accept_license)
    private TextView m_tv_accept_license;
    private FilterQuotationListBean.ResDataBean filterQuotationResDataBean;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null == dataBean) dataBean = new GetQuotationedListBean.ResDataBean();
        if (null == filterQuotationResDataBean) filterQuotationResDataBean = new FilterQuotationListBean.ResDataBean();
    }

    @Event(R.id.tv_accept_license)

    @Subscriber
    public void getDemandSubmitBean(DemandSubmitDataBean dataBean){
        this.dataBean.demand_gid = dataBean.demand_gid;
        m_tv_day.setText(DateUtil.getDateToStr(dataBean.begin_time*1000,"MM 月 dd日"));
        m_tv_passenger_num.setText("" + dataBean.pepole_num + "人");
        m_et_campany_name.setText("" + dataBean.contacts_name);
        m_tv_user_telephone_num.setText("" + dataBean.contacts_phone);
        if (StringUtils.isNotEmpty(dataBean.invoice_name)){

            m_tv_compand.setText(dataBean.invoice_name);
        }else{
            m_tv_compand.setText("发票名:暂无信息");
        }
        if (StringUtils.isNotEmpty(dataBean.invoice_address)){
            m_tv_address.setText("地址:" + dataBean.invoice_address);
            m_tv_address.setText("地址:暂无信息");
        }
        if(StringUtils.isNotEmpty(dataBean.invoice_phone)&&StringUtils.isNotEmpty(dataBean.invoice_name))
        {
            m_tv_invoice_contact.setText(dataBean.invoice_name + dataBean.invoice_phone);
            m_tv_invoice_contact.setText("姓名：暂无 电话:暂无");
        }
    }

    @Override
    public void checkedChanged(MyRadioGroup group, int checkedId) {
        XgoLog.i("dataBean.deposit:" + dataBean.deposit);
        if (filterQuotationResDataBean != null && StringUtils.isNotEmpty(filterQuotationResDataBean.deposit)) {
            double total = Double.valueOf(filterQuotationResDataBean.deposit) + m_qv_num_select.getQuantity() * m_selling_price;
            if (checkedId == m_rdoBtn_wx.getId()) {
                m_tv_ok.setText("微信付款" + total + "元");
            } else if (checkedId == m_rdoBtn_Alipay.getId()) {
                m_tv_ok.setText("支付宝" + total + "元");
            }
        }
    }

    @Subscriber
    public void getFilterQuotationListBean(FilterQuotationListBean.ResDataBean dataBean){
        filterQuotationResDataBean = dataBean;
        m_tv_sum_include_tax.setText("￥" + dataBean.motorcade_quotation_money);
        m_tv_deposit.setText("￥" + dataBean.deposit);
        int checkedRadioButtonId = m_rg_payment.getCheckedRadioButtonId();
        double total = Double.valueOf(dataBean.deposit) + m_qv_num_select.getQuantity() * m_selling_price;
        if (checkedRadioButtonId == m_rdoBtn_wx.getId()) {
            m_tv_ok.setText("微信付款" + total + "元");
        } else if (checkedRadioButtonId == m_rdoBtn_Alipay.getId()) {
            m_tv_ok.setText("支付宝付款" + total + "元");
        }
        this.dataBean.quotation_gid = dataBean.quotation_gid;
        m_address.setText(dataBean.begin_address + "~" + dataBean.end_address);
        m_tv_car.setText(dataBean.cars_info.get(0).motorcade_name +  dataBean.cars_info.get(0).vehicle_model + dataBean.cars_info.get(0).plate_number);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().removeStickyEvent(FilterQuotationListBean.ResDataBean.class);
        EventBus.getDefault().removeStickyEvent(DemandSubmitDataBean.class);
    }
}
