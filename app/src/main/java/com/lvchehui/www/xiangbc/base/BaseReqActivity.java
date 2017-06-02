package com.lvchehui.www.xiangbc.base;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.Fragment.ContactFragment;
import com.lvchehui.www.xiangbc.Fragment.InvoiceFragment;
import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.activity.ContactActivity;
import com.lvchehui.www.xiangbc.activity.InvoiceActivity;
import com.lvchehui.www.xiangbc.activity.chooseneeds.ConfirmOrderActivity;
import com.lvchehui.www.xiangbc.activity.mine.WebActivity;
import com.lvchehui.www.xiangbc.app.App;
import com.lvchehui.www.xiangbc.bean.DemandSubmitBean;
import com.lvchehui.www.xiangbc.bean.DemandSubmitRequestBean;
import com.lvchehui.www.xiangbc.bean.InvoiceListDataBean;
import com.lvchehui.www.xiangbc.impl.OnOperationListener;
import com.lvchehui.www.xiangbc.utils.CalcUtil;
import com.lvchehui.www.xiangbc.utils.ConnectionManager;
import com.lvchehui.www.xiangbc.utils.ConnectionUtil;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.SPUtil;
import com.lvchehui.www.xiangbc.utils.StringUtils;
import com.lvchehui.www.xiangbc.utils.Utils;
import com.lvchehui.www.xiangbc.utils.XgoLog;
import com.lvchehui.www.xiangbc.view.HalfListView;
import com.lvchehui.www.xiangbc.view.dialog.CustomAlertDialog;
import com.lvchehui.www.xiangbc.view.dialog.CustomDialog;
import com.lvchehui.www.xiangbc.view.dialog.CustomEditDialog;
import com.lvchehui.www.xiangbc.view.dialog.ListDialog;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by 张灿能 on 2016/6/21.
 * 作用：发布需求基类
 */
public class BaseReqActivity extends BaseActivity implements OnOperationListener,ConnectionUtil.OnDataLoadEndListener{
    private static final int REQUEST_CODE_INVOICE = 103;
    private static final int REQUST_CODE_CONTACT = 104;
    boolean isHideRemarks;
    @ViewInject(R.id.switch_iv)
    public ImageView m_switch_iv;
    @ViewInject(R.id.ll_remarks_edit)
    public LinearLayout m_ll_remarks_edit;
    @ViewInject(R.id.spinner_seat_num1)
    public TextView spinner_seat_num1;
    @ViewInject(R.id.spinner_seat_num2)
    public TextView spinner_seat_num2;
    @ViewInject(R.id.spinner_seat_num3)
    public TextView spinner_seat_num3;

    @ViewInject(R.id.et_contacts)
    public EditText m_et_contacts;
    @ViewInject(R.id.et_contacts_phone)
    public EditText m_et_contacts_phone;
    @ViewInject(R.id.tv_passenger_num)
    public TextView m_tv_people_num;


    @ViewInject(R.id.tv_car_num)
    public TextView m_tv_car_num;

    @ViewInject(R.id.ll_invoice)
    public LinearLayout m_ll_invoice;
    @ViewInject(R.id.et_remarks_edit)
    public EditText m_et_remarks_edit;

    @ViewInject(R.id.tv_invoice)
    public TextView m_tv_invoice;

    /*@ViewInject(R.id.tv_explanation)
    public TextView m_tv_explanation;*/
    @ViewInject(R.id.tv_ok)
    public TextView m_tv_ok;

    @ViewInject(R.id.content_scroll_view)
    public ScrollView m_scroll_view;

