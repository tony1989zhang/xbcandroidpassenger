package com.lvchehui.www.xiangbc.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.OneKeyShare.OnekeyShare;
import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.activity.mine.AgentActivity;
import com.lvchehui.www.xiangbc.activity.mine.RedpacketListActivity;
import com.lvchehui.www.xiangbc.view.TextView.lotter.LotterView;
import com.lvchehui.www.xiangbc.view.TextView.lotter.LotteryInfo;
import com.lvchehui.www.xiangbc.view.TitleView;
import com.lvchehui.www.xiangbc.view.toast.ToastManager;

import cn.sharesdk.framework.ShareSDK;

/**
 * Created by 张灿能 on 2016/6/16.
 * 作用：刮奖
 */

public class LotterDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private TitleView m_title_view;
    private LotterView m_lotter;
    private TextView m_btn_again;//我想刮;
    private TextView m_btn_share;

    private OnLotterDialogOnClick mOnLotterDialogOnClick;
    public LotterDialog(Context context) {
        super(context, R.style.custom_full_dialog);
        setContentView(R.layout.dialog_lotter);
        ShareSDK.initSDK(context);
        this.mContext = context;
        m_title_view = (TitleView) findViewById(R.id.title_view);
        m_title_view.setTitle("刮奖");
        m_title_view.setTitleBackVisibility(View.VISIBLE);
        m_title_view.setTitleBackOnClickListener(this);
        m_lotter = (LotterView) findViewById(R.id.lotter);
        m_btn_again = (TextView) findViewById(R.id.btn_again);
        m_btn_again.setOnClickListener(this);
        m_btn_share = (TextView) findViewById(R.id.btn_share);
        m_btn_share.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_again:
              //  m_lotter.againLotter();
                mContext.startActivity(new Intent(mContext, RedpacketListActivity.class));
                dismiss();
                break;
            case R.id.btn_share:
                LotteryInfo info = m_lotter.getLotterInfo();
                if (info.getScratchPercentage() == 0) {
                    ToastManager.getManager().show("这么好的奖不刮怎么分享啊");
                } else if (info.getScratchPercentage() < 0.3) {
                    ToastManager.getManager().show("再刮开一点嘛，这么一点点都看不到重点");
                } else {
                   // shareLotter(info);
                    mContext.startActivity(new Intent(mContext, AgentActivity.class));
                    dismiss();
                }
                break;
            case R.id.title_back_iv:
                if (null == mOnLotterDialogOnClick) {
                    dismiss();
                }else{
                    mOnLotterDialogOnClick.lotterDialogOnClick();
                }
                break;
        }
    }


    public void setOnLotterDialogOnClick(OnLotterDialogOnClick onLotterDialogOnClic){
        this.mOnLotterDialogOnClick = onLotterDialogOnClic;
    }

    private void shareLotter(LotteryInfo info) {
        OnekeyShare one = new OnekeyShare();
        one.setText(info.getShareText());
        one.show(mContext);
    }


    @Override
    public void dismiss() {
        ShareSDK.stopSDK(mContext);
        super.dismiss();

    }

    public interface OnLotterDialogOnClick{
        void lotterDialogOnClick();
    }
}
