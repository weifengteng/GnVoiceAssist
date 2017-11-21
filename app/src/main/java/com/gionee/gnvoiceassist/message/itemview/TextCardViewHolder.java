package com.gionee.gnvoiceassist.message.itemview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gionee.gnvoiceassist.GnVoiceAssistApplication;
import com.gionee.gnvoiceassist.R;
import com.gionee.gnvoiceassist.message.model.render.RenderEntity;
import com.gionee.gnvoiceassist.util.LogUtil;

/**
 * Created by liyingheng on 11/16/17.
 */

public class TextCardViewHolder extends BaseViewHolder {

    private static final String TAG = TextCardViewHolder.class.getSimpleName();

    private TextView tvContent;
    private TextView tvMoreInfo;

    private boolean query;

    public TextCardViewHolder(View itemView) {
        super(itemView);
        tvContent = (TextView) itemView.findViewById(R.id.content);
        tvMoreInfo = (TextView) itemView.findViewById(R.id.moreinfo);
    }

    @Override
    public void bind(RenderEntity data, Context context) {
        setContentView(data.getContent(),tvContent);
        setMoreInfoView(data.getLink(),tvMoreInfo);
    }

    public static TextCardViewHolder newInstance(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.text_card_info_item_lyt,parent,false);
        return new TextCardViewHolder(itemView);
    }

    /**
     * 设置是否为原始查询文字
     * @param query 用户查询原始文字
     */
    public void setQueryText(boolean query) {
        this.query = query;
    }

    private void setContentView(String content, TextView tvContent) {
        if (TextUtils.isEmpty(content)) {
            LogUtil.e(TAG,"setContentView() empty content!");
            tvContent.setVisibility(View.GONE);
            return;
        }
        tvContent.setText(content);
    }

    private void setMoreInfoView(final RenderEntity.LinkModel link, TextView tvMoreInfo) {
        if (link == null || TextUtils.isEmpty(link.url)) {
            //没有更多信息
            tvMoreInfo.setVisibility(View.GONE);
            return;
        }
        if (!TextUtils.isEmpty(link.anchorText)) {
            tvMoreInfo.setVisibility(View.VISIBLE);
            tvMoreInfo.setText(link.anchorText);
        } else {
            tvMoreInfo.setVisibility(View.VISIBLE);
            tvMoreInfo.setText("显示更多");
        }
        tvMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 通知谁 按钮被按下然后执行打开网页操作？
                openWebSite(link.url);
            }
        });
    }

    private void openWebSite(String url) {
        //TODO OpenWebSite操作写在这里肯定是不好的。那么应该回调到主界面呢还是直接在这里操作？
        Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        mIntent.addCategory(Intent.CATEGORY_BROWSABLE);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		mIntent.setPackage(APP_BROWSER_PACKAGE_NAME);
        GnVoiceAssistApplication.getInstance().startActivity(mIntent);
    }

}
