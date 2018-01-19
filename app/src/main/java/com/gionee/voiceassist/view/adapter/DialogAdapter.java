package com.gionee.voiceassist.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.gionee.voiceassist.datamodel.card.CardEntity;
import com.gionee.voiceassist.datamodel.card.CardType;
import com.gionee.voiceassist.datamodel.card.CardTypeCode;
import com.gionee.voiceassist.util.LogUtil;
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
    private static final String TAG = DialogAdapter.class.getSimpleName();
    private List<CardEntity> mPayloads = new ArrayList<>();
    private Context mContext;
    private DialogSubItemPool mSubItemPool;

    public DialogAdapter(Context context) {
        mContext = context;
        mSubItemPool = new DialogSubItemPool();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LogUtil.d(TAG, "onCreateViewHolder viewType= " + viewType);
        switch (viewType) {
            case CardTypeCode.TEXT_CARD:
                return TextCardViewHolder.newInstance(parent);
            case CardTypeCode.STANDARD_CARD:
                return StandardCardViewHolder.newInstance(parent);
            case CardTypeCode.LIST_CARD:
                return ListCardViewHolder.newInstance(parent, mSubItemPool);
            case CardTypeCode.IMAGE_LIST_CARD:
                return ImageListCardViewHolder.newInstance(parent, mSubItemPool);
            default:
                break;

        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        LogUtil.d(TAG, "onBindViewHolder position= " + position);
        holder.bind(mPayloads.get(position));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemViewType(int position) {
        return mPayloads.get(position).getType().code();
    }

    @Override
    public int getItemCount() {
        return mPayloads.size();
    }

    @Override
    public void onViewRecycled(BaseViewHolder holder) {
        super.onViewRecycled(holder);
        holder.onRecycled();
    }

    public void addDialogItem(CardEntity payload) {
        LogUtil.d(TAG, "addDialogItem CardEntity = " + payload.getType());
        if (payload != null) {
            mPayloads.add(payload);
            notifyItemInserted(mPayloads.size() - 1);
        }
    }

    public void updateDialogItem(CardEntity payload) {
        //TODO 更新DialogItem

    }



}
