package com.lvchehui.www.xiangbc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.lvchehui.www.xiangbc.Fragment.InvoiceFragment;
import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.app.App;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.bean.BaseBean;
import com.lvchehui.www.xiangbc.bean.ExpressInfoAddBean;
import com.lvchehui.www.xiangbc.bean.InvoiceAddBean;
import com.lvchehui.www.xiangbc.bean.InvoiceListDataBean;
import com.lvchehui.www.xiangbc.utils.ConnectionManager;
import com.lvchehui.www.xiangbc.utils.ConnectionUtil;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.SPUtil;
import com.lvchehui.www.xiangbc.utils.StringUtils;
import com.lvchehui.www.xiangbc.view.TitleView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;


/**
 * 作用：发票信息
 */
@ContentView(R.layout.activity_invoice_info)
public class InvoiceInfoActivity extends BaseActivity {
    private static final int REQUEST_SELECT_INVOICE = 100;

    @ViewInject(R.id.title_view)
    private TitleView m_titleView;
    @ViewInject(R.id.tv_tickethead_content)
    private EditText m_tv_tickethead_content;
    @ViewInject(R.id.tv_sj_name_content)
    private EditText m_tv_sj_name_content;
    @ViewInject(R.id.tv_phone_content)
    private EditText m_tv_phone_content;
    @ViewInject(R.id.tv_address_content)
    private EditText m_tv_address_content;
    private InvoiceAddBean invoiceAddBean;
    private String m_source_name;
    private InvoiceListDataBean invoiceListDataBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_titleView.setTitleLeftText("取消");
        setTitleView(m_titleView, "发票", "确定");

