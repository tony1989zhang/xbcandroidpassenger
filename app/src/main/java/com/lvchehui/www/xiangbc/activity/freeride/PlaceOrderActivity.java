package com.lvchehui.www.xiangbc.activity.freeride;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.Fragment.ContactFragment;
import com.lvchehui.www.xiangbc.Fragment.InvoiceFragment;
import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.activity.ContactActivity;
import com.lvchehui.www.xiangbc.activity.InvoiceActivity;
import com.lvchehui.www.xiangbc.activity.chooseneeds.PoiKeyWordSearchActivity;
import com.lvchehui.www.xiangbc.activity.pay.PaymentActivity;
import com.lvchehui.www.xiangbc.activity.pay.PaymentForRideActivity;
import com.lvchehui.www.xiangbc.app.App;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.bean.DemandSubmitBean;
import com.lvchehui.www.xiangbc.bean.DemandSubmitRequestBean;
import com.lvchehui.www.xiangbc.bean.FilterQuotationListBean;
import com.lvchehui.www.xiangbc.bean.InvoiceListDataBean;
import com.lvchehui.www.xiangbc.bean.RideDemandSubmitRequest;
import com.lvchehui.www.xiangbc.utils.ConnectionManager;
import com.lvchehui.www.xiangbc.utils.ConnectionUtil;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.SPUtil;
import com.lvchehui.www.xiangbc.utils.StringUtils;
import com.lvchehui.www.xiangbc.utils.XgoLog;
import com.lvchehui.www.xiangbc.view.HalfListView;
import com.lvchehui.www.xiangbc.view.TitleView;
import com.lvchehui.www.xiangbc.view.dialog.ListDialog;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by 张灿能 on 2016/5/18.
 * 作用：订单填写
 */
@ContentView(R.layout.activity_place_order)
public class PlaceOrderActivity extends BaseActivity {

    private static final int REQUEST_CODE_CONTACTS = 100;
    private static final int REQUEST_CODE_START_AREA = 101;
    private static final int REQUEST_CODE_DEST_AREA = 102;
    private static final int REQUEST_CODE_INVOICE = 103;

    private boolean isHideRemarks;

    @ViewInject(R.id.switch_iv)
    private ImageView m_switch_iv;
    @ViewInject(R.id.title_view)
    private TitleView m_titleView;
    @ViewInject(R.id.et_contacts)
    private EditText m_et_contacts; //联系人;
    @ViewInject(R.id.et_contacts_phone)
    private EditText m_et_contacts_phone; //15859254561;
    @ViewInject(R.id.tv_start_area)
    private TextView m_tv_start_area;
    @ViewInject(R.id.tv_dest_area)
    private TextView m_tv_dest_area; //福建省厦门市;
    @ViewInject(R.id.tv_invoice)
    private TextView m_tv_invoice; //xxx公司;
    @ViewInject(R.id.ll_remarks)
    private LinearLayout m_ll_remarks;
    @ViewInject(R.id.tv_remarks)
    private TextView m_tv_remarks;
    @ViewInject(R.id.ll_remarks_edit)
    private LinearLayout m_ll_remarks_edit;
    @ViewInject(R.id.et_remarks_edit)
    private EditText m_et_remarks_edit;
    private FilterQuotationListBean.ResDataBean filterDataBean;
    private RideDemandSubmitRequest demandSubmitParams;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().registerSticky(this);
        setTitleView(m_titleView, "顺风车");
    }

    @Event({
            R.id.switch_iv,
            R.id.change_contacts,//选择联系人
            R.id.ll_start_area,
            R.id.ll_dest_area,
            R.id.ll_invoice,
            R.id.tv_ok
    })
    private void placeOrderOnClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.switch_iv:
                isHideRemarks = !isHideRemarks;
                if (isHideRemarks) {
                    m_switch_iv.setImageResource(R.mipmap.switch_on);
                    m_ll_remarks_edit.setVisibility(View.VISIBLE);
                } else {
                    m_switch_iv.setImageResource(R.mipmap.switch_off);
                    m_ll_remarks_edit.setVisibility(View.GONE);
                }
                break;
            case R.id.change_contacts:
