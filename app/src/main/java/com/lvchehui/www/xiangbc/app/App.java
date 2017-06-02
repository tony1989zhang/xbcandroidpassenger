package com.lvchehui.www.xiangbc.app;

import android.app.Activity;
import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.lvchehui.www.xiangbc.bean.BaseBean;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.CrashHandler;
import com.lvchehui.www.xiangbc.utils.XgoLog;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.pgyersdk.crash.PgyCrashManager;
//import com.squareup.leakcanary.LeakCanary;
//import com.squareup.leakcanary.RefWatcher;

import org.xutils.x;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 作用：全局Application
 */
public class App extends Application {
    private static App mApp;

    /**
     * 引用/内存泄漏观察者?
     */
//    private RefWatcher refWatcher;

    /**
     * 屏幕高度
     */
    public int mScreenHeight;

    /**
     * 屏幕宽度
     */
    public int mScreenWidth;

    /**
     * 状态栏高度
     */
    public int mTintInsertTop;

    /**
     * 状态栏+ActionBar高度
     */
    public int mTintInsertTopWithActionBar;

    public List<WeakReference<Activity>> aliveActivitys = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;

        // 内存泄漏的检测
//        refWatcher = LeakCanary.install(this);

        PgyCrashManager.register(this, Constants.APPID);

        // 全局异常捕获，保存至手机或者服务器
        CrashHandler.getInstance().init(this);

        x.Ext.init(this);
        // 输出debug日志，会影响性能
        x.Ext.setDebug(true);

        initImageLoader();

    }

//    public RefWatcher getRefWatcher(Context context) {
//        App application = (App) context.getApplicationContext();
//        return application.refWatcher;
//    }

    public static App getInstance() {
        if (mApp == null) {
            mApp = new App();
        }
        return mApp;
    }

    /**
     * 将Json格式的数据转化为Bean
     *
     * @param ret
     * @param c
     * @param <T>
     * @return
     */
    public <T> T getBeanFromJson(String ret, Class<T> c) {
        Class<?> c1  = c.getClass();
        T bean = null;
        Gson gson = new Gson();
        try {
            bean = gson.fromJson(ret, c);
        } catch (Exception e) {
            //format json error
//            formatJsonError(ret, c1, gson);
        }
        if (bean == null) {
            try {
                bean = c.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return bean;
    }

   /* private void formatJsonError(String ret, Class<?> c1, Gson gson) {
        try {
            BaseBean baseBean = gson.fromJson(ret, BaseBean.class);
            Object o1 = c1.newInstance();
            Field resData = c1.getDeclaredField("resData");
            resData.setAccessible(true);
            resData.set(o1, null);
            Field errCode = null;
            Field resMsg = null;
            for (; c1 != Object.class; c1 = c1.getSuperclass()) {
                try {
                    errCode = c1.getDeclaredField("errCode");
                    resMsg = c1.getDeclaredField("resMsg");
                } catch(Exception a) {

                }
                errCode.setAccessible(true);
                errCode.set(o1, baseBean.errCode);
                resMsg.setAccessible(true);
                resMsg.set(o1, baseBean.resMsg);
            }

        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (NoSuchFieldException e1) {
            e1.printStackTrace();
        }
    }*/

    /**
     * 解释服务端返回json格式不对的进行处理
     */
    public <T> T getBeanFromJson(String ret, Class<T> c, JsonDeserializer<T> d) {
        T bean = null;
        try {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(c, d);
            bean = gsonBuilder.create().fromJson(ret, c);
        } catch (Exception e) {
            //format json error
        }
        if (bean == null) {
            try {
                bean = c.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return bean;
    }

    /**
     * @Description: 初始化imageLoader
     * @author 张灿能
     * @date Oct 26, 2015 12:15:55 PM .bitmapConfig(Bitmap.Config.RGB_565)
     */
    private void initImageLoader() {
        DisplayImageOptions options = new DisplayImageOptions.Builder() // 设置图片下载期间显示的图片？
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED).cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                .resetViewBeforeLoading(true)
                        // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 创建配置过的DisplayImageOption对象

        // 创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .memoryCacheExtraOptions(480, 800).threadPoolSize(3) // 线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // 你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .discCacheSize(50 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator()) // 将保存图片时的URI名称用MD5加密
                .tasksProcessingOrder(QueueProcessingType.LIFO).discCacheFileCount(100) // 缓存的文件数量
                .defaultDisplayImageOptions(options)
                        // connectTimeout(5s)，readTimeout(30s)超时时间
                .imageDownloader(new BaseImageDownloader(getApplicationContext(), 5 * 1000, 30 * 1000))
                .writeDebugLogs() // Remove for release app
                .build(); // 开始构建

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(configuration);

    }

    /*public void initImageLoad(){
        ImageOptions options = new ImageOptions.Builder()
            .setIgnoreGif(false)
            .setImageScaleType(ScaleType.CENTER_CROP)
            .setLoadingDrawableId(R.drawable.ic_launcher)
            .setFailureDrawableId(R.drawable.ic_launcher)
            .build();
    }*/

    public void finishAllActivity() {
        XgoLog.e("finishAllActivity");
        for (int i = 0; i < aliveActivitys.size(); i++) {
            if (aliveActivitys.get(i) != null) {
                aliveActivitys.get(i).get().finish();
            }
        }
    }

    public Activity getTopActivity() {
        return aliveActivitys.get(aliveActivitys.size() - 1).get();
    }
}
