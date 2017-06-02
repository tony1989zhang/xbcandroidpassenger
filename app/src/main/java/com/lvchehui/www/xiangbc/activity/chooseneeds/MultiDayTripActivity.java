package com.lvchehui.www.xiangbc.activity.chooseneeds;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.base.BaseReqActivity;
import com.lvchehui.www.xiangbc.impl.OnOperationListener;
import com.lvchehui.www.xiangbc.utils.DateUtil;
import com.lvchehui.www.xiangbc.utils.PickersUtil;
import com.lvchehui.www.xiangbc.utils.StringUtils;
import com.lvchehui.www.xiangbc.utils.Utils;
import com.lvchehui.www.xiangbc.utils.XgoLog;
import com.lvchehui.www.xiangbc.view.TitleView;
import com.lvchehui.www.xiangbc.view.dialog.CustomDesDialog;
import com.lvchehui.www.xiangbc.view.dialog.CustomDialog;
import com.lvchehui.www.xiangbc.view.wheelData.DateChooseWheelViewDialog;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import cn.qqtheme.framework.picker.DatePicker;

/**
 * Created by 张灿能 on 2016/6/20.
 * 作用：发布需求-多日用车
 */

@ContentView(R.layout.activity_multi_day)
public class MultiDayTripActivity extends BaseReqActivity {
    private static final String TAG = "MultiDayTripActivity";

    @ViewInject(R.id.title_view)
    private TitleView m_title_view;

    @ViewInject(R.id.tv_start_city)
    private TextView m_tv_start_city;

    @ViewInject(R.id.ll_start_date)
    private LinearLayout m_ll_start_date;

    @ViewInject(R.id.tv_start_date)
    private TextView m_tv_start_date; //选择出发日期;

    @ViewInject(R.id.ll_finish_date)
    private LinearLayout m_ll_finish_date;

    @ViewInject(R.id.tv_finish_date)
    private TextView m_tv_finish_date; //选择结束日期;

    @ViewInject(R.id.ll_intraday_trip_info)
    private LinearLayout m_ll_intraday_trip_info; // 当日用车情况：第一天

    @ViewInject(R.id.tv_intraday_date)
    private TextView m_tv_intraday_date;

    @ViewInject(R.id.tv_intraday_trip_info)
    private TextView m_tv_intraday_trip_info;

    @ViewInject(R.id.ll_multi_day_trip_info)
    private LinearLayout m_ll_multi_day_trip_info; // 后续用车情况：第二到第n天

    @ViewInject(R.id.rl_root)
    private RelativeLayout m_rl_root;

//    private ArrayList<HashMap<String, String>> mTripInfo;

