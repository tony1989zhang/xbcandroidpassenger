package com.lvchehui.www.xiangbc.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.utils.PixelUtil;
import com.lvchehui.www.xiangbc.view.EditText.EditTextWithDel;
import com.lvchehui.www.xiangbc.view.TitleView;
import com.lvchehui.www.xiangbc.view.popupwindow.CommonAdapter;
import com.lvchehui.www.xiangbc.view.popupwindow.CommonViewHolder;
import com.lvchehui.www.xiangbc.view.popupwindow.PopupWindowListView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张灿能 on 2016/7/16.
 * 作用：付款账号
 */
@ContentView(R.layout.activity_pay_account)
public class PayAccountActivity extends BaseActivity {

    @ViewInject(R.id.title_view)
    private TitleView m_title_view;
    @ViewInject(R.id.tv_pay_type)
    private TextView m_tv_pay_type;
    @ViewInject(R.id.account_et)
    private EditTextWithDel m_account_et;

    @ViewInject(R.id.tv_explanation)
    private TextView m_tv_explanation;

    @ViewInject(R.id.tv_ok)
    private TextView m_tv_ok;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(m_title_view,"绑定到账户");
        m_tv_ok.setText("提交");
    }

    @Event(value = {R.id.tv_ok,R.id.tv_pay_type})
    private void onPayAccountOnClick(View view){
       switch (view.getId()){
           case R.id.tv_ok:
               startActivity(new Intent(this,EnterSmsActivity.class));
               break;
           case R.id.tv_pay_type:

               final List<String> listDatas = new ArrayList<>();
               listDatas.add("支付宝");
               listDatas.add("微信");


               CommonAdapter commonAdapter = new DateAdapter(this,listDatas,R.layout.spinner_list_item);
               final PopupWindowListView popupWindowListView = new PopupWindowListView(this, PixelUtil.dp2px(this, 80), commonAdapter);
               popupWindowListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                   @Override
                   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                       showToast("listDatas:" + listDatas.get(position));
                       m_tv_pay_type.setText(listDatas.get(position));
                       popupWindowListView.dismiss();
                   }
               });
               popupWindowListView.showAsDropDown(m_tv_pay_type);
               break;
       }
    }


    class DateAdapter extends CommonAdapter<String>{

        public DateAdapter(Context context, List<String> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(CommonViewHolder helper, String item) {
            helper.setText(R.id.tv_tinted_spinner,item);
        }
    }
}
