package com.lvchehui.www.xiangbc.utils.photoselector.utils;

import java.io.Serializable;

import android.content.Context;
import android.widget.ImageView;

/**
 * ImageLoader
 * Created by Yancy on 2015/12/6.
 */
public interface ImageLoader extends Serializable {
    void displayImage(Context context, String path, ImageView imageView);
}