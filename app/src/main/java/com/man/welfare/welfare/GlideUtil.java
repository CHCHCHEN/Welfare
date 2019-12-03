package com.man.welfare.welfare;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * Glide加载工具类
 */

public class GlideUtil {
    /**
     * 参数1：上下文
     * 参数2：图片地址
     * 参数3：加载图的控件名称
     * 参数4：RequestOptions
     * */
    public static void load(Context context, String url, ImageView imageView, RequestOptions options) {
        Glide.with(context).load(url).apply(options).into(imageView);
    }
}
