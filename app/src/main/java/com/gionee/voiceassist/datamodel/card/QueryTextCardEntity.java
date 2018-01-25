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
        void onCallbackBind(QueryTextCardViewHolder.AsrPartialResultCallback callback);

        void onTextUpdateFreeze(String freezeText);
    }

    public boolean isForceSet() {
        return isForceSet;
    }

    public void setForceSet(boolean forceSet) {
        isForceSet = forceSet;
    }
}
