package com.lvchehui.www.xiangbc.bean;

import java.io.Serializable;
// implements Serializable
public class BaseBean implements Serializable{
	public int errCode;
	public String resMsg;

	@Override
	public String toString() {
		return "BaseBean{" +
				"errCode=" + errCode +
				", resMsg='" + resMsg + '\'' +
				'}';
	}
}
