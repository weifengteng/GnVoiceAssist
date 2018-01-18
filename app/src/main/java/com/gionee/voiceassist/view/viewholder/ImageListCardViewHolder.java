package com.gionee.voiceassist.view.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gionee.voiceassist.R;
import com.gionee.voiceassist.datamodel.card.CardEntity;
import com.gionee.voiceassist.datamodel.card.ImageListCardEntity;
import com.gionee.voiceassist.util.LogUtil;
import com.gionee.voiceassist.util.component.ImageLoader;
import com.gionee.voiceassist.view.adapter.DialogSubItemPool;


/**
 * Created by liyingheng on 1/16/18.
 */

public class ImageListCardViewHolder extends BaseViewHolder {

    private ImageLoader mImageLoader;
    private LayoutInflater mInflater;
    private LinearLayout mListContainer;
    private static final int MAX_IMAGE_SHOW_COUNT = 5;
    private DialogSubItemPool mSubItemPool;

    public ImageListCardViewHolder(View itemView, DialogSubItemPool itemPool) {
        super(itemView);
        mInflater = LayoutInflater.from(itemView.getContext());
        mSubItemPool = itemPool;
        mImageLoader = new ImageLoader(itemView.getContext());
        mListContainer = (LinearLayout) itemView.findViewById(R.id.lyt_list_container);
    }

    @Override
    public void bind(CardEntity payload) {
        ImageListCardEntity imageListCardPayload = (ImageListCardEntity) payload;
        int imageItemSize = imageListCardPayload.getImageItems().size();
        // 一次最多显示 MAX_IMAGE_SHOW_COUNT 张图片
        int maxCount = imageItemSize < MAX_IMAGE_SHOW_COUNT ? imageItemSize : MAX_IMAGE_SHOW_COUNT;
        for(int i=0; i<maxCount; i++) {
            bindItem(imageListCardPayload.getImageItems().get(i));
        }
    }

    private void bindItem(ImageListCardEntity.ImageItem item) {
        String imgSrc = item.imgSrc;
        View itemView = mSubItemPool.getImageListItemView(mListContainer);
        ImageView ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
        mImageLoader.loadImageFromRemote(imgSrc, ivImage, 75, 75, R.drawable.call_pressed);
        log("subitem add." + " adding subItem url = " + imgSrc);
        mListContainer.addView(itemView);
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
