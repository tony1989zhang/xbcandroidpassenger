//			                  _oo0oo_
//			                 o8888888o
//			                 88" . "88
//			                 (| -_- |)
//						     0\  =  /0
//						   ___/`___'\___
//						 .' \\|     |// '.
//						/ \\|||  :  |||// \
//					   / _||||| -:- |||||_ \
//					  |   | \\\  _  /// |   |
//					  | \_|  ''\___/''  |_/ |
//					  \  .-\__  '_'  __/-.  /
//					___'. .'  /--.--\  '. .'___
//				  ."" '<  .___\_<|>_/___. '>' "".
//			   | | :  `_ \`.;` \ _ / `;.`/ - ` : | |
//			   \ \  `_.   \_ ___\ /___ _/   ._`  / /
//			====`-.____` .__ \_______/ __. -` ___.`====
//							 `=-----='
//         ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//                    佛祖保佑           永无BUG
//                   
//                     Power   By    4evercai
//         ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
package com.lvchehui.www.xiangbc.view.dialog;


import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.lvchehui.www.xiangbc.R;

/**
 * @Description: 多项的选择
 * @author 张灿能 修改
 * @date 2016-06-06
 *
 */

public class ListDialog extends Dialog {

	private LinearLayout layoutContent;

	public ListDialog(Context context, View view, View.OnClickListener listener) {
		super(context, R.style.custom_dialog);
		initView(context, view);
		findViewById(R.id.btnLayout).setVisibility(View.VISIBLE);
		findViewById(R.id.dialogListConfirmBtn).setOnClickListener(listener);
	}

	public ListDialog(Context context, View view) {
		super(context, R.style.custom_dialog);
		initView(context, view);
	}

	/**
	 * 更改主题类型
	 * */
	public ListDialog(Context context, View view,int themeResId) {
		super(context,themeResId);
		initView(context, view);
	}


	public void initView(Context context, View view) {
		setContentView(R.layout.dialog_list);
		layoutContent = (LinearLayout) findViewById(R.id.dialog_layout_content);
		layoutContent.addView(view);
		findViewById(R.id.iv_dialog_close).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ListDialog.this.dismiss();
			}
		});
		
		/*DisplayMetrics dm = new DisplayMetrics();
		dm = context.getResources().getDisplayMetrics();
		int screenHeight = dm.heightPixels;
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metrics);
		if (metrics.heightPixels < screenHeight / 2) {
			this.getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		} else {
			this.getWindow().setLayout(LayoutParams.WRAP_CONTENT, screenHeight / 2);
		}*/
	}
}
