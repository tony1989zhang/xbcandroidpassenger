/**
 * @Package com.loftsex.www.bean 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 张灿能
 * @date Dec 15, 2015 4:48:20 PM 
 * @version V1.0  
 */
package com.lvchehui.www.xiangbc.view.NodeListView;

/** 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author 张灿能
* @date Dec 15, 2015 4:48:20 PM 
*/
public class LogisticsInfo {

	public String time;
	public String context;
	public String ftime;
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getFtime() {
		return ftime;
	}
	public void setFtime(String ftime) {
		this.ftime = ftime;
	}
	@Override
	public String toString() {
		return "LogisticsInfo [time=" + time + ", context=" + context + ", ftime=" + ftime + "]";
	}
	
}
