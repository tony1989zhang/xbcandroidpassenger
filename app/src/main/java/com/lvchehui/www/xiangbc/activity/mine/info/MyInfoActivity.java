package com.lvchehui.www.xiangbc.activity.mine.info;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.activity.mine.WebActivity;
import com.lvchehui.www.xiangbc.app.App;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.bean.BaseBean;
import com.lvchehui.www.xiangbc.bean.UploadBean;
import com.lvchehui.www.xiangbc.db.UserInfoTable;
import com.lvchehui.www.xiangbc.impl.OnOperationListener;
import com.lvchehui.www.xiangbc.utils.ConnectionManager;
import com.lvchehui.www.xiangbc.utils.ConnectionUtil;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.DateUtil;
import com.lvchehui.www.xiangbc.utils.DbUtil;
import com.lvchehui.www.xiangbc.utils.PickersUtil;
import com.lvchehui.www.xiangbc.utils.SPUtil;
import com.lvchehui.www.xiangbc.utils.StringUtils;
import com.lvchehui.www.xiangbc.utils.UtilsSetPhoto;
import com.lvchehui.www.xiangbc.utils.XgoLog;
import com.lvchehui.www.xiangbc.view.TitleView;
import com.lvchehui.www.xiangbc.view.dialog.ChooseWayDialog;
import com.lvchehui.www.xiangbc.view.dialog.CustomEditDialog;
import com.lvchehui.www.xiangbc.view.wheelData.DateChooseWheelViewDialog;

import org.xutils.common.util.DensityUtil;
import org.xutils.ex.DbException;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * 作用：个人信息页
 */
@ContentView(R.layout.activity_my_info)
public class MyInfoActivity extends BaseActivity implements OnOperationListener, UtilsSetPhoto.PhotoResultIml {

    @ViewInject(R.id.title_view)
    private TitleView m_title_view;

    @ViewInject(R.id.tv_nickname)
    private TextView m_tv_nickname;

    @ViewInject(R.id.tv_gender)
    private TextView m_tv_gender;

    @ViewInject(R.id.tv_birthday)
    private TextView m_tv_birthday;

    @ViewInject(R.id.tv_email)
    private TextView m_tv_email;

    @ViewInject(R.id.tv_credit_value)
    private TextView m_tv_credit_value;

    @ViewInject(R.id.iv_avatar)
    private ImageView m_iv_avatar;

    private String picUrl = "";

    private CustomEditDialog customEditDialog;
    private boolean isReset;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitleView(m_title_view, getString(R.string.personal_info));
        loadView();
        if (null == customEditDialog) {
            customEditDialog = new CustomEditDialog(this);
            customEditDialog.setEditText("");
            // customEditDialog.setEditHint("修改称呼");
            customEditDialog.setTitle(getString(R.string.input_nickname));
        }

