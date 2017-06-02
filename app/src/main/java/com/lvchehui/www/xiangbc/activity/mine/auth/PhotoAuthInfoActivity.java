package com.lvchehui.www.xiangbc.activity.mine.auth;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.app.App;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.bean.IdentificationInfoBean;
import com.lvchehui.www.xiangbc.bean.UploadBean;
import com.lvchehui.www.xiangbc.db.IdentificationCredential;
import com.lvchehui.www.xiangbc.utils.ConnectionManager;
import com.lvchehui.www.xiangbc.utils.ConnectionUtil;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.DbUtil;
import com.lvchehui.www.xiangbc.utils.PixelUtil;
import com.lvchehui.www.xiangbc.utils.Utils;
import com.lvchehui.www.xiangbc.utils.UtilsPhotoNoZoom;
import com.lvchehui.www.xiangbc.utils.UtilsSetPhoto;
import com.lvchehui.www.xiangbc.utils.XgoLog;
import com.lvchehui.www.xiangbc.view.EditText.EditTextWithDel;
import com.lvchehui.www.xiangbc.view.TitleView;

import org.xutils.DbManager;
import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by 张灿能 on 2016/5/25.
 * 作用：上传认证所需的照片，组织机构代码证/营业执照/学生证
 */
@ContentView(R.layout.activity_photo_auth_info)
public class PhotoAuthInfoActivity extends BaseActivity implements UtilsSetPhoto.PhotoResultIml,ConnectionUtil.OnDataLoadEndListener,UtilsPhotoNoZoom.OnPhotoNoZoomListener {

    @ViewInject(R.id.title_view)
    private TitleView m_title_view;

    @ViewInject(R.id.ll_credential_number)
    private LinearLayout m_ll_credential_number;

    @ViewInject(R.id.tv_credential_number)
    private EditTextWithDel m_tv_credential_number;

    @ViewInject(R.id.tv_describe)
    private TextView m_tv_describe; //上传学生图片\n注：图片信息必须包含学校名称和个人姓名及照片;

    @ViewInject(R.id.iv_photo)
    private ImageView m_iv_photo;


    @ViewInject(R.id.tv_ok)
    private TextView m_tv_ok; //保存;
    private int intExtra;
    private static final String ENT_PHOTO_NAME = "营业执照";
    private static final String GOV_PHOTO_NAME = "组织机构代码证";
    private static final String TRAVERL_PHOTO_NAME = "营业执照";
    private static final String STU_PHOTO_NAME = "学生证";

    private static final int ENT_PHOTO_DESCRIBE =R.array.contents_ENT_PHOTO;
    private static final int GOV_PHOTO_DESCRIBE = R.array.contents_GOV_PHOTO;
    private static final int TRAVERL_PHOTO_DESCRIBE = R.array.contents_TRAVERL_PHOTO;
    private static final int STU_PHOTO_DESCRIBE = R.array.contents_STU_PHOTO;

