package com.lvchehui.www.xiangbc.activity.mine.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.app.App;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.bean.BaseBean;
import com.lvchehui.www.xiangbc.bean.IdentificationInfoBean;
import com.lvchehui.www.xiangbc.db.IdentificationCredential;
import com.lvchehui.www.xiangbc.db.IdentificationInfo;
import com.lvchehui.www.xiangbc.db.IdentificationSign;
import com.lvchehui.www.xiangbc.utils.ConnectionManager;
import com.lvchehui.www.xiangbc.utils.ConnectionUtil;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.DbUtil;
import com.lvchehui.www.xiangbc.utils.SPUtil;
import com.lvchehui.www.xiangbc.utils.StringUtils;
import com.lvchehui.www.xiangbc.view.TitleView;

import org.xutils.db.Selector;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by 张灿能 on 2016/5/24.
 * 作用：提交认证
 */
@ContentView(R.layout.activity_auth_info)
public class SubmitAuthActivity extends BaseActivity implements ConnectionUtil.OnDataLoadEndListener {

    @ViewInject(R.id.title_view)
    private TitleView m_titleView;

    @ViewInject(R.id.rl_info)
    private RelativeLayout m_rl_info;

    @ViewInject(R.id.tv_info)
    private TextView m_tv_info; //填写企业信息;

    @ViewInject(R.id.iv_info)
    private ImageView m_iv_info;

    @ViewInject(R.id.rl_photo)
    private RelativeLayout m_rl_photo;


    @ViewInject(R.id.tv_photo)
    private TextView m_tv_photo; //上传营业执照;

    @ViewInject(R.id.iv_photo)
    private ImageView m_iv_photo;

    @ViewInject(R.id.rl_application)
    private RelativeLayout m_rl_application;

    @ViewInject(R.id.tv_application)
    private TextView m_tv_application; //填写申请人信息;

    @ViewInject(R.id.iv_application)
    private ImageView m_iv_application;

    @ViewInject(R.id.tv_ok)
    private TextView m_tv_ok; //提交认证;

    private int intExtra;

    private static final String COMPANY_NAME = "企业";
    private static final String TRAVEL_AGENCY_NAME = "旅行社";
    private static final String SCHOOL_NAME = "学校";
    private static final String GOV_NAME = "政府/事业单位";
    private static final String COMPANY_LICENSE_PHOTO = "营业执照";
    private static final String TRAVEL_AGENCY_PHOTO = "营业执照";
    private static final String STU_PHOTO = "学生证";
    private static final String GOV_PHOTO = "组织机构代码证";

