package com.gionee.voiceassist.view.viewholder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gionee.voiceassist.R;
import com.gionee.voiceassist.datamodel.card.CardEntity;
import com.gionee.voiceassist.datamodel.card.StandardCardEntity;
import com.gionee.voiceassist.util.ContextUtil;
import com.gionee.voiceassist.util.component.ImageLoader;
import com.squareup.picasso.Picasso;

/**
 * Created by liyingheng on 1/15/18.
 */

public class StandardCardViewHolder extends BaseViewHolder {

    private TextView tvTitle;
    private TextView tvContent;
    private TextView tvMoreInfo;
    private ImageButton ivImage;
    private Context mContext;

    public StandardCardViewHolder(View itemView, Context context) {
        super(itemView);
        tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        tvContent = (TextView) itemView.findViewById(R.id.tv_content);
        tvMoreInfo = (TextView) itemView.findViewById(R.id.tv_moreinfo);
        ivImage = (ImageButton) itemView.findViewById(R.id.iv_image);
        mContext = context;
    }

    @Override
    public void bind(CardEntity payload) {
        setTitle((StandardCardEntity) payload);
        setContent((StandardCardEntity) payload);
        setImg((StandardCardEntity) payload);
        setMoreInfo((StandardCardEntity) payload);
    }

    public static StandardCardViewHolder newInstance(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.card_item_standardcard, parent, false);
        return new StandardCardViewHolder(itemView, parent.getContext());
    }

    private void setTitle(StandardCardEntity payload) {
        String title = payload.getTitle();
        boolean hasTitle = !TextUtils.isEmpty(title);
        if (hasTitle) {
            setVisible(tvTitle);
            tvTitle.setText(title);
        } else {
            setGone(tvTitle);
            tvTitle.setText("");
        }
    }

    private void setContent(StandardCardEntity payload) {
        String content = payload.getContent();
        boolean hasContent = !TextUtils.isEmpty(content);
        if (hasContent) {
            setVisible(tvContent);
            tvContent.setText(content);
        } else {
            setGone(tvContent);
            tvContent.setText("");
        }
    }

    private void setImg(StandardCardEntity payload) {
        String imgSrc = payload.getImgSrc();
        boolean hasImg = !TextUtils.isEmpty(imgSrc);
        if (hasImg) {
            ivImage.setVisibility(View.VISIBLE);
            ImageLoader imageLoader = new ImageLoader(mContext);
            imageLoader.loadImageFromRemote(imgSrc, ivImage, 500, 500, R.drawable.call_btn);
        } else {
            ivImage.setVisibility(View.GONE);
        }
    }

    private void setMoreInfo(StandardCardEntity payload) {
        CardEntity.Link link = payload.getExtLink();
        if (!TextUtils.isEmpty(link.src)) {
            if (!TextUtils.isEmpty(link.anchorText)) {
                //有链接文字
                tvMoreInfo.setText(link.anchorText);
            } else {
                //无链接文字，默认显示"查看更多"
                tvMoreInfo.setText("查看更多");
            }
            final String url = link.src;
            tvMoreInfo.setVisibility(View.VISIBLE);
            tvMoreInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO 打开网页
                    Uri webpage = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                    if (intent.resolveActivity(ContextUtil.getAppContext().getPackageManager()) != null) {
                        ContextUtil.getAppContext().startActivity(intent);
                    }
                }
            });
        } else {
            tvMoreInfo.setVisibility(View.GONE);
        }
    }

}