    private String auth_type_name = "";
    private int auth_type_describe;
    private boolean IS_EDIT;
    private int identification_id;
    private String identification_gid;
    private  String picString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentExtra();
        setTitleView(m_title_view, getString(R.string.upload) + auth_type_name);
    }

    /**
     * 初始化字体颜色与字体代销设置
     * */
    private void initDescribe() {
        String[] colors = getResources().getStringArray(R.array.colors);
        String[] fonts = getResources().getStringArray(R.array.font);
        String[] textContents = getResources().getStringArray(auth_type_describe);
        StringBuffer sb = new StringBuffer();
        for (int i = 0;i<textContents.length;i++){
            sb.append(textContents[i]);
        }
        SpannableStringBuilder spannable = new SpannableStringBuilder(sb.toString());
        int begin = 0;
        for (int j = 0;j < colors.length;j++)
        {
            String color = colors[j];
            String[] split = color.split(",");
            int r = Integer.parseInt(split[0]);
            int g = Integer.parseInt(split[1]);
            int b = Integer.parseInt(split[2]);
            int myColor = Color.rgb(r, g, b);
            String font = fonts[j];
            int textLength = textContents[j].length();
            spannable.setSpan(new ForegroundColorSpan(myColor),begin,begin+textLength, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            spannable.setSpan(new AbsoluteSizeSpan(PixelUtil.sp2px(this,Integer.parseInt(font))),begin,begin+textLength,Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            begin += textLength;
        }
        m_tv_describe.setText(spannable);
    }

    private void getIntentExtra() {
        Intent intent = getIntent();
        intExtra = intent.getIntExtra(Constants.AuthPutExtra.AUTH_TYPE, 0);
        IdentificationInfoBean resDataBean = (IdentificationInfoBean) intent.getSerializableExtra(Constants.AuthPutExtra.IDENTIFICATIONBEAN_TYPE);
        identification_id = Integer.valueOf(resDataBean.identification_id);
        identification_gid =resDataBean.identification_gid;
        IS_EDIT =false;
        IdentificationCredential identificationCredential = initDb(resDataBean);
        if (null != identificationCredential){
            IS_EDIT = true;
            ConnectionUtil.getInstance().loadImage(m_iv_photo,identificationCredential.credential_photo_url);
        }
        m_ll_credential_number.setVisibility(View.VISIBLE);
        if (intExtra == Constants.AuthPutExtra.AUTH_ENT) {
            auth_type_name = ENT_PHOTO_NAME;
            auth_type_describe = ENT_PHOTO_DESCRIBE;
        } else if (intExtra == Constants.AuthPutExtra.AUTH_GOV) {
            auth_type_name = GOV_PHOTO_NAME;
            auth_type_describe = GOV_PHOTO_DESCRIBE;
        } else if (intExtra == Constants.AuthPutExtra.AUTH_TRAVEL) {
            auth_type_name = TRAVERL_PHOTO_NAME;
            auth_type_describe = TRAVERL_PHOTO_DESCRIBE;
        } else if (intExtra == Constants.AuthPutExtra.AUTH_STU) {
            auth_type_name = STU_PHOTO_NAME;
            auth_type_describe = STU_PHOTO_DESCRIBE;
            m_ll_credential_number.setVisibility(View.GONE);
        }


        initDescribe();
        m_tv_ok.setText("保存");
    }

    private IdentificationCredential initDb(IdentificationInfoBean resDataBean) {
        try {
            IdentificationCredential identificationCredential = DbUtil.getInstance()
                    .getDbManager().selector(IdentificationCredential.class)
                    .where("identification_id", "=", resDataBean.identification_id)
                    .and("identification_gid", "=",resDataBean.identification_gid).findFirst();
            return identificationCredential;
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Event(value = {
            R.id.btn_ok,
            R.id.tv_ok
    })
    private void photoAuthInfoOnClick(View v){
        switch (v.getId()){
            case R.id.btn_ok:
                UtilsPhotoNoZoom.getInstance().init(this,this).showDialog(this);
                break;
            case R.id.tv_ok:
                if (null != picString) {
                    showProgressDialog();
                    ConnectionManager.getInstance().upLoadFile(this, picString, this);
                }else{
                    showToast("请先设置要上传的照片");
                }
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
        picString = UtilsSetPhoto.getInstance().savaBitmap(ib);

    }


    @Override
    public void OnLoadEnd(String ret) {
             dismissProgressDialog();
            XgoLog.e("ret:" +ret);
            try {
                String s = Utils.deCodeString(ret);
                XgoLog.e("s:" + s);
                UploadBean upLoadBean = App.getInstance().getBeanFromJson(s, UploadBean.class);
                IdentificationCredential identificationCredential = new IdentificationCredential();
                identificationCredential.identification_id = identification_id;
                identificationCredential.identification_gid = identification_gid;
                identificationCredential.credential_number = ""+m_tv_credential_number.getText().toString();
                identificationCredential.credential_photo_url = upLoadBean.resData.thumburl;
                DbManager db = DbUtil.getInstance().getDbManager();
                if (IS_EDIT){
                    KeyValue[] keyValue = new KeyValue[]{
                            new KeyValue("credential_number",""),
                            new KeyValue("credential_photo_url",upLoadBean.resData.thumburl)
                    };
                    db.update(IdentificationCredential.class, WhereBuilder.b("identification_id","=",identification_id).and("identification_gid","=",identification_gid),keyValue);

                }else {
                    db.save(identificationCredential);
                }
                finish();
            } catch (DbException e) {
                e.printStackTrace();
            }

    }
}
