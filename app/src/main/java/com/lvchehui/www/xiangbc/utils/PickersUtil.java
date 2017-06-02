package com.lvchehui.www.xiangbc.utils;

import android.app.Activity;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lvchehui.www.xiangbc.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import cn.qqtheme.framework.picker.AddressPicker;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.DateTimePicker;
import cn.qqtheme.framework.picker.OptionPicker;

/**
 * Created by tendoasan on 2016/8/29.
 * 选择器工具类
 */
public class PickersUtil {
    private static final String TAG = "PickersUtil";
    private static final String NORMAL_DATE_FORMAT = "normal_date_format";
    public static final String BIRTHDAY_DATE_FORMAT = "birthday_date_format";

    // 格式：年－月－日
    public static final String LONG_DATE_FORMAT = "yyyy-MM-dd";

    private static PickersUtil sPickersUtil;

    private PickersUtil() {
    }

    public static PickersUtil getInstance(){
        if (null == sPickersUtil){
            sPickersUtil = new PickersUtil();
        }

        return sPickersUtil;
    }

    public void pickGender(Activity context, final TextView tv){
        String[] genders = context.getResources().getStringArray(R.array.genders);
        OptionPicker picker = new OptionPicker(context, genders);
        // picker.setOffset(2);
        picker.setSelectedIndex(0);
        picker.setTextSize(18);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int position, String option) {
                tv.setText(option);
            }
        });
        picker.show();
    }

    public void pickDate(Activity context, final TextView tv){
        this.pickDate(context, tv, NORMAL_DATE_FORMAT);
    }

    /**
     * 选择日期
     * @param context
     * @param tv
     * @param formatFlag
     */
    public void pickDate(Activity context, final TextView tv, String formatFlag){
        DatePicker picker = new DatePicker(context, DatePicker.YEAR_MONTH_DAY);
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        final int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        if(formatFlag == NORMAL_DATE_FORMAT){
            picker.setRangeStart(currentYear, currentMonth, currentDay);
            picker.setRangeEnd(currentYear + 1, currentMonth, currentDay);
        } else {
            picker.setRangeStart(currentYear, currentMonth, currentDay);
            picker.setRangeEnd(currentYear - 80, currentMonth, currentDay);
        }

        picker.setSelectedItem(currentYear, currentMonth, currentDay);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                tv.setText(year + "-" + month + "-" + day);
            }
        });
        picker.show();
    }

    public void pickTime(Activity context, final TextView tv) {
        DateTimePicker picker = new DateTimePicker(context, DateTimePicker.YEAR_MONTH_DAY);
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);

        // 年份范围
        picker.setRange(currentYear, currentYear + 1);
        picker.setSelectedItem(currentYear, currentMonth, currentDay, currentHour, currentMinute);
        picker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
            @Override
            public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
                tv.setText(year + "-" + month + "-" + day + " " + hour + ":" + minute);
            }
        });

        picker.show();
    }

    public void pickCity(Activity context, final TextView tv){
        ArrayList<AddressPicker.Province> data = new ArrayList<>();
        String json = AssetsUtils.readText(context, "city.json");
        data.addAll(JSON.parseArray(json, AddressPicker.Province.class));
        AddressPicker picker = new AddressPicker(context, data);
        picker.setSelectedItem("福建", "厦门");
        // picker.setHideProvince(true); //加上此句举将只显示地级及县级
        picker.setHideCounty(true); //加上此句举将只显示省级及地级
        picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
            @Override
            public void onAddressPicked(AddressPicker.Province province, AddressPicker.City city, AddressPicker.County county) {
                tv.setText(province.toString().split("=")[2] + "-" + city.toString().split("=")[2]);
            }
        });
        picker.show();
    }

    public void pickArea(Activity context, final TextView tv) {
        ArrayList<AddressPicker.Province> data = new ArrayList<>();
        String json = AssetsUtils.readText(context, "city.json");
        data.addAll(JSON.parseArray(json, AddressPicker.Province.class));
        AddressPicker picker = new AddressPicker(context, data);
        picker.setSelectedItem("福建", "厦门", "集美");
        // picker.setHideProvince(true); //加上此句举将只显示地级及县级
        // picker.setHideCounty(true); //加上此句举将只显示省级及地级
        picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
            @Override
            public void onAddressPicked(AddressPicker.Province province, AddressPicker.City city, AddressPicker.County county) {
                tv.setText(province.toString().split("=")[2] + "-" +
                        city.toString().split("=")[2] + "-" +
                        county.toString().split("=")[2]);
            }
        });
        picker.show();
    }

    public static String dateToString(java.util.Date date, String format) {
        String result = "";
        SimpleDateFormat formater = new SimpleDateFormat(format);
        try {
            result = formater.format(date);
        } catch (Exception e) {
            // log.error(e);
        }
        return result;
    }

    public static Integer[] stringToDateIntArray(String yyyyMMdd){
        Integer[] dateIntArray = new Integer[3];
        String[] dateStringArray = yyyyMMdd.split("-");
        for (int i = 0; i < 3; i++){
            dateIntArray[i] = Integer.valueOf(dateStringArray[i]);
        }
        return dateIntArray;
    }

    public static Integer[] stringToTimeIntArray(String yyyyMMddHHmm){
        Integer[] timeIntArray = new Integer[5];
        String[] timeStringArray = yyyyMMddHHmm.split("-");
        for (int i = 0; i < 5; i++) {
            timeIntArray[i] = Integer.valueOf(timeStringArray[i]);
        }
        return timeIntArray;
    }
}
