package com.lvchehui.www.xiangbc.view.wheelcity;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lvchehui.www.xiangbc.R;

public class UtilsCity {
    private String cityTxt;

    private UtilsCity() {

    }

    private static UtilsCity u;

    public static UtilsCity getInstance() {
        if (null == u)
            u = new UtilsCity();

        return u;
    }

    public void cityChooseDialog(final Activity activity, final UtilsCityImpl uc) {
        cityChooseDialog(activity, uc, false);
    }

    public void cityChooseDialog(final Activity activity, final UtilsCityImpl uc, boolean visibilityCcity) {
        View view = dialogm(activity, visibilityCcity);
        final MyAlertDialog dialog1 = new MyAlertDialog(activity)
                .builder()
                .setTitle("城市选择")
                // .setMsg("再连续登陆15天，就可变身为QQ达人。退出QQ可能会使你现有记录归零，确定退出？")
                // .setEditText("1111111111111")
                .setView(view)
                .setNegativeButton("取消", new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
        dialog1.setPositiveButton("保存", new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, cityTxt, Toast.LENGTH_SHORT).show();
                uc.cityOnClick(cityTxt);
            }
        });
        dialog1.show();
    }

    private View dialogm(final Activity activity, final boolean visibilityCcity) {
        View contentView = LayoutInflater.from(activity).inflate(
                R.layout.wheelcity_cities_layout, null);
        final WheelView country = (WheelView) contentView
                .findViewById(R.id.wheelcity_country);
        country.setVisibleItems(3);
        country.setViewAdapter(new CountryAdapter(activity));

        final String cities[][] = AddressData.CITIES;
        final String ccities[][][] = AddressData.COUNTIES;
        final WheelView city = (WheelView) contentView
                .findViewById(R.id.wheelcity_city);
        city.setVisibleItems(0);

        // 地区选择
        final WheelView ccity = (WheelView) contentView
                .findViewById(R.id.wheelcity_ccity);
        if (visibilityCcity) {
            ccity.setVisibility(View.VISIBLE);
        } else {
            ccity.setVisibility(View.GONE);
        }
        ccity.setVisibleItems(0);// 不限城市

        country.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateCities(activity, city, cities, newValue);
                if (visibilityCcity) {
                    cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
                            + " | "
                            + AddressData.CITIES[country.getCurrentItem()][city
                            .getCurrentItem()]
                            + " | "
                            + AddressData.COUNTIES[country.getCurrentItem()][city
                            .getCurrentItem()][ccity.getCurrentItem()];
                } else {
                    cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
                            + " | "
                            + AddressData.CITIES[country.getCurrentItem()][city
                            .getCurrentItem()];
                }
            }
        });

        city.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updatecCities(activity, ccity, ccities, country.getCurrentItem(),
                        newValue);
                if (visibilityCcity) {
                    cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
                            + " | "
                            + AddressData.CITIES[country.getCurrentItem()][city
                            .getCurrentItem()]
                            + " | "
                            + AddressData.COUNTIES[country.getCurrentItem()][city
                            .getCurrentItem()][ccity.getCurrentItem()];
                } else {
                    cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
                            + " | "
                            + AddressData.CITIES[country.getCurrentItem()][city
                            .getCurrentItem()];
                }
            }
        });

        ccity.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (visibilityCcity) {
                    cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
                            + " | "
                            + AddressData.CITIES[country.getCurrentItem()][city
                            .getCurrentItem()]
                            + " | "
                            + AddressData.COUNTIES[country.getCurrentItem()][city
                            .getCurrentItem()][ccity.getCurrentItem()];
                } else {
                    cityTxt = AddressData.PROVINCES[country.getCurrentItem()]
                            + " | "
                            + AddressData.CITIES[country.getCurrentItem()][city
                            .getCurrentItem()];
//						+ " | "
//						+ AddressData.COUNTIES[country.getCurrentItem()][city
//								.getCurrentItem()][ccity.getCurrentItem()];
                }
            }
        });

        country.setCurrentItem(1);// 设置北京
        city.setCurrentItem(1);
        ccity.setCurrentItem(1);
        return contentView;
    }

    private void updateCities(Activity activity, WheelView city, String cities[][], int index) {
        ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(activity,
                cities[index]);
        adapter.setTextSize(18);
        city.setViewAdapter(adapter);
        city.setCurrentItem(0);
    }

    /**
     * Updates the ccity wheel
     */
    private void updatecCities(Activity activity, WheelView city, String ccities[][][], int index,
                               int index2) {
        ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(activity,
                ccities[index][index2]);
        adapter.setTextSize(18);
        city.setViewAdapter(adapter);
        city.setCurrentItem(0);
    }


    private class CountryAdapter extends AbstractWheelTextAdapter {
        // Countries names
        private String countries[] = AddressData.PROVINCES;

        /**
         * Constructor
         */
        protected CountryAdapter(Context context) {
            super(context, R.layout.wheelcity_country_layout, NO_RESOURCE);
            setItemTextResource(R.id.wheelcity_country_name);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return countries.length;
        }

        @Override
        protected CharSequence getItemText(int index) {
            return countries[index];
        }
    }

    public interface UtilsCityImpl {
        void cityOnClick(String result);
    }
}
