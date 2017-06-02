package com.lvchehui.www.xiangbc.activity.mine.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.bean.IdentificationInfoBean;
import com.lvchehui.www.xiangbc.db.IdentificationInfo;
import com.lvchehui.www.xiangbc.impl.OnOperationListener;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.DbUtil;
import com.lvchehui.www.xiangbc.utils.PickersUtil;
import com.lvchehui.www.xiangbc.view.EditText.EditTextWithDel;
import com.lvchehui.www.xiangbc.view.HalfListView;
import com.lvchehui.www.xiangbc.view.TitleView;
import com.lvchehui.www.xiangbc.view.dialog.CustomEditDialog;
import com.lvchehui.www.xiangbc.view.dialog.ListDialog;

import org.xutils.DbManager;
import org.xutils.common.util.KeyValue;
import org.xutils.db.Selector;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by 张灿能 on 2016/5/24.
 * 作用：填写认证信息
 */
@ContentView(R.layout.activity_enter_auth_info)
public class EnterAuthInfoActivity extends BaseActivity implements OnOperationListener {
    private int currentIndex;

    @ViewInject(R.id.root)
    private View m_root;

    @ViewInject(R.id.title_view)
    private TitleView m_titleView;

    @ViewInject(R.id.linearLayout1)
    private LinearLayout m_linearLayout1;

    @ViewInject(R.id.ll_company_name)
    private LinearLayout m_ll_ent_name;

    @ViewInject(R.id.et_campany_name)
    private EditTextWithDel m_tv_name;

    @ViewInject(R.id.ll_city)
    private LinearLayout m_ll_city;

    @ViewInject(R.id.tv_city)
    private TextView m_tv_city;

    @ViewInject(R.id.ll_deputy)
    private LinearLayout m_ll_deputy;

    @ViewInject(R.id.tv_company_name)
    private TextView m_tv_title_name;


    @ViewInject(R.id.tv_deputy)
    private EditTextWithDel m_tv_deputy; //填写法人名称;

    @ViewInject(R.id.ll_industry)
    private LinearLayout m_ll_industry;

    @ViewInject(R.id.tv_industry)
    private TextView m_tv_industry; //选择行业类型;
//
//    @ViewInject(R.id.tv_ok)
//    private TextView m_tv_ok; //保存;

    @ViewInject(R.id.line_deputy)
    private View m_line_deputy;

    @ViewInject(R.id.line_industry)
    private View m_line_industry;

    private CustomEditDialog customEditDialog;
    private static final String ENT_NAME = "企业";
    private static final String TRAVEL_NAME = "旅行社";
    private static final String STU_NAME = "学校";
    private static final String GOV_NAME = "政府/事业单位";
    private String auth_type = "";
    int intExtra;

    private static final int TYPE_NAME = 101;
    private static final int TYPE_DEPUTY = 102;
    private int identification_id;
    private String identification_gid;
    private boolean IS_EDIT;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentExtra();
        setTitleView(m_titleView, getString(R.string.input) + auth_type + getString(R.string.info));
        m_tv_title_name.setText(auth_type + getString(R.string.name_title));
        currentIndex = TYPE_NAME;
        m_tv_name.setHint(getString(R.string.hint_input) + auth_type + getString(R.string.name_title));

