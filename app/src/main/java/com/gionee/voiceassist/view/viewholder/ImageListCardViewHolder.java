package com.gionee.voiceassist.view.viewholder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gionee.voiceassist.ImageListCardBigPictureActivity;
import com.gionee.voiceassist.R;
import com.gionee.voiceassist.coreservice.sdk.module.screen.message.ImageListCardPayload;
import com.gionee.voiceassist.datamodel.card.CardEntity;
import com.gionee.voiceassist.datamodel.card.ImageListCardEntity;
import com.gionee.voiceassist.util.LogUtil;
import com.gionee.voiceassist.util.component.ImageLoader;
import com.gionee.voiceassist.view.adapter.DialogSubItemPool;


import java.util.ArrayList;

/**
 * Created by liyingheng on 1/16/18.
 */

public class ImageListCardViewHolder extends BaseViewHolder {

    private ImageLoader mImageLoader;
    private LayoutInflater mInflater;
    private LinearLayout mListContainer;
    private Context mCtx;
    private static final int MAX_IMAGE_SHOW_COUNT = 5;
    private DialogSubItemPool mSubItemPool;

    public ImageListCardViewHolder(View itemView, DialogSubItemPool itemPool) {
        super(itemView);
        mInflater = LayoutInflater.from(itemView.getContext());
        mSubItemPool = itemPool;
        mImageLoader = new ImageLoader(itemView.getContext());
        mListContainer = (LinearLayout) itemView.findViewById(R.id.lyt_list_container);
        mCtx = itemView.getContext();
    }

    @Override
    public void bind(CardEntity payload) {
        ImageListCardEntity imageListCardPayload = (ImageListCardEntity) payload;
        ArrayList<String> imageUrlList = new ArrayList<>();
        for(ImageListCardEntity.ImageItem imageItem : imageListCardPayload.getImageItems()) {
            imageUrlList.add(imageItem.imgSrc);
        }
        /*// 一次最多显示 MAX_IMAGE_SHOW_COUNT 张图片
        int maxCount = imageItemSize < MAX_IMAGE_SHOW_COUNT ? imageItemSize : MAX_IMAGE_SHOW_COUNT;
        for(int i=0; i<maxCount; i++) {
            bindItem(imageListCardPayload.getImageItems().get(i));
        }*/

        ImageListCardEntity.ImageItem firstImage = imageListCardPayload.getImageItems().get(0);
         bindContainerImge(firstImage, imageUrlList);
    }

    private void bindItem(ImageListCardEntity.ImageItem item) {
        String imgSrc = item.imgSrc;
        View itemView = mSubItemPool.getImageListItemView(mListContainer);
        ImageView ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
        mImageLoader.loadImageFromRemote(imgSrc, ivImage, 75, 75, R.drawable.call_pressed);
        log("subitem add." + " adding subItem url = " + imgSrc);
        mListContainer.addView(itemView);
    }

    private void bindContainerImge(ImageListCardEntity.ImageItem imageItem, final ArrayList<String> imageUrlList) {
        String imgSrc = imageItem.imgSrc;
        View containerView = mInflater.inflate(R.layout.card_item_imagelistcard_container, mListContainer, false);
        ImageView ivImage = (ImageView) containerView.findViewById(R.id.iv_container_image);
        TextView tvImageCount = (TextView) containerView.findViewById(R.id.tv_image_count);
        tvImageCount.setText(String.valueOf(imageUrlList.size()));
        mImageLoader.loadImageFromRemote(imgSrc, ivImage, 120, 160, R.drawable.pic_bg_big);

        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mCtx, ImageListCardBigPictureActivity.class);
                intent.putStringArrayListExtra("imageList", imageUrlList);
                mCtx.startActivity(intent);
            }
        });
        mListContainer.addView(containerView);
    }

    @Override
    public void onRecycled() {
        super.onRecycled();
        int childViewCount = mListContainer.getChildCount();
        log("subitem recycle begin." + " ChildViewCount = " + mListContainer.getChildCount());
        for (int i = childViewCount; i > 0; i--) {
            View subItemView = mListContainer.getChildAt(i - 1);
            mSubItemPool.recycleImageListItemView(subItemView);
            mListContainer.removeViewAt(i - 1);
//            log("subitem recycled." + " removing subItem... ChildViewCount = " + mListContainer.getChildCount());
        }
        log("subitem recycled." + " all subitem removed. ChildViewCount = " + mListContainer.getChildCount());
    }

    public static ImageListCardViewHolder newInstance(ViewGroup parent, DialogSubItemPool subItemPool) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.card_item_listcard_container, parent, false);
        return new ImageListCardViewHolder(itemView, subItemPool);
    }

    private void log(String message) {
        String msg = "ImageListCardView At position " + getAdapterPosition() + ": " + message;
        LogUtil.d("ImageListCard", msg);
    }

}
