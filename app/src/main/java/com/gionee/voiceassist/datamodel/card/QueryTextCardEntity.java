package com.gionee.voiceassist.datamodel.card;

import com.gionee.voiceassist.view.viewholder.QueryTextCardViewHolder;

/**
 *
 * @author twf
 * @date 2018/1/19
 */

public class QueryTextCardEntity extends CardEntity {

    private IPartialResultCallbackBind callbackBindInterface;
    private boolean isForceSet;
    public QueryTextCardEntity() {
        setType(CardType.QUERY_TEXT_CARD);
    }

    public IPartialResultCallbackBind getCallbackBindInterface() {
        return callbackBindInterface;
    }
    public void setCallbackBindInterface(IPartialResultCallbackBind callbackBindInterface) {
        this.callbackBindInterface = callbackBindInterface;
    }

    public interface IPartialResultCallbackBind {
        /**
         * 返回识别结果更新回调接口
         * @param callback
         */
        void onCallbackBind(QueryTextCardViewHolder.AsrPartialResultCallback callback);

        /**
         * 更新识别结果被冻结，不能再更新识别文本
         * @param freezeText 冻结状态的识别结果（最终的识别结果）
         */
        void onTextUpdateFreeze(String freezeText);
    }

    /**
     * 强制设置，返回值为 true 时冻结识别结果更新
     * @return
     */
    public boolean isForceSet() {
        return isForceSet;
    }

    public void setForceSet(boolean forceSet) {
        isForceSet = forceSet;
    }
}
