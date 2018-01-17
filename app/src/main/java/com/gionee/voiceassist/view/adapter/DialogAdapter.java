package com.gionee.voiceassist.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.gionee.voiceassist.datamodel.card.CardEntity;
import com.gionee.voiceassist.datamodel.card.CardTypeCode;
import com.gionee.voiceassist.view.viewholder.BaseViewHolder;
import com.gionee.voiceassist.view.viewholder.ImageListCardViewHolder;
import com.gionee.voiceassist.view.viewholder.ListCardViewHolder;
import com.gionee.voiceassist.view.viewholder.StandardCardViewHolder;
import com.gionee.voiceassist.view.viewholder.TextCardViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页Activity对话流的RecyclerView Adapter。用于绑定界面与数据源。
 */

public class DialogAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private List<CardEntity> mPayloads = new ArrayList<>();
    private Context mContext;

    public DialogAdapter(Context context) {
        mContext = context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case CardTypeCode.TEXT_CARD:
                return TextCardViewHolder.newInstance(parent);
            case CardTypeCode.STANDARD_CARD:
                return StandardCardViewHolder.newInstance(parent);
            case CardTypeCode.LIST_CARD:
                return ListCardViewHolder.newInstance(parent);
            case CardTypeCode.IMAGE_LIST_CARD:
                return ImageListCardViewHolder.newInstance(parent);
            default:
                break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.bind(mPayloads.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return mPayloads.get(position).getType().code();
    }

    @Override
    public int getItemCount() {
        return mPayloads.size();
    }

    public void addDialogItem(CardEntity payload) {
        if (payload != null) {
            mPayloads.add(payload);
            notifyItemInserted(mPayloads.size() - 1);
        }
    }

    public void updateDialogItem(CardEntity payload) {
        //TODO 更新DialogItem

    }



}
