package com.lvchehui.www.xiangbc.activity.freeride;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.util.Util;
import com.lvchehui.www.xiangbc.R;
import com.lvchehui.www.xiangbc.app.App;
import com.lvchehui.www.xiangbc.base.BaseListActivity;
import com.lvchehui.www.xiangbc.base.BasePageAdapter;
import com.lvchehui.www.xiangbc.bean.FilterQuotationListBean;
import com.lvchehui.www.xiangbc.utils.ConnectionManager;
import com.lvchehui.www.xiangbc.utils.ConnectionUtil;
import com.lvchehui.www.xiangbc.utils.DateUtil;
import com.lvchehui.www.xiangbc.utils.Utils;

import org.simple.eventbus.EventBus;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.support.v7.widget.RecyclerView.OnClickListener;
import static android.support.v7.widget.RecyclerView.ViewHolder;

/**
 * Created by 张灿能 on 2016/5/18.
 * 作用：顺风车列表
 */
@ContentView(R.layout.activity_ride_list)
public class RideListActivity extends BaseListActivity {

    private static final String EXTRA_START_DATE = "extra_start_date";
    private static final String EXTRA_START_CITY = "extra_start_city";
    private static final String EXTRA_DEST_CITY = "extra_dest_city";
    private static final String EXTRA_PEOPLE_NUM = "extra_people_num";

    @ViewInject(R.id.tv_today)
    private TextView m_tv_today;
    @ViewInject(R.id.tv_tomorrow)
    private TextView m_tv_tomorrow;
    @ViewInject(R.id.tv_yesterday)
    private TextView m_tv_yesterday;

    private int theDay = 0;

    private String mStartDate;
    private String mStartCity;
    private String mDestCity;
    private String nowDay = "7月20日";
    private String mPeoPleNum;


    public static Intent newIntent(Context context,
                                   String peopleNum,
                                   String startDate,
                                   String startCity,
                                   String destCity){
        Intent intent = new Intent(context, RideListActivity.class);
        intent.putExtra(EXTRA_PEOPLE_NUM,peopleNum);
        intent.putExtra(EXTRA_START_DATE, startDate);
        intent.putExtra(EXTRA_START_CITY, startCity);
        intent.putExtra(EXTRA_DEST_CITY, destCity);
        return intent;
    }

    @Event(value = {R.id.tv_today, R.id.tv_tomorrow, R.id.tv_yesterday})
    private void rideOnClick(View v) {
        switch (v.getId()) {
            case R.id.tv_today:
                break;
            case R.id.tv_tomorrow:
                theDay++;
                if (theDay <= 2) {
                    m_tv_yesterday.setTextColor(getResources().getColor(R.color.text_default_yellow));
                    m_tv_yesterday.setClickable(true);
                    m_tv_tomorrow.setTextColor(getResources().getColor(R.color.text_default_yellow));
                    m_tv_tomorrow.setClickable(true);
                }
                if (theDay == 3) {
                    m_tv_tomorrow.setTextColor(getResources().getColor(R.color.warmGrey));
                    m_tv_tomorrow.setClickable(false);
                }
                if (theDay > 3) {
                    theDay = 3;
                    return;
                }
                m_tv_today.startAnimation(AnimationUtils.loadAnimation(getBaseContext(),
                        R.anim.ride_from_right));
                showToast("theDay:" + theDay);
                setDayofMonth();

                break;
            case R.id.tv_yesterday:
                theDay--;
                if (theDay >= -2) {
                    m_tv_yesterday.setTextColor(getResources().getColor(R.color.text_default_yellow));
                    m_tv_yesterday.setClickable(true);
                    m_tv_tomorrow.setTextColor(getResources().getColor(R.color.text_default_yellow));
                    m_tv_tomorrow.setClickable(true);
                }
                if (theDay == -3) {
                    m_tv_yesterday.setTextColor(getResources().getColor(R.color.warmGrey));
                    m_tv_yesterday.setClickable(false);
                }
                if (theDay < -3) {
                    theDay = -3;
                    return;
                }
                m_tv_today.startAnimation(AnimationUtils.loadAnimation(getBaseContext(),
                        R.anim.ride_from_left));

                showToast("theDay:" + theDay);
                setDayofMonth();
                break;
        }
    }

    @Override
    protected void initViews() {
        super.initViews();
        Intent intent = getIntent();
        if (null != intent){
             mPeoPleNum = intent.getStringExtra(EXTRA_PEOPLE_NUM);
            mStartDate = intent.getStringExtra(EXTRA_START_DATE);
            mStartCity = intent.getStringExtra(EXTRA_START_CITY);
            mDestCity = intent.getStringExtra(EXTRA_DEST_CITY);
            setTitleView(mTitleView, mStartCity + "-" + mDestCity);
        } else {
            setTitleView(mTitleView, getString(R.string.free_ride));
        }

        setDayofMonth();
       // initData();
    }

 /*   private void initData() {
        ConnectionManager.getInstance().quotationFilterquotationlist(
                this,mPeoPleNum , "" + DateUtil.getLongTime(mStartDate),
                mStartCity, mDestCity, new filterQuotationOnDataLoadEndListener());
    }

    class filterQuotationOnDataLoadEndListener implements ConnectionUtil.OnDataLoadEndListener{

        @Override
        public void OnLoadEnd(String ret) {
            FilterQuotationListBean baseBean = App.getInstance().getBeanFromJson(ret, FilterQuotationListBean.class);
            if (baseBean.errCode != -1)
            {

            }
        }
    }*/

