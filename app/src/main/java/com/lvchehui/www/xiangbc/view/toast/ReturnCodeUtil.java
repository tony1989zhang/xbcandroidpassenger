package com.lvchehui.www.xiangbc.view.toast;

import android.content.Context;

import com.lvchehui.www.xiangbc.app.App;

/**
 * @author 张灿能
 * 返回状态码
 */
public class ReturnCodeUtil {

	private static void showNoticeToast(int stringResId) {
		if (stringResId == 0) {
			ToastManager.getManager().show("操作失败");
		} else {
			ToastManager.getManager().show(stringResId);
		}

	}

	public static void showToast(int code) {
		Context context = App.getInstance();
		int stringResId = context.getResources().getIdentifier("return_code_" + code,
				"string", "com.wbaiju.ichat");
		showNoticeToast(stringResId);
	}

	public static void showToast(String code) {
		Context context = App.getInstance();
		int stringResId = context.getResources().getIdentifier("return_code_" + code,
				"string", "com.lvchehui.www");
		showNoticeToast(stringResId);
	}

	

}
