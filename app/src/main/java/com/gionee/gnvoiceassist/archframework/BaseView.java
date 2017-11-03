package com.gionee.gnvoiceassist.archframework;

/**
 * Created by liyingheng on 11/2/17.
 */

public interface BaseView<T extends BasePresenter> {

    /**
     * 将Presenter实例返回到View的实现中
     * @param presenter
     */
    void setPresenter(T presenter);

}
