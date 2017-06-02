package com.lvchehui.www.xiangbc.activity.mine.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.app.App;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.bean.GetIdentificationListBean;
import com.lvchehui.www.xiangbc.bean.IdentificationInfoBean;
import com.lvchehui.www.xiangbc.utils.ConnectionManager;
import com.lvchehui.www.xiangbc.utils.ConnectionUtil;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.SPUtil;
import com.lvchehui.www.xiangbc.view.TitleView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * Created by 张灿能 on 2016/5/24.
 * 作用：特权认证列表
 */
@ContentView(R.layout.activity_auth)
public class IdentificationListActivity extends BaseActivity {
    @ViewInject(R.id.title_view)
    TitleView m_title_view;

    @ViewInject(R.id.tv_title_auth_gov)
    private TextView m_tv_title_auth_gov; //@string/gov_auth_title;

    @ViewInject(R.id.tv_text_auth_gov)
    private TextView m_tv_text_auth_gov; //@string/gov_auth_text;


    @ViewInject(R.id.tv_title_auth_travel)
    private TextView m_tv_title_auth_travel; //@string/travel_auth_title;

    @ViewInject(R.id.tv_text_auth_travel)
    private TextView m_tv_text_auth_travel; //@string/travel_auth_text;


    @ViewInject(R.id.tv_title_auth_ent)
    private TextView m_tv_title_auth_ent; //@string/ent_auth_title;

    @ViewInject(R.id.tv_text_auth_ent)
    private TextView m_tv_text_auth_ent; //@string/ent_auth_text;


    @ViewInject(R.id.tv_title_auth_stu)
    private TextView m_tv_title_auth_stu; //@string/stu_auth_title;

    @ViewInject(R.id.tv_text_auth_stu)
    private TextView m_tv_text_auth_stu; //@string/stu_auth_text;
    private ArrayList<IdentificationInfoBean> identificationBean = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(m_title_view, getString(R.string.privilege_auth));
        showProgressDialog(getString(R.string.loading));
        ConnectionManager.getInstance().getIdentificationList(this,(String) SPUtil.getInstance(this).get(Constants.USER_GID,""),new IdentificationListOnDataEndListener());
    }

    @Event({R.id.ll_auth_gov,R.id.ll_auth_travel,R.id.ll_auth_ent,R.id.ll_auth_stu})
    private void authOnClick(View v){

        int auth_type = 0 ;
        int auth_index = 0;
        Intent intent = new Intent(IdentificationListActivity.this, SubmitAuthActivity.class);

        switch(v.getId()){

            case R.id.ll_auth_gov:
                auth_type = Constants.AuthPutExtra.AUTH_GOV;
                auth_index = 0;
                break;
            case R.id.ll_auth_travel:
                auth_type = Constants.AuthPutExtra.AUTH_TRAVEL;
                auth_index = 1;
                break;
            case R.id.ll_auth_ent:
                auth_type = Constants.AuthPutExtra.AUTH_ENT;
                auth_index = 2;
                break;
            case R.id.ll_auth_stu:
                auth_type = Constants.AuthPutExtra.AUTH_STU;
                auth_index = 3;
                break;
        }

        if (identificationBean.size() <4){
            showToast(getString(R.string.server_requesting));
        }

        // 考虑把这部分放SubmitAuthActivity？？？
        intent.putExtra(Constants.AuthPutExtra.AUTH_TYPE,auth_type);
        Bundle bundle = new Bundle();

        bundle.putSerializable(Constants.AuthPutExtra.IDENTIFICATIONBEAN_TYPE, identificationBean.get(auth_index));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    class IdentificationListOnDataEndListener implements ConnectionUtil.OnDataLoadEndListener {
        public IdentificationListOnDataEndListener(){}

        @Override
        public void OnLoadEnd(String ret) {
            identificationBean.clear();
            GetIdentificationListBean getIdentificationListBean = App.getInstance().getBeanFromJson(ret, GetIdentificationListBean.class);
            if (getIdentificationListBean.errCode != -1 && getIdentificationListBean.resData!=null){
                 for (int i = 0;i<getIdentificationListBean.resData.size();i++){
                     IdentificationInfoBean dataBean = getIdentificationListBean.resData.get(i);

                     if (i > 1){
                         identificationBean.add(dataBean);
                     }
                     switch (i){
                         case 2:
                             m_tv_title_auth_gov.setText(dataBean.name);
                             m_tv_text_auth_gov.setText(dataBean.description);
                             break;
                         case 3:
                             m_tv_title_auth_travel.setText(dataBean.name);
                             m_tv_text_auth_travel.setText(dataBean.description);
                             break;
                         case 4:
                             m_tv_title_auth_ent.setText(dataBean.name);
                             m_tv_text_auth_ent.setText(dataBean.description);
                             break;
                         case 5:
                             m_tv_title_auth_stu.setText(dataBean.name);
                             m_tv_text_auth_stu.setText(dataBean.description);
                             break;
                     }
                     dismissProgressDialog();
                 }
            }

        }
    }

}
