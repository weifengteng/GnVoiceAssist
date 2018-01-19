package com.gionee.voiceassist;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.gionee.voiceassist.util.component.BannerImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.List;

/**
 *
 * @author twf
 * @date 2018/1/18
 */

public class ImageListCardBigPictureActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagelistcard_bigmode);

        List<String> imageList = getIntent().getStringArrayListExtra("imageList");
        if(imageList == null || imageList.size() == 0) {
            return;
        }
        Banner banner = (Banner) findViewById(R.id.banner);
        banner.setImages(imageList)
                .setImageLoader(new BannerImageLoader())
                .isAutoPlay(false)
                .setOffscreenPageLimit(2)
                .setBannerStyle(BannerConfig.NUM_INDICATOR);

        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Toast.makeText(ImageListCardBigPictureActivity.this, "Click Position= " + position, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        banner.start();
    }
}
