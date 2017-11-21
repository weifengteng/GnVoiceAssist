package com.gionee.gnvoiceassist.message.model.render;

import java.util.List;

/**
 * 列表卡片的数据结构
 */

public class ListRenderEntity extends RenderEntity {

    public ListRenderEntity() {
        this.setType(Type.ListCard);
    }

    //列表
    private List<ListItemModel> list;

    public List<ListItemModel> getList() {
        return list;
    }

    public void setList(List<ListItemModel> list) {
        this.list = list;
    }
}