//                Utils.getCursorRequest(this);
                intent = new Intent(PlaceOrderActivity.this, ContactActivity.class);
                /*startActivityForResult(intent, 0);*/
                startActivityForResult(intent, REQUEST_CODE_CONTACTS);
                break;
            case R.id.tv_ok:
                XgoLog.e("tv_ok:点击");
                submitDemand();

                break;
            case R.id.ll_start_area:
                intent = new Intent(this, PoiKeyWordSearchActivity.class);
                startActivityForResult(intent, REQUEST_CODE_START_AREA);
                break;
            case R.id.ll_dest_area:
                intent = new Intent(this, PoiKeyWordSearchActivity.class);
                startActivityForResult(intent, REQUEST_CODE_DEST_AREA);
                break;
            case R.id.ll_invoice:
                intent = new Intent(this, InvoiceActivity.class);
                startActivityForResult(intent, REQUEST_CODE_INVOICE);
                break;
        }
    }

    private void submitDemand() {

        if (!(isNotEmpty(m_et_contacts)
                &&isNotEmpty(m_et_contacts_phone)
                &&StringUtils.isNotEmpty(m_tv_start_area)
                &&StringUtils.isNotEmpty(m_tv_dest_area))){
            return;
        }
        if (null == demandSubmitParams)
        {
            showToast("顺风车信息未详细");
            return;
        }
        initDemandData();
        showProgressDialog("正在发布顺风车");
        ConnectionManager.getInstance().demandSubmit(this, demandSubmitParams,
                new DemandSubmitOnDataLoadEndListener());
    }
    private boolean isNotEmpty(EditText et)
    {return StringUtils.isNotEmpty(et);
    }
    private void initDemandData() {
        demandSubmitParams.contacts_name = m_et_contacts.getText().toString();
        demandSubmitParams.contacts_phone = m_et_contacts_phone.getText().toString();
        demandSubmitParams.use_remark = m_et_remarks_edit.getText().toString();
        demandSubmitParams.begin_address = m_tv_start_area.getText().toString();
        demandSubmitParams.end_address = m_tv_dest_area.getText().toString();
    }

    class DemandSubmitOnDataLoadEndListener implements ConnectionUtil.OnDataLoadEndListener {

        @Override
        public void OnLoadEnd(String ret) {
            XgoLog.e("demandSubmit:" + ret.toString());
            DemandSubmitBean demandSubmitBean = App
                    .getInstance()
                    .getBeanFromJson(ret, DemandSubmitBean.class);
            if (demandSubmitBean.errCode != -1) {
                Intent intent = new Intent(PlaceOrderActivity.this,PaymentForRideActivity.class);
                startActivity(intent);//跳转预支付订单页面
                EventBus.getDefault().postSticky(demandSubmitBean.resData);
                dismissProgressDialog();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        XgoLog.e("resultCode:" + resultCode);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CONTACTS:
                    setContacts(data);
                    break;
                case REQUEST_CODE_START_AREA:
                    m_tv_start_area.setText(data.getStringExtra(Constants.DETAILED_ADDRESS));
                    break;
                case REQUEST_CODE_DEST_AREA:
                    m_tv_dest_area.setText(data.getStringExtra(Constants.DETAILED_ADDRESS));
                    break;
                case REQUEST_CODE_INVOICE:
                    m_tv_invoice.setText(data.getStringExtra(InvoiceFragment.INVOICE_NAME));
                    InvoiceListDataBean invoiceListDataBean = (InvoiceListDataBean) data.getSerializableExtra(InvoiceFragment.INVOICE_GET_BUNDLE);
                    demandSubmitParams.invoice_address = invoiceListDataBean.address;
                    demandSubmitParams.invoice_consignee = invoiceListDataBean.consignee;
                    demandSubmitParams.invoice_phone = invoiceListDataBean.phone;
                    break;
            }
        }

    }

    private void setContacts(Intent data) {
        String name = data.getStringExtra(ContactFragment.CONTACT_NAME);
        String phone = data.getStringExtra(ContactFragment.CONTACT_PHONE);
        XgoLog.e("name:" + name + "phone:" + phone);
        m_et_contacts.setText(name);
        m_et_contacts_phone.setText(phone);
    }

    public void showDelAndEdit(final String gid, final String contacts_gid) {
        final String[] params = {"编辑", "删除"};


        HalfListView halfListView = new HalfListView(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_choose, params);
        halfListView.setAdapter(adapter);
        halfListView.setCacheColorHint(0x000000);

        final ListDialog listDialog = new ListDialog(this, halfListView);
        listDialog.show();
        halfListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {

                } else if (position == 1) {
//                    ConnectionManager.getInstance().contactsList(PlaceOrderActivity.this, gid, new ConnectionUtil.OnDataLoadEndListener() {
//                        @Override
//                        public void OnLoadEnd(String ret) {
//                            BaseBean beanFromJson = App.getInstance().getBeanFromJson(ret, BaseBean.class);
//                            showToast(beanFromJson.resMsg);
//                            if (beanFromJson.errCode != -1) {
//                            }
//                        }
//                    });
                }
                listDialog.dismiss();
            }
        });
    }

    @Subscriber
    public void getFilterQuotation(FilterQuotationListBean.ResDataBean filterDataBean) {
        this.filterDataBean = filterDataBean;
        if (demandSubmitParams==null)
            return;

        demandSubmitParams.begin_time = "" + filterDataBean.end_booking_time;
        demandSubmitParams.use_type = "5";
        demandSubmitParams.car_num = "" + filterDataBean.cars_info.size();
        demandSubmitParams.quotation_gid = filterDataBean.quotation_gid;
    }

    @Subscriber
    public void getDemandSubmitRequestBean(RideDemandSubmitRequest demandSubmitParams){
        this.demandSubmitParams = demandSubmitParams;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
