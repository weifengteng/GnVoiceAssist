package com.gionee.voiceassist.view.viewholder;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gionee.voiceassist.R;
import com.gionee.voiceassist.datamodel.card.CardEntity;
import com.gionee.voiceassist.datamodel.card.ListCardEntity;
import com.gionee.voiceassist.util.LogUtil;
import com.gionee.voiceassist.util.component.ImageLoader;
import com.gionee.voiceassist.view.adapter.DialogSubItemPool;


/**
 * Created by liyingheng on 1/16/18.
 */

public class ListCardViewHolder extends BaseViewHolder {

    private LinearLayout listContainer;
    private LayoutInflater mInflater;
    private Context mContext;
    private static final int MAX_IMAGE_SHOW_COUNT = 5;
    private DialogSubItemPool mItemPool;

    private ListCardViewHolder(View itemView, Context context, DialogSubItemPool itemPool) {
        super(itemView);
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mItemPool = itemPool;
        listContainer = (LinearLayout) itemView.findViewById(R.id.lyt_list_container);
    }

    @Override
    public void bind(CardEntity payload) {
        ListCardEntity listCardPayload = (ListCardEntity) payload;
        int listCardItemSize = listCardPayload.getItems().size();
        int maxCount = listCardItemSize > MAX_IMAGE_SHOW_COUNT ? MAX_IMAGE_SHOW_COUNT : listCardItemSize;

        for(int i=0; i < maxCount; i++) {
            bindListItem(listCardPayload.getItems().get(i));
        }
    }

    public static ListCardViewHolder newInstance(ViewGroup parent, DialogSubItemPool subItemPool) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.card_item_listcard_container, parent, false);
        return new ListCardViewHolder(itemView, parent.getContext(), subItemPool);
    }

    private void bindListItem(ListCardEntity.ListItem item) {
        String title = item.title;
        String content = item.content;
        String imgSrc = item.imgSrc;

        boolean hasTitle = !TextUtils.isEmpty(title);
        boolean hasContent = !TextUtils.isEmpty(content);
        //TODO ItemEntity目前暂时没有实现link
        boolean hasLink = false;
        boolean hasImg = !TextUtils.isEmpty(imgSrc);

        View itemView = mItemPool.getListItemView(listContainer);
        TextView tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        TextView tvContent = (TextView) itemView.findViewById(R.id.tv_content);
        TextView tvMoreInfo = (TextView) itemView.findViewById(R.id.tv_moreinfo);
        ImageButton ivImage = (ImageButton) itemView.findViewById(R.id.iv_image);

        tvTitle.setVisibility(hasTitle ? View.VISIBLE:View.GONE);
        tvContent.setVisibility(hasContent ? View.VISIBLE:View.GONE);
        tvMoreInfo.setVisibility(hasLink ? View.VISIBLE:View.GONE);
        ivImage.setVisibility(hasImg ? View.VISIBLE:View.GONE);

        if (hasTitle) {
            tvTitle.setText(title);
        }
        if (hasContent) {
            tvContent.setText(content);
        }
        if (hasLink) {

        }
        if (hasImg) {
            ImageLoader imageLoader = new ImageLoader(mContext);
            imageLoader.loadImageFromRemote(
                    imgSrc,
                    ivImage,
                    50,
                    50 ,
                    R.drawable.call_pressed);
        }

        listContainer.addView(itemView);
        log("subitem add." + " adding subItem url = " + content);
    }

    @Override
    public void onRecycled() {
        super.onRecycled();
        int childViewCount = listContainer.getChildCount();
        log("subitem recycle begin." + " ChildViewCount = " + listContainer.getChildCount());
        for (int i = childViewCount; i > 0; i--) {
            View subItemView = listContainer.getChildAt(i - 1);
            mItemPool.recycleListItemView(subItemView);
            listContainer.removeViewAt(i - 1);
//            log("subitem recycled." + " removing subItem... ChildViewCount = " + listContainer.getChildCount());
        }
        log("subitem recycled." + " all subitem removed. ChildViewCount = " + listContainer.getChildCount());
    }

    private void log(String message) {
        String msg = "ListCardView At position " + getAdapterPosition() + ": " + message;
        LogUtil.d("ListCard", msg);
    }
}
