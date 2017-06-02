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
package com.lvchehui.www.xiangbc.view;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View.MeasureSpec;
import android.widget.ListView;

/**
 * @ClassName: HalfListView
 * @Description: TODO
 * @author 4evercai
 * @date 2014年9月4日 下午2:23:49
 *
 */

public class HalfListView extends ListView{
	private int MAX_HEIGHT = 300;
	public HalfListView(Context context) {
		super(context);
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getResources().getDisplayMetrics();
		MAX_HEIGHT  = dm.heightPixels / 2;
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    heightMeasureSpec = MeasureSpec.makeMeasureSpec(MAX_HEIGHT, MeasureSpec.AT_MOST);
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
