# 目录结构

+ root 
    + basefunction  具体场景操作，未来会改成usecase
    + controller    与SDK直接沟通的控制器。
        + recordcontrol     录音控制器
        + ttscontrol        TTS（语音转文字）控制器
    + customlink    内部自定义通信协议的定义
    + directiveListener SDK解析结果回调监听器
    + sdk           与SDK有关的东西。包括SDK的管理，及自定义端功能DeviceModule
    + statemachine  
    + systemctrl    系统Framework控制接口及实现类。如开关手电筒、截屏的实现。
    + util          工具类
    + widget        列表中的ViewItem以及自定义控件