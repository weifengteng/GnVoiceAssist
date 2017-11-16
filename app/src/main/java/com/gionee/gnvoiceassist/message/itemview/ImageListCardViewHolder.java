package com.gionee.gnvoiceassist.message.itemview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gionee.gnvoiceassist.R;
import com.gionee.gnvoiceassist.message.model.render.ImageListRenderEntity;
import com.gionee.gnvoiceassist.message.model.render.RenderEntity;
import com.squareup.picasso.Picasso;

/**
 * Created by liyingheng on 11/16/17.
 */

public class ImageListCardViewHolder extends BaseViewHolder {

    private static final int MAX_DISPLAY_ITEM = 5;

    LinearLayout llCustomPanel;

    public ImageListCardViewHolder(View itemView) {
        super(itemView);
        llCustomPanel = (LinearLayout) itemView.findViewById(R.id.custom_panel);
        int count = 0;

    }

    @Override
    public void bind(RenderEntity data, Context context) {
        ImageListRenderEntity renderData = (ImageListRenderEntity) data;
        int count = 0;
        for(RenderEntity.ImageModel img : renderData.getImageList()) {
            View itemView = View.inflate(context,R.layout.imagelist_info_item_lyt, null);
            if(img == null) {
                llCustomPanel.addView(itemView);
                continue;
            }

            setImage(img.src, itemView, context);
            count++;
            if(count > MAX_DISPLAY_ITEM) {
                break;
            }
            llCustomPanel.addView(itemView);
        }
        llCustomPanel.addView(View.inflate(context, R.layout.restaurant_dianping_info, null));
    }

    public static ImageListCardViewHolder newInstance(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.empty_note_board,parent,false);
        return new ImageListCardViewHolder(itemView);
    }

    private void setImage(String imageUrl, View itemView, Context context) {
        ImageView imageView = (ImageView) itemView.findViewById(R.id.image);
        imageView.setVisibility(View.VISIBLE);
        Picasso.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.gn_detail_item_icon_phone_normal)
                .resize(640, 960)
                .centerInside()
                .into(imageView);
    }
}
