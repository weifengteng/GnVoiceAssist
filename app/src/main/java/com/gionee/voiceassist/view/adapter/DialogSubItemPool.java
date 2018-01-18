package com.gionee.voiceassist.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gionee.voiceassist.R;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 对话列表ListItem和ImageListItem等子item的视图池。
 * 当子列表项在界面可见时，会先判断视图池中有没有对应类型缓存的视图。若有，直接从视图池中取出对应的视图。
 * 当子列表项不可见，触发回收时，会把当前视图保存到视图池中，以便再利用。
 */

public class DialogSubItemPool {


    private List<View> mListItemPool;
    private List<View> mImageListItemPool;


    public DialogSubItemPool() {
        mListItemPool = new LinkedList<>();
        mImageListItemPool = new LinkedList<>();
    }

    public View getListItemView(ViewGroup parent) {
        View itemView;
        if (mListItemPool.size() > 0) {
            itemView = mListItemPool.remove(0);
        } else {
            itemView = createListItemView(parent);
        }
        return itemView;
    }

    public void recycleListItemView(View listItemView) {
        mListItemPool.add(listItemView);
    }

    private View createListItemView(ViewGroup parent) {
        return LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.card_item_listcard, parent, false);
    }

    public View getImageListItemView(ViewGroup parent) {
        View itemView;
        if (mImageListItemPool.size() > 0) {
            itemView = mImageListItemPool.remove(0);
        } else {
            itemView = createImageListItemView(parent);
        }
        return itemView;
    }

    public void recycleImageListItemView(View imageListItemView) {
        mImageListItemPool.add(imageListItemView);
    }

    private View createImageListItemView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_imagelistcard, parent, false);
    }

}
