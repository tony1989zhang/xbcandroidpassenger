package com.lvchehui.www.xiangbc.utils.evenbus;

/**
 * Created by 张灿能 on 2016/7/22.
 * 作用：传递城市
 */
public class CityEvent {
    private String mCity;
    public CityEvent(String city){
        this.mCity = city;
    }

    public String getCity(){
        return mCity;
    }
}
