package com.gionee.gnvoiceassist.message.model.render;

import org.json.JSONObject;

/**
 * Created by liyingheng on 11/7/17.
 */

public class StandardRenderEntity extends RenderEntity {

    //图片
    private ImageModel image;

    public ImageModel getImage() {
        return image;
    }

    public void setImage(ImageModel image) {
        this.image = image;
    }
}
