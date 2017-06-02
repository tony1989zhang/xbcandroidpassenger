package com.lvchehui.www.xiangbc.view;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * Create by 张灿能
 * 自定义ScrollView，侦听ScrollView 事件滑动问题
 */
public class CustomScrollView extends ScrollView {

    private int lastScrollY;
    private OnScrollListener onScrollListener;
    public CustomScrollView(Context context) {
        super(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (null != onScrollListener){
            onScrollListener.onScroll(lastScrollY = getScrollY());
        }
        switch (ev.getAction()){
            case MotionEvent.ACTION_UP:
                handler.sendMessageDelayed(handler.obtainMessage(),5);
                break;

        }
        return super.onTouchEvent(ev);

    }

    public void setOnScrollListener(OnScrollListener onScrollListener){
    	this.onScrollListener = onScrollListener;
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
           int scrollY = CustomScrollView.this.getScrollY();
            if (lastScrollY != scrollY){
                lastScrollY = scrollY;
                handler.sendMessageDelayed(handler.obtainMessage(),5);
            }
            if (null != onScrollListener){
                onScrollListener.onScroll(scrollY);
            }
        }
    };
    public interface OnScrollListener{
        void onScroll(int scrollY);
    }
}