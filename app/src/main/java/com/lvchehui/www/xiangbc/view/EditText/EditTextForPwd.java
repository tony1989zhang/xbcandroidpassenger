package com.lvchehui.www.xiangbc.view.EditText;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.lvchehui.www.xiangbc.R;

/**
 * 作用：自定义密码文本输入框，设置隐藏或显示密码
 */
public class EditTextForPwd extends EditText {
    private Drawable imageOn, imageOff;
    private Context mContext;
    private boolean isHide = true;
    private int drawableWidth;

    public EditTextForPwd(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public EditTextForPwd(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    public EditTextForPwd(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        imageOn = mContext.getResources().getDrawable(R.mipmap.switch_on);
        imageOff = mContext.getResources().getDrawable(R.mipmap.switch_off);
        setDrawable(true);
        //drawableWidth = PixelUtil.dp2px(getContext(), 50);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.switch_on);
        drawableWidth = bm.getWidth();
        try {
            bm.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置密码显示/隐藏的图片
     *
     * @param isHide
     */
    private void setDrawable(boolean isHide) {
        if (isHide) {
            setCompoundDrawablesWithIntrinsicBounds(null, null, imageOff, null);
            // 隐藏密码
            setTransformationMethod(PasswordTransformationMethod.getInstance());
            // setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            // 显示密码
            setCompoundDrawablesWithIntrinsicBounds(null, null, imageOn, null);
            setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            // setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
        this.setSelection(this.getText().length());
    }

    /**
     * 处理删除事件
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getCompoundDrawables()[2] != null) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                int eventX = (int) event.getRawX();
                int eventY = (int) event.getRawY();
                Rect rect = new Rect();
                getGlobalVisibleRect(rect);
                rect.left = rect.right - drawableWidth;
                if (rect.contains(eventX, eventY)) {
                    if (isHide) {
                        setDrawable(false);
                        isHide = false;

                    } else {
                        setDrawable(true);
                        isHide = true;
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

}
