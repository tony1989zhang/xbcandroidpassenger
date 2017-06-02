package com.lvchehui.www.xiangbc.activity.mine;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.utils.DateUtil;
import com.lvchehui.www.xiangbc.utils.PixelUtil;
import com.lvchehui.www.xiangbc.view.DividerItemDecoration;
import com.lvchehui.www.xiangbc.view.TitleView;
import com.lvchehui.www.xiangbc.view.popupwindow.CommonAdapter;
import com.lvchehui.www.xiangbc.view.popupwindow.CommonViewHolder;
import com.lvchehui.www.xiangbc.view.popupwindow.PopupWindowListView;
import com.lvchehui.www.xiangbc.view.wrap.TestAdapter;
import com.lvchehui.www.xiangbc.view.wrap.WrapRecyclerView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_member_layout)
public class MemberActivity extends BaseActivity {
    @ViewInject(R.id.wrapRecyclerView1)
    private WrapRecyclerView mListView;
    @ViewInject(R.id.lv_spiner)
    private LinearLayout m_lv_spiner;
    @ViewInject(R.id.text_month_year)
    private TextView m_text_month_year;

    @ViewInject(R.id.title_view)
    private TitleView m_titleView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setTitleView(m_titleView,"我的伙伴");
        iniRecyclerView();


        m_text_month_year.setText(DateUtil.refFormatDate(0,"月/"));

    }

    private void iniRecyclerView() {
        ArrayList mList = setData();
        TestAdapter mTestAdapter = new TestAdapter(mList);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        mListView.setLayoutManager(manager);
        mListView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        View inflate = getLayoutInflater().inflate(R.layout.activity_member_layout_hear, null);
        inflate.setLayoutParams(params);
        mListView.addHeaderView(inflate);
        mListView.setAdapter(mTestAdapter);
    }


    @Event(value= R.id.lv_spiner)
    private void onSpiner(View v){
        initpopupwindow();
    }
    private ArrayList setData() {
        ArrayList mList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            mList.add("my love " + i);
        }
        return mList;
    }

    public void initpopupwindow(){

      final  List<String> listDatas = new ArrayList<>();
        listDatas.add(DateUtil.refFormatDate(-2));
        listDatas.add(DateUtil.refFormatDate(-1));
        listDatas.add(DateUtil.refFormatDate(0));
        listDatas.add(DateUtil.refFormatDate(1));
        listDatas.add(DateUtil.refFormatDate(2));


        CommonAdapter commonAdapter = new DateAdapter(this,listDatas,R.layout.spinner_list_item);
        final PopupWindowListView popupWindowListView = new PopupWindowListView(this, PixelUtil.dp2px(this, 107), commonAdapter);
        popupWindowListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                m_text_month_year.setText(DateUtil.refFormatDate(position-2,"月/"));
                showToast("当前日期：" + DateUtil.refFormatDate(position - 2, "月/"));
                popupWindowListView.dismiss();
            }
        });
        popupWindowListView.showAsDropDown(m_lv_spiner);
    }


    class DateAdapter extends CommonAdapter<String>{

        public DateAdapter(Context context, List<String> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(CommonViewHolder helper, String item) {
            helper.setText(R.id.tv_tinted_spinner,item);
        }
    }
}
