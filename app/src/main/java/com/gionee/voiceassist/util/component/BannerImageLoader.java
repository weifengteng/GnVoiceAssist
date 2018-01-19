package com.gionee.voiceassist.util.component;

import android.content.Context;
import android.widget.ImageView;

import com.gionee.voiceassist.GnVoiceAssistApplication;
import com.gionee.voiceassist.R;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by twf on 2018/1/18.
 */

public class BannerImageLoader extends ImageLoader {

    private com.gionee.voiceassist.util.component.ImageLoader imageLoader;

    public BannerImageLoader() {
        imageLoader = new com.gionee.voiceassist.util.component.ImageLoader(GnVoiceAssistApplication.getInstance().getApplicationContext());
    }

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        imageLoader.loadImageFromRemote(String.valueOf(path), imageView, 160, 200, R.drawable.pic_bg_big);
//        imageLoader.loadImageFromRemote(String.valueOf(path), imageView);
    }
}
