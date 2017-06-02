package com.lvchehui.www.xiangbc.activity.freeride;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.bean.FilterQuotationListBean;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.photoselector.DemoActivity;
import com.lvchehui.www.xiangbc.utils.photoselector.GalleryActivity;
import com.lvchehui.www.xiangbc.utils.photoselector.ImageSelectorActivity;
import com.lvchehui.www.xiangbc.view.CustomScrollView;
import com.lvchehui.www.xiangbc.view.ImageCycleView;
import com.lvchehui.www.xiangbc.view.TitleView;
import com.lvchehui.www.xiangbc.view.dialog.CustomAlertDialog;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * Created by 张灿能 on 2016/5/18.
 * 作用：顺风车详情页面
 */
@ContentView(R.layout.activity_ride_details)
public class RideDetailsActivity extends BaseActivity implements CustomScrollView.OnScrollListener, ImageCycleView.ImageCycleViewListener {


    @ViewInject(R.id.image_cv)
    ImageCycleView m_image_cv;

    @ViewInject(R.id.title_view)
    private TitleView m_titleView;

    @ViewInject(R.id.custom_scollView)
    private CustomScrollView m_custom_scrollview;

    /*@ViewInject(R.id.tv_explanation)
    private TextView m_tv_explanation;*/

    @ViewInject(R.id.tv_car_name)
    private TextView m_tv_car_name;
    @ViewInject(R.id.tv_ok)
    private TextView m_tv_ok;

    @ViewInject(R.id.ll_feedback_about_fleet)
    private LinearLayout m_ll_feedback_about_fleet;

    @ViewInject(R.id.tv_include_cost)
    private TextView m_tv_include_cost;
    @ViewInject(R.id.tv_include_tax_sum)
    private TextView m_tv_include_tax_sum;
    /*@ViewInject(R.id.ll_day_total)
    private LinearLayout m_ll_day_total;*/
    private int color;
    private int m_title_height;
    private ArrayList<String> imgLists = new ArrayList<>();
    private FilterQuotationListBean.ResDataBean filterDataBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleView(m_titleView, "丰田海狮", "");
        EventBus.getDefault().registerSticky(this);
        color = getResources().getColor(R.color.transparent);
        m_titleView.setTitleBackgroundColor(color);
        m_custom_scrollview.setOnScrollListener(this);
        imgLists.add("http://img1.imgtn.bdimg.com/it/u=2951585222,2666953953&fm=21&gp=0.jpg");
        imgLists.add("http://image.11467.com/79/11/79115151616715.jpg");
        imgLists.add("http://www.guibinzhilv.com/uploads/140729/1-140H91R92aZ.jpg");
        m_image_cv.setImageResources(imgLists, this);


        // m_tv_explanation.setVisibility(View.GONE);
        m_tv_ok.setText("预订");
//        addFaceItem(6);
    }

    /*private void addFaceItem(int num) {
        for (int i = 0; i < num; i++) {
            View m_item_fare_total = getLayoutInflater().inflate(R.layout.item_fare_total, null);
            TextView m_tv_fare = (TextView) m_item_fare_total.findViewById(R.id.tv_fare);
            TextView m_tv_day = (TextView) m_item_fare_total.findViewById(R.id.tv_day);
            m_tv_fare.setText("￥320");
            m_tv_day.setText("第" + i + "天");
            m_ll_day_total.addView(m_item_fare_total);
        }
    }*/


    @Event({R.id.tv_ok, R.id.tv_include_cost, R.id.ll_feedback_about_fleet})
    private void riderDetailsOnClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ok:
                startActivity(new Intent(this, PlaceOrderActivity.class));
                break;
            case R.id.ll_feedback_about_fleet:
                showToast("此处应显示车队评价列表");
                break;
            case R.id.tv_include_cost:
                /*CustomTextDialog customTextDialog = new CustomTextDialog(this);
                customTextDialog.setCustomText(getResources().getString(R.string.expand));
                customTextDialog.setTitle("座位险");
                customTextDialog.setCancelable(true);
                customTextDialog.show();*/

                CustomAlertDialog dialog = new CustomAlertDialog(this);
                dialog.builder().setTitle("座位险")
                        .setMsg(getString(R.string.expand))
                        .setNegativeButton("关闭", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                dialog.show();
                break;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            m_title_height = m_titleView.getHeight();
        }
    }

    @Override
    public void onScroll(int scrollY) {
        if (scrollY >= m_title_height) {
            color = getResources().getColor(R.color.title_bg_color);

        } else {
            color = getResources().getColor(R.color.transparent);
        }
        m_titleView.setTitleBackgroundColor(color);
    }

    @Override
    public void onImageClick(int position, View imageView) {
        Intent data = new Intent(this, GalleryActivity.class);
        data.putExtra(Constants.SOURCE, RideDetailsActivity.class.getSimpleName());
        data.putStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT, imgLists);
        data.putExtra("position", position);
        startActivityForResult(data, DemoActivity.REQUEST_CODE);
    }


    @Subscriber
    public void getFilterQuotation(FilterQuotationListBean.ResDataBean filterDataBean) {
        this.filterDataBean = filterDataBean;
        FilterQuotationListBean.ResDataBean.CarsInfoBean carsInfoBean = filterDataBean.cars_info.get(0);
        FilterQuotationListBean.ResDataBean.MotorcadeInfoBean motorcadeInfoBean = filterDataBean.motorcade_info.get(0);
        setTitleView(m_titleView, carsInfoBean.motorcade_name, "");
        m_tv_car_name.setText(String.format(getString(R.string.vehicle_info), carsInfoBean.seat_number_just, carsInfoBean.vehicle_model, carsInfoBean.vehicle_color));
       /*
        m_tv_seat_risks.setText(String.format(getString(R.string.seat_risks), carsInfoBean.passanger_premium));
        */
      /*  setQuotedCost(quotationInfo);
        ArrayList<String> listParams = getCredentials(carsInfoBean, motorcadeInfoBean);
        */
       /* setCredentials(listParams);*/

        m_tv_include_tax_sum.setText("￥" + filterDataBean.motorcade_quotation_money);
        //  m_tv_prepay_subscription.setText("￥" + filterDataBean.deposit);
        // EventBus.getDefault().removeStickyEvent(filterDataBean.getClass());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
