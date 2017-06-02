package com.lvchehui.www.xiangbc.activity.itinerary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.activity.pay.PaymentForNeedsActivity;
import com.lvchehui.www.xiangbc.base.BaseActivity;
import com.lvchehui.www.xiangbc.bean.GetQuotationedListBean;
import com.lvchehui.www.xiangbc.bean.GetQuotationedListBean.ResDataBean.CarsInfoBean;
import com.lvchehui.www.xiangbc.bean.GetQuotationedListBean.ResDataBean.MotorcadeInfoBean;
import com.lvchehui.www.xiangbc.utils.Constants;
import com.lvchehui.www.xiangbc.utils.StringUtils;
import com.lvchehui.www.xiangbc.utils.photoselector.DemoActivity;
import com.lvchehui.www.xiangbc.utils.photoselector.GalleryActivity;
import com.lvchehui.www.xiangbc.utils.photoselector.ImageSelectorActivity;
import com.lvchehui.www.xiangbc.view.CustomScrollView;
import com.lvchehui.www.xiangbc.view.ImageCycleView;
import com.lvchehui.www.xiangbc.view.TitleView;
import com.lvchehui.www.xiangbc.view.dialog.CustomTextDialog;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * 作用：已报价车队的详情页面
 */
@ContentView(R.layout.activity_quoted_car_details)
public class QuotedCarDetailsActivity extends BaseActivity implements CustomScrollView.OnScrollListener, ImageCycleView.ImageCycleViewListener {

    @ViewInject(R.id.image_cv)
    ImageCycleView m_image_cv;

    @ViewInject(R.id.title_view)
    private TitleView m_titleView;

    @ViewInject(R.id.custom_scollView)
    private CustomScrollView m_custom_scrollview;

    @ViewInject(R.id.tv_explanation)
    private TextView m_tv_explanation;

    @ViewInject(R.id.tv_ok)
    private TextView m_tv_ok;

    @ViewInject(R.id.tv_include_cost)
    private TextView m_tv_include_cost;
    @ViewInject(R.id.tv_car_name)
    private TextView m_tv_car_name;

    @ViewInject(R.id.ll_include_cost_detail)
    private LinearLayout m_ll_include_cost_detail;


    @ViewInject(R.id.tv_include_line)
    private TextView m_tv_include_line;

    @ViewInject(R.id.cb_is_road_and_bridge)
    private CheckBox m_cb_is_road_and_bridge;

    @ViewInject(R.id.tv_seat_risks)
    private TextView m_tv_seat_risks;

    @ViewInject(R.id.cb_is_seat_risks)
    private CheckBox m_cb_is_seat_risks;

    @ViewInject(R.id.cb_is_table)
    private CheckBox m_cb_is_table;

    @ViewInject(R.id.cb_is_parking)
    private CheckBox m_cb_is_parking;

    @ViewInject(R.id.cb_is_gasoline)
    private CheckBox m_cb_is_gasoline;

    @ViewInject(R.id.tv_remarks)
    private TextView m_tv_remarks;
    @ViewInject(R.id.tv_credentials)
    private TextView m_tv_credentials;
    @ViewInject(R.id.tv_prepay_subscription)
    private TextView m_tv_prepay_subscription;
    @ViewInject(R.id.tv_include_tax_sum)
    private TextView m_tv_include_tax_sum;


