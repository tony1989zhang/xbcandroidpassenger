package com.lvchehui.www.xiangbc.activity.splash;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.activity.home.HomeActivity;
import com.lvchehui.www.xiangbc.activity.login.LoginActivity;
import com.lvchehui.www.xiangbc.activity.welcome.WelcomeActivity;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.SPUtil;
import com.lvchehui.www.xiangbc.utils.StringUtils;

import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by 张灿能 on 2016/5/13.
 * 作用：程序入口，欢迎页面
 */
@ContentView(R.layout.activity_splansh)
public class SplashActivity extends Activity {


    private long splashTime = 4000;

    @ViewInject(R.id.imageView2)
    private ImageView m_imageView2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        // activity 进入动画
        overridePendingTransition(R.anim.zoomin, 0);
        x.task().postDelayed(new Runnable() {
            @Override
            public void run() {
                showAdverSplan();
            }
        }, 1000);

        doJump();
    }

    private void doJump() {
        final boolean isFirstLaunch = (boolean) SPUtil.getInstance(this).get(Constants.IS_FIRST_LAUNCH, true);
        x.task().postDelayed(new Runnable() {
            @Override
            public void run() {
                Class activity = null;
                if (isFirstLaunch) {
                    activity = WelcomeActivity.class;
                    SPUtil.getInstance(SplashActivity.this).save(Constants.IS_FIRST_LAUNCH, false);
                } else {
                    if (StringUtils.isEmpty(SPUtil.getInstance(SplashActivity.this).get(Constants.USER_GID, ""))) {
                        activity = LoginActivity.class;
                    } else {
                        activity = HomeActivity.class;
                    }
                }
                startActivity(new Intent(SplashActivity.this, activity));
                finish();
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        }, splashTime);
    }

    public void showAdverSplan() {


        ImageOptions options = new ImageOptions.Builder()
                //设置加载过程中的图片
                .setLoadingDrawableId(R.mipmap.load_img)
                        //设置加载失败后的图片
                .setFailureDrawableId(R.mipmap.load_img)
                        //设置使用缓存
                .setUseMemCache(true)
                        //设置显示圆形图片
                .setCircular(false)
                        //设置支持gif
                .setIgnoreGif(false)
                .build();
        //https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1472088194&di=db4dca288e8967cf60d7a5971703f4b1&src=http://img.zcool.cn/community/01313f56c3d5546ac7256cb094ff05.jpg
        //http://7xlh8k.com1.z0.glb.clouddn.com/%E8%BD%A6%E9%98%9F%EF%BC%8D%E5%B9%BF%E5%91%8A.png
        x.image().loadDrawable("http://7xlh8k.com1.z0.glb.clouddn.com/%E8%BD%A6%E9%98%9F%EF%BC%8D%E5%B9%BF%E5%91%8A.png", options, new Callback.CommonCallback<Drawable>() {
            @Override
            public void onSuccess(Drawable drawable) {
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                ViewGroup.LayoutParams layoutParams = m_imageView2.getLayoutParams();
                int height = ViewGroup.LayoutParams.WRAP_CONTENT;
                m_imageView2.setLayoutParams(layoutParams);
                m_imageView2.setMaxWidth(dm.widthPixels);
                m_imageView2.setMaxHeight((int) (dm.widthPixels * 5));

                m_imageView2.setImageDrawable(drawable);
                m_imageView2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("http://img4.imgtn.bdimg.com/it/u=250883362,3555496764&fm=11&gp=0.jpg"));
                        SplashActivity.this.startActivity(intent);
                    }
                });
            }

            @Override
            public void onError(Throwable throwable, boolean b) {

            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {

            }
        });


    }
}
