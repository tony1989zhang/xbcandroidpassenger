package com.lvchehui.www.xiangbc.bean;

import java.io.Serializable;

public class Result<T> implements Serializable{
    public int errCode;
    public String resMsg;
    public T resData;
}
