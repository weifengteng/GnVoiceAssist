package com.gionee.gnvoiceassist.home;

import com.gionee.gnvoiceassist.archframework.BasePresenter;
import com.gionee.gnvoiceassist.archframework.BaseView;
import com.gionee.gnvoiceassist.message.model.render.RenderEntity;
import com.gionee.gnvoiceassist.util.Constants;

/**
 * Created by liyingheng on 11/2/17.
 */

public interface HomeContract {

    interface View extends BaseView<Presenter>{
        /**
         * 语音识别状态改变
         * @param state
         */
        void onRecognizeStateChanged(Constants.RecognitionState state);

        /**
         * 引擎状态回调
         * @param state 引擎状态：已加载、正在加载、未加载
         */
        void onEngineState(Constants.EngineState state);

        /**
         * 转写文字、识别数据回调
         * @param renderData 供屏幕显示的数据源
         */
        void onResult(RenderEntity renderData);

    }

    interface Presenter extends BasePresenter{

        /**
         * 异步查询当前引擎状态（未初始化/正在初始化/已初始化）
         * 查询结果通过View.onEngineState(Constants.EngineState)进行回调
         */
        void queryEngineState();

        /**
         * 调用底层，开始/停止语音识别
         */
        void fireVoiceRequest();
    }

}
