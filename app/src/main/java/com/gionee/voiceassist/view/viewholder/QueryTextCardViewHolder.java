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
 * TODO: 有时会出现显示顺序错乱的问题，怀疑是消息延迟或者 sdk 返回消息顺序不对，待查
 * @author twf
 * @date 2018/1/19
 */

public class QueryTextCardViewHolder extends BaseViewHolder {
    private static final String TAG = QueryTextCardEntity.class.getSimpleName();
    private TextView tvQueryText;
    private AsrPartialResultCallback asrPartialResultCallback;
    private volatile boolean isTextSetFreeze;
    public QueryTextCardViewHolder(View itemView) {
        super(itemView);
        tvQueryText = (TextView) itemView.findViewById(R.id.tv_querytext);
    }

    @Override
    public void bind(CardEntity payload) {
        LogUtil.d("twf_test", payload.getContent());
        QueryTextCardEntity queryTextCardEntity = (QueryTextCardEntity) payload;
        boolean isForceSet = queryTextCardEntity.isForceSet();
        if(isForceSet) {
            isTextSetFreeze = true;
            LogUtil.d("twf_test", "isForceSet bind text = " + payload.getContent());
            tvQueryText.setText(payload.getContent());
            if(queryTextCardEntity.getCallbackBindInterface() != null) {
                queryTextCardEntity.getCallbackBindInterface().onTextUpdateFreeze(payload.getContent());
            }
        }

        if(!isTextSetFreeze) {
            LogUtil.d("twf_test", "Text not freeze, bind text = " + payload.getContent());
            tvQueryText.setText(payload.getContent());
            registerAsrPartialResultCallback((QueryTextCardEntity) payload);
        }
    }

    public void registerAsrPartialResultCallback(QueryTextCardEntity entity) {
        if(asrPartialResultCallback == null) {
            asrPartialResultCallback = new AsrPartialResultCallback() {
                @Override
                public void onPartialResult(final String text) {
                    tvQueryText.post(new Runnable() {
                        @Override
                        public void run() {
                            if(!isTextSetFreeze) {
                                LogUtil.d("twf_test", "registerAsrPartialResultCallback, text not freeze, update: " + text);
                                tvQueryText.setText(text);
                            } else {
                                LogUtil.d("twf_test", "registerAsrPartialResultCallback, text is frozen ,cannot update");
                            }
                        }
                    });

                }
            };
        }
        LogUtil.d("TWF", "onCallbackBind");
        if(entity.getCallbackBindInterface() != null) {
            entity.getCallbackBindInterface().onCallbackBind(asrPartialResultCallback);
        }
    }

    public static QueryTextCardViewHolder newInstance(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_item_query_textcard, parent, false);
        return new QueryTextCardViewHolder(view);
    }

    public interface AsrPartialResultCallback {
        /**
         * 中间识别结果回调接口
         * @param text 中间识别结果
         */
        void onPartialResult(String text);
    }
}
