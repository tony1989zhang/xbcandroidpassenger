package com.lvchehui.www.xiangbc.activity.itinerary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.utils.photoselector.ImageSelectorActivity;
import com.lvchehui.www.xiangbc.utils.photoselector.adapter.DemoAdapter;
import com.lvchehui.www.xiangbc.view.RatingBarView;
import com.lvchehui.www.xiangbc.view.TitleView;
import com.lvchehui.www.xiangbc.view.dialog.LotterDialog;
import com.lvchehui.www.xiangbc.view.tag.Tag;
import com.lvchehui.www.xiangbc.view.tag.TagListView;
import com.lvchehui.www.xiangbc.view.tag.TagView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 张灿能 on 2016/6/15.
 * 作用：添加评价页面
 */
@ContentView(R.layout.activity_add_eva)
public class AddEvaActivity extends BaseActivity implements RatingBarView.OnRatingListener {

    private static final int[] RATING_STATUS = {R.string.eva_ver_bad,R.string.eva_bad,R.string.eva_general,R.string.eva_good};


    private static final int BIND_TYPE_ONTIME = 1;
    private static final int BIND_TYPE_SERVER = 2;
    private static final int BIND_TYPE_HEALTH = 3;
    private static final int BIND_TYPE_CAR_SITUATION = 4;
    public static final int REQUEST_CODE = 1000;


    @ViewInject(R.id.title_view)
    TitleView m_title_view;

    @ViewInject(R.id.tag_view)
    private TagListView m_tag_view;

    /*@ViewInject(R.id.radiogroup_rate_car)
    private MyRadioGroup m_radiogroup_rate_car;*/

    @ViewInject(R.id.recycler)
    private RecyclerView m_recycler;


    @ViewInject(R.id.rating_ontime)
    private RatingBarView m_rating_ontime;

    @ViewInject(R.id.tv_ontime)
    private TextView m_tv_ontime;

    @ViewInject(R.id.rating_server)
    private RatingBarView m_rating_server;

    @ViewInject(R.id.tv_server)
    private TextView m_tv_server;

    @ViewInject(R.id.rating_health)
    private RatingBarView m_rating_health;

    @ViewInject(R.id.tv_health)
    private TextView m_tv_health;

    @ViewInject(R.id.rating_car_situation)
    private RatingBarView m_rating_car_situation;

    @ViewInject(R.id.tv_car_situation)
    private TextView m_tv_car_situation;

    private DemoAdapter adapter;
    private ArrayList<String> path = new ArrayList<>();



    private final List<Tag> mTags_fun = new ArrayList<>();
    private final String[] funTitles = {"车辆卫生好", "司机服务差", "车况较新", "旅途很愉快", "车辆没有准时", "享包车很便捷"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitleView(m_title_view,"评价");
        setPic();

        setTagData();
        setTagView();

        setRating();
    }

    private void setPic() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 6);
        m_recycler.setLayoutManager(gridLayoutManager);
        adapter = new DemoAdapter(this, path);
        m_recycler.setAdapter(adapter);
    }

    private void setRating() {
        m_rating_ontime.setmClickable(true);
        m_rating_ontime.setBindObject(BIND_TYPE_ONTIME);
        m_rating_ontime.setOnRatingListener(this);

        m_rating_server.setmClickable(true);
        m_rating_server.setBindObject(BIND_TYPE_SERVER);
        m_rating_server.setOnRatingListener(this);

        m_rating_health.setmClickable(true);
        m_rating_health.setBindObject(BIND_TYPE_HEALTH);
        m_rating_health.setOnRatingListener(this);

        m_rating_car_situation.setmClickable(true);
        m_rating_car_situation.setBindObject(BIND_TYPE_CAR_SITUATION);
        m_rating_car_situation.setOnRatingListener(this);
    }


    private void setTagData() {
        mTags_fun.clear();
        for (int i = 0; i < funTitles.length; i++) {
            Tag tag = new Tag();
            tag.setId(i);
            tag.setChecked(false);
            tag.setTitle(funTitles[i]);
            tag.setBackgroundResId(getResources().getColor(R.color.title_bg_color));
            mTags_fun.add(tag);
        }
    }

    private void setTagView() {
        m_tag_view.setTags(mTags_fun, true);
        m_tag_view.setTagViewBackgroundRes(R.drawable.rect_normal_yellow);
        //m_tag_view.setTagViewTextColorRes(R.drawable.bg_yellow_textcolor);
        /*m_tag_view.setTagViewBackgroundRes(R.drawable.tag_bg_selector);*/
        m_tag_view.setTagViewTextColorRes(R.color.black);
        m_tag_view.setTagViewTextSizeResId(R.dimen.text_default_size);
        m_tag_view.setOnTagCheckedChangedListener(new TagListView.OnTagCheckedChangedListener() {
            @Override
            public void onTagCheckedChanged(TagView tagView, Tag tag) {
                showToast(tag.getTitle() + ":" + tag.isChecked());
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            for (String path : pathList) {
                Log.i("ImagePathList", path);
            }
            path.clear();
            path.addAll(pathList);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRating(Object bindObject, int RatingScore) {
        if (RatingScore > 4){
            RatingScore = 4;
        }
        int bindStaus = (int) bindObject;
        switch (bindStaus){
            case BIND_TYPE_ONTIME:
                m_tv_ontime.setText(RATING_STATUS[RatingScore-1]);
                break;
            case BIND_TYPE_SERVER:
                m_tv_server.setText(RATING_STATUS[RatingScore-1]);
                break;
            case BIND_TYPE_HEALTH:
                m_tv_health.setText(RATING_STATUS[RatingScore-1]);
                break;
            case BIND_TYPE_CAR_SITUATION:
                m_tv_car_situation.setText(RATING_STATUS[RatingScore-1]);
                break;
        }
        showToast(getResources().getString(RATING_STATUS[RatingScore - 1]));
    }

    @Event(value = R.id.btn_ok,type = View.OnClickListener.class)
    private void onAddEvaOnClick(View v){

        LotterDialog lotterDialog = new LotterDialog(this);
        lotterDialog.show();
        lotterDialog.setOnLotterDialogOnClick(new LotterDialog.OnLotterDialogOnClick() {
            @Override
            public void lotterDialogOnClick() {
                AddEvaActivity.this.finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        showToast("finish");
    }
}