package com.gionee.gnvoiceassist.message.itemview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.gionee.gnvoiceassist.message.model.render.RenderEntity;

/**
 * 问题建议ViewHolder
 */

public class SuggestionViewHolder extends BaseViewHolder {
    public SuggestionViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(RenderEntity data, Context context) {

    }

    public BaseViewHolder newInstance(ViewGroup parent) {
        return null;
    }
}