    private String auth_type = "";
    private String auth_photo = "";
    private IdentificationInfoBean resDataBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_tv_ok.setText(getString(R.string.submit_auth));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getIntentExtra();
    }

    private void getIntentExtra() {
        Intent intent = getIntent();
        intExtra = intent.getIntExtra(Constants.AuthPutExtra.AUTH_TYPE, 0);
        resDataBean = (IdentificationInfoBean) intent.getSerializableExtra(Constants.AuthPutExtra.IDENTIFICATIONBEAN_TYPE);
        if (intExtra == Constants.AuthPutExtra.AUTH_ENT) {
            auth_type = COMPANY_NAME;
            auth_photo = COMPANY_LICENSE_PHOTO;
        } else if (intExtra == Constants.AuthPutExtra.AUTH_GOV) {
            auth_type = GOV_NAME;
            auth_photo = GOV_PHOTO;
        } else if (intExtra == Constants.AuthPutExtra.AUTH_TRAVEL) {
            auth_type = TRAVEL_AGENCY_NAME;
            auth_photo = TRAVEL_AGENCY_PHOTO;
        } else if (intExtra == Constants.AuthPutExtra.AUTH_STU) {
            auth_type = SCHOOL_NAME;
            auth_photo = STU_PHOTO;
        }
        setTitleView(m_titleView, auth_type + getString(R.string.auth));
        m_tv_info.setText(getString(R.string.input) + auth_type + getString(R.string.info));
        m_tv_photo.setText(getString(R.string.upload) + auth_photo);
        initDB();
    }

    private void initDB() {
        try {
            Selector<IdentificationInfo> identification_id = DbUtil.getInstance().getDbManager()
                    .selector(IdentificationInfo.class).where("identification_id", "=", resDataBean.identification_id);
            IdentificationInfo indentificationInfo = identification_id.findFirst();
            if (null != indentificationInfo) {
                m_tv_info.setTextColor(getResources().getColor(R.color.text_default_yellow));
                m_tv_info.setText(getString(R.string.already_input) + auth_type + getString(R.string.info));
                m_iv_info.setVisibility(View.VISIBLE);
                m_rl_info.setSelected(true);
            }
            IdentificationCredential identificationCredential = DbUtil.getInstance().getDbManager().selector(IdentificationCredential.class).where("identification_id", "=", resDataBean.identification_id).and("identification_gid", "=", resDataBean.identification_gid).findFirst();
            if (null != identificationCredential) {
                m_tv_photo.setText(getString(R.string.already_upload) + auth_photo);
                m_tv_photo.setTextColor(getResources().getColor(R.color.text_default_yellow));
                m_iv_photo.setVisibility(View.VISIBLE);
                m_rl_photo.setSelected(true);
            }
            IdentificationSign IdentificationSign = DbUtil.getInstance().getDbManager().selector(IdentificationSign.class).where("identification_id", "=", resDataBean.identification_id).and("identification_gid", "=", resDataBean.identification_gid).findFirst();
            if (null != IdentificationSign) {
                m_tv_application.setText(getString(R.string.already_input_proposer_info));
                m_tv_application.setTextColor(getResources().getColor(R.color.text_default_yellow));
                m_iv_application.setVisibility(View.VISIBLE);
                m_rl_application.setSelected(true);
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Event(value = {R.id.rl_info, R.id.rl_photo, R.id.rl_application, R.id.tv_ok})
    private void authInfoOnClick(View v) {

        Class activityClass = null;
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.rl_info:
                activityClass = EnterAuthInfoActivity.class;
                break;
            case R.id.rl_photo:
                activityClass = PhotoAuthInfoActivity.class;
                break;
            case R.id.rl_application:
                activityClass = ApplicantAuthInfoActivity.class;
                break;
            case R.id.tv_ok:
                submitAuth();
                break;
        }
        if (null == activityClass) {
            return;
        }
        intent.setClass(this, activityClass);
//        Intent intent = new Intent(SubmitAuthActivity.this, activityClass);
        intent.putExtra(Constants.AuthPutExtra.AUTH_TYPE, intExtra);
        Bundle bundle = new Bundle();
        if (null != resDataBean) {
            bundle.putSerializable(Constants.AuthPutExtra.IDENTIFICATIONBEAN_TYPE, resDataBean);
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    private void submitAuth() {
        try {
            IdentificationInfo identificationInfo = DbUtil.getInstance().getDbManager().selector(IdentificationInfo.class)
                    .where("identification_id", "=", resDataBean.identification_id)
                    .and("identification_gid", "=", resDataBean.identification_gid)
                    .findFirst();
            if (null == identificationInfo) {
                showToast(auth_type + "信息不能为空");
                return;
            }
            IdentificationCredential identificationCredential = DbUtil.getInstance().getDbManager().selector(IdentificationCredential.class)
                    .where("identification_id", "=", resDataBean.identification_id)
                    .and("identification_gid", "=", resDataBean.identification_gid)
                    .findFirst();
            if (null == identificationCredential) {
                showToast(auth_photo + "不能为空");
                return;
            }
            IdentificationSign identificationSign = DbUtil.getInstance().getDbManager().selector(IdentificationSign.class)
                    .where("identification_id", "=", resDataBean.identification_id)
                    .and("identification_gid", "=", resDataBean.identification_gid)
                    .findFirst();
            if (null == identificationSign) {
                showToast("申请人信息不能为空");
                return;
            }
            showProgressDialog("提交认证");
            String industry_type = "";
            if (!StringUtils.isEmpty(identificationInfo.industry_type)) {
                industry_type = identificationInfo.industry_type.substring(0, identificationInfo.industry_type.indexOf("-"));

            }
            ConnectionManager.getInstance().SubmitAuth(this, (String) SPUtil.getInstance(this).get(Constants.USER_GID, "")
                    , resDataBean.identification_id, resDataBean.identification_gid, identificationInfo.name, identificationInfo.address, identificationInfo.owner, industry_type
                    , identificationCredential.credential_number, identificationCredential.credential_photo_url, identificationSign.sign_in_person_name, identificationSign.sign_in_preson_id_card_no,
                    identificationSign.sign_in_person_id_card_photo_url, this

            );
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnLoadEnd(String ret) {
        BaseBean baseBean = App.getInstance().getBeanFromJson(ret, BaseBean.class);
        showToast(baseBean.resMsg);
        dismissProgressDialog();
    }
}