    private HashMap<String,String> mTripInfo;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(m_title_view, getResources().getString(R.string.mutil_day_trip));
        // 默认出发日期为当天
        // m_tv_start_date.getText().toString() = DateUtil.getToday();
        // m_tv_intraday_date.setText(DateUtil.getToday());
        m_tv_intraday_date.setText(DateUtil.fetchMonthDay(DateUtil.getToday()));

//        m_tv_start_time.setText(DateUtil.refFormatNowDate());
//        m_tv_finish_time.setText(DateUtil.refFormatNowDate());
    }

    @Event(value = {R.id.ll_start_city, R.id.ll_start_date, R.id.ll_finish_date, R.id.ll_intraday_trip_info},
            type = View.OnClickListener.class)
    private void multiDayOnClick(View v) {
        DatePicker picker = new DatePicker(MultiDayTripActivity.this, DatePicker.YEAR_MONTH_DAY);
        Calendar calendar = Calendar.getInstance();
        switch (v.getId()) {
            case R.id.ll_start_city:
                PickersUtil.getInstance().pickCity(MultiDayTripActivity.this, m_tv_start_city);
                break;
            case R.id.ll_start_date:
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
                        if (m_tv_finish_date.getText().toString() != null){
                            checkMultiDay(m_tv_start_date.getText().toString(), m_tv_finish_date.getText().toString());
                        } else {
                            showToast("请选择结束日期");
                        }
                    }
                });
                picker.show();
                break;
            case R.id.ll_finish_date:
                // 结束日期选择范围上限同出发日期
                picker.setRangeStart(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH) + 1,
                        calendar.get(Calendar.DAY_OF_MONTH)
                );

                // 结束日期选择范围下限设置为下一年的12月31日。
                picker.setRangeEnd(
                        calendar.get(Calendar.YEAR) + 1,
                        12,
                        31
                );
                picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener(){
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        m_tv_finish_date.setText(year + "-" + month + "-" + day);
                        if (m_tv_start_date.getText().toString() != null){
                            checkMultiDay(m_tv_start_date.getText().toString(), m_tv_finish_date.getText().toString());
                        } else {
                            showToast("请选择出发日期");
                        }
                    }
                });
                picker.show();
                break;
            case R.id.ll_intraday_trip_info:
                CustomDesDialog customDesDialog = new CustomDesDialog(MultiDayTripActivity.this);
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
        }
    }

    private void checkMultiDay(String startDate, String finshDate) {

        // 判断日期是否已经选择，将出发日期默认设置为当前日期
        m_ll_multi_day_trip_info.removeAllViews();

        if (StringUtils.isEmpty(startDate) || StringUtils.isEmpty(finshDate)) {
            //m_tv_start_date.setText(DateUtil.refFormatNowDate());
            showToast("请先选择日期");
            return;
        }


        // 判断出发日期是否小于当前日期
        if (DateUtil.getLongTime(m_tv_start_date.getText().toString()) < DateUtil.getLongTime(DateUtil.refFormatNowDate())) {
            //m_tv_start_date.setText(DateUtil.refFormatNowDate());
            showToast("出发日期不能小于当前日期");
            return;
        }

        // 结束日期是否小于当前日期
        if (DateUtil.getLongTime(finshDate) < DateUtil.getLongTime(DateUtil.refFormatNowDate())) {
            //m_tv_finish_date.setText(DateUtil.refFormatNowDate());
            showToast("结束日期不能小于当前日期");
            return;
        }

        // 结束日期是否小于开始日期
        if (DateUtil.getLongTime(finshDate) < DateUtil.getLongTime(startDate)) {
            showToast("结束日期不能小于出发日期");
            //m_tv_finish_date.setText(DateUtil.refFormatNowDate());
            return;
        }

        int daysBetween = DateUtil.calDaysBetween(DateUtil.strToDateLong(startDate), DateUtil.strToDateLong(finshDate));
        if (daysBetween > 31) {
            final CustomDialog customDialog = new CustomDialog(this);
            customDialog.setTitle("警告");
            customDialog.setMessage("长时间用车,请联系客服400-885566222");
            customDialog.setButtonsText("取消", "确认");
            customDialog.show();
            customDialog.setOperationListener(new OnOperationListener() {
                @Override
                public void onLeftClick() {
                    customDialog.dismiss();
                }

                @Override
                public void onRightClick() {
                    Utils.call(MultiDayTripActivity.this, "15859254561");
                    customDialog.dismiss();
                }
            });
            return;
        }

        if (null == mTripInfo) {
//            mTripInfo = new ArrayList<>();
            mTripInfo = new HashMap<>();
        } else {
            mTripInfo.clear();
        }

        for (int i = 0; i < daysBetween; i++) {
            final String intradayDateTitle = generateIntradayDate(i + 1);
            addIntradayInfoLl(intradayDateTitle);
            Log.d(TAG, "call addIntradayInfoLl()....");
        }
    }

    /*private void checkMultiDay() {
        //判断选择的时间是否小于当前的的日期，结束时间是否小于开始时间
        m_ll_multi_day_trip_info.removeAllViews();
        if (StringUtils.isEmpty(m_tv_start_date.getText().toString()) || StringUtils.isEmpty(m_tv_finish_date.getText().toString())) {
            m_tv_start_date.setText(DateUtil.refFormatNowDate());
            showToast("请先选择日期");
            return;
        }
        if (DateUtil.getLongTime(m_tv_start_date.getText().toString()) < DateUtil.getLongTime(DateUtil.refFormatNowDate())) {
            m_tv_start_date.setText(DateUtil.refFormatNowDate());
            showToast("开始时间不能小于当前时间");
            return;
        }
        if (DateUtil.getLongTime(m_tv_finish_date.getText().toString()) < DateUtil.getLongTime(DateUtil.refFormatNowDate())) {
            m_tv_finish_date.setText(DateUtil.refFormatNowDate());
            showToast("结束时间不能小于当前时间");
            return;
        }

        if (DateUtil.getLongTime(m_tv_finish_date.getText().toString()) < DateUtil.getLongTime(m_tv_start_date.getText().toString())) {
            showToast("结束时间不能小于开始时间");
            m_tv_finish_date.setText(DateUtil.refFormatNowDate());
            return;
        }
        int daysBetween = DateUtil.calDaysBetween(DateUtil.strToDateLong(m_tv_start_date.getText().toString()), DateUtil.strToDateLong(m_tv_finish_date.getText().toString()));
        if (daysBetween > 31) {
            final CustomDialog customDialog = new CustomDialog(this);
            customDialog.setTitle("警告");
            customDialog.setMessage("长时间用车,请联系客服400-885566222");
            customDialog.setButtonsText("取消", "确认");
            customDialog.show();
            customDialog.setOperationListener(new OnOperationListener() {
                @Override
                public void onLeftClick() {
                    customDialog.dismiss();
                }

                @Override
                public void onRightClick() {
                    Utils.call(MultiDayTripActivity.this, "15859254561");
                    customDialog.dismiss();
                }
            });
            return;
        }
        if (null == mTripInfo) {
            mTripInfo = new ArrayList<>();
        } else {
            mTripInfo.clear();
        }
        for (int i = 0; i < daysBetween; i++) {
            final String titleStr = generateIntradayDate(i);
            addIntradayInfoLl(titleStr);
        }
    }*/

    /**
     * 根据i生成多日用车的每日用车情况的时间
     *
     * @param i
     * @return
     */
    @NonNull
    private String generateIntradayDate(int i) {
        /*String str = m_tv_start_date.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, i);
        return sdf.format(calendar.getTime());*/
        Date date = DateUtil.strToDateLong(m_tv_start_date.getText().toString());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, i);
        return new SimpleDateFormat(DateUtil.YEAR_MONTH_DAY_FORMAT).format(calendar.getTime());
    }

    private void addIntradayInfoLl(final String dateStr) {
        LinearLayout ll_mutilday_trip_singleday_info = (LinearLayout) getLayoutInflater()
                .inflate(R.layout.ll_mutilday_trip_singleday_info, null);
        TextView tv_mutilday_trip_singleday_date = (TextView) ll_mutilday_trip_singleday_info
                .findViewById(R.id.tv_mutilday_trip_singleday_date);
        final TextView tv_mutilday_trip_singleday_info = (TextView) ll_mutilday_trip_singleday_info
                .findViewById(R.id.tv_mutilday_trip_singleday_info);

        String monthDay = DateUtil.fetchMonthDay(dateStr);
        tv_mutilday_trip_singleday_date.setText(monthDay);

        ll_mutilday_trip_singleday_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDesDialog customDesDialog = new CustomDesDialog(MultiDayTripActivity.this);
                customDesDialog.setTitle(getResources().getString(R.string.trip_detail_info), dateStr);
                customDesDialog.setOnCustomDesDialogListener(new CustomDesDialog.OnCustomDesDialogListener() {
                    @Override
                    public void setDESText(String text) {
                        tv_mutilday_trip_singleday_info.setText(text);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(tv_mutilday_trip_singleday_info, InputMethodManager.SHOW_FORCED);
                     //   HashMap<String, String> hashMaps = new HashMap<>();
                        mTripInfo.put(dateStr, tv_mutilday_trip_singleday_info.getText().toString());
                       // mTripInfo.add(hashMaps);
                    }
                });
                customDesDialog.show();
            }
        });

        m_ll_multi_day_trip_info.addView(ll_mutilday_trip_singleday_info);

        if (StringUtils.isEmpty(tv_mutilday_trip_singleday_info.getText().toString())){
            tv_mutilday_trip_singleday_info.setText("无");
        }
