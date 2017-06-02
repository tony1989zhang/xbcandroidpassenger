package com.lvchehui.www.xiangbc.activity.mine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.lvchehui.www.xiangbc.Fragment.ContactFragment;
import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.app.App;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.bean.BaseBean;
import com.lvchehui.www.xiangbc.utils.ConnectionManager;
import com.lvchehui.www.xiangbc.utils.ConnectionUtil;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.SPUtil;
import com.lvchehui.www.xiangbc.utils.StringUtils;
import com.lvchehui.www.xiangbc.utils.Utils;
import com.lvchehui.www.xiangbc.view.TitleView;
import com.lvchehui.www.xiangbc.view.dialog.ChooseWayDialog;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;

@ContentView(R.layout.activity_add_contact_layout)
public class AddContactActivity extends BaseActivity implements ConnectionUtil.OnDataLoadEndListener {

    @ViewInject(R.id.title_view)
    TitleView titleView;
    @ViewInject(R.id.tv_sj_name_content)
    EditText contentTVName;
    @ViewInject(R.id.tv_phone_content)
    EditText phoneContentTV;

    private int currentIndex;
    private String m_source_name;
    private String contactsGid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(titleView, getString(R.string.hint_contact), getString(R.string.save));

        Intent intent = getIntent();
        m_source_name = intent.getStringExtra(Constants.SOURCE);
        if (!StringUtils.isEmpty(m_source_name) && m_source_name.equals(ContactFragment.class.getSimpleName())) {
            String contactName = intent.getStringExtra(ContactFragment.CONTACT_NAME);
            String contactPhone = intent.getStringExtra(ContactFragment.CONTACT_PHONE);
            contactsGid = intent.getStringExtra(ContactFragment.CONTACTS_GID);
            contentTVName.setText(contactName);
            phoneContentTV.setText(contactPhone);
        }

    }

    @Event(value = { R.id.title_right_tv, R.id.tv_import_contact}, type = View.OnClickListener.class)
    private void addContactOnClick(View v) {
        switch (v.getId()) {
            case R.id.title_right_tv:
                showProgressDialog("提交更改");
                if (StringUtils.isEmpty(m_source_name)) {
                    ConnectionManager.getInstance().addContacts(this, (String) SPUtil.getInstance(this).get(Constants.USER_GID, ""), phoneContentTV.getText().toString(),
                            contentTVName.getText().toString(), this);
                } else {

                    ConnectionManager.getInstance().editContacts(this, (String) SPUtil.getInstance(this).get(Constants.USER_GID, ""), contactsGid,
                            phoneContentTV.getText().toString(), contentTVName.getText().toString(), this);
                }
                break;

            case R.id.tv_import_contact:
                Utils.getCursorRequest(this);
                break;
            default:
                break;
        }


    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void OnLoadEnd(String ret) {
        if (StringUtils.isEmpty(ret)) {
            return;
        }
        dismissProgressDialog();
        BaseBean beanFromJson = App.getInstance().getBeanFromJson(ret, BaseBean.class);
        showToast(beanFromJson.resMsg);
        if (beanFromJson.errCode != -1) {
            setResult(Activity.RESULT_OK);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            HashMap<String, Object> cursorResult = Utils.getCursorResult(data, this);
            final String cursorName = (String) cursorResult.get("name");
            final ArrayList<String> phones = (ArrayList<String>) cursorResult.get("phone");
            ChooseWayDialog chooseWayDialog = new ChooseWayDialog(this);
            chooseWayDialog.settitle(cursorName);
            if (phones.size() > 1) {
                chooseWayDialog.setData(phones.get(0), phones.get(1), null);
            } else {
                chooseWayDialog.setData(phones.get(0), null, null);
            }
            chooseWayDialog.setWayBack(new ChooseWayDialog.ChooseBack() {
                @Override
                public void wayback(int i) {
                    contentTVName.setText(StringUtils.replaceSpace(cursorName));
                    phoneContentTV.setText(StringUtils.replaceSpace(phones.get(i)));
                }
            });
            chooseWayDialog.show();

        }
    }


}
