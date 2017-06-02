package com.lvchehui.www.xiangbc.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.view.scrollingAd.ImageAdapter;
import com.lvchehui.www.xiangbc.view.scrollingAd.InkPageIndicator;

import java.util.ArrayList;

/**
 * Created by 张灿能 on 2016/6/8.
 * 作用：代理商滑动式广告
 */
public class AgentDialog  extends Dialog implements View.OnClickListener{

    private ViewPager m_image_view_pager;
    private  TextView m_tv_agent_expand;

    private TextView m_tv_close; //关闭;

    private InkPageIndicator m_ink_pager_indicator;

    private TextView m_r_ty;
    private onAgentDialogListener onAgentDialogListener;
    ArrayList<String> images;

    public AgentDialog(Context context) {
        super(context, R.style.custom_dialog);
        setContentView(R.layout.dialog_agent);
        m_image_view_pager = (ViewPager) findViewById(R.id.image_view_pager);
        m_ink_pager_indicator = (InkPageIndicator) findViewById(R.id.ink_pager_indicator);
        m_r_ty = (TextView) findViewById(R.id.r_ty);
        m_r_ty.setOnClickListener(this);
        m_tv_close = (TextView) findViewById(R.id.tv_close);
        m_tv_close.setOnClickListener(this);
        m_tv_agent_expand = (TextView) findViewById(R.id.tv_agent_expand);
        images = new ArrayList<>();
        images.add("");
        setViewPager();
    }

    private void setViewPager() {
        m_image_view_pager.setAdapter(new ImageAdapter(images));
        m_ink_pager_indicator.setViewPager(m_image_view_pager);
        m_image_view_pager.addOnPageChangeListener(new AgentOnPageChangeListener(images.size()));
        m_r_ty.setVisibility(View.GONE);
    }

    public void setImages(ArrayList<String> images ) {
        this.images = images;
        setViewPager();
        if (images.size() == 1){
            m_r_ty.setVisibility(View.VISIBLE);
        }
    }

    public void setAgentExpland(String expland){
        m_tv_agent_expand.setText(expland);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_close:
                this.dismiss();
                break;
            case R.id.r_ty:
                onAgentDialogListener.tryOnClick();
                this.dismiss();
                break;
        }
    }


    public void setAgentDialogListener(onAgentDialogListener onAgentDialogListener){

        this.onAgentDialogListener = onAgentDialogListener;
    }

    public interface onAgentDialogListener{
        void tryOnClick();
    }
    class AgentOnPageChangeListener implements ViewPager.OnPageChangeListener {
        private int size = -1;

        private AgentOnPageChangeListener(int size) {
            this.size = size;
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int p) {
            // TODO Auto-generated method stub
            if (size != -1) {
                if (p == (size - 1)) {
                    m_r_ty.setVisibility(View.VISIBLE);
                } else {
                    m_r_ty.setVisibility(View.GONE);
                }
            }
        }
    }
}
