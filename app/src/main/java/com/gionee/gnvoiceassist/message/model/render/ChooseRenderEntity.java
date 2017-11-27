package com.gionee.gnvoiceassist.message.model.render;

import java.util.List;

/**
 * Created by liyingheng on 11/7/17.
 */

public class ChooseRenderEntity extends RenderEntity {

    public ChooseRenderEntity(boolean isChooseList) {
        if (isChooseList) {
            setType(Type.ChooseListCard);
        } else {
            setType(Type.ChooseBoxCard);
        }
    }

    //选择列表
    private List<ChooseItemModel> selectors;

    public List<ChooseItemModel> getSelectors() {
        return selectors;
    }

    public void setSelectors(List<ChooseItemModel> selectors) {
        this.selectors = selectors;
    }
}
