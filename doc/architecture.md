# VoiceAssist Architecture

## 应用架构简述
应用采用 MVP + Service 模式开发，以更好地对业务的职责进行划分、扩展。

## MVP模式简述
MVP模式即 Model - View - Presenter 的缩写，其为一种软件的架构思想，用以分离具体业务职责。
View为视图层，负责呈现数据、相应用户对界面的交互。Model为具体业务逻辑。View和Model为了解除耦合，
不能直接通信。Presenter是两者沟通的桥梁，处于View层和Model层之间，使用接口的方式，处理两层之间
互相通信的数据，实现View和Model层的间接沟通。

## 为什么要使用MVP？
为了提高代码的可读性、可扩展性和可测试性。同时，更有效地分离每一个业务的职责。

## 应用具体架构

*View*的组成主要有Activity以及其他的显示控件，如RecyclerView.Holder、Adapter、自定义的ScrollView等。

*Presenter*中主要是对各自Activity交互事件的处理。Presenter接收到Activity的请求，比如用户点击了"录音"
按钮后，Presenter会根据不同的Activity事件，调度相应的Model对此事件进行处理。当Model处理完成后，通过接口
将数据传递回Presenter。Presenter将这些数据进行加工后，通过其持有的View接口回调到View层，供界面适配。

*Model*中能够控制语音SDK的录音、接收结果返回、根据具体场景调用用例进行操作等。Controller是与SDK交互的
直接控制器，所有对SDK的操作都需要委托Controller进行通信。UseCase是针对识别场景做出相应操作的用例。



