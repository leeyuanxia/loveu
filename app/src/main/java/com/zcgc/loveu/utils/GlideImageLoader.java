package com.zcgc.loveu.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.engine.ImageEngine;
import com.zcgc.loveu.R;

import jp.wasabeef.glide.transformations.BlurTransformation;


public class GlideImageLoader{

    public static void displayImage(Context context, Object path,ImageView imageView) {
        //Glide 加载图片简单用法
        Glide.with(context).load(path).centerCrop().into(imageView);
    }

    public static void displayImage(Context context, Object path, int placeholder, ImageView imageView) {
        //Glide 加载图片简单用法
        Glide.with(context).load(path).centerCrop().placeholder(placeholder).into(imageView);
    }
    public static void displayImage(Context context, Object path, ImageView imageView,int radius,int sampling) {
        //Glide 加载图片简单用法
        Glide.with(context).load(path)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(radius, sampling)))
                .centerCrop()
                .into(imageView);
    }
}

