package com.lvchehui.www.xiangbc.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.utils.PixelUtil;

import org.xutils.x;

/**
 * Created by 张灿能 on 2016/5/23.
 * 作用：邮箱弹框
 */
public class EmailDialog extends Dialog {
    private Context context;
    private View m_dialog_email;

    private ImageView m_iv_email;

    private TextView m_change_tv; //@string/dialog_email_account;

    private TextView m_email_ac_tv; //@string/dialog_email_change_tv;

    private Button m_btn_ok; //更换邮箱;


    public EmailDialog(Context context) {
        super(context, R.style.custom_full_dialog);
        this.context = context;
        setContentView(R.layout.dialog_email);
        m_dialog_email = findViewById(R.id.dialog_email);
        m_iv_email = (ImageView) findViewById(R.id.iv_email);
        m_change_tv = (TextView) findViewById(R.id.change_tv);
        m_email_ac_tv = (TextView) findViewById(R.id.email_ac_tv);
        m_btn_ok = (Button) findViewById(R.id.btn_ok);
        m_dialog_email.setOnClickListener(new MyDialogOnClickListener());
    }

    public void setEmailParamsAndOnClick(final EmailOnClickListener ec) {
        m_btn_ok.setOnClickListener(new MyDialogOnClickListener(ec));
        ec.setEmailParams(m_iv_email, m_email_ac_tv);
        show();
    }

    public void setSmailTitle(String text){
        m_change_tv.setText(text);
    }
    private class MyDialogOnClickListener implements View.OnClickListener {
        public MyDialogOnClickListener() {

        }

        EmailOnClickListener ec;

        public MyDialogOnClickListener(EmailOnClickListener ec) {
            this.ec = ec;

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.dialog_email:
                    EmailDialog.this.dismiss();
                    break;
                case R.id.btn_ok:
                    ec.btnOnClick();
                    EmailDialog.this.dismiss();
                    break;
            }
        }


    }

    public interface EmailOnClickListener {
        void btnOnClick();

        void setEmailParams(ImageView iv, TextView emailAc);
    }
}
