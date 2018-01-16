package com.gionee.voiceassist.view.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gionee.voiceassist.datamodel.card.CardEntity;

/**
 * Created by liyingheng on 1/15/18.
 */

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bind(CardEntity payload);

    void setVisible(View view) {
        view.setVisibility(View.VISIBLE);
    }

    void setGone(View view) {
        view.setVisibility(View.GONE);
    }

}
