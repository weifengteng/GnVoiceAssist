package com.gionee.voiceassist.view.viewholder;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gionee.voiceassist.R;
import com.gionee.voiceassist.datamodel.card.CardEntity;
import com.gionee.voiceassist.datamodel.card.TextCardEntity;
import com.gionee.voiceassist.util.ContextUtil;

/**
 * 显示纯文字内容TextCard的ViewHolder
 */

public class TextCardViewHolder extends BaseViewHolder {

    private TextView tvContent;
    private TextView tvMoreInfo;

    public TextCardViewHolder(View itemView) {
        super(itemView);
        tvContent = (TextView) itemView.findViewById(R.id.tv_content);
        tvMoreInfo = (TextView) itemView.findViewById(R.id.tv_moreinfo);
    }

    @Override
    public void bind(CardEntity payload) {
        bindContent((TextCardEntity) payload);
        bindMoreInfo((TextCardEntity) payload);
    }

    public static TextCardViewHolder newInstance(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.card_item_textcard, parent, false);
        return new TextCardViewHolder(itemView);
    }

    private void bindContent(TextCardEntity payload) {
        String content = payload.getContent();
        if (content != null && !TextUtils.isEmpty(content)) {
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(content);
        } else {
            tvContent.setVisibility(View.GONE);
        }
    }

    private void bindMoreInfo(TextCardEntity payload) {
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