        customEditDialog.setOperationListener(this);
        m_tv_gender.setText(getString(R.string.man));
    }

    private void loadView() {
        try {
            UserInfoTable userInfoTable = DbUtil.getInstance().getDbManager().findFirst(UserInfoTable.class);
            if (null == userInfoTable) {
                return;
            }

            m_tv_nickname.setText("" + userInfoTable.nick_name);

            String genderStr = "";
            if (userInfoTable.sex == 0) {
                genderStr = getString(R.string.man);
            } else if (userInfoTable.sex == 1) {
                genderStr = getString(R.string.woman);
            } else {
                genderStr = getString(R.string.unknown);
            }
            m_tv_gender.setText(genderStr);

            m_tv_birthday.setText(DateUtil.getStrTime(userInfoTable.birthday, true));

            m_tv_email.setText(userInfoTable.email);

            m_tv_credit_value.setText("" + userInfoTable.credit_score);

            ImageOptions imageOptions = new ImageOptions.Builder().setSize(DensityUtil.dip2px(120), DensityUtil.dip2px(120))
                    .setCircular(true)
                    .setRadius(DensityUtil.dip2px(5)).setCrop(true)
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP).setLoadingDrawableId(R.mipmap.default_sex_man_icon)
                    .setFailureDrawableId(R.mipmap.default_sex_man_icon).build();
            x.image().bind(m_iv_avatar, userInfoTable.head_url, imageOptions);

        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Event({R.id.iv_avatar, R.id.ll_nickname, R.id.ll_gender, R.id.ll_birthday, R.id.ll_email,
            R.id.ll_credit})
    private void myInfoOnClick(View v) {
        isReset = true;
        showToast("点击效果");
        switch (v.getId()) {
            case R.id.iv_avatar:
                UtilsSetPhoto.getInstance().showDialog(this, m_iv_avatar);
                break;
            case R.id.ll_nickname:
                customEditDialog.show();
                break;
            case R.id.ll_gender:
                PickersUtil.getInstance().pickGender(MyInfoActivity.this, m_tv_gender);
                // 切换性别后默认头像需要跟随改变吗？
                // setSex();
                break;
            case R.id.ll_birthday:
                PickersUtil.getInstance().pickDate(MyInfoActivity.this, m_tv_birthday, PickersUtil.BIRTHDAY_DATE_FORMAT);
                // setBriday();
                break;
            case R.id.ll_email:
                Intent intent = new Intent(MyInfoActivity.this, ChangeEmailActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.ll_credit:
                startActivity(new Intent(MyInfoActivity.this, CreditActivity.class));
                break;
            default:
                break;
        }
    }

    private void setSex() {
        final ArrayList<String> sexArr = new ArrayList<>();
        sexArr.add("先生");
        sexArr.add("女士");

        ChooseWayDialog chooseWayDialog = new ChooseWayDialog(this);
        chooseWayDialog.settitle("选择您的性别");
        chooseWayDialog.setData(sexArr.get(0), sexArr.get(1), null);
        chooseWayDialog.setWayBack(new ChooseWayDialog.ChooseBack() {
            @Override
            public void wayback(int i) {
                m_tv_gender.setText(sexArr.get(i));
            }
        });
        chooseWayDialog.show();
    }

    private void setBriday() {
        Calendar calendar = Calendar.getInstance();
        String nowTime = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
                + calendar.get(Calendar.DAY_OF_MONTH) + "";

        DateChooseWheelViewDialog endDateChooseDialog = new DateChooseWheelViewDialog(MyInfoActivity.this,
                new DateChooseWheelViewDialog.DateChooseInterface() {
                    @Override
                    public void getDateTime(String time, boolean longTimeChecked) {
                        m_tv_birthday.setText(time);
                    }
                }, true);
        endDateChooseDialog.setTimePickerGone(true);
        endDateChooseDialog.setDateDialogTitle("结束时间");
        endDateChooseDialog.showDateChooseDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            UtilsSetPhoto.getInstance().photoResult(requestCode, data, this, this);
        } else if (resultCode == RESULT_FIRST_USER) {
            String strExtra = data.getStringExtra(Constants.EMAIL_CHANGE_TYPE);
            m_tv_email.setText(strExtra);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        customEditDialog = null;
        setContentView(R.layout.view_null);
    }

    @Override
    public void onLeftClick() {
        // TODO Auto-generated method stub
        showToast("左");
        customEditDialog.cancel();
    }

    @Override
    public void onRightClick() {
        showToast("右");
        customEditDialog.cancel();
        m_tv_nickname.setText(customEditDialog.getEditText());
    }

    @Override
    public void OnPotoResult(Bitmap ib) {
        m_iv_avatar.setImageBitmap(ib);
        final String xbc = UtilsSetPhoto.getInstance().savaBitmap(ib);
        ConnectionManager.getInstance().upLoadFile(this, xbc, new ConnectionUtil.OnDataLoadEndListener() {
            @Override
            public void OnLoadEnd(String ret) {
                if (StringUtils.isEmpty(ret)) {
                    return;
                }
                UploadBean beanFromJson = App.getInstance().getBeanFromJson(ret, UploadBean.class);
                XgoLog.e("ret:" + ret);
                XgoLog.e("beanFromJson:" + beanFromJson.toString());
                showToast(ret);
                Intent intent = new Intent(MyInfoActivity.this, WebActivity.class);
                intent.putExtra(WebActivity.WEB_EXT_URL, beanFromJson.resData.url);
                startActivity(intent);
            }
        });
    }

    @Override
    public void finish() {
        if (isReset) {
            showProgressDialog();
            String sexStr = "";
            if (getString(R.string.man).equals(m_tv_gender.getText().toString())) {
                sexStr = "0";
            } else if (getString(R.string.woman).equals(m_tv_gender.getText().toString())) {
                sexStr = "1";
            } else {
                sexStr = "2";
            }

            ConnectionManager.getInstance().resetUserInfo(this, (String) SPUtil.getInstance(this).get(Constants.USER_GID, ""),
                    m_tv_nickname.getText().toString(), sexStr,
                    DateUtil.getSubLongStr("" + DateUtil.getLongTime(m_tv_birthday.getText().toString())), m_tv_email.getText().toString(), picUrl, new ConnectionUtil.OnDataLoadEndListener() {
                        @Override
                        public void OnLoadEnd(String ret) {
                            dismissProgressDialog();
                            BaseBean baseBean = App.getInstance().getBeanFromJson(ret, BaseBean.class);
                            if (baseBean.errCode != -1) {
                            }
                        }
                    });
        }
        super.finish();
    }

}
