package com.gionee.gnvoiceassist.message.itemview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gionee.gnvoiceassist.R;
import com.gionee.gnvoiceassist.message.model.render.ChooseRenderEntity;
import com.gionee.gnvoiceassist.message.model.render.RenderEntity;

import java.util.List;

/**
 * 选择列表的ViewHolder
 * 主要用于多轮交互的列表选择
 */

public class ChooseListViewHolder extends BaseViewHolder {

    private TextView tvTitle;
    private TextView tvContent;
    private LinearLayout llOptionView;
    private OptionClickListener mListener;

    public ChooseListViewHolder(View itemView) {
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

    public static ChooseListViewHolder newInstance(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.chooselist_card_item_lyt, parent,false);
        return new ChooseListViewHolder(itemView);
    }

    public void setOptionClickListener(OptionClickListener listener) {
        mListener = listener;
    }

    private void setTitleAndContent(RenderEntity data) {
        tvTitle.setText(data.getTitle());
        tvContent.setText(data.getContent());
    }

    private void setOptionView(ChooseRenderEntity data, Context context) {
        List<RenderEntity.ChooseItemModel> options = data.getSelectors();
        for (int i = 0; i < options.size(); i++) {
            RenderEntity.ChooseItemModel option = options.get(i);
            LinearLayout optionView = (LinearLayout) LayoutInflater
                    .from(context)
                    .inflate(R.layout.chooselist_card_child, null, false);
            TextView childTvTitle = (TextView) optionView.findViewById(R.id.title);
            TextView childTvContent = (TextView) optionView.findViewById(R.id.info);
            childTvTitle.setText(option.title);
            childTvContent.setText(option.content);
            optionView.setTag(option.metadata);
            optionView.setId(i);
            optionView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onOptionClicked(v.getId(), v);
                    }
                }
            });
            llOptionView.addView(optionView);
        }

    }

    public interface OptionClickListener {
        void onOptionClicked(int position, View v);
    }
}
