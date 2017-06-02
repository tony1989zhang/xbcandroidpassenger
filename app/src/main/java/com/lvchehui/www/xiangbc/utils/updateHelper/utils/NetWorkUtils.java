package com.lvchehui.www.xiangbc.utils.updateHelper.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by 张灿能 on 16-5-6.
 * 作用：判断网络情况
 */
public class NetWorkUtils {
	
	private Context mContext;
	private NetworkInfo networkInfo;
	
	public NetWorkUtils(Context context) {
		this.mContext = context;
		ConnectivityManager connectivityManager = (ConnectivityManager) this.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		networkInfo = connectivityManager.getActiveNetworkInfo();
	}
	
	public boolean isConnected(){
		return networkInfo != null && networkInfo.isConnected();				
	}

	/**
	 * 
	 * @return 0: 无网络， 1:WIFI， 2:其他（流量）
	 */
	public int getNetType(){
		if (!isConnected()) {
			return 0;
		}
		int type = networkInfo.getType();
		if (type == ConnectivityManager.TYPE_WIFI) {
			return 1;
		}else {
			return 2;
		} 					
	}

}