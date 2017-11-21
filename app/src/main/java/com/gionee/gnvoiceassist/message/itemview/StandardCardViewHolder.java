package com.gionee.gnvoiceassist.message.itemview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gionee.gnvoiceassist.GnVoiceAssistApplication;
import com.gionee.gnvoiceassist.R;
import com.gionee.gnvoiceassist.message.model.render.RenderEntity;
import com.gionee.gnvoiceassist.message.model.render.StandardRenderEntity;
import com.gionee.gnvoiceassist.util.LogUtil;
import com.squareup.picasso.Picasso;

import static android.R.id.list;

/**
 * Created by liyingheng on 11/16/17.
 */

public class StandardCardViewHolder extends BaseViewHolder {

    private static final String TAG = StandardCardViewHolder.class.getSimpleName();
    private static final String APP_BROWSER_PACKAGE_NAME = "com.android.browser";

    private TextView tvTitle;
    private TextView tvContent;
    private TextView tvMoreInfo;
    private ImageButton ivImage;

    public StandardCardViewHolder(View itemView) {
        super(itemView);
        tvTitle = (TextView) itemView.findViewById(R.id.title);
        tvContent = (TextView) itemView.findViewById(R.id.content);
        tvMoreInfo = (TextView) itemView.findViewById(R.id.moreinfo);
        ivImage = (ImageButton) itemView.findViewById(R.id.image);
    }

    @Override
    public void bind(RenderEntity data, Context context) {
        StandardRenderEntity renderData = (StandardRenderEntity)data;
        setTitleTextView(renderData.getTitle(),tvTitle);
        setContentView(renderData.getContent(),tvContent);
        setMoreInfoView(renderData.getLink(),tvMoreInfo);
        setImage(renderData.getImage(),ivImage, context);
    }

    public static StandardCardViewHolder newInstance(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.standard_card_info_item_lyt, parent,false);
        return new StandardCardViewHolder(itemView);
    }

    private void setTitleTextView(String name, View itemView) {
        if(name != null) {
            LogUtil.d(TAG, "SimpleStandardCardItem setTitleTextView name = " + name);
            ((TextView) itemView.findViewById(R.id.title)).setText(name);
        } else {
            LogUtil.d(TAG, "SimpleStandardCardItem setTitleTextView name == null");
        }
    }

    private void setContentView(String content, View itemView) {

        if(TextUtils.isEmpty(content)) {
            LogUtil.e(TAG, "SimpleStandardCardItem setContentView content is empty");
            return;
        }

        TextView contentView = (TextView)itemView.findViewById(R.id.content);
        contentView.setText(content);

        LogUtil.d(TAG, "SimpleStandardCardItem setContentView list = " + list);
    }

    private void setImage(RenderEntity.ImageModel image, View itemView, Context context) {
        if(image == null) {
            LogUtil.d(TAG, "SimpleStandardCardItem setImage image = " + list);
            return;
        }
        ImageButton imageButton = (ImageButton) itemView.findViewById(R.id.image);
        imageButton.setVisibility(View.VISIBLE);
        Picasso.with(context)
                .load(image.src)
                .placeholder(R.drawable.gn_detail_item_icon_phone_normal)
                .resize(1240, 1563)
                .onlyScaleDown()
                .into(imageButton);
    }

    private void setMoreInfoView(final RenderEntity.LinkModel link, View itemView) {
        if(link != null) {
            TextView detailUrlView = (TextView) itemView.findViewById(R.id.moreinfo);
            detailUrlView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
            detailUrlView.setText(link.anchorText);
            detailUrlView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    openLink(link.url);
                }
            });
        } else {
            LogUtil.e(TAG, "SimpleStandardCardItem setMoreInfoView link == null");
        }
    }

    private void openLink(String url) {
        Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        mIntent.addCategory(Intent.CATEGORY_BROWSABLE);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mIntent.setPackage(APP_BROWSER_PACKAGE_NAME);
        GnVoiceAssistApplication.getInstance().startActivity(mIntent);
    }

}
