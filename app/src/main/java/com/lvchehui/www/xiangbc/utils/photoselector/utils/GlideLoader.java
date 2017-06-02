package com.lvchehui.www.xiangbc.utils.photoselector.utils;

import android.content.Context;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.lvchehui.www.xiangbc.R;

/**
 * GlideLoader
 * Created by Yancy on 2015/12/6.
 * 修改:2016/5/24 张灿能
 */
public class GlideLoader implements ImageLoader {

    @Override
    public void displayImage(Context context, String path, ImageView imageView) {
        Glide.with(context)
                .load(path)
                .placeholder(R.mipmap.imageselector_photo)
                .centerCrop()
                .into(imageView);
    }

}
