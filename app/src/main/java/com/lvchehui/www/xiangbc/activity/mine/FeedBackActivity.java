package com.lvchehui.www.xiangbc.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

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
import com.lvchehui.www.xiangbc.view.mutilRadioGroup.MyRadioGroup;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by 张灿能 on 2016/5/26.
 * 作用：用户反馈
 */
@ContentView(R.layout.activity_feedback)
public class FeedBackActivity extends BaseActivity implements ConnectionUtil.OnDataLoadEndListener {
    @ViewInject(R.id.title_view)
    private TitleView m_title_view;
    @ViewInject(R.id.l2)
    private LinearLayout m_l2;
    @ViewInject(R.id.tv_ride_des)
    private TextView m_tv_ride_des; //@string/ride_des;
    @ViewInject(R.id.tv_fun_des)
    private TextView m_tv_fun_des; //@string/fun_des;
    /*@ViewInject(R.id.textView2)
    private TextView m_textView2; //反馈内容;*/
    @ViewInject(R.id.add_content)
    private EditText m_add_content;
    @ViewInject(R.id.tv_feedback_tj)
    private TextView m_tv_feedback_tj; //提交;
    @ViewInject(R.id.radiogroup_use_car)
    private MyRadioGroup m_radiogroup_use_car;

    @ViewInject(R.id.radiogroup_function_cate)
    private MyRadioGroup m_radiogroup_function_cate;
    private String use_car_category;
    private String function_category;
    private String content = "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(m_title_view, getString(R.string.feedback));
        Utils.hideInput(this);
        m_radiogroup_use_car.setOnCheckedChangeListener(new MyRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(MyRadioGroup group, int checkedId) {
                RadioButton radioBtn = (RadioButton) findViewById(checkedId);
                use_car_category = radioBtn.getText().toString();
            }
        });
        m_radiogroup_function_cate.setOnCheckedChangeListener(new MyRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(MyRadioGroup group, int checkedId) {
                RadioButton radioBtn = (RadioButton) findViewById(checkedId);
                function_category = (String) radioBtn.getText();
            }
        });
    }
    @Event(value = R.id.tv_feedback_tj)
    private void tjFeedBack(View v){
        if (StringUtils.isEmpty(use_car_category)){
            showToast(getString(R.string.please_choose_ride_type));
            return;
        }
        if (StringUtils.isEmpty(function_category)){
            showToast(getString(R.string.please_choose_funtion_type));
            return;
        }

        showProgressDialog();
        ConnectionManager.getInstance().feedbackSubmit(this,(String) SPUtil.getInstance(this).get(Constants.USER_GID,""),use_car_category,function_category,content,this);
    }

    @Override
    public void OnLoadEnd(String ret) {
        dismissProgressDialog();
        BaseBean baseBean = App.getInstance().getBeanFromJson(ret, BaseBean.class);
        showToast(baseBean.resMsg);
        if (baseBean.errCode != -1){
            finish();
        }

    }
}
