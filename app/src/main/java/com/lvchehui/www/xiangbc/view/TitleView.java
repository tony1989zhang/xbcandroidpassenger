package com.lvchehui.www.xiangbc.view;


import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.utils.StringUtils;

/**
 * @author 张灿能
 *         功能：自定义标题栏
 */
public class TitleView extends LinearLayout {

    ImageView mTitleBackIv;
    TextView mTitleMainTv;
    TextView mTitleRightTv;
    ImageView mTitleRightIv;
    TextView mTitleLeftTv;
    RelativeLayout mTitleViewRoot;
    TextView mEmCountTv;

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_title, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTitleViewRoot = (RelativeLayout) findViewById(R.id.title_view_root);
        mTitleBackIv = (ImageView) findViewById(R.id.title_back_iv);
        mTitleLeftTv = (TextView) findViewById(R.id.title_left_tv);
        mTitleMainTv = (TextView) findViewById(R.id.title_main_tv);
        mTitleRightTv = (TextView) findViewById(R.id.title_right_tv);
        mTitleRightIv = (ImageView) findViewById(R.id.title_right_iv);
        mEmCountTv = (TextView) findViewById(R.id.em_count_tv);
        final LayoutParams param = (LayoutParams) mTitleViewRoot.getLayoutParams();
        param.height = this.getResources().getDimensionPixelSize(R.dimen.title_height);
        mTitleViewRoot.setLayoutParams(param);
    }

    public void settitleBackImg(int dr) {
        mTitleBackIv.setImageResource(dr);
    }

    public void setTitle(int resid) {
        setTitle(this.getResources().getString(resid));
    }

    /**
     * 能否根据countStr的数值(> 0 or = 0)
     * 来判断消息提示红点的显示与否
     * @param countStr
     */
    public void setEmCount(String countStr) {
        if (!StringUtils.isEmpty(countStr)) {
            mEmCountTv.setVisibility(View.VISIBLE);
            mEmCountTv.setText(countStr);
        } else {
            mEmCountTv.setVisibility(View.GONE);
        }
    }

    public void setTitle(String title) {
        if (mTitleMainTv != null) {
            mTitleMainTv.setText(title);
        }
    }

    public void setTitleBackVisibility(int visibility) {
        if (mTitleBackIv != null) {
            mTitleBackIv.setVisibility(visibility);
        }
    }

    public void setTitleRightText(String text) {
        mTitleRightIv.setVisibility(View.GONE);
        if (mTitleRightTv != null) {
            if (TextUtils.isEmpty(text)) {
                mTitleRightTv.setVisibility(View.GONE);
            } else {
                mTitleRightTv.setText(text);
                mTitleRightTv.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setTitleLeftText(String text) {
        if (null != mTitleBackIv) {
            mTitleBackIv.setVisibility(View.GONE);
        }
        if (mTitleLeftTv != null) {
            if (TextUtils.isEmpty(text)) {
                mTitleLeftTv.setVisibility(View.GONE);
            } else {
                mTitleLeftTv.setText(text);
                mTitleLeftTv.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setTitleRightIv(int resid) {
        mTitleRightTv.setVisibility(View.GONE);
        if (mTitleRightIv != null) {
            if (resid < 0) {
                mTitleRightIv.setVisibility(View.GONE);
            } else {
                mTitleRightIv.setImageResource(resid);
                mTitleRightIv.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setTitleBackOnClickListener(OnClickListener listener) {
        if (mTitleBackIv != null) {
            mTitleBackIv.setOnClickListener(listener);
        }
        if (mTitleRightIv != null) {
            mTitleRightIv.setOnClickListener(listener);
        }
    }

    public void setTitleRightOnClickListener(OnClickListener listener) {
        if (mTitleRightTv != null) {
            mTitleRightTv.setOnClickListener(listener);
        }
        if (mTitleRightIv != null) {
            mTitleRightIv.setOnClickListener(listener);
        }
    }

    public void setTitleBackgroundColor(int color) {
        mTitleViewRoot.setBackgroundColor(color);
        if (color == getResources().getColor(R.color.transparent)) {
            mTitleViewRoot.getBackground().setAlpha(100);
        }
    }

    public void setStatusBarTopInsert(int topMargin) {
        final LayoutParams param = (LayoutParams) mTitleViewRoot.getLayoutParams();
        param.height = topMargin + this.getResources().getDimensionPixelSize(R.dimen.title_height);
        mTitleViewRoot.setLayoutParams(param);
    }

}