    private  CustomEditDialog m_customEditDialog;
    private int seat_index = 0;
    private static final int SEAT_NUM1 = 1;
    private static final int SEAT_NUM2 = 2;
    private static final int SEAT_NUM3 = 3;
    private   ListDialog listDialog;
    public DemandSubmitRequestBean submitRequestBean;
    private Class nowActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nowActivity = BaseReqActivity.class;
        if (null == submitRequestBean){
            submitRequestBean = new DemandSubmitRequestBean();
        }
        initSpinnerSeat();
        initBtn();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        x.task().postDelayed(new Runnable() {
            @Override
            public void run() {
                m_scroll_view.scrollTo(0, 0);
            }
        }, 500);

    }

    public void initBtn() {
//        m_et_people_num.addTextChangedListener(new GoAndBackTextWatcher());
//        Editable ea = m_et_people_num.getText();
//        m_et_people_num.setSelection(ea.length());
//        Editable ea2 = m_et_car_num.getText();
//        m_et_car_num.setSelection(ea2.length());
        /*m_tv_explanation.setVisibility(View.GONE);*/
        m_tv_ok.setText("生成订单");
    }

    public void showOrHideRemarks() {
        isHideRemarks = !isHideRemarks;
        if (isHideRemarks) {
            m_switch_iv.setImageResource(R.mipmap.switch_on);
            m_ll_remarks_edit.setVisibility(View.VISIBLE);
        } else {
            m_switch_iv.setImageResource(R.mipmap.switch_off);
            m_ll_remarks_edit.setVisibility(View.GONE);
        }
    }

    public void initSpinnerSeat() {


        final String[] carSeatNums = { "4座",
                "6座", "9座", "12座", "15座", "18座", "21座", "24座", "28座", "34座", "38座", "46座", "50座", "54座" };


        HalfListView halfListView = new HalfListView(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_sample, carSeatNums);
        halfListView.setAdapter(adapter);
        halfListView.setCacheColorHint(0x000000);
        if (listDialog != null){
            return;
        }
        listDialog = new ListDialog(BaseReqActivity.this,halfListView);
        halfListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (seat_index == SEAT_NUM1) {
                    spinner_seat_num1.setText(carSeatNums[position]);
                } else if (seat_index == SEAT_NUM2) {
                    spinner_seat_num2.setText(carSeatNums[position]);
                } else if (seat_index == SEAT_NUM3) {
                    spinner_seat_num3.setText(carSeatNums[position]);
                }
                listDialog.dismiss();
            }
        });
    }


    public void initCarNum() {

        final String[] carNums = { "1","2","3",">3"};
        HalfListView halfListView = new HalfListView(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_sample, carNums);
        halfListView.setAdapter(adapter);
        halfListView.setCacheColorHint(0x000000);
         final ListDialog listDialog = new ListDialog(BaseReqActivity.this,halfListView);
        halfListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listDialog.dismiss();
                if (position == 3) {
                    LarghtDingD();
                    return;
                }
                m_tv_car_num.setText(carNums[position]);
                if (position == 0) {
                    spinner_seat_num1.setVisibility(View.VISIBLE);
                    spinner_seat_num2.setVisibility(View.GONE);
                    spinner_seat_num3.setVisibility(View.GONE);
                } else if (position == 1) {
                    spinner_seat_num1.setVisibility(View.VISIBLE);
                    spinner_seat_num2.setVisibility(View.VISIBLE);
                    spinner_seat_num3.setVisibility(View.GONE);

                } else if (position == 2) {
                    spinner_seat_num1.setVisibility(View.VISIBLE);
                    spinner_seat_num2.setVisibility(View.VISIBLE);
                    spinner_seat_num3.setVisibility(View.VISIBLE);
                } else {
                    spinner_seat_num1.setVisibility(View.GONE);
                    spinner_seat_num2.setVisibility(View.GONE);
                    spinner_seat_num3.setVisibility(View.GONE);
                }
            }
        });
        listDialog.show();
    }

