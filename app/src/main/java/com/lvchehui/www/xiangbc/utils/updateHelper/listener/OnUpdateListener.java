package com.lvchehui.www.xiangbc.utils.updateHelper.listener;


import com.lvchehui.www.xiangbc.utils.updateHelper.pojo.UpdateInfo;

/**
 * Created by 张灿能  2016-5-6
 */
public interface OnUpdateListener {
    /**
     * on start check
     */
     void onStartCheck();

    /**
     * on finish check
     */
     void onFinishCheck(UpdateInfo info);

     void onStartDownload();
    
     void onDownloading(int progress);
    
     void onFinshDownload();

}
