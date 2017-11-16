package com.gionee.gnvoiceassist.message.itemview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gionee.gnvoiceassist.message.model.render.RenderEntity;

/**
 * Created by liyingheng on 11/16/17.
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
