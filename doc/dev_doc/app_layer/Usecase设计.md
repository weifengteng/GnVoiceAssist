# Usecase设计

## 需要解决的问题
+ CoreService层返回的DirectivePayload分发机制
+ 每个Usecase内数据的生命周期设计
+ UI层的交互反馈结果如何返回给对应Usecase


## 简述
Usecase即具体用例。识别结果返回后，处理具体场景的操作。如识别结果需要打开相应的应用，则分发到应用场景(AppLaunch)的Usecase下进行打开对应应用的操作。

## DirectivePayload的分发机制
DirectivePayload即CoreService层语义解析的结果。上层接收到语义解析结果后，需要把结果分发给具体的场景，进行实际操作。
DirectivePayload从[ServiceController][cls_ServiceController]接收，然后针对具体的场景，调用[UsecaseDispatcher][cls_UsecaseDispatcher]的sendToUsecase()方法将
DirectivePayload发送给对应场景的Usecase。
sendToUsecase方法使用：
`` 
@param payload: DirectivePayload，即语义解析后的结果。
@param usecaseAlias: 具体场景Usecase别名
sendToUsecase(DirectiveEntity payload, String usecaseAlias)
``


UsecaseDispatcher如何判断应该分发给哪个场景？
UsecaseDispatcher内部维护一个Map，这个Map的key为对应场景Usecase的别名(Alias)，value为其持有的Usecase实例。

[cls_ServiceController]:../app/src/main/java/com/gionee/voiceassist/controller/appcontrol/ServiceController.java
[cls_UsecaseDispatcher]:../app/src/main/java/com/gionee/voiceassist/controller/appcontrol/UsecaseDispatcher.java

## Usecase数据生命周期
每一个Usecase是有状态的。其Usecase记录的数据的生命周期，从一个场景的第一个操作开始，到这个场景的最后一个操作结束，为一个数据生命周期。

目前遇到的问题：
+ 如何记录当前的场景？
+ 当一个场景开始，或一个场景结束时，如何通知具体Usecase创建、销毁其数据？