    private int color;
    private int m_title_height;
    private ArrayList<String> imgLists = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().registerSticky(this);
        color = getResources().getColor(R.color.transparent);
        m_titleView.setTitleBackgroundColor(color);
        m_custom_scrollview.setOnScrollListener(this);
        m_tv_explanation.setVisibility(View.GONE);
        m_tv_ok.setText("预订");
//        addFaceItem(6);
    }

    private void addFaceItem(int num) {
        for (int i = 0; i < num; i++) {
            View m_item_fare_total = getLayoutInflater().inflate(R.layout.item_fare_total, null);
            TextView m_tv_fare = (TextView) m_item_fare_total.findViewById(R.id.tv_fare);
            TextView m_tv_day = (TextView) m_item_fare_total.findViewById(R.id.tv_day);
            m_tv_fare.setText("￥320");
            m_tv_day.setText("第" + i + "天");
            m_ll_include_cost_detail.addView(m_item_fare_total);
        }
    }

    @Subscriber
    public void getQuotationInfo(GetQuotationedListBean.ResDataBean dataBean) {
        setAd(dataBean);
        CarsInfoBean carsInfoBean = dataBean.cars_info.get(0);
        MotorcadeInfoBean motorcadeInfoBean = dataBean.motorcade_info.get(0);
        setTitleView(m_titleView, carsInfoBean.motorcade_name, "");
        m_tv_car_name.setText(String.format(getString(R.string.vehicle_info), carsInfoBean.seat_number_just, carsInfoBean.vehicle_model, carsInfoBean.vehicle_color));
        m_tv_seat_risks.setText(String.format(getString(R.string.seat_risks), carsInfoBean.passanger_premium));
        setQuotedCost(dataBean);
        ArrayList<String> listParams = getCredentials(carsInfoBean, motorcadeInfoBean);
        setCredentials(listParams);
        m_tv_include_tax_sum.setText("￥" + dataBean.sum_money);
        m_tv_prepay_subscription.setText("￥" + dataBean.deposit);
//        EventBus.getDefault().removeStickyEvent(quotationInfo.getClass());

    }

    @NonNull
    private ArrayList<String> getCredentials(CarsInfoBean carsInfoBean, MotorcadeInfoBean motorcadeInfoBean) {
        boolean hasBussinessLicence = !StringUtils.isEmpty(motorcadeInfoBean.business_licence_number);
        boolean hasRoadTransport = !StringUtils.isEmpty(carsInfoBean.road_transport_number);
        boolean hasRoadManageLicense = !StringUtils.isEmpty(motorcadeInfoBean.road_manage_license_number);
        ArrayList<String> listParams = new ArrayList<>();
        if (hasBussinessLicence) {
            String bussinessLicenceStr = "/营业执照";
            listParams.add(bussinessLicenceStr);
        }
        ;
        if (hasRoadTransport) {
            String roadTransport = "/运营证";
            listParams.add(roadTransport);
        }
        if (hasRoadManageLicense) {
            String roadManageLicenseStr = "/道路经营许可证";
            listParams.add(roadManageLicenseStr);
        }
        return listParams;
    }

    private void setCredentials(ArrayList<String> params) {
        String credetialStr = "乘客险/行驶证";
        for (String param : params) {
            credetialStr += param;
        }
        m_tv_credentials.setText(credetialStr);
    }

    private void setQuotedCost(GetQuotationedListBean.ResDataBean dataBean) {
        setQuotedCostCheck(m_cb_is_gasoline, dataBean.is_gasoline);
        setQuotedCostCheck(m_cb_is_road_and_bridge, dataBean.is_road_and_bridge);
        setQuotedCostCheck(m_cb_is_gasoline, dataBean.is_table);
        setQuotedCostCheck(m_cb_is_table, dataBean.is_gasoline);
        setQuotedCostCheck(m_cb_is_parking, dataBean.is_parking);
    }

    private void setQuotedCostCheck(CheckBox cb, int value) {
        if (value == 1) {
            cb.setChecked(true);
        } else if (value == 0) {
            cb.setChecked(false);
        } else {
            cb.setChecked(false);
        }
    }


    private void setAd(GetQuotationedListBean.ResDataBean dataBean) {
        for (String picUrl : dataBean.cars_info.get(0).car_photos_url.in) {
            imgLists.add(picUrl);
        }

        for (String picUrl : dataBean.cars_info.get(0).car_photos_url.out) {
            imgLists.add(picUrl);
        }
        m_image_cv.setImageResources(imgLists, this);
    }

    @Event(value = {R.id.tv_ok, R.id.tv_include_cost})
    private void riderDetailsOnClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ok:
//                startActivity(new Intent(this, PlaceOrderActivity.class));
                startActivity(new Intent(this, PaymentForNeedsActivity.class));//跳转预支付订单页面
                break;
            case R.id.tv_include_cost:
                CustomTextDialog customTextDialog = new CustomTextDialog(this);
                customTextDialog.setCustomText(getString(R.string.expand));
                customTextDialog.setTitle("座位险");
                customTextDialog.setCancelable(true);
                customTextDialog.show();
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
        data.putExtra(Constants.SOURCE, QuotedCarDetailsActivity.class.getSimpleName());
        data.putStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT, imgLists);
        data.putExtra("position", position);
        startActivityForResult(data, DemoActivity.REQUEST_CODE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imgLists.clear();
        m_image_cv = null;
    }
}
