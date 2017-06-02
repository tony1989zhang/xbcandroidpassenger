package com.lvchehui.www.xiangbc.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.app.App;
import com.lvchehui.www.xiangbc.view.toast.ToastManager;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

/**
 * 作用：网络请求工具类
 */
public class ConnectionUtil {

    //测试的IP地址  http://120.76.119.55:8008/api.php/main
    private static final String IP = "120.76.119.55:8008/api.php/main";
    private static final String PORT = ""; // ":80";
    //正式上线的IP地址

    private static ConnectionUtil connectionUtil;
    public RequestQueue mQueue;
    private ImageOptions imageOptions;
    private Drawable drawableResult;

    protected ConnectionUtil() {
    }

    public static ConnectionUtil getInstance() {
        if (connectionUtil == null) {
            connectionUtil = new ConnectionUtil();
        }
        return connectionUtil;
    }

    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    private void initRequestQueue(Context context) {
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(context);
        }
    }

    public RequestQueue getRequestQueue(Context context) {
        initRequestQueue(context);
        return mQueue;
    }

    public String getUrl(String path) {
        return "http://" + IP + PORT + path;
    }

    /**
     * 相对地址的get方法
     */
    public Request doGet(final Context context, String path, final OnDataLoadEndListener listener) {
        return doAbPathGet(context, getUrl(path), listener);
    }

    /**
     * 绝对地址的get方法
     */
    public Request doAbPathGet(final Context context, String url, final OnDataLoadEndListener listener) {
        initRequestQueue(context);
        final StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (listener != null) {
                    listener.OnLoadEnd(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (listener != null) {
                    listener.OnLoadEnd(null);
                    ToastManager.getManager().show(context.getResources().getString(R.string.not_connected));
                }
            }
        });
        return mQueue.add(request);
    }

    /**
     * 自定义的post 方法
     */
    public Request doPost(final Context context, String path, final Map<String, String> params,
                          final OnDataLoadEndListener listener) {
        return doPost(context, path, params, null, listener);
    }

    /**
     * 自己指定Content-Type post 方法
     */
    public Request doPost(final Context context, String path, final Map<String, String> params,
                          final String contentType, final OnDataLoadEndListener listener) {
        String strUrl = "";
        if (StringUtils.isEmpty(contentType)) {
            strUrl = getUrl(path);
        } else {
            strUrl = path;
        }

        XgoLog.i("params:" + params);

        initRequestQueue(context);
        final StringRequest request = new StringRequest(Request.Method.POST, strUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (listener != null) {
                    listener.OnLoadEnd(response);
                    XgoLog.i("volley_Data:" + response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (listener != null) {
                    listener.OnLoadEnd(null);
                  //  ToastManager.getManager().show(context.getResources().getString(R.string.not_connected));
                    XgoLog.i(context.getResources().getString(R.string.not_connected));
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (!StringUtils.isEmpty(contentType)) {
                    Map<String, String> headerParams = new HashMap<>();
                    headerParams.put("Content-Type", contentType);
                    return headerParams;
                }
                return super.getHeaders();
            }
        };

        return mQueue.add(request);
    }

    /**
     * 数据载入完成的监听器
     */
    public interface OnDataLoadEndListener {
        void OnLoadEnd(String ret);
    }

    public String getCurrentNetType(Context context) {
        String type = context.getString(R.string.not_connected);
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            type = context.getString(R.string.not_connected);
        } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            type = "Wifi";
        } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int subType = info.getSubtype();
            if (subType == TelephonyManager.NETWORK_TYPE_CDMA || subType == TelephonyManager.NETWORK_TYPE_GPRS
                    || subType == TelephonyManager.NETWORK_TYPE_EDGE) {
                type = "2G";
            } else if (subType == TelephonyManager.NETWORK_TYPE_UMTS || subType == TelephonyManager.NETWORK_TYPE_HSDPA
                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_A || subType == TelephonyManager.NETWORK_TYPE_EVDO_0
                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_B) {
                type = "3G";
            } else if (subType == TelephonyManager.NETWORK_TYPE_LTE) {// LTE是3g到4g的过渡，是3.9G的全球标准
                type = "4G";
            }
        }
        return type;
    }

    Bitmap bitmap = null;

    public Bitmap loadDrawableBitmap(String picUrl) {
        bitmap = null;
        if (null == imageOptions) {
            initImageOptions();
        }
        x.image().loadDrawable(picUrl, imageOptions, new Callback.CommonCallback<Drawable>() {
            @Override
            public void onSuccess(Drawable drawable) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                bitmap = bitmapDrawable.getBitmap();
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
        return bitmap;
    }

    /**
     * volley 加载图片
     *
     * @param imageView
     * @param url
     */
    public void loadImgae(NetworkImageView imageView, String url) {
        ImageLoader imageLoader = new ImageLoader(ConnectionUtil.getInstance().getRequestQueue(imageView.getContext()), ImageFileCache.getInstance());
        imageView.setImageUrl(TextUtils.isEmpty(url) ? "" : url, imageLoader);
    }

    /**
     * x.image 加载图片
     *
     * @param iv
     * @param picUrl
     * @return
     */
    public Drawable loadImage(ImageView iv, String picUrl) {

        if (null == imageOptions) {
            initImageOptions();
        }
        x.image().bind(iv, picUrl, imageOptions, new Callback.CommonCallback<Drawable>() {

            @Override
            public void onSuccess(Drawable result) {
                drawableResult = result;
            }

            @Override
            public void onFinished() {
                drawableResult = App.getInstance().getBaseContext().getResources().getDrawable(R.mipmap.load_img);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                drawableResult = App.getInstance().getBaseContext().getResources().getDrawable(R.mipmap.load_img);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                drawableResult = App.getInstance().getBaseContext().getResources().getDrawable(R.mipmap.load_img);
            }
        });
        return drawableResult;
    }

    private void initImageOptions() {
        imageOptions = new ImageOptions.Builder().setSize(DensityUtil.dip2px(120), DensityUtil.dip2px(120))
                .setRadius(DensityUtil.dip2px(5)).setCrop(true)
                // .setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP).setLoadingDrawableId(R.mipmap.load_img)
                .setFailureDrawableId(R.mipmap.load_img).build();
    }

}

