package com.gionee.gnvoiceassist.message.itemview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gionee.gnvoiceassist.message.model.render.RenderEntity;

/**
 * 含选项选择的卡片ViewHolder。如下载还是取消，选择第几个联系人，卡一还是卡二等选择场景会用到
 */

public class ChooseCardViewHolder extends BaseViewHolder {

    public ChooseCardViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(RenderEntity data, Context context) {

    }

    public ChooseCardViewHolder newInstance(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return null;
    }
}
