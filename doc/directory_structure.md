# 目录结构

+ root 
    + controller    业务层（相当于Model）
        + appcontrol        上层app的业务控制
        + recordcontrol     （CoreService）录音控制器
        + ttscontrol        （CoreService）TTS（语音转文字）控制器
    + coreservice   底层对外开放的服务层。对外提供语音识别、TTS语音转文字的服务。
        + datamodel 服务层的数据实体
        + listener  服务层内部的监听器（包括SDK状态监听、SDK结果解析监听）
        + sdk       与SDK相关的组件。包括SDK的管理，自定义端功能DeviceModule
    + systemctrl    系统Framework控制接口及实现类。如开关手电筒、截屏的实现。
    + util          工具类
    + widget        列表中的ViewItem以及自定义控件
    HomeActivity    语音助手主Activity（重构后的入口Activity，仍在测试）
    


## 修订历史
2018.1.14   更新重构后的新目录结构 (liyingheng)