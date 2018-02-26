#坑


## 没完善的
+ CoreService的Binder没有提供removeCallback()选项，导致unbindService()时无法注销监听器。   (liyingheng at 2018.01.09)
+ CoreService没有对Record和TTS的stop()方法     (liyingheng at 2018.01.09)
+ CoreService没有针对SDK初始化异常情况的回调。导致ServiceController无法接收SDK初始化开始、初始化错误的回调      (liyingheng at 2018.01.09)
+ SDK的初始化状态在SdkController中记录了。但是外部要查询这个初始化状态时，调用的是SdkController.InitStatus枚举（CoreService里的东西）   (liyingheng at 2018.01.09)


## 写得不好的
+ ServiceController中每调用一次Service操作，都需要先判断service是否为null。    (liyingheng at 2018.01.09)
+ ServiceController与CoreService的方法好像都是一样的。如何优化？ (liyingheng at 2018.01.09)
+ CoreService中调了Controller、App外部也调了Controller。应该想办法将两个Controller分离开。    (liyingheng at 2018.01.09)
+ IRecognizerStateListener中的回调方法大部分与CoreService.SceneCallback都是一样的。 (liyingheng at 2018.01.09)
+ DataController中回调给MainActivity的方法，没有转换到主线程。   (liyingheng at 2018.01.09)
+ UsecaseDispatcher中，太多toXXXUsecase()了。 (liyingheng at 2018.01.09)
+ GnRemoteUsecase依然依赖TeleControlPresenter   (liyingheng at 2018.01.10)
+ createAlarm创建了好多Date对象。如何优化？  (liyingheng at 2018.01.11)


## 没有考虑好的
+ DataController中调用ServiceController的attach()和detach()在onCreate()时调用还是onResume()时调用？    (liyingheng at 2018.01.09)
+ TtsController是Service内部调用的。但是其有个utter的回调，外部有需要的时候要调用。如何将其utter回调传进去？  (已解决) (liyingheng at 2018.01.09)


## 新想法
+ 每个Usecase可以定义一个注解。每个Usecase进行装载时，可不可以使用注解装载？  (liyingheng at 2018.01.26)