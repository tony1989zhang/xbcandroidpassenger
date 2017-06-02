package com.lvchehui.www.xiangbc.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.view.menu.MenuView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.utils.XgoLog;
import com.lvchehui.www.xiangbc.view.TitleView;
import com.lvchehui.www.xiangbc.view.toast.ToastManager;

import java.util.ArrayList;

/**
 * Created by 张灿能 on 2016/6/13.
 * 作用：图片浏览放大缩小
 */
public class PhotoViewDialog extends Dialog implements OnClickListener {
    private ViewPager mPager;
    private TextView m_tv_photo_num;
    private TitleView m_title_view;

    private ArrayList<String> mImgs = new ArrayList<>();
    private PhotoViewPageAdapter photoViewPageAdapter;
    public PhotoViewDialog(final Context context) {
        super(context, R.style.custom_full_dialog);
        setContentView(R.layout.dialog_view_pager);

        m_title_view = (TitleView) findViewById(R.id.title_view);
        m_title_view.setTitleBackVisibility(View.VISIBLE);
        m_title_view.setTitleBackgroundColor(context.getResources().getColor(R.color.transparent));
        m_title_view.setOnClickListener(this);
        m_tv_photo_num = (TextView) findViewById(R.id.tv_photo_num);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setPageMargin((int) (context.getResources().getDisplayMetrics().density * 15));
        photoViewPageAdapter = new PhotoViewPageAdapter(context);
        mPager.setAdapter(photoViewPageAdapter);

    }

    class PhotoViewPageAdapter extends PagerAdapter {


        Context mContext;

        public PhotoViewPageAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return mImgs.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            PhotoView view = new PhotoView(mContext);
            view.enable();
            view.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Glide.with(mContext)
                    .load(mImgs.get(position))
                    .crossFade()
                    .into(view);
            container.addView(view);

            return view;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            XgoLog.e("position:" +position);
            return mImgs.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            XgoLog.e("position:vdestroyItem" + position);
            container.removeView((View) object);
        }
    }

    public void setImages(ArrayList<String> mImgs) {
        this.mImgs = mImgs;
        photoViewPageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_view:
                dismiss();
                break;
        }
    }
}
