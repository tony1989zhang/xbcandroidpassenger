package com.lvchehui.www.xiangbc.view.scrollingAd;

import java.util.ArrayList;
import java.util.Collection;

import com.bumptech.glide.Glide;
import com.lvchehui.www.xiangbc.R;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageAdapter extends PagerAdapter {

    private ArrayList<String> mData = new ArrayList<String>();

    public ImageAdapter(Collection<String> images) {
        setData(images);
    }

    public void setData(Collection<String> data) {
        if (data != null && !data.isEmpty()) {
            mData.clear();
            mData.addAll(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        View v = LayoutInflater.from(collection.getContext()).inflate(R.layout.item_image, collection, false);
        ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
        Glide.with(collection.getContext())
                .load(mData.get(position))
                .into(imageView);


        collection.addView(v, 0);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
