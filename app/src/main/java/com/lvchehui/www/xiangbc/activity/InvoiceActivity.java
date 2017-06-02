package com.lvchehui.www.xiangbc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.lvchehui.www.xiangbc.Fragment.InvoiceFragment;
import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.base.ActivityForFragmentNormal;
import com.lvchehui.www.xiangbc.bean.InvoiceListDataBean;

import org.xutils.view.annotation.Event;

/**
 * Created by 张灿能 on 2016/7/3.
 * 作用：发票列表
 */
public class InvoiceActivity extends ActivityForFragmentNormal {
    private String invoiceName;
    private String invoiceAddress;
    private InvoiceListDataBean bean;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(m_title_view, "发票列表", "添加");

    }

    @Event(value = R.id.title_right_tv,type = View.OnClickListener.class)
    private  void contactOnClick(View v){
        startActivityForResult(new Intent(this, AddInvoiceActivity.class), 0);
    }
    @Override
    public Fragment initFragment() {
        return new InvoiceFragment();
    }

    public void setInvoiceName(String invoiceName) {
        this.invoiceName = invoiceName;
    }
    public void setInvoiceInvoiceListDataBean(InvoiceListDataBean bean){
        this.bean = bean;
    }


    public void setInvoiceAddress(String invoiceAddress) {
        this.invoiceAddress = invoiceAddress;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (mFragment instanceof InvoiceFragment) {
                InvoiceFragment m_invoicefragment = (InvoiceFragment) mFragment;
                m_invoicefragment.onRefresh();
            }
        }
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        if (null==bean||null==invoiceName) {
            bean = new InvoiceListDataBean();
            invoiceName="";
        }
        bundle.putSerializable(InvoiceFragment.INVOICE_GET_BUNDLE, bean);
        intent.putExtras(bundle);
        intent.putExtra(InvoiceFragment.INVOICE_NAME, invoiceName);
        setResult(RESULT_OK,intent);
        super.finish();
    }
}
