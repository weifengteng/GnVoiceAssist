package com.gionee.gnvoiceassist.message.itemview;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gionee.gnvoiceassist.R;
import com.gionee.gnvoiceassist.message.model.render.ChooseRenderEntity;
import com.gionee.gnvoiceassist.message.model.render.RenderEntity;

import java.util.List;

/**
 * 含选项选择的卡片ViewHolder。
 * 如下载还是取消等场景
 */

public class ChooseBoxViewHolder extends BaseViewHolder {

    private TextView tvTitle;
    private TextView tvContent;
    private LinearLayout llOptionView;

    private OptionClickListener mListener;

    public ChooseBoxViewHolder(View itemView) {
        super(itemView);
        tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        tvContent = (TextView) itemView.findViewById(R.id.tv_content);
        llOptionView = (LinearLayout) itemView.findViewById(R.id.option_layout);
    }

    @Override
    public void bind(RenderEntity data, Context context) {
        setTitleAndContent(data);
        setOptionView((ChooseRenderEntity) data, context);
    }

    public void setOptionClickListener(OptionClickListener listener) {
        if (listener != null) {
            mListener = listener;
        }
    }

    public static ChooseBoxViewHolder newInstance(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.chooselist_card_item_lyt, parent, false);
        return new ChooseBoxViewHolder(v);
    }

    private void setTitleAndContent(RenderEntity data) {
        tvTitle.setText(data.getTitle());
        tvContent.setText(data.getContent());
        tvTitle.setVisibility(!TextUtils.isEmpty(data.getTitle()) ? View.VISIBLE:View.GONE);
        tvTitle.setVisibility(!TextUtils.isEmpty(data.getContent()) ? View.VISIBLE:View.GONE);
    }

    private void setOptionView(ChooseRenderEntity data, Context context) {
        List<RenderEntity.ChooseItemModel> options = data.getSelectors();
        for (int i = 0; i < options.size(); i++) {
            RenderEntity.ChooseItemModel option = options.get(i);
            Button optionButton = new Button(context);
            optionButton.setTag(option.metadata);
            optionButton.setId(i);
            optionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onOptionClicked(v.getId(), v);
                    }
                }
            });
            llOptionView.addView(optionButton);     //TODO 为每一个动态生成的按钮设置权重，使其等分排列
        }
    }

    public interface OptionClickListener {
        void onOptionClicked(int position, View v);
    }


}
