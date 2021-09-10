package com.zcgc.loveu.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zcgc.loveu.R;


public class GlideImageLoader {

    public static void displayImage(Context context, Object path,ImageView imageView) {
        //Glide 加载图片简单用法
        Glide.with(context).load(path).centerCrop().into(imageView);
    }

    public static void displayImage(Context context, Object path, int placeholder, ImageView imageView) {
        //Glide 加载图片简单用法
        Glide.with(context).load(path).centerCrop().placeholder(placeholder).into(imageView);
    }
}

