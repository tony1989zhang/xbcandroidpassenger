package com.lvchehui.www.xiangbc.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.view.TitleView;

/**
 * Created by 张灿能 on 2016/6/20.
 * 作用：描述保存取消弹框
 */
public class CustomDesDialog extends Dialog implements View.OnClickListener{
    private static final String BTN_CANCEL = "取消";
    private static final String BTN_CONFIRM = "确定";

    private OnCustomDesDialogListener onCustomDesDialogListener;
    private TitleView m_title_view;
    private TextView m_tv_title_smail;
    private EditText m_et_des;
    private Context context;
    private InputMethodManager imm;
    public CustomDesDialog(Context context) {
        super(context,R.style.custom_full_dialog_zoom);
        setContentView(R.layout.dialog_custom_des);
        this.context = context;
        m_title_view = (TitleView) findViewById(R.id.title_view);
        /* 不能用获取资源的方法获取字符串？？？？？
        m_title_view.setTitleLeftText(getOwnerActivity().getString(R.string.cancel));
        m_title_view.setTitleRightText(getOwnerActivity().getString(R.string.confirm));
        */
        m_title_view.setTitleLeftText(BTN_CANCEL);
        m_title_view.setTitleRightText(BTN_CONFIRM);
        m_title_view.setTitleRightOnClickListener(this);
        m_title_view.findViewById(R.id.title_left_tv).setOnClickListener(this);
        m_tv_title_smail = (TextView) findViewById(R.id.tv_title_smail);
        m_et_des = (EditText) findViewById(R.id.et_des);
    }

    public void setOnCustomDesDialogListener(OnCustomDesDialogListener onCustomDesDialogListener){
        this.onCustomDesDialogListener = onCustomDesDialogListener;
        if (null == imm) {
            imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        boolean isOpen = imm.isActive();
        if (!isOpen){
            imm.showSoftInput(m_et_des,InputMethodManager.SHOW_FORCED);
        }

    }

    public void setTitle(String title,String smailTitle){
        m_title_view.setTitle(title);
        m_tv_title_smail.setText(smailTitle);
    }
    @Override
    public void onClick(View v) {
        if (null == imm) {
            imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        boolean isOpen = imm.isActive();
        switch (v.getId()){
            case R.id.title_left_tv:

                if (isOpen){
                    imm.hideSoftInputFromWindow(m_et_des.getWindowToken(),InputMethodManager.SHOW_FORCED);
                }
                dismiss();
                break;
            case R.id.title_right_tv:
                if (null == onCustomDesDialogListener){
                    dismiss();
                }else {
                    String etDes = m_et_des.getText().toString();
                    onCustomDesDialogListener.setDESText(etDes);

                    if (isOpen){
                        imm.hideSoftInputFromWindow(m_et_des.getWindowToken(), InputMethodManager.SHOW_FORCED);
                    }
//                    if(getWindow().getAttributes().softInputMode== WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED)
//                    {
//                    //隐藏软键盘
//                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//                    }
                    dismiss();
                }
                break;
        }
    }

    public interface OnCustomDesDialogListener{
        void setDESText(String text);
    }
}
