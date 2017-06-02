package com.lvchehui.www.xiangbc.activity.chooseneeds;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.base.BaseReqActivity;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.DateUtil;
import com.lvchehui.www.xiangbc.utils.PickersUtil;
import com.lvchehui.www.xiangbc.utils.StringUtils;
import com.lvchehui.www.xiangbc.utils.XgoLog;
import com.lvchehui.www.xiangbc.view.TitleView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * Created by 张灿能 on 2016/6/17.
 * 作用：发布需求------单程与往还
 */
@ContentView(R.layout.activity_round_trip)
public class RoundTripActivity extends BaseReqActivity {

    public static final String IS_GO_AND_BACK = "is_go_and_back";
    private static final int REQUEST_CODE_START_AREA = 1002;
    private static final int REQUEST_CODE_DEST_AREA = 1003;
    private static final int REQUEST_CODE_PASSYBY_AREA = 1004;

    @ViewInject(R.id.title_view)
    private TitleView mTitleView;

    @ViewInject(R.id.root)
    private View m_root;

    @ViewInject(R.id.tv_start_time)
    private TextView m_tv_start_time;

    @ViewInject(R.id.ll_back_time)
    private LinearLayout m_ll_back_time;

    @ViewInject(R.id.tv_back_time)
    private TextView m_tv_back_time;

    @ViewInject(R.id.tv_start_area)
    private TextView m_tv_start_rea;

    @ViewInject(R.id.tv_dest_area)
    private TextView m_tv_dest_area;

    /**
     * 途径地点
     */
    @ViewInject(R.id.tv_passby_area)
    private TextView m_tv_passby_area;

    /**
     * 所有的新途径地点存放的视图
     */
    @ViewInject(R.id.ll_area)
    private LinearLayout m_ll_area;

    /**
     * 新增途径地点
     */
    @ViewInject(R.id.ll_add_passby_area)
    private LinearLayout m_ll_add_passy_area;

    int i;
    private ArrayList<TextView> tvArrayLists = new ArrayList<>();
    private boolean isGoANdBack;

