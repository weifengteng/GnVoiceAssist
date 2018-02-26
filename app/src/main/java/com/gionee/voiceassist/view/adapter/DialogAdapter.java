package com.gionee.voiceassist.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.gionee.voiceassist.controller.appcontrol.RenderEvent;
import com.gionee.voiceassist.datamodel.card.CardEntity;
import com.gionee.voiceassist.datamodel.card.CardTypeCode;
import com.gionee.voiceassist.util.LogUtil;
import com.gionee.voiceassist.view.viewholder.AlarmCardViewHolder;
import com.gionee.voiceassist.view.viewholder.BaseViewHolder;
import com.gionee.voiceassist.view.viewholder.ImageListCardViewHolder;
import com.gionee.voiceassist.view.viewholder.ListCardViewHolder;
import com.gionee.voiceassist.view.viewholder.QueryTextCardViewHolder;
import com.gionee.voiceassist.view.viewholder.QuickSettingsViewHolder;
import com.gionee.voiceassist.view.viewholder.ReminderCardViewHolder;
import com.gionee.voiceassist.view.viewholder.StandardCardViewHolder;
import com.gionee.voiceassist.view.viewholder.StopwatchCardViewHolder;
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
            case CardTypeCode.QUERY_TEXT_CARD:
                return QueryTextCardViewHolder.newInstance(parent);
            case CardTypeCode.ANSWER_TEXT_CARD:
                return TextCardViewHolder.newInstance(parent);
            case CardTypeCode.STANDARD_CARD:
                return StandardCardViewHolder.newInstance(parent);
            case CardTypeCode.LIST_CARD:
                return ListCardViewHolder.newInstance(parent, mSubItemPool);
            case CardTypeCode.IMAGE_LIST_CARD:
                return ImageListCardViewHolder.newInstance(parent, mSubItemPool);
            case CardTypeCode.STOPWATCH_CARD:
                return StopwatchCardViewHolder.newInstance(parent);
            case CardTypeCode.QUICKSETTING_CARD:
                return QuickSettingsViewHolder.newInstance(parent, mSubItemPool);
            case CardTypeCode.ALARM_CARD:
                return AlarmCardViewHolder.newInstance(parent);
            case CardTypeCode.REMINDER_CARD:
                return ReminderCardViewHolder.newInstance(parent);
            default:
                break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        LogUtil.d("twf_test", "DialogAdapter onBindViewHolder. position = " + position);
        holder.bind(mPayloads.get(position));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position, List<Object> payloads) {
        LogUtil.d("twf_test", "DialogAdapter onBindViewHolder w/Payload . position = " + position);
        if(payloads != null && !payloads.isEmpty()) {
            for(Object o : payloads) {
                holder.bind(((RenderEvent)o).getPayload());
            }
        } else {
            holder.bind(mPayloads.get(position));
        }

    }

    @Override
    public int getItemViewType(int position) {
        return mPayloads.get(position).getType().code();
    }

    @Override
    public int getItemCount() {
        return mPayloads.size();
    }

    public int addDialogItem(CardEntity payload) {
        LogUtil.d(TAG, "addDialogItem CardEntity = " + payload.getType());
        if (payload != null) {
            mPayloads.add(payload);
            notifyItemInserted(mPayloads.size() - 1);
            return mPayloads.size() - 1;
        }
        return -1;
    }

    public void updateDialogItem(CardEntity payload) {
        //TODO 更新DialogItem

    }

    @Override
    public void onViewRecycled(BaseViewHolder holder) {
        super.onViewRecycled(holder);
        holder.onRecycled();
    }
}