        Intent intent = getIntent();
        m_source_name = intent.getStringExtra(Constants.SOURCE);
        if (!StringUtils.isEmpty(m_source_name) && m_source_name.equals(InvoiceFragment.class.getSimpleName())) {
//            String invoice_gid = intent.getStringExtra(InvoiceFragment.INVOICE_GID);
            invoiceListDataBean = (InvoiceListDataBean) intent.getSerializableExtra(InvoiceFragment.INVOICEINFO_BUNDLE);
            m_tv_tickethead_content.setText(invoiceListDataBean.invoice_name);
            m_tv_sj_name_content.setText(invoiceListDataBean.consignee);
            m_tv_phone_content.setText(invoiceListDataBean.phone);
            m_tv_address_content.setText(invoiceListDataBean.address);
        }
    }

    @Event(value = {R.id.title_right_tv, R.id.rl_tickethead, R.id.rl_sj_name, R.id.rl_phone
            , R.id.rl_address
    }, type = View.OnClickListener.class)
    private void addInvoiceOnClick(View v) {
        switch (v.getId()) {
            case R.id.title_right_tv:
                if(!checkInput()){
                    return;
                }
                if (StringUtils.isEmpty(m_source_name)) {
                    showToast("来自添加");
                    showProgressDialog("添加发票抬头");
                    ConnectionManager.getInstance().invoiceAdd(this,
                            (String) SPUtil.getInstance(this).get(Constants.USER_GID, ""),
                            m_tv_tickethead_content.getText().toString(),
                            new AddinvoiceOnDataLoadListener(AddinvoiceOnDataLoadListener.REQUEST_INVOICEADD_TYPE));
                } else {
                    ConnectionManager.getInstance().invoiceEdit(this, (String) SPUtil.getInstance(this).get(Constants.USER_GID, ""),
                            invoiceListDataBean.invoice_gid, m_tv_tickethead_content.getText().toString(),
                            new EditInVoiceOnDataLoadListener(EditInVoiceOnDataLoadListener.REQUEST_INVOICEEDIT_TYPE));

                }
                break;
            case R.id.title_left_tv:
                finish();
                break;
            case R.id.tv_common_use:
                // 跳转到发票列表
                Intent intent = new Intent(this, InvoiceActivity.class);
                startActivityForResult(intent, REQUEST_SELECT_INVOICE);
            default:
                break;
        }
    }



    private boolean checkInput() {
        if (StringUtils.isEmpty(m_tv_tickethead_content.getText())) {
            showToast("发票抬头不能为空");
            return false;
        } else if (StringUtils.isEmpty(m_tv_sj_name_content.getText())) {
            showToast("收件人不能为空");
            return false;
        } else if (StringUtils.isEmpty(m_tv_phone_content.getText())) {
            showToast("联系人不能为空");
            return false;
        } else if (StringUtils.isEmpty(m_tv_address_content.getText())) {
            showToast("收件地址不能为空");
            return false;
        }
        return true;
    }

    class EditInVoiceOnDataLoadListener implements ConnectionUtil.OnDataLoadEndListener {
        private int request_type;
        public static final int REQUEST_INVOICEEDIT_TYPE = 2001;
        public static final int REQUEST_EXPRESSINFOEDIT_TYPE = 2002;

        public EditInVoiceOnDataLoadListener(int request_type) {
            this.request_type = request_type;
        }

        @Override
        public void OnLoadEnd(String ret) {

            switch (request_type) {
                case REQUEST_INVOICEEDIT_TYPE:
                    BaseBean beanFromJson = App.getInstance().getBeanFromJson(ret, BaseBean.class);
                    if (beanFromJson.errCode != -1) {
                        ConnectionManager.getInstance().expressInfoEdit(InvoiceInfoActivity.this,
                                (String) SPUtil.getInstance(InvoiceInfoActivity.this).get(Constants.USER_GID, ""),
                                invoiceListDataBean.express_info_gid, m_tv_sj_name_content.getText().toString(),
                                m_tv_phone_content.getText().toString(), m_tv_address_content.getText().toString(),
                                new EditInVoiceOnDataLoadListener(EditInVoiceOnDataLoadListener.REQUEST_EXPRESSINFOEDIT_TYPE));
                    }
                    break;
                case REQUEST_EXPRESSINFOEDIT_TYPE:
                    BaseBean beanFromJson2 = App.getInstance().getBeanFromJson(ret, BaseBean.class);
                    if(beanFromJson2.errCode != -1){
                        setResult(RESULT_OK);
                        finish();
                    }else{
                        showToast(beanFromJson2.resMsg);
                    }
                    break;
            }
        }
    }

    class AddinvoiceOnDataLoadListener implements ConnectionUtil.OnDataLoadEndListener {
        private int request_type;
        public static final int REQUEST_EXPRESSINFOADD_TYPE = 1001;
        public static final int REQUEST_INVOICEADD_TYPE = 1002;
        public static final int REQUEST_BINDEXPRESS_TYPE = 1003;

        public AddinvoiceOnDataLoadListener(int request_type) {
            this.request_type = request_type;
        }

        @Override
        public void OnLoadEnd(String ret) {
            switch (request_type) {
                case REQUEST_INVOICEADD_TYPE:
                    invoiceAddBean = App.getInstance().getBeanFromJson(ret, InvoiceAddBean.class);
                    if (invoiceAddBean.errCode != -1) {
                        ConnectionManager.getInstance().expressInfoAdd(InvoiceInfoActivity.this,
                                (String) SPUtil.getInstance(InvoiceInfoActivity.this).get(Constants.USER_GID, ""),
                                m_tv_sj_name_content.getText().toString(), m_tv_phone_content.getText().toString(),
                                m_tv_address_content.getText().toString(), new AddinvoiceOnDataLoadListener(REQUEST_EXPRESSINFOADD_TYPE));
                    } else {
                        dismissProgressDialog();
                        showToast(invoiceAddBean.resMsg);
                    }
                    break;
                case REQUEST_EXPRESSINFOADD_TYPE:
                    ExpressInfoAddBean expressInfoAddBean = App.getInstance().getBeanFromJson(ret, ExpressInfoAddBean.class);
                    if (expressInfoAddBean.errCode != -1) {
                        if (StringUtils.isEmpty(invoiceAddBean.resData.invoice_gid)) {
                            return;
                        }
                        ConnectionManager.getInstance().bindExpressInfo(InvoiceInfoActivity.this,
                                (String) SPUtil.getInstance(InvoiceInfoActivity.this).get(Constants.USER_GID, ""),
                                invoiceAddBean.resData.invoice_gid,
                                expressInfoAddBean.resData.express_info_gid,
                                new AddinvoiceOnDataLoadListener(REQUEST_BINDEXPRESS_TYPE));
                    } else {
                        dismissProgressDialog();
                        showToast(expressInfoAddBean.resMsg);
                    }
                    break;
                case REQUEST_BINDEXPRESS_TYPE:
                    BaseBean baseBean = App.getInstance().getBeanFromJson(ret, BaseBean.class);
                    dismissProgressDialog();
                    if (baseBean.errCode != -1) {
                        setResult(RESULT_OK);
                        finish();
                    } else {
                    }
                    showToast(baseBean.resMsg);
                    break;
            }
        }
    }
}
