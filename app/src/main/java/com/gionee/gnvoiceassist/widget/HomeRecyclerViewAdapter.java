package com.gionee.gnvoiceassist.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gionee.gnvoiceassist.R;
import com.gionee.gnvoiceassist.message.itemview.ChooseBoxViewHolder;
import com.gionee.gnvoiceassist.message.itemview.ChooseListViewHolder;
import com.gionee.gnvoiceassist.message.itemview.ImageListCardViewHolder;
import com.gionee.gnvoiceassist.message.itemview.ListCardViewHolder;
import com.gionee.gnvoiceassist.message.itemview.StandardCardViewHolder;
import com.gionee.gnvoiceassist.message.itemview.TextCardViewHolder;
import com.gionee.gnvoiceassist.message.model.render.RenderEntity;
import com.gionee.gnvoiceassist.message.model.render.TextRenderEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 主页显示查询结果的列表（RecyclerView）
 */

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter {

    private static final int INVALID_VIEW_TYPE = -1;
    private static final int VIEW_TYPE_TEXT_RESULT_CARD = 1;
    private static final int VIEW_TYPE_TEXT_QUERY_CARD = 2;
    private static final int VIEW_TYPE_STANDARD_CARD = 3;
    private static final int VIEW_TYPE_LIST_CARD = 4;
    private static final int VIEW_TYPE_IMAGE_LIST_CARD = 5;
    private static final int VIEW_TYPE_CHOOSE_BOX_CARD = 6;
    private static final int VIEW_TYPE_CHOOSE_LIST_CARD = 7;

    private LayoutInflater mInflater;
    private List<RenderEntity> mRenderData;
    private Map<String,Integer> mQueryMap;
    private Context mContext;
    private OnItemClickedListener mListener;


    public HomeRecyclerViewAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mRenderData = new ArrayList<>();
        mQueryMap = new HashMap<>();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_TEXT_RESULT_CARD:
            case VIEW_TYPE_TEXT_QUERY_CARD:
                return TextCardViewHolder.newInstance(parent);
            case VIEW_TYPE_STANDARD_CARD:
                return StandardCardViewHolder.newInstance(parent);
            case VIEW_TYPE_LIST_CARD:
                return ListCardViewHolder.newInstance(parent);
            case VIEW_TYPE_IMAGE_LIST_CARD:
                return ImageListCardViewHolder.newInstance(parent);
            case VIEW_TYPE_CHOOSE_BOX_CARD:
                return ChooseBoxViewHolder.newInstance(parent);
            case VIEW_TYPE_CHOOSE_LIST_CARD:
                return ChooseListViewHolder.newInstance(parent);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_TEXT_RESULT_CARD:
            case VIEW_TYPE_TEXT_QUERY_CARD:
                ((TextCardViewHolder)holder).bind(mRenderData.get(position),mContext);
                ((TextCardViewHolder)holder).setQueryText(holder.getItemViewType() == VIEW_TYPE_TEXT_QUERY_CARD);
                break;
            case VIEW_TYPE_STANDARD_CARD:
                ((StandardCardViewHolder)holder).bind(mRenderData.get(position),mContext);
                break;
            case VIEW_TYPE_LIST_CARD:
                ((ListCardViewHolder)holder).bind(mRenderData.get(position),mContext);
                break;
            case VIEW_TYPE_IMAGE_LIST_CARD:
                ((ImageListCardViewHolder)holder).bind(mRenderData.get(position),mContext);
                break;
            case VIEW_TYPE_CHOOSE_BOX_CARD:
                ((ChooseBoxViewHolder)holder).bind(mRenderData.get(position), mContext);
                ((ChooseBoxViewHolder)holder).setOptionClickListener(new ChooseBoxViewHolder.OptionClickListener() {
                    @Override
                    public void onOptionClicked(int position, View v) {
                        if (mListener != null) {
                            mListener.onOptionClicked(position, v);
                        }
                    }
                });
                break;
            case VIEW_TYPE_CHOOSE_LIST_CARD:
                ((ChooseListViewHolder)holder).bind(mRenderData.get(position), mContext);
                ((ChooseListViewHolder)holder).setOptionClickListener(new ChooseListViewHolder.OptionClickListener() {
                    @Override
                    public void onOptionClicked(int position, View v) {
                        if (mListener != null) {
                            mListener.onOptionClicked(position, v);
                        }
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mRenderData.size();
    }

    @Override
    public int getItemViewType(int position) {
        RenderEntity data = mRenderData.get(position);
        switch (data.getType()) {
            case TextCard:
                if (((TextRenderEntity)data).isQueryText()) {
                    return VIEW_TYPE_TEXT_QUERY_CARD;
                } else {
                    return VIEW_TYPE_TEXT_RESULT_CARD;
                }
            case StandardCard:
                return VIEW_TYPE_STANDARD_CARD;
            case ListCard:
                return VIEW_TYPE_LIST_CARD;
            case ImageListCard:
                return VIEW_TYPE_IMAGE_LIST_CARD;
            case ChooseBoxCard:
                return VIEW_TYPE_CHOOSE_BOX_CARD;
            case ChooseListCard:
                return VIEW_TYPE_CHOOSE_LIST_CARD;
        }
        return INVALID_VIEW_TYPE;
    }

    public void addQueryItem(RenderEntity entity) {
        if (entity != null) {
            if (!mRenderData.isEmpty()) {
                RenderEntity lastRender = mRenderData.get(mRenderData.size() - 1);
                if (lastRender.getType() == RenderEntity.Type.TextCard
                        && ((TextRenderEntity)lastRender).isQueryText()
                        && ((TextRenderEntity)lastRender).isPartial()) {
                    mRenderData.set(mRenderData.size() - 1, entity);
                    notifyItemChanged(mRenderData.size() - 1);
                } else {
                    mRenderData.add(entity);
                    notifyItemInserted(mRenderData.size() - 1);
                }
            }
        }
    }

    public void addResultItem(RenderEntity entity) {
        if (entity != null) {
            mRenderData.add(entity);
            notifyItemInserted(mRenderData.size() - 1);
        }
    }

    public void setOnItemClickedListener(OnItemClickedListener listener) {
        if (listener != null) {
            mListener = listener;
        }
    }

    public interface OnItemClickedListener {
        void onItemClicked(View view);
        void onOptionClicked(int position, View view);
    }

}