    private String mMidwayAddress = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        isGoANdBack = intent.getBooleanExtra(IS_GO_AND_BACK, false);
        if (isGoANdBack) {
            // 往返用车
            setTitleView(mTitleView, getResources().getString(R.string.round_trip));
            m_ll_back_time.setVisibility(View.VISIBLE);
        } else {
            // 单程用车
            setTitleView(mTitleView, getResources().getString(R.string.one_way_trip));
            m_ll_back_time.setVisibility(View.GONE);
        }
    }

    @Event({R.id.ll_start_time, R.id.ll_back_time,
            R.id.ll_start_area, R.id.ll_dest_area,
            R.id.ll_passby_area, R.id.ll_add_passby_area})
    private void GoAndBackTripOnClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.ll_start_time:
                PickersUtil.getInstance().pickTime(RoundTripActivity.this, m_tv_start_time);
                break;
            case R.id.ll_back_time:
                PickersUtil.getInstance().pickTime(RoundTripActivity.this, m_tv_back_time);
                break;
            case R.id.ll_start_area:
                intent = new Intent(this, PoiKeyWordSearchActivity.class);
                startActivityForResult(intent, REQUEST_CODE_START_AREA);
                break;
            case R.id.ll_dest_area:
                intent = new Intent(this, PoiKeyWordSearchActivity.class);
                startActivityForResult(intent, REQUEST_CODE_DEST_AREA);
                break;
            case R.id.ll_passby_area:
                // 途径地点，启动地址搜索页
                intent = new Intent(RoundTripActivity.this, PoiKeyWordSearchActivity.class);
                startActivityForResult(intent, REQUEST_CODE_PASSYBY_AREA);
                break;
            case R.id.ll_add_passby_area:
                if (i > 8) {
                    showToast("最多允许添加9个地址");
                    return;
                }
                // 填充新途径地点的视图
                final LinearLayout ll_passby_area_new = (LinearLayout) getLayoutInflater().inflate(R.layout.ll_passby_area_new, null);
                final TextView tv_passby_area_new = (TextView) ll_passby_area_new.findViewById(R.id.tv_passby_area_new);
                tv_passby_area_new.setId(10000 + i);
                tv_passby_area_new.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(RoundTripActivity.this, PoiKeyWordSearchActivity.class);
                        startActivityForResult(intent, tv_passby_area_new.getId());
                        XgoLog.e("id:" + tv_passby_area_new.getId());
                    }
                });
                // TextView m_tv_address = (TextView) ll_passby_area_new.findViewById(R.id.tv_address);
                // m_tv_address.setText("途径地" + (i + 1) + ":");
                tvArrayLists.add(tv_passby_area_new);
                XgoLog.e("m_tv_address_contentL:" + tv_passby_area_new + "==i:" + i);
                i++;

                ImageView iv_passby_area_new = (ImageView) ll_passby_area_new.findViewById(R.id.iv_passby_area_new);
                /*TextView remove_btn = (android.widget.TextView) ll_passby_area_new.findViewById(R.id.remove_btn);
                remove_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        m_ll_passby_area_new.removeView(ll_passby_area_new);
                        i--;
                    }
                });*/
                iv_passby_area_new.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        m_ll_area.removeView(ll_passby_area_new);
                        i--;
                    }
                });
                m_ll_area.addView(ll_passby_area_new);

                submitRequestBean.midway_address = "[\"谁说的\",\"谁说的\"]";
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_START_AREA:
                    m_tv_start_rea.setText(data.getStringExtra(Constants.DETAILED_ADDRESS));
                    break;
                case REQUEST_CODE_DEST_AREA:
                    m_tv_dest_area.setText(data.getStringExtra(Constants.DETAILED_ADDRESS));
                    break;
                case REQUEST_CODE_PASSYBY_AREA:
                    m_tv_passby_area.setText(data.getStringExtra(Constants.DETAILED_ADDRESS));
            }

            for (int j = 0; j < tvArrayLists.size(); j++) {
                TextView tv = tvArrayLists.get(j);
                int id = tv.getId();
                XgoLog.e("id:" + id);
                if (requestCode == id) {
                    tv.setText(data.getStringExtra(Constants.DETAILED_ADDRESS));
                    break;
                }
            }
        }
    }

    @Override
    public void submitDingD() {

        // 核查订单日期错误
        if (StringUtils.isEmpty(m_tv_start_time.getText().toString())) {
            m_tv_start_time.setError("出发时间不为空");
            showToast("出发时间不为空");
            return;
        }
        if (isGoANdBack && StringUtils.isEmpty(m_tv_back_time.getText().toString())) {
            m_tv_back_time.setError("返回时间不为空");
            showToast("返回时间不为空");
            return;
        }
        if (StringUtils.isEmpty(m_tv_start_rea.getText().toString())) {
            m_tv_start_rea.setError("出发地点不能为空");
            showToast("出发地点不能为空");
            return;
        }
        if (StringUtils.isEmpty(m_tv_dest_area.getText().toString())) {
            m_tv_dest_area.setError("目的地不能为空");
            showToast("目的地不能为空");
            return;
        }

        submitRequestBean.begin_time = DateUtil.getSubLongStr("" + DateUtil.getLongTime(m_tv_start_time.getText().toString(), false));
        submitRequestBean.use_type = "" + 1;
        if (isGoANdBack) {
            submitRequestBean.end_time = DateUtil.getSubLongStr("" + DateUtil.getLongTime(m_tv_back_time.getText().toString(), false));
            submitRequestBean.use_type = "" + 2;
        }
        submitRequestBean.begin_address = m_tv_start_rea.getText().toString();
        submitRequestBean.end_address = m_tv_dest_area.getText().toString();

        super.submitDingD();
    }
}
