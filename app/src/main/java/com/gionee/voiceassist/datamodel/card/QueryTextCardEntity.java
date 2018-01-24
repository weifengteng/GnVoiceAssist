package com.gionee.voiceassist.datamodel.card;

import com.gionee.voiceassist.view.viewholder.QueryTextCardViewHolder;

/**
 *
 * @author twf
 * @date 2018/1/19
 */

public class QueryTextCardEntity extends CardEntity {

    private IPartialResultCallbackBind callbackBindInterface;
    public QueryTextCardEntity(IPartialResultCallbackBind iCallbackBind) {
        setType(CardType.QUERY_TEXT_CARD);
        this.callbackBindInterface = iCallbackBind;
    }

    public IPartialResultCallbackBind getCallbackBindInterface() {
        return callbackBindInterface;
    }

    public void setCallbackBindInterface(IPartialResultCallbackBind callbackBindInterface) {
        this.callbackBindInterface = callbackBindInterface;
    }

    public interface IPartialResultCallbackBind {
        void onCallbackBind(QueryTextCardViewHolder.AsrPartialResultCallback callback);
    }
}