//        HashMap<String, String> hashMaps = new HashMap<>();
//        hashMaps.put(dateStr, tv_mutilday_trip_singleday_info.getText().toString());
//        mTripInfo.add(hashMaps);
    }

    /*private void addIntradayInfoLl(final String titleStr) {

        final LinearLayout m_multi_use_day = (LinearLayout) getLayoutInflater().inflate(R.layout.item_multi_use_day, null);
        final TextView m_tv_date_content = (TextView) m_multi_use_day.findViewById(R.id.tv_date_content);
        m_tv_date_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDesDialog customDesDialog = new CustomDesDialog(MultiDayTripActivity.this);
                customDesDialog.setTitle("详细行程", titleStr);
                customDesDialog.setOnCustomDesDialogListener(new CustomDesDialog.OnCustomDesDialogListener() {
                    @Override
                    public void setDESText(String text) {
                        m_tv_date_content.setText(text);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(m_tv_date_content, InputMethodManager.SHOW_FORCED);
                    }
                });
                customDesDialog.show();
            }
        });
        TextView m_tv_date_des = (TextView) m_multi_use_day.findViewById(R.id.tv_date_des);
        m_tv_date_des.setText("" + titleStr);
        m_ll_multi_day_trip_info.addView(m_multi_use_day);

//        if (StringUtils.isEmpty(m_tv_date_content.getText().toString())){
//            m_tv_date_content.setText("无");
//        }
        HashMap<String, String> hashMaps = new HashMap<>();
        hashMaps.put(titleStr, m_tv_date_content.getText().toString());
        mTripInfo.add(hashMaps);
    }*/

    private void setDataTimeDialog(Context context, String title, final TextView tv, boolean timePickerGone) {
        DateChooseWheelViewDialog startDateChooseDialog = new DateChooseWheelViewDialog(context, new DateChooseWheelViewDialog.DateChooseInterface() {
            @Override
            public void getDateTime(String time, boolean longTimeChecked) {
                tv.setText(time);
                checkMultiDay(m_tv_start_date.getText().toString(), m_tv_finish_date.getText().toString());
            }
        }, true);

        startDateChooseDialog.setDateDialogTitle(title);
        startDateChooseDialog.setTimePickerGone(timePickerGone);
        startDateChooseDialog.showDateChooseDialog();
    }


    class useTripBean {

        useTripBean(String dateStr, String des) {
            this.dateStr = dateStr;
            this.des = des;

        }

        String dateStr;
        String des;
    }

    @Override
    public String getPassEngErDetail() {
        return "多日用车旅客信息";
    }

    @Override
    public String getTravelDetail() {
        return "多日用车行程信息";
    }

    @Override
    public void submitDingD() {
        //添加提交的信息
        if (!StringUtils.isEmpty(m_tv_start_city.getText().toString())) {
            submitRequestBean.begin_address = m_tv_start_city.getText().toString();
        }

        if (!StringUtils.isEmpty(m_tv_start_date.getText().toString())) {
            submitRequestBean.begin_time = DateUtil.getSubLongStr("" + DateUtil.getLongTime(m_tv_start_date.getText().toString()));
        }
        if (!StringUtils.isEmpty(m_tv_finish_date.getText().toString())) {
            submitRequestBean.end_time = DateUtil.getSubLongStr("" + DateUtil.getLongTime(m_tv_finish_date.getText().toString()));
        }
        submitRequestBean.use_type = "" + 4;
        submitRequestBean.use_trip = new Gson().toJson(mTripInfo);
        XgoLog.e("  submitRequestBean.use_trip:" + submitRequestBean.use_trip);
        super.submitDingD();
    }

    /**
     * 暂时性使用该参数
     * */
//    public String addUseTrip(ArrayList<useTripBean> dateStrs){
//        String toJson = new Gson().toJson(dateStrs);
//        XgoLog.e("toJson:" + toJson);
//        String useTrips = "";
//        for (String s:dateStrs) {
//            useTrips += s + ",";
//        }
//        String substring = useTrips.substring(0, useTrips.length() - 1);
//        useTrips = "{" + substring +"}";
//        XgoLog.e("useTrips:" + useTrips);
//        return toJson;
//    }
}
