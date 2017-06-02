package com.lvchehui.www.xiangbc.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 张灿能 on 2016/6/6.
 * 作用：特征码红包分享共用一个bean
 */
public class RedpacketShareDataBean {
    @SerializedName("code")
    public String redPacketCode;

    @Override
    public String toString() {
        return "RedpacketShareDataBean{" +
                "redPacketCode='" + redPacketCode + '\'' +
                '}';
    }
}
