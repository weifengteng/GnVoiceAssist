package com.gionee.gnvoiceassist.message.itemview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.gionee.gnvoiceassist.message.model.render.RenderEntity;

/**
 * Created by liyingheng on 11/16/17.
 */

public abstract class BaseViewHolder extends RecyclerView.ViewHolder{

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bind(RenderEntity data, Context context);

}
