package com.gionee.gnvoiceassist.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gionee.gnvoiceassist.message.model.render.RenderEntity;
import com.gionee.gnvoiceassist.message.model.render.TextRenderEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyingheng on 10/24/17.
 */

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_TEXT_RESULT_CARD = 1;
    private static final int VIEW_TYPE_TEXT_QUERY_CARD = 2;
    private static final int VIEW_TYPE_STANDARD_CARD = 3;
    private static final int VIEW_TYPE_LIST_CARD = 4;
    private static final int VIEW_TYPE_IMAGE_LIST_CARD = 5;
    private static final int VIEW_TYPE_CHOOSE_BOX_CARD = 6;
    private static final int VIEW_TYPE_CHOOSE_LIST_CARD = 7;

    private LayoutInflater mInflater;
    private List<RenderEntity> mRenderData;

    public HomeRecyclerViewAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mRenderData = new ArrayList<>();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mRenderData.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (mRenderData.get(position).getType()) {
            case TextCard:
                if (((TextRenderEntity)mRenderData).isQueryText()) {
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
        return -1;
    }

    public void addQueryItem(RenderEntity entity) {

    }

    public void addResultItem(RenderEntity entity) {

    }
}
