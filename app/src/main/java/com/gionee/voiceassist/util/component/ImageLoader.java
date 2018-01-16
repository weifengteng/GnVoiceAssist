package com.gionee.voiceassist.util.component;

import android.content.Context;
import android.support.annotation.IdRes;
import android.widget.ImageView;

import com.gionee.voiceassist.util.DisplayMetricUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

/**
 * Created by liyingheng on 1/16/18.
 */

public class ImageLoader {

    private Context mContext;

    public ImageLoader(Context context) {
        mContext = context;
    }

    public void loadImageFromRes(@IdRes int imageRes) {

    }

    public void loadImageFromDisk(String fileSrc) {

    }

    public void loadImageFromRemote(String imgSrc, ImageView imageView) {
        beginImageLoad(imgSrc)
                .into(imageView);
    }

    public void loadImageFromRemote(String imgSrc, ImageView imageView, int heightDip, int widthDip, int placeholder) {
        beginImageLoad(imgSrc)
                .resize((int)DisplayMetricUtil.dp2px(heightDip), (int)DisplayMetricUtil.dp2px(widthDip))
                .onlyScaleDown()
                .placeholder(placeholder)
                .into(imageView);
    }

    private RequestCreator beginImageLoad(String imgSrc) {
        return Picasso
                .with(mContext)
                .load(imgSrc);
    }



}
