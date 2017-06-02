package com.lvchehui.www.xiangbc.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.lvchehui.www.xiangbc.Fragment.ContactFragment;
import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.activity.mine.AddContactActivity;
import com.lvchehui.www.xiangbc.base.ActivityForFragmentNormal;
import com.lvchehui.www.xiangbc.utils.XgoLog;

import org.xutils.view.annotation.Event;

/**
 * Created by 张灿能 on 2016/6/15.
 * 作用：联系人列表
 */
public class ContactActivity extends ActivityForFragmentNormal {
    private String mName;
    private String mPhone;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(m_title_view, "联系人列表", "添加");
    }

    @Override
    public Fragment initFragment() {
        return new ContactFragment();
    }

    @Event(value = R.id.title_right_tv,type = View.OnClickListener.class)
    private  void contactOnClick(View v){
        startActivityForResult(new Intent(this, AddContactActivity.class), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (mFragment instanceof ContactFragment) {
                ContactFragment contactFragment = (ContactFragment) mFragment;
                contactFragment.onRefresh();
            }
        }
    }
    public void setContactName(String name){
        mName = name;
    }
    public void setContactPhone(String phone){
        mPhone = phone;
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra(ContactFragment.CONTACT_NAME,mName);
        intent.putExtra(ContactFragment.CONTACT_PHONE,mPhone);
        XgoLog.e("mName:" + mName + "mPhone:" + mPhone);
        setResult(Activity.RESULT_OK, intent);
        super.finish();
    }
}
