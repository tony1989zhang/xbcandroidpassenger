package com.lvchehui.www.xiangbc.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.view.toast.ToastManager;

/**
 * Created by 张灿能 on 2016/6/6.
 * 作用：说明弹框
 */
public class CustomTextDialog extends Dialog {
    private Context context;
    private TextView m_dialog_tv_text;
    private TextView m_tv_title;
    public CustomTextDialog(Context context) {
        super(context, R.style.custom_dialog);
        this.context = context;
        setContentView(R.layout.dialog_tv);
        m_dialog_tv_text = (TextView) findViewById(R.id.dialog_tv_text);
        m_tv_title = (TextView) findViewById(R.id.tv_title);
    }

    public void setTitle(String title)
    {
        m_tv_title.setText(title);
    }
    public void setCustomText(String text){

        m_dialog_tv_text.setText("" + text);
    }

}
