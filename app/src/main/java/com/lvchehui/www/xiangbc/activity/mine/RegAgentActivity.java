package com.lvchehui.www.xiangbc.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.app.App;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.bean.AgentApplyBean;
import com.lvchehui.www.xiangbc.bean.AgentApplyDeserializer;
import com.lvchehui.www.xiangbc.utils.ConnectionManager;
import com.lvchehui.www.xiangbc.utils.ConnectionUtil;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.SPUtil;
import com.lvchehui.www.xiangbc.utils.StringUtils;
import com.lvchehui.www.xiangbc.view.HalfListView;
import com.lvchehui.www.xiangbc.view.TitleView;
import com.lvchehui.www.xiangbc.view.dialog.ListDialog;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by 张灿能 on 2016/5/23.
 * 作用：代理商信息输入
 */
@ContentView(R.layout.activity_regagent)
public class RegAgentActivity extends BaseActivity implements ConnectionUtil.OnDataLoadEndListener {
    @ViewInject(R.id.title_view)
    private TitleView m_titleView;
    @ViewInject(R.id.et_campany_name)
    private EditText m_tv_name;
    @ViewInject(R.id.tv_user_telephone_num)
    private TextView m_tv_phone;
    @ViewInject(R.id.tv_profession)
    private TextView m_tv_profession;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(m_titleView, getString(R.string.agent));
        m_tv_phone.setText((String) SPUtil.getInstance(this).get(Constants.USER_PHONE, ""));
    }

    @Event(value = {R.id.ll_profession, R.id.btn_ok})
    private void regAgentOnClick(View v) {
        switch (v.getId()) {

            case R.id.ll_profession:
                setIndustry();
                break;
            case R.id.btn_ok:
                if (StringUtils.isEmpty(m_tv_name.getText().toString())) {
                    showToast("用户名不能为空");
                    return;
                }
                String industry_type = "";
                if (StringUtils.isEmpty(m_tv_profession.getText().toString())) {
                    showToast("职业不能为空");
                    return;
                } else {
                    industry_type = m_tv_profession.getText().toString().substring(0, m_tv_profession.getText().toString().indexOf("-"));
                }

                showProgressDialog(getString(R.string.under_check));
                ConnectionManager.getInstance().agentApply(this, (String) SPUtil.getInstance(this).get(Constants.USER_GID, ""),
                        (String) SPUtil.getInstance(this).get(Constants.USER_PHONE, ""),
                        m_tv_name.getText().toString(), "" + industry_type, this);
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void selectJob(){

    }

    private void setIndustry() {
        final String[] jobs = {"1-学生", "2-企业", "3-自由职业者", "4-政府、事业单位"};

        HalfListView halfListView = new HalfListView(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_sample, jobs);
        halfListView.setAdapter(adapter);
        halfListView.setCacheColorHint(0x000000);


        final ListDialog listDialog = new ListDialog(this, halfListView);
        listDialog.show();
        halfListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                m_tv_profession.setText(jobs[position]);
                listDialog.dismiss();
            }
        });
    }

    @Override
    public void OnLoadEnd(final String ret) {

        x.task().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismissProgressDialog();
                AgentApplyBean agentApplyBean = App.getInstance().getBeanFromJson(ret, AgentApplyBean.class, new AgentApplyDeserializer());
                showToast(agentApplyBean.resMsg);
                if (agentApplyBean.errCode != -1) {
                    Intent intent = new Intent(RegAgentActivity.this, AgentActivity.class);
                    startActivity(intent);
                }
            }
        }, 2000);

    }

}
