package com.gionee.voiceassist.view.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gionee.voiceassist.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyingheng on 10/24/17.
 */

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.ChildViewHolder> {

    LayoutInflater mInflater;
    List<View> mChildViews;

    public HomeRecyclerViewAdapter(Context context) {
        mChildViews = new ArrayList<>();
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ChildViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) mInflater.inflate(R.layout.list_item_child,null);
        return new ChildViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ChildViewHolder holder, int position) {
        View childView = mChildViews.get(position);
        holder.mViews.addView(childView);
    }



    @Override
    public int getItemCount() {
        return mChildViews.size();
    }

    public void addChildView(View v) {
        if (!mChildViews.contains(v)) {
            mChildViews.add(v);
            notifyItemInserted(getItemCount());
        }
    }

    public class ChildViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mViews;

        public ChildViewHolder(LinearLayout itemView) {
            super(itemView);
            mViews = itemView;
        }
    }

}
