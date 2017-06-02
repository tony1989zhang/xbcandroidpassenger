package com.lvchehui.www.xiangbc.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.lvchehui.www.xiangbc.app.App;
import com.lvchehui.www.xiangbc.bean.YunTuPostBean;
import com.lvchehui.www.xiangbc.bean.YunTuResponseBaseBean;
import com.lvchehui.www.xiangbc.bean.YunTuResponseBean;
import com.lvchehui.www.xiangbc.bean.YunTuResponseCreateBean;
import com.lvchehui.www.xiangbc.db.UserInfoTable;
import com.lvchehui.www.xiangbc.utils.ConnectionManager;
import com.lvchehui.www.xiangbc.utils.ConnectionUtil;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.DbUtil;
import com.lvchehui.www.xiangbc.utils.SPUtil;
import com.lvchehui.www.xiangbc.utils.XgoLog;

import org.xutils.ex.DbException;

/**
 * Created by 张灿能 on 2016/7/26.
 * 作用：
 */
public class LocationService extends Service implements AMapLocationListener {
    private AMapLocationClient locationClient;
    private AMapLocationClientOption locationClientOption;
    private boolean hasYunTu_Id = false;
    private String _yuntu_id = "0";
    private String user_gid;
    private   UserInfoTable userInfoTable;
    @Override
    public void onCreate() {
        super.onCreate();
        ConnectionManager.getInstance().amapYuntuSelector(this, new LocationServiceListener(LocationServiceListener.AMAP_YUNTU_SELECTOR));
        try {
            user_gid = (String) SPUtil.getInstance(this).get(Constants.USER_GID, "");
             userInfoTable = DbUtil.getInstance().getDbManager().findFirst(UserInfoTable.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

        if (aMapLocation == null || aMapLocation.getErrorCode() != 0){
            if (aMapLocation != null)
            {
               //定位错误，异常处理
            }
            return;
        }
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT_ACTION.UPDATE_DATA);
        intent.putExtra(Constants.INTENT_ACTION.UPDATE_DATA_EXTRA_LATITUDE, "" + aMapLocation.getLatitude());
        intent.putExtra(Constants.INTENT_ACTION.UPDATE_DATA_EXTRA_LONGITUDE,""+aMapLocation.getLongitude());
        intent.putExtra(Constants.INTENT_ACTION.UPDATE_DATA_EXTRA_CITY, "" + aMapLocation.getCity());
        this.sendBroadcast(intent);
        createUpdateAmapLocation(aMapLocation);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != locationClient) {
            locationClient.onDestroy();
            locationClient = null;
            locationClientOption = null;
        }
    }

    private void initLocation() {
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationClientOption = new AMapLocationClientOption();
        locationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        locationClient.setLocationListener(this);
        locationClientOption.setOnceLocation(false);
        //初始化参数
        initOption();
        //启动定位
        locationClient.setLocationOption(locationClientOption);
        locationClient.startLocation();
    }

    private void initOption(){
        locationClientOption.setNeedAddress(true);//设置是否显示地址信息。
        locationClientOption.setGpsFirst(true);
        locationClientOption.setInterval(Constants.AMAP_UPLOAD_INTERVAL);
    }

    class LocationServiceListener implements ConnectionUtil.OnDataLoadEndListener{
        public static final int AMAP_YUNTU_SELECTOR = 100;
        public static final int AMAP_YUNTU_UPDATE = 101;
        public static final int AMAP_YUNTU_CREATE = 102;
        private int index;
        public LocationServiceListener(int index){
              this.index = index;
        }

        @Override
        public void OnLoadEnd(String ret) {
            switch (index){
                case AMAP_YUNTU_CREATE:
                    YunTuResponseCreateBean createBean = App.getInstance().getBeanFromJson(ret, YunTuResponseCreateBean.class);
                    if (createBean.status==1&&createBean.infocode.equals("10000")){
                        hasYunTu_Id = true;
                        _yuntu_id = createBean._id;
                    }else{
                        hasYunTu_Id = false;
                    }
                    break;
                case AMAP_YUNTU_UPDATE:
                    YunTuResponseBaseBean baseBean = App.getInstance().getBeanFromJson(ret, YunTuResponseBaseBean.class);
                    if (baseBean.status==1&&baseBean.infocode.equals("10000")){
                        hasYunTu_Id = true;
                    }
                    break;
                case AMAP_YUNTU_SELECTOR:
                    YunTuResponseBean beanFromJson = App.getInstance().getBeanFromJson(ret, YunTuResponseBean.class);
//                    XgoLog.e("beanFromJson:" + beanFromJson.toString());
                    if (beanFromJson.status == 1&&beanFromJson.datas != null){
                       if (beanFromJson.datas.size() > 0 && user_gid.equals(beanFromJson.datas.get(0).xbc_gid)){
                           _yuntu_id = beanFromJson.datas.get(0)._id;
                           hasYunTu_Id = true;
                       }else{
                           hasYunTu_Id = false;
                       }
                        initLocation();
                    }
                    break;
            }
        }

    }

    private void createUpdateAmapLocation(AMapLocation aMapLocation){
        //进行上传操作
        YunTuPostBean yunTuPostBean = new YunTuPostBean();
        yunTuPostBean._id = _yuntu_id ;
        yunTuPostBean._name = ""+userInfoTable.nick_name;
        yunTuPostBean._location = ""+aMapLocation.getLongitude()+"," + aMapLocation.getLatitude();
        yunTuPostBean.xbc_gid = user_gid;
        String json = new Gson().toJson(yunTuPostBean);
        boolean networkConnected = ConnectionUtil.getInstance().isNetworkConnected(LocationService.this);
        if (networkConnected) {
            if (hasYunTu_Id) {
                ConnectionManager.getInstance().amapYuntuUpdate(LocationService.this, Constants.AMAP_WEB_API_KEY, json, new LocationServiceListener(LocationServiceListener.AMAP_YUNTU_UPDATE));
            } else {
                ConnectionManager.getInstance().amapYuntuCreate(LocationService.this, Constants.AMAP_WEB_API_KEY, json, new LocationServiceListener(LocationServiceListener.AMAP_YUNTU_CREATE));
                hasYunTu_Id = true;
            }
        }
    }
}
