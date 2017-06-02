package com.lvchehui.www.xiangbc.activity.freeride;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.app.App;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.bean.DemandSubmitRequestBean;
import com.lvchehui.www.xiangbc.bean.FilterQuotationListBean;
import com.lvchehui.www.xiangbc.bean.RideDemandSubmitRequest;
import com.lvchehui.www.xiangbc.impl.OnOperationListener;
import com.lvchehui.www.xiangbc.utils.ConnectionManager;
import com.lvchehui.www.xiangbc.utils.ConnectionUtil;
import com.lvchehui.www.xiangbc.utils.DateUtil;
import com.lvchehui.www.xiangbc.utils.PickersUtil;
import com.lvchehui.www.xiangbc.utils.StringUtils;
import com.lvchehui.www.xiangbc.view.TitleView;
import com.lvchehui.www.xiangbc.view.dialog.CustomEditDialog;
import com.lvchehui.www.xiangbc.view.wheelData.DateChooseWheelViewDialog;

import org.simple.eventbus.EventBus;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 张灿能 on 2016/5/17.
 * 作用：搜索顺风车
 */
@ContentView(R.layout.activity_free_ride)
public class FreeRideActivity extends BaseActivity {


    @ViewInject(R.id.title_view)
    private TitleView m_titleView;

    @ViewInject(R.id.ll_passenger_num)
    private LinearLayout m_ll_passenger_num;

    @ViewInject(R.id.tv_passenger_num)
    private EditText m_tv_passenger_num;

    @ViewInject(R.id.ll_start_date)
    private LinearLayout m_ll_start_date;

    @ViewInject(R.id.tv_start_date)
    private TextView m_tv_start_date;

    @ViewInject(R.id.ll_start_city)
    private LinearLayout m_ll_start_city;

    @ViewInject(R.id.tv_start_city)
    private TextView m_tv_start_city;

    @ViewInject(R.id.ll_dest_city)
    private LinearLayout m_ll_dest_city;

    @ViewInject(R.id.tv_dest_city)
    private TextView m_tv_dest_city;

    @ViewInject(R.id.tv_ok)
    private TextView m_tv_ok;

    private CustomEditDialog customEditDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitleView(m_titleView, getResources().getString(R.string.free_ride));
        m_tv_ok.setText(getResources().getString(R.string.rapid_research));

        // m_tv_start_date.setText(getToday());

        if (null == customEditDialog) {
            customEditDialog = new CustomEditDialog(this);
            customEditDialog.setEditText("");

            customEditDialog.setEditHint(getString(R.string.passengers_num));
            customEditDialog.setEditInputType(InputType.TYPE_CLASS_NUMBER);
            customEditDialog.setEditMax(3);
            customEditDialog.setTitle(getResources().getString(R.string.hint_passengers_num));
            customEditDialog.setTag(null);
            customEditDialog.setEditText(null);
        }

        customEditDialog.setOperationListener(new CustomEditListener());
    }

    @Event({
            R.id.ll_passenger_num,
            R.id.ll_start_date,
            R.id.ll_start_city,
            R.id.ll_dest_city,
            R.id.tv_ok
    })
    private void seaRideOnClick(View v) {
        switch (v.getId()) {
            /*case R.id.ll_passenger_num:
                customEditDialog.showAndClearEdit();
                break;*/
            case R.id.ll_start_date:
                PickersUtil.getInstance().pickDate(FreeRideActivity.this, m_tv_start_date);
                //setStartDate();
                break;
            case R.id.ll_start_city:
                PickersUtil.getInstance().pickCity(FreeRideActivity.this, m_tv_start_city);
                /*UtilsCity.getInstance().cityChooseDialog(this, new UtilsCity.UtilsCityImpl() {
                    @Override
                    public void cityOnClick(String result) {
                        m_tv_start_city.setText(result);
                    }
                });*/
                break;
            case R.id.ll_dest_city:
                PickersUtil.getInstance().pickCity(FreeRideActivity.this, m_tv_dest_city);
                /*UtilsCity.getInstance().cityChooseDialog(this, new UtilsCity.UtilsCityImpl() {
                    @Override
                    public void cityOnClick(String result) {
                        m_tv_dest_city.setText(result);
                    }
                });*/
                break;
            case R.id.tv_ok:
                // 闪电搜
                String passengersNum = m_tv_passenger_num.getText().toString();
                String startDate = m_tv_start_date.getText().toString();
                String startCity = getSingleCity(m_tv_start_city.getText().toString());
                String destCity = getSingleCity(m_tv_dest_city.getText().toString());
                if (StringUtils.isEmpty(passengersNum)){
                    showToast(getString(R.string.hint_passengers_num));
                    return;
                } else if (StringUtils.isEmpty(startDate)){
                    showToast(getString(R.string.hint_start_date));
                    return;
                } else if (StringUtils.isEmpty(startCity)){
                    showToast(getString(R.string.hint_start_city));
                    return;
                } else if (StringUtils.isEmpty(destCity)){
                    showToast(getString(R.string.hint_dest_city));
                    return;
                } else {

                }

                RideDemandSubmitRequest demandSubmitRequestBean = new RideDemandSubmitRequest();
                demandSubmitRequestBean.pepole_num = passengersNum;
                demandSubmitRequestBean.begin_time = ""+ DateUtil.getLongTime(startDate,DateUtil.TIME_FORMAT_YMD2);
                demandSubmitRequestBean.request_start_city = startCity;
                demandSubmitRequestBean.end_address = destCity;
                Intent intent = RideListActivity.newIntent(FreeRideActivity.this,
                        passengersNum,startDate,
                        startCity, destCity);
                startActivity(intent);
                EventBus.getDefault().postSticky(demandSubmitRequestBean);
                // startActivity(new Intent(FreeRideActivity.this, RideListActivity.class));
                break;
        }
    }



    private void setStartDate() {
        String nowDate = m_tv_start_date.getText().toString();
        DateChooseWheelViewDialog endDateChooseDialog = new DateChooseWheelViewDialog(FreeRideActivity.this,
                new DateChooseWheelViewDialog.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        showToast(time);
                        m_tv_start_date.setText(time);
                    }
                }, true);
        endDateChooseDialog.setTimePickerGone(true);
        endDateChooseDialog.setDateDialogTitle("出发时间");
        endDateChooseDialog.showDateChooseDialog();
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }

    @NonNull
    private String getToday() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
                + calendar.get(Calendar.DAY_OF_MONTH) + "";
    }

    class CustomEditListener implements OnOperationListener {
        @Override
        public void onLeftClick() {
            customEditDialog.cancel();
        }

        @Override
        public void onRightClick() {
            customEditDialog.cancel();
            m_tv_passenger_num.setText(customEditDialog.getEditText());
        }
    }

    private String getSingleCity(String provinceAndCity){
        String[] strArray = null;
        if (!StringUtils.isEmpty(provinceAndCity)){
            strArray = provinceAndCity.split("-");
            return strArray[1];
        } else {
            return "null";
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**顺风车页面关闭，对数据进行移除*/
       // EventBus.getDefault().removeStickyEvent(RideDemandSubmitRequest.class);
    }
}
