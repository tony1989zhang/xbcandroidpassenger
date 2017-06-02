package com.lvchehui.www.xiangbc.activity.pay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.activity.FinishPayMentActivity;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.view.QuantityView;
import com.lvchehui.www.xiangbc.view.TitleView;
import com.lvchehui.www.xiangbc.view.dialog.CustomAlertDialog;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import cn.qqtheme.framework.picker.OptionPicker;

/**
 * Created by 张灿能 on 2016/6/13.
 * 作用：预付定金
 */
@ContentView(R.layout.activity_pay_ment)
public class PaymentActivity extends BaseActivity {


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

    @ViewInject(R.id.tv_reselect_insurance_type)
    private TextView m_tv_reselect_insurance_type;

    @ViewInject(R.id.qv_num_select)
    private QuantityView m_qv_num_select;

    @ViewInject(R.id.cb_use_public_account_transfer)
    private CheckBox m_cb_use_public_account_transfer;

    /*@ViewInject(R.id.spinner_insurance_type)
    private NiceSpinner m_spinner_insurance_type;

    @ViewInject(R.id.tv_insurance_num)
    private TextView m_tv_insurance_num;*/


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(m_title_view, getString(R.string.pro_deposit));
        List<String> dataset = new LinkedList<>(Arrays.asList("不购买保险x0", "长安意外旅游保险单价x10元", "长安意外旅游保险单价x20元"));
        /*m_spinner_insurance_type.attachDataSource(dataset);
        m_spinner_insurance_type.setTextColor(getResources().getColor(R.color.black));*/
    }

    /*@Event(value = {R.id.tv_ok,R.id.tv_insurance_num} ,type = View.OnClickListener.class)
    private void payMentOnClick(View v){
        switch (v.getId()){
            case R.id.tv_ok:
                showToast("点击ok");
                startActivity(new Intent(this, FinishPayMentActivity.class));
                break;
            case R.id.tv_insurance_num:
                CustomEditDialog customEditDialog = new CustomEditDialog(this);
                customEditDialog.setTitle("人数");
                customEditDialog.setEditHint("购买保险的人数");
                customEditDialog.setEditInputType(InputType.TYPE_CLASS_NUMBER);
                customEditDialog.setEditMax(3);
                customEditDialog.show();
                break;
        }
    }*/

    @Event( {R.id.tv_ok, R.id.tv_insurance_info, R.id.ll_select_insurance_type,
            R.id.tv_reselect_insurance_type, R.id.cb_use_public_account_transfer}
           )
    private void payMentOnClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ok:
                showToast("点击ok");
                startActivity(new Intent(this, FinishPayMentActivity.class));
                break;
            case R.id.tv_reselect_insurance_type:
                String[] insuranceTypesCost = getResources().getStringArray(R.array.insurance_types_with_costs);
                final String[] insuranceTypes = getResources().getStringArray(R.array.insurance_types);
                final String[] insuranceCosts = getResources().getStringArray(R.array.insurance_costs);
                OptionPicker picker = new OptionPicker(this, insuranceTypesCost);
                picker.setSelectedIndex(0);
                picker.setTextSize(16);
                picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(int position, String option) {
                        m_tv_insurance_type.setText(insuranceTypes[position]);
                        m_tv_insurance_cost.setText(insuranceCosts[position]);
                    }
                });
                picker.show();
                break;
            case R.id.tv_insurance_info:
                showToast("此处应显示意外险页面");
                break;
            case R.id.cb_use_public_account_transfer:
                // 使用对公转账
                if (m_cb_use_public_account_transfer.isChecked() == true){
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
        }
    }
}
