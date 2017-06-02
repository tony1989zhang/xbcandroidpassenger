package com.lvchehui.www.xiangbc.activity.chooseneeds;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.base.BaseReqActivity;
import com.lvchehui.www.xiangbc.utils.DateUtil;
import com.lvchehui.www.xiangbc.utils.PickersUtil;
import com.lvchehui.www.xiangbc.utils.StringUtils;
import com.lvchehui.www.xiangbc.view.TitleView;
import com.lvchehui.www.xiangbc.view.dialog.CustomDesDialog;
import com.lvchehui.www.xiangbc.view.wheelData.DateChooseWheelViewDialog;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Calendar;

import cn.qqtheme.framework.picker.DatePicker;

/**
 * Created by 张灿能 on 2016/6/21.
 * 作用：发布需求---单日用车
 */
@ContentView(R.layout.activity_one_day_trip)
public class OneDayTripActivity extends BaseReqActivity {

    @ViewInject(R.id.title_view)
    private TitleView m_title_view;

    @ViewInject(R.id.tv_start_city)
    private TextView m_tv_start_city;

    @ViewInject(R.id.ll_start_date)
    private LinearLayout m_ll_start_date;

    @ViewInject(R.id.tv_start_date)
    private TextView m_tv_start_date;

    @ViewInject(R.id.ll_intraday_trip_info)
    private LinearLayout m_ll_intraday_trip_info;

    @ViewInject(R.id.tv_intraday_date)
    private TextView m_tv_intraday_date;

    @ViewInject(R.id.tv_intraday_trip_info)
    private TextView m_tv_intraday_trip_info;

    /*@ViewInject(R.id.ll_dest_city)
    private View include_des;*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(m_title_view, "单日用车");
        m_tv_intraday_date.setText(DateUtil.fetchMonthDay(DateUtil.getToday()));
        // m_tv_intraday_date.setVisibility(View.GONE);
        // include_des.setVisibility(View.GONE);
    }

    @Event(value = {R.id.ll_start_city, R.id.ll_start_date, R.id.ll_intraday_trip_info},
            type = View.OnClickListener.class)
    private void oneDayCarOnClick(View view) {
        switch (view.getId()) {
            case R.id.ll_start_city:
                PickersUtil.getInstance().pickCity(OneDayTripActivity.this, m_tv_start_city);
                break;
            case R.id.ll_start_date:
                /*PickersUtil.getInstance().pickDate(OneDayTripActivity.this, m_tv_start_date);*/
                DatePicker picker = new DatePicker(this, DatePicker.YEAR_MONTH_DAY);
                Calendar calendar = Calendar.getInstance();
                // 默认日期选择范围从当天开始，只能往后选
                picker.setRangeStart(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH) + 1,
                        calendar.get(Calendar.DAY_OF_MONTH)
                );

                // 出发日期的上限设置为下一年的12月31日。
                picker.setRangeEnd(
                        calendar.get(Calendar.YEAR) + 1,
                        12,
                        31
                );

                picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        m_tv_start_date.setText(year + "-" + month + "-" + day);
                        String monthDay = DateUtil.fetchMonthDay(m_tv_start_date.getText().toString());
                        m_tv_intraday_date.setText(monthDay);
                    }
                });
                picker.show();
                break;
            case R.id.ll_intraday_trip_info:
                // 跳转到详细行程页面
                CustomDesDialog customDesDialog = new CustomDesDialog(OneDayTripActivity.this);
                customDesDialog.setTitle(getResources().getString(R.string.trip_detail_info), m_tv_intraday_date.getText().toString());
                customDesDialog.setOnCustomDesDialogListener(new CustomDesDialog.OnCustomDesDialogListener() {
                    @Override
                    public void setDESText(String text) {
                        m_tv_intraday_trip_info.setText(text);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(m_tv_intraday_trip_info, InputMethodManager.SHOW_FORCED);
                    }
                });
                customDesDialog.show();
                break;
            /*case R.id.tv_date_content:
                CustomDesDialog customDesDialog = new CustomDesDialog(OneDayTripActivity.this);
                customDesDialog.setTitle("详细行程", m_tv_start_date.getText().toString());
                customDesDialog.setOnCustomDesDialogListener(new CustomDesDialog.OnCustomDesDialogListener() {
                    @Override
                    public void setDESText(String text) {
                        m_tv_intraday_trip_info.setText(text);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    }
                });
                customDesDialog.show();
                break;*/
        }
    }

    private void setDataTimeDialog(Context context, String title, final TextView tv, boolean timePickerGone) {
        DateChooseWheelViewDialog startDateChooseDialog = new DateChooseWheelViewDialog(context, new DateChooseWheelViewDialog.DateChooseInterface() {
            @Override
            public void getDateTime(String time, boolean longTimeChecked) {
                chooseDataTime(time, tv);
            }
        }, true);

        startDateChooseDialog.setDateDialogTitle(title);
        startDateChooseDialog.setTimePickerGone(timePickerGone);
        startDateChooseDialog.showDateChooseDialog();
    }

    private void chooseDataTime(String time, TextView tv) {
        tv.setText(time);
        //include_des.setVisibility(View.VISIBLE);
//        if (StringUtils.isEmpty(m_tv_start_date.getText().toString())) {
//            m_tv_start_date.setText(DateUtil.refFormatNowDate());
//            return;
//        }
        if (DateUtil.getLongTime(m_tv_start_date.getText().toString()) < DateUtil.getLongTime(DateUtil.refFormatNowDate())) {
            m_tv_start_date.setText(DateUtil.refFormatNowDate());
            showToast("开始时间不能小于当前时间");
            return;
        }
    }

    @Override
    public String getTravelDetail() {
        return "单日用车提交订单_行程信息";
    }

    @Override
    public String getPassEngErDetail() {
        return "单日用车提交订单_旅客信息";
    }

    @Override
    public void submitDingD() {
        if (!StringUtils.isEmpty(m_tv_start_city.getText().toString())) {
            submitRequestBean.begin_address = m_tv_start_city.getText().toString();
        }

        if (!StringUtils.isEmpty(m_tv_start_date.getText().toString())) {
            submitRequestBean.begin_time = DateUtil.getSubLongStr("" + DateUtil.getLongTime(m_tv_start_date.getText().toString()));
        }
        submitRequestBean.use_type = "" + 3;
//        addUseTrip("07-15:背景","07-16:上海","07-17：深证");
        super.submitDingD();
    }


}