        if (null == customEditDialog) {
            customEditDialog = new CustomEditDialog(this);
            customEditDialog.setOperationListener(this);
        }
    }

    private IdentificationInfo initDb(IdentificationInfoBean resDataBean) {
        IS_EDIT = false;
        try {
            Selector<IdentificationInfo> identificationInfoSelector = DbUtil.getInstance()
                    .getDbManager().selector(IdentificationInfo.class)
                    .where("identification_id", "=", resDataBean.identification_id)
                    .and("identification_gid", "=", resDataBean.identification_gid);
            IdentificationInfo identificationInfo = identificationInfoSelector.findFirst();
            this.identification_id = Integer.valueOf(resDataBean.identification_id);
            this.identification_gid = resDataBean.identification_gid;
            return identificationInfo;
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void getIntentExtra() {
        Intent intent = getIntent();
        intExtra = intent.getIntExtra(Constants.AuthPutExtra.AUTH_TYPE, 0);
        IdentificationInfoBean resDataBean = (IdentificationInfoBean) intent
                .getSerializableExtra(Constants.AuthPutExtra.IDENTIFICATIONBEAN_TYPE);

        IdentificationInfo identificationInfo = initDb(resDataBean);
        if (null != identificationInfo) {
            m_tv_name.setText(identificationInfo.name);
            m_tv_city.setText(identificationInfo.address);
            m_tv_deputy.setText(identificationInfo.owner);
            m_tv_industry.setText(identificationInfo.industry_type);
            IS_EDIT = true;
        }
        if (intExtra == Constants.AuthPutExtra.AUTH_ENT) {
            auth_type = ENT_NAME;
        } else if (intExtra == Constants.AuthPutExtra.AUTH_GOV) {
            auth_type = GOV_NAME;
            m_ll_deputy.setVisibility(View.GONE);
            m_line_deputy.setVisibility(View.GONE);

//            m_tv_name.setText(identificationInfo.name);
//            m_tv_city.setText(identificationInfo.address);
            m_tv_deputy.setText("");
//            m_tv_industry.setText(identificationInfo.industry_type);
        } else if (intExtra == Constants.AuthPutExtra.AUTH_TRAVEL) {
            auth_type = TRAVEL_NAME;
            m_ll_industry.setVisibility(View.GONE);
            m_line_industry.setVisibility(View.GONE);
            m_tv_industry.setText("");
        } else if (intExtra == Constants.AuthPutExtra.AUTH_STU) {
            auth_type = STU_NAME;
            m_ll_industry.setVisibility(View.GONE);
            m_ll_deputy.setVisibility(View.GONE);
            m_line_industry.setVisibility(View.GONE);
            m_line_industry.setVisibility(View.GONE);
            m_tv_deputy.setText("");
            m_tv_industry.setText("");
        }
    }

    @Event(value = {R.id.ll_company_name, R.id.ll_city, R.id.ll_deputy, R.id.ll_industry, R.id.tv_ok},
            type = View.OnClickListener.class)
    private void entAuthOnClick(View v) {
        switch (v.getId()) {
            case R.id.ll_company_name:
                /*currentIndex = TYPE_NAME;
                customEditDialog.setEditText("");
                customEditDialog.setEditHint(getString(R.string.hint_input) + auth_type + getString(R.string.name_title));
                customEditDialog.setTitle(getString(R.string.edit) + auth_type + getString(R.string.name_title));
                customEditDialog.setEditInputType(InputType.TYPE_CLASS_TEXT);
                customEditDialog.setEditMax(25);
                customEditDialog.show();*/
                break;
            case R.id.ll_city:
                PickersUtil.getInstance().pickCity(this, m_tv_city);
               /* UtilsCity.getInstance().cityChooseDialog(this, new UtilsCity.UtilsCityImpl() {
                    @Override
                    public void cityOnClick(String result) {
                        m_tv_city.setText(result);
                    }
                });*/
                break;
            case R.id.ll_deputy:
                /*currentIndex = TYPE_DEPUTY;
                customEditDialog.setEditText("");
                customEditDialog.setEditHint(getString(R.string.hint_input_legal_personality));
                customEditDialog.setTitle(R.string.input_legal_personality);
                customEditDialog.setEditInputType(InputType.TYPE_CLASS_TEXT);
                customEditDialog.setEditMax(25);
                customEditDialog.show();*/
                break;
            case R.id.ll_industry:
                setIndustry();
                break;
            case R.id.tv_ok:
                IdentificationInfo identificationInfo = new IdentificationInfo();
                identificationInfo.identification_id = identification_id;
                identificationInfo.identification_gid = identification_gid;
                identificationInfo.name = m_tv_name.getText().toString();
                identificationInfo.address = m_tv_city.getText().toString();
                identificationInfo.owner = m_tv_deputy.getText().toString();
                identificationInfo.industry_type = m_tv_industry.getText().toString();
                DbManager db = DbUtil.getInstance().getDbManager();
                try {
                    if (IS_EDIT) {
                        KeyValue[] changeParam = new KeyValue[]{
                                new KeyValue("name", identificationInfo.name),
                                new KeyValue("address", identificationInfo.address),
                                new KeyValue("owner", identificationInfo.owner),
                                new KeyValue("industry_type", identificationInfo.industry_type)
                        };
                        db.update(IdentificationInfo.class, WhereBuilder.b("identification_id", "=", identification_id), changeParam);
                    } else {
                        db.save(identificationInfo);
                    }
                    finish();
                } catch (DbException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void setIndustry() {
        final String[] jobs = {"1-农林牧渔", "2-租凭/商务", "3-房地产业", "4-制造业",
                "5-旅游休闲", "6-批发和零售业", "7-居民服务和其它服务业", "8-协会", "9-文化、体育和娱乐", "10-教育", "11-医院", "12-民政", "13-公安", "14-交通", "15-司法", "16-市政", "17-公共事业", "18-研究院", "19-其它"};

        HalfListView halfListView = new HalfListView(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_sample, jobs);
        halfListView.setAdapter(adapter);
        halfListView.setCacheColorHint(0x000000);


        final ListDialog listDialog = new ListDialog(EnterAuthInfoActivity.this, halfListView);
        listDialog.show();
        halfListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                m_tv_industry.setText(jobs[position]);
                listDialog.dismiss();
            }
        });
    }

    @Override
    public void onLeftClick() {
        customEditDialog.cancel();
    }

    @Override
    public void onRightClick() {
        if (currentIndex == TYPE_NAME) {
            m_tv_name.setText("" + customEditDialog.getEditText());
        } else if (currentIndex == TYPE_DEPUTY) {
            m_tv_deputy.setText("" + customEditDialog.getEditText());
        }
        customEditDialog.cancel();
    }
}
