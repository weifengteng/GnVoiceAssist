package com.gionee.voiceassist.view.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gionee.voiceassist.R;
import com.gionee.voiceassist.datamodel.card.CardEntity;

/**
 * Created by twf on 2018/1/19.
 */

public class QueryTextCardViewHolder extends BaseViewHolder {

    private TextView tvQueryText;
    public QueryTextCardViewHolder(View itemView) {
        super(itemView);
        tvQueryText = (TextView) itemView.findViewById(R.id.tv_querytext);
    }

    @Override
    public void bind(CardEntity payload) {
        tvQueryText.setText(payload.getContent());
    }

    public static QueryTextCardViewHolder newInstance(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_item_query_textcard, parent, false);
        return new QueryTextCardViewHolder(view);
    }
}
