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
import com.gionee.voiceassist.util.component.ImageLoader;

/**
 * Created by liyingheng on 1/16/18.
 */

public class ListCardViewHolder extends BaseViewHolder {

    private LinearLayout listContainer;
    private LayoutInflater mInflater;
    private Context mContext;

    private ListCardViewHolder(View itemView, Context context) {
        super(itemView);
        mInflater = LayoutInflater.from(context);
        mContext = context;
        listContainer = (LinearLayout) itemView.findViewById(R.id.lyt_list_container);
    }

    @Override
    public void bind(CardEntity payload) {
        ListCardEntity listCardPayload = (ListCardEntity) payload;
        for (ListCardEntity.ListItem item : listCardPayload.getItems()) {
            bindListItem(item);
        }
    }

    public static ListCardViewHolder newInstance(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.card_item_listcard_container, parent, false);
        return new ListCardViewHolder(itemView, parent.getContext());
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

        View itemView = mInflater.inflate(R.layout.card_item_listcard, listContainer, false);
        TextView tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        TextView tvContent = (TextView) itemView.findViewById(R.id.tv_content);
        TextView tvMoreInfo = (TextView) itemView.findViewById(R.id.tv_moreinfo);
        ImageButton ivImage = (ImageButton) itemView.findViewById(R.id.iv_image);

        tvTitle.setVisibility(hasTitle ? View.VISIBLE:View.GONE);
        tvContent.setVisibility(hasContent ? View.VISIBLE:View.GONE);
        tvMoreInfo.setVisibility(hasLink ? View.VISIBLE:View.GONE);

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
    }

}
