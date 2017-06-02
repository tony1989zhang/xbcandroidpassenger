package com.lvchehui.www.xiangbc.activity.welcome;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.activity.login.LoginActivity;
import com.lvchehui.www.xiangbc.adapter.ViewPaperAdapter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class WelcomeActivity extends Activity implements OnClickListener {
    private ViewPager viewpager;
    private Button btnOk;
    private ViewPaperAdapter adapter;
    private Intent intent = new Intent();
    private ImageView[] imageViews;
    private LinearLayout group;
    private LinearLayout btnLayout;
    final List<View> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        btnLayout = (LinearLayout) findViewById(R.id.layout_btn);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        btnOk = (Button) findViewById(R.id.btn_ok);
        group = (LinearLayout) findViewById(R.id.iv_image);
        btnOk.setOnClickListener(this);
        setAdapter();
    }

    private void setAdapter() {

        data.clear();
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;

        ImageView imageView = new ImageView(this);
        InputStream is = getResources().openRawResource(R.raw.welcome_01);
        Bitmap bm = BitmapFactory.decodeStream(is, null, opt);
        imageView.setImageBitmap(bm);
        imageView.setScaleType(ScaleType.CENTER_CROP);
        data.add(imageView);

        imageView = new ImageView(this);
        is = getResources().openRawResource(R.raw.welcome_02);
        bm = BitmapFactory.decodeStream(is, null, opt);
        imageView.setImageBitmap(bm);
        // imageView.setBackgroundResource(R.drawable.pic_welcome_2);
        imageView.setScaleType(ScaleType.CENTER_CROP);
        data.add(imageView);

        imageView = new ImageView(this);
        is = getResources().openRawResource(R.raw.welcome_03);
        bm = BitmapFactory.decodeStream(is, null, opt);
        imageView.setImageBitmap(bm);
        // imageView.setBackgroundResource(R.drawable.pic_welcome_3);
        imageView.setScaleType(ScaleType.CENTER_CROP);
        data.add(imageView);

        imageView = new ImageView(this);
        is = getResources().openRawResource(R.raw.welcome_04);
        bm = BitmapFactory.decodeStream(is, null, opt);
        imageView.setImageBitmap(bm);
        // imageView.setBackgroundResource(R.drawable.pic_welcome_4);
        imageView.setScaleType(ScaleType.CENTER_CROP);
        data.add(imageView);

//        imageView = new ImageView(this);
//        is = getResources().openRawResource(R.drawable.pic_welcome_5);
//        bm = BitmapFactory.decodeStream(is, null, opt);
//        imageView.setImageBitmap(bm);
        // imageView.setBackgroundResource(R.drawable.pic_welcome_5);
//         imageView.setScaleType(ScaleType.CENTER_CROP);
//        data.add(imageView);

        // imageView = new ImageView(this);
        // is = getResources().openRawResource(R.drawable.pic_welcome_6);
        // bm = BitmapFactory.decodeStream(is, null, opt);
        // imageView.setImageBitmap(bm);
        // // imageView.setBackgroundResource(R.drawable.pic_welcome_6);
        // data.add(imageView);
        adapter = new ViewPaperAdapter(data);
        viewpager.setAdapter(adapter);
        // indicator.setViewPager(viewpager);
        setCirclePageIndicator();
        viewpager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int arg0) {

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int arg0) {
                arg0 = arg0 % data.size();

                // 当viewpager换页时 改掉下面对应的小点
                if (arg0 == data.size() - 1) {
                    btnLayout.setVisibility(View.VISIBLE);
                } else {
                    btnLayout.setVisibility(View.GONE);
                }
                if (data.size() > 0) {
                    for (int i = 0; i < imageViews.length; i++) {
                        // 设置当前的对应的小点为选中状态
                        imageViews[arg0].setBackgroundResource(R.mipmap.jshop_banner_point_active);
                        if (arg0 != i) {
                            // 设置为非选中状态
                            imageViews[i].setBackgroundResource(R.mipmap.jshop_banner_point_inactive);
                        }
                    }
                }
            }

        });
    }

    /**
     * 设置圆点指示器
     */
    private void setCirclePageIndicator() {
        group.removeAllViews();
        // 对应小点个数 final ImageView[]
        int pageCount = data.size();
        imageViews = new ImageView[pageCount];
        if (this.data.size() > 0) {
            for (int i = 0; i < pageCount; i++) {
                // 设置每个小圆点距离左边的间距
                LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                if (i > 0) {
                    margin.setMargins(10, 0, 0, 0);
                } else {
                    margin.setMargins(0, 0, 0, 0);
                }

                ImageView imageView = new ImageView(this); // 设置每个小圆点的宽高
                // imageView.setLayoutParams(new LayoutParams(15, 15));
                imageViews[i] = imageView;
                if (i == 0) {
                    // 默认选中第一张图片
                    imageViews[i].setBackgroundResource(R.mipmap.jshop_banner_point_active);
                } else {
                    // 其他图片都设置未选中状态
                    imageViews[i].setBackgroundResource(R.mipmap.jshop_banner_point_inactive);
                }
                group.addView(imageViews[i], margin);
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewpager = null;
        setContentView(R.layout.view_null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                intent.setClass(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }
}
