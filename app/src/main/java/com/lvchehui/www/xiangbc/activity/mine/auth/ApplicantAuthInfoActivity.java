package com.lvchehui.www.xiangbc.activity.mine.auth;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.app.App;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.bean.IdentificationInfoBean;
import com.lvchehui.www.xiangbc.bean.UploadBean;
import com.lvchehui.www.xiangbc.db.IdentificationSign;
import com.lvchehui.www.xiangbc.impl.OnOperationListener;
import com.lvchehui.www.xiangbc.utils.ConnectionManager;
import com.lvchehui.www.xiangbc.utils.ConnectionUtil;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.DbUtil;
import com.lvchehui.www.xiangbc.utils.StringUtils;
import com.lvchehui.www.xiangbc.utils.Utils;
import com.lvchehui.www.xiangbc.utils.UtilsPhotoNoZoom;
import com.lvchehui.www.xiangbc.utils.UtilsPhotoNoZoom.OnPhotoNoZoomListener;
import com.lvchehui.www.xiangbc.utils.UtilsSetPhoto;
import com.lvchehui.www.xiangbc.view.EditText.EditTextWithDel;
import com.lvchehui.www.xiangbc.view.TitleView;
import com.lvchehui.www.xiangbc.view.dialog.CustomEditDialog;

import org.xutils.DbManager;
import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by 张灿能 on 2016/5/25.
 * 作用：填写申请人信息
 */
@ContentView(R.layout.activity_applicant_auth_info)
public class ApplicantAuthInfoActivity extends BaseActivity implements UtilsSetPhoto.PhotoResultIml,
        ConnectionUtil.OnDataLoadEndListener, OnPhotoNoZoomListener {
    @ViewInject(R.id.title_view)
    private TitleView m_title_view;

    @ViewInject(R.id.ll_nickname)
    private LinearLayout m_ll_name;

    @ViewInject(R.id.tv_company_name)
    private TextView m_tv_title_name; //姓名:;

    @ViewInject(R.id.et_campany_name)
    private EditTextWithDel m_et_name;

    @ViewInject(R.id.ll_id)
    private LinearLayout m_ll_id;

    @ViewInject(R.id.tv_id)
    private EditTextWithDel m_tv_id;

    @ViewInject(R.id.tv_describe)
    private TextView m_tv_describe; //上传学生图片\n注：图片信息必须包含学校名称和个人姓名及照片;

    @ViewInject(R.id.iv_photo)
    private ImageView m_iv_photo;

    @ViewInject(R.id.btn_ok)
    private Button m_btn_ok; //上传;


    private boolean IS_EDIT;
    private String picStr;
    private int identification_id;
    private String identification_gid;

    int intExtra;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(m_title_view, getString(R.string.input_proposer_info));
        getIntentExtra();
    }


    private void getIntentExtra() {
        Intent intent = getIntent();
        intExtra = intent.getIntExtra(Constants.AuthPutExtra.AUTH_TYPE, 0);
        IdentificationInfoBean resDataBean = (IdentificationInfoBean) intent.getSerializableExtra(Constants.AuthPutExtra.IDENTIFICATIONBEAN_TYPE);
        identification_id = Integer.valueOf(resDataBean.identification_id);
        identification_gid = resDataBean.identification_gid;

        IdentificationSign identificationSign = initDb(resDataBean);
        if (null != identificationSign) {
            m_et_name.setText(identificationSign.sign_in_person_name);
            m_tv_id.setText(identificationSign.sign_in_preson_id_card_no);
            ConnectionUtil.getInstance().loadImage(m_iv_photo, identificationSign.sign_in_person_id_card_photo_url);
            IS_EDIT = true;
        }

    }

    private IdentificationSign initDb(IdentificationInfoBean resDataBean) {
        try {
            IdentificationSign identificationSign = DbUtil.getInstance().getDbManager().selector(IdentificationSign.class).where("identification_id", "=", resDataBean.identification_id).and("identification_gid", "=", resDataBean.identification_gid).findFirst();
            return identificationSign;
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Event(value = {R.id.btn_ok, R.id.tv_ok, R.id.ll_nickname, R.id.ll_id})
    private void applicantAuthInfoOnClick(View v) {
        final CustomEditDialog dialog = new CustomEditDialog(this);
        dialog.setTitle("");
        switch (v.getId()) {
            case R.id.btn_ok:
                UtilsPhotoNoZoom.getInstance().init(this, this).showDialog(this);
                break;
            case R.id.tv_ok:
                if (StringUtils.isEmpty(m_et_name.getText().toString())) {
                    showToast("姓名不能为空");
                    return;
                }
                if (StringUtils.isEmpty(m_tv_id.getText().toString())) {
                    showToast("身份证不能为空");
                    return;
                }
                if (StringUtils.isEmpty(picStr)) {
                    showToast("请先选择照片");
                    return;
                }
                showProgressDialog();
                ConnectionManager.getInstance().upLoadFile(this, picStr, this);
                break;
            case R.id.ll_nickname:
                break;
            case R.id.ll_id:
                dialog.setTitle("填写身份证号码");
                dialog.setOperationListener(new OnOperationListener() {
                    @Override
                    public void onLeftClick() {
                        dialog.cancel();
                    }

                    @Override
                    public void onRightClick() {
                        m_tv_id.setText(dialog.getEditText());
                    }
                });

                dialog.show();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UtilsPhotoNoZoom.getInstance().getActivityResult(requestCode, resultCode, data, this);
    }

    @Override
    public void OnPotoResult(Bitmap ib) {
        m_iv_photo.setImageBitmap(ib);
        picStr = UtilsSetPhoto.getInstance().savaBitmap(ib);

    }

    @Override
    public void OnLoadEnd(String ret) {
        dismissProgressDialog();
        try {
            String s = Utils.deCodeString(ret);
            UploadBean uploadBean = App.getInstance().getBeanFromJson(s, UploadBean.class);
            IdentificationSign identificationSign = new IdentificationSign();
            identificationSign.identification_id = identification_id;
            identificationSign.identification_gid = identification_gid;
            identificationSign.sign_in_person_name = m_et_name.getText().toString();
            identificationSign.sign_in_preson_id_card_no = m_et_name.getText().toString();
            identificationSign.sign_in_person_id_card_photo_url = uploadBean.resData.thumburl;
            DbManager db = DbUtil.getInstance().getDbManager();

            if (IS_EDIT) {
                KeyValue[] keyValues = new KeyValue[]{
                        new KeyValue("sign_in_person_name", m_et_name.getText().toString()),
                        new KeyValue("sign_in_preson_id_card_no", m_tv_id.getText().toString()),
                        new KeyValue("sign_in_person_id_card_photo_url", uploadBean.resData.thumburl)
                };
                db.update(IdentificationSign.class, WhereBuilder.b("identification_id", "=", identification_id).and("identification_gid", "=", identification_gid), keyValues);
            } else {
                db.save(identificationSign);
            }
            finish();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