//    class GoAndBackTextWatcher extends AbstractTextWatcher {
//        @Override
//        public void afterTextChanged(Editable s) {
//
//
//    }
    @Event(value = {R.id.ll_passenger_num,R.id.tv_passenger_num,R.id.tv_car_num,R.id.ll_car_num})
    private void peopleOrCarOnClick(View view){

        switch (view.getId()){
            case R.id.ll_passenger_num:
            case R.id.tv_passenger_num:
                if (null == m_customEditDialog) {
                    m_customEditDialog = new CustomEditDialog(this);
                    m_customEditDialog.setEditMax(3);
                    m_customEditDialog.setEditInputType(InputType.TYPE_CLASS_NUMBER);
                    m_customEditDialog.setEditHint("输入用车人数");
                    m_customEditDialog.setTitle("人数");
                    m_customEditDialog.setOperationListener(this);
                }
                m_customEditDialog.show();
                break;
            case R.id.ll_car_num:
            case R.id.tv_car_num:
                initCarNum();
                break;
        }

    }

    @Override
    public void onLeftClick() {
        m_customEditDialog.cancel();
    }

    @Override
    public void onRightClick() {
        m_customEditDialog.cancel();
//        m_tv_people_num.setText(m_customEditDialog.getEditText());
        checkPeople(Integer.valueOf(m_customEditDialog.getEditText()));
    }



    private void checkPeople(int peopleNum){
        if (peopleNum > 0) {
            Integer[] integers = CalcUtil.calCarCount(peopleNum);
            if (null == integers) {
                LarghtDingD();
                return;
            }
//            XgoLog.e("建议车辆数：" + integers);
            m_tv_people_num.setText("" + peopleNum);
            switch (integers.length) {
                case 0:
                    break;
                case 1:
                    m_tv_car_num.setText("1");
                    spinner_seat_num1.setText("" + integers[0]+"座");
                    spinner_seat_num2.setText("");
                    spinner_seat_num3.setText("");
                    spinner_seat_num1.setVisibility(View.VISIBLE);
                    spinner_seat_num2.setVisibility(View.INVISIBLE);
                    spinner_seat_num3.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    m_tv_car_num.setText("2");
                    spinner_seat_num1.setText("" + integers[0] + "座");
                    spinner_seat_num2.setText("" + integers[1]+ "座");
                    spinner_seat_num3.setText("");
                    spinner_seat_num1.setVisibility(View.VISIBLE);
                    spinner_seat_num2.setVisibility(View.VISIBLE);
                    spinner_seat_num3.setVisibility(View.INVISIBLE);
                    break;
                case 3:
                    m_tv_car_num.setText("3");
                    spinner_seat_num1.setText("" + integers[0] + "座");
                    spinner_seat_num2.setText("" + integers[1] + "座");
                    spinner_seat_num3.setText("" + integers[2] + "座");
                    spinner_seat_num1.setVisibility(View.VISIBLE);
                    spinner_seat_num2.setVisibility(View.VISIBLE);
                    spinner_seat_num3.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }else{
            showToast("用车人数必须大于0");
            return;
        }
    }

    private void LarghtDingD() {
        CustomAlertDialog dialog = new CustomAlertDialog(this);
        CustomDialog customDialog = new CustomDialog(BaseReqActivity.this);
        customDialog.setTitle("警告");
        customDialog.setMessage("大订单请联系客服400-885566222");
        customDialog.setButtonsText("取消", "确认");
        customDialog.show();
    }

    @Event(value = {R.id.switch_iv,R.id.tv_ok,R.id.change_contacts,R.id.spinner_seat_num1,R.id.spinner_seat_num2,R.id.spinner_seat_num3,R.id.ll_invoice,R.id.tv_invoice})
    private void baseReqOnClick(View view){
        switch (view.getId()) {
            case R.id.switch_iv:
                showToast("点击");
                showOrHideRemarks();
                x.task().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        m_scroll_view.fullScroll(View.FOCUS_DOWN);
                    }
                }, 500);
                break;
            case R.id.tv_ok:
                submitDingD();
                break;
            case R.id.change_contacts:
                startActivityForResult(new Intent(this, ContactActivity.class), REQUST_CODE_CONTACT);
                break;
            case R.id.spinner_seat_num1:
                seat_index = SEAT_NUM1;
                listDialog.show();
                break;
            case R.id.spinner_seat_num2:
                seat_index = SEAT_NUM2;
                listDialog.show();
                break;
            case R.id.spinner_seat_num3:
                seat_index = SEAT_NUM3;
                listDialog.show();
                break;
            case R.id.ll_invoice:
            case R.id.tv_invoice:
                Intent intent = new Intent(this, InvoiceActivity.class);
                startActivityForResult(intent,REQUEST_CODE_INVOICE);
                break;
        }
    }


    public void submitDingD(){

        if (setBaseReqSubmitParams()) return;

        showProgressDialog("正发布需求");
        XgoLog.e("submitRequestBean:" + submitRequestBean.toString());
        ConnectionManager.getInstance().demandSubmit(this, (String) SPUtil.getInstance(this).get(Constants.USER_GID, ""), submitRequestBean, this);


    }

    private boolean setBaseReqSubmitParams() {
        String car_model = "";

        if (StringUtils.isEmpty(m_et_contacts.getText().toString()))
        {
            m_et_contacts.setError("联系人不能为空");
            showToast("联系人不能为空");
            return true;
        }
        if(StringUtils.isEmpty(m_et_contacts_phone.getText().toString())){
            m_et_contacts_phone.setError("联系电话不能为空");
            showToast("联系电话不能为空");
            return true;
        }

        if(StringUtils.isEmpty(m_tv_people_num.getText().toString())){
            m_tv_people_num.setError("请输入人数");
            showToast("请输入人数");
            return true;
        }
        if (null == submitRequestBean)
        {
            return true;
        }

        if(!StringUtils.isEmpty(m_tv_invoice.getText().toString())){
            submitRequestBean.invoice_name = m_tv_invoice.getText().toString();
        }
        if(!StringUtils.isEmpty(m_et_remarks_edit.getText().toString())){
            submitRequestBean.use_remark = m_et_remarks_edit.getText().toString();
        }
        if(!StringUtils.isEmpty(m_tv_car_num.getText().toString())){
            submitRequestBean.car_num = m_tv_car_num.getText().toString();

        }
        submitRequestBean.contacts_name = m_et_contacts.getText().toString();
        submitRequestBean.contacts_phone = m_et_contacts_phone.getText().toString();
        submitRequestBean.pepole_num = m_tv_people_num.getText().toString();

        car_model = getString(car_model, spinner_seat_num1);
        car_model = getString(car_model,spinner_seat_num2);
        car_model = getString(car_model,spinner_seat_num3);

        car_model = car_model.substring(0, car_model.length() - 1);
        car_model = "[" + car_model +"]";
        submitRequestBean.car_model = car_model;
        submitRequestBean.request_start_city = "厦门";
        return false;
    }

    private String getString(String car_model,TextView tv) {
        if (!StringUtils.isEmpty(tv.getText())){
            car_model += tv.getText().toString().substring(0,tv.getText().toString().indexOf("座")) + ",";

        }
        return car_model;
    }

    public String getTravelDetail(){
        return "暂无";
    }

    public String getPassEngErDetail(){
        return "暂无";
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        XgoLog.e("resultCode:" + resultCode);
        if (resultCode == RESULT_OK){
            switch (requestCode) {
                case REQUST_CODE_CONTACT:
                    String name = data.getStringExtra(ContactFragment.CONTACT_NAME);
                    String phone = data.getStringExtra(ContactFragment.CONTACT_PHONE);
                    XgoLog.e("name:" + name + "phone:" + phone);
                    m_et_contacts.setText(name);
                    m_et_contacts_phone.setText(phone);
                    break;
                case REQUEST_CODE_INVOICE:
                    XgoLog.i("data:" + data);
                    String invoiceName = data.getStringExtra(InvoiceFragment.INVOICE_NAME);
                    InvoiceListDataBean invoiceListDataBean = (InvoiceListDataBean) data.getSerializableExtra(InvoiceFragment.INVOICE_GET_BUNDLE);
                    if (null == invoiceListDataBean)
                    {
                        return;
                    }

                    if (null == invoiceListDataBean.address){
                        invoiceListDataBean.address = "";
                    }
                    submitRequestBean.invoice_address = invoiceListDataBean.address;
                    if (null == invoiceListDataBean.consignee)
                    {
                        invoiceListDataBean.consignee = "";
                    }
                    submitRequestBean.invoice_consignee = invoiceListDataBean.consignee;
                    if (null == invoiceListDataBean.phone)
                    {
                        invoiceListDataBean.phone = "";
                    }
                    submitRequestBean.invoice_phone = invoiceListDataBean.phone;
                    m_tv_invoice.setText(invoiceName);
                    break;

            }
        }

    }

    @Override
    public void OnLoadEnd(String ret) {
        XgoLog.i("baseReq:" +ret);
      /*  dismissProgressDialog();
        DemandSubmitBean demandSubmitBean = App.getInstance().getBeanFromJson(ret, DemandSubmitBean.class);
        if (demandSubmitBean.errCode != -1)
        {
            Intent intent = new Intent(this, ConfirmOrderActivity.class);
            intent.putExtra(Constants.SOURCE,nowActivity.getSimpleName());
            Bundle bundle = new Bundle();
            bundle.putSerializable(ConfirmOrderActivity.DEMAND_SUBMITBEAN_TYPE, demandSubmitBean.resData);
            intent.putExtras(bundle);
            XgoLog.e("demandSubmitBean.resData:" + demandSubmitBean.resData);
//            intent.putExtra(ConfirmOrderActivity.CONFIRM_ORDER_STRING_TRAVEL,getTravelDetail());
//            intent.putExtra(ConfirmOrderActivity.CONFIRM_ORDER_STRING_PASSENGER,getPassEngErDetail());
            startActivity(intent);
        }
        showToast(demandSubmitBean.resMsg);
        */

        dismissProgressDialog();
        DemandSubmitBean demandSubmitBean = App.getInstance().getBeanFromJson(ret, DemandSubmitBean.class);
        if (demandSubmitBean.errCode != -1)
        {
            Intent intent = new Intent(this, ConfirmOrderActivity.class);
            intent.putExtra(Constants.SOURCE, nowActivity.getSimpleName());
            Bundle bundle = new Bundle();
            bundle.putSerializable(ConfirmOrderActivity.DEMAND_SUBMITBEAN_TYPE, demandSubmitBean.resData);
            intent.putExtras(bundle);
            intent.putExtra(WebActivity.WEB_EXT_TITLE, "我的订单");
            XgoLog.i(Utils.getDemandUrl(demandSubmitBean.resData.demand_gid));
            intent.putExtra(WebActivity.WEB_EXT_URL, Utils.getDemandUrl(demandSubmitBean.resData.demand_gid));
            //intent.putExtra(ConfirmOrderActivity.CONFIRM_ORDER_STRING_TRAVEL,getTravelDetail());
            //intent.putExtra(ConfirmOrderActivity.CONFIRM_ORDER_STRING_PASSENGER,getPassEngErDetail());
            startActivity(intent);


        }
        showToast(demandSubmitBean.resMsg);
    }
    /**
     * 设置当前Activity
     * */
    public void setNowActivity(Class nowActivity){
        this.nowActivity = nowActivity;
    }
}