    private void setDayofMonth() {
        long longTime = DateUtil.getLongTime(mStartDate);
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
        nowDay = sdf.format(new Date(longTime));
        Date date = null;
        try {
            date = sdf.parse(nowDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, theDay);

        m_tv_today.setText(sdf.format(calendar.getTime()));
        onRefresh();

    }

    @Override
    protected List convertToBeanList(String json) {
        FilterQuotationListBean quotationListBean = App.getInstance().getBeanFromJson(json, FilterQuotationListBean.class);

        if (quotationListBean.errCode != -1)
        {
            return quotationListBean.resData;
        }
        return null;
    }

    @Override
    protected BasePageAdapter initAdapter() {
        return new RideListAdapter();
    }

    @Override
    protected boolean isSwipeRefreshLayoutEnabled() {
        return true;
    }

    @Override
    protected int getSizeInPage() {
        return 0;
    }

    @Override
    protected Request initRequest(int start) {
        return ConnectionManager.getInstance().quotationFilterquotationlist(
                this, mPeoPleNum,
                "" + DateUtil.getLongTime(Calendar.getInstance().get(Calendar.YEAR) + "年" + m_tv_today.getText().toString(),
                        DateUtil.TIME_FORMAT_YMD),
                mStartCity, mDestCity, this);
    }

    @Override
    protected boolean isPageEnabled() {
        return false;
    }

    @Override
    protected boolean isDataGot() {
        return false;
    }

    class RideListAdapter extends BasePageAdapter {

        class RideItemViewHolder extends ViewHolder {
            @ViewInject(R.id.root)
            private LinearLayout m_root;

            @ViewInject(R.id.imageView_pic)
            private ImageView m_imageView_pic;

            @ViewInject(R.id.et_campany_name)
            private TextView m_et_campany_name; //考斯特 45座;

            @ViewInject(R.id.textView1)
            private TextView m_textView1; //享八级;

            @ViewInject(R.id.tv_small_title)
            private TextView m_tv_small_title; //接单量120笔;

            @ViewInject(R.id.tv_order_time)
            private TextView m_tv_order_time; //9:30  ～  12:00;

            @ViewInject(R.id.tv_order_num)
            private TextView m_tv_order_num; //接受预订时间;

            @ViewInject(R.id.tv_print)
            private TextView m_tv_print; //¥3500;

            public RideItemViewHolder(View itemView) {
                super(itemView);
                x.view().inject(this,itemView);
            }
        }

        @Override
        protected ViewHolder initViewHolder(ViewGroup viewGroup, int viewType) {
            View inflate = View.inflate(viewGroup.getContext(), R.layout.item_ride, null);
            return new RideItemViewHolder(inflate);
        }

        @Override
        public void doBindViewHolder(ViewHolder viewHoder, int position) {

            if (viewHoder instanceof RideItemViewHolder) {
                RideItemViewHolder rideItemViewHolder = (RideItemViewHolder) viewHoder;
                FilterQuotationListBean.ResDataBean filterDataBean = (FilterQuotationListBean.ResDataBean) mItems.get(position);
                FilterQuotationListBean.ResDataBean.CarsInfoBean carsInfoBean = filterDataBean.cars_info.get(0);
                ConnectionUtil.getInstance().loadImage(rideItemViewHolder.m_imageView_pic,
                        carsInfoBean.car_photos_url.in.get(0));
                rideItemViewHolder.m_et_campany_name.setText(carsInfoBean.vehicle_model + carsInfoBean.seat_number_just);
                rideItemViewHolder.m_tv_print.setText(" ¥" + filterDataBean.motorcade_quotation_money);
                FilterQuotationListBean.ResDataBean.MotorcadeInfoBean motorcadeInfoBean = filterDataBean.motorcade_info.get(0);
                String orderQuantity = getResources().getString(R.string.order_quantity);
                int volume = motorcadeInfoBean.volume;
                String orderQuantityStr = String.format(orderQuantity, volume);
                rideItemViewHolder.m_tv_small_title.setText(orderQuantityStr);
                String startBookingTimeStr = Utils.getDateToStr(filterDataBean.start_booking_time, "hh:mm");
                String endBookingTimeStr = Utils.getDateToStr(filterDataBean.end_booking_time, "hh:mm");
                rideItemViewHolder.m_tv_order_time.setText( startBookingTimeStr+ "~" + endBookingTimeStr);
                rideItemViewHolder.m_root.setOnClickListener(new RideItemOnclickListener(filterDataBean));
            }
        }

        class RideItemOnclickListener implements OnClickListener {
            FilterQuotationListBean.ResDataBean filterDataBean;

            public RideItemOnclickListener(FilterQuotationListBean.ResDataBean filterDataBean) {
                this.filterDataBean = filterDataBean;
            }

            @Override
            public void onClick(View v) {
                startActivity(new Intent(RideListActivity.this, RideDetailsActivity.class));
                EventBus.getDefault().postSticky(filterDataBean);
            }
        }
    }
}
