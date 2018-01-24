package com.gionee.voiceassist.view.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gionee.voiceassist.R;
import com.gionee.voiceassist.datamodel.card.CardEntity;
import com.gionee.voiceassist.datamodel.card.QueryTextCardEntity;
import com.gionee.voiceassist.util.LogUtil;

/**
 *
 * @author twf
 * @date 2018/1/19
 */

public class QueryTextCardViewHolder extends BaseViewHolder {
    private static final String TAG = QueryTextCardEntity.class.getSimpleName();
    private TextView tvQueryText;
    private AsrPartialResultCallback asrPartialResultCallback;
    public QueryTextCardViewHolder(View itemView) {
        super(itemView);
        tvQueryText = (TextView) itemView.findViewById(R.id.tv_querytext);
    }

    @Override
    public void bind(CardEntity payload) {
        LogUtil.d("twf_test", payload.getContent());
        tvQueryText.setText(payload.getContent());
        registerAsrPartialResultCallback((QueryTextCardEntity) payload);
    }

    public void registerAsrPartialResultCallback(QueryTextCardEntity entity) {
        if(asrPartialResultCallback == null) {
            asrPartialResultCallback = new AsrPartialResultCallback() {
                @Override
                public void onPartialResult(final String text) {
                    tvQueryText.post(new Runnable() {
                        @Override
                        public void run() {
                            tvQueryText.setText(text);
                        }
                    });

                }
            };
        }
        LogUtil.d("TWF", "onCallbackBind");
        entity.getCallbackBindInterface().onCallbackBind(asrPartialResultCallback);
    }

    public static QueryTextCardViewHolder newInstance(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_item_query_textcard, parent, false);
        return new QueryTextCardViewHolder(view);
    }

    public interface AsrPartialResultCallback {
        void onPartialResult(String text);
    }
}
