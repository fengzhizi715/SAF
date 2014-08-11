SAF
===
SAF已经在多个项目中使用，包括今夜酒店特价app、锦江之星app、京东内部的一个app等等。目前它刚刚到1.1版本，肯定会存在各种各样的问题。 
这个项目第一次提交到google code是2012年的3月26号，我已经断断续续做了2年了。目前google code上的工程暂停维护，迁移到github上。遇到任何问题欢迎跟我的qq联系，qq：63067756


主要功能
-----------------------------------
1.SAFApp<br />
2.Event Bus<br />
3.Rest Client<br />
4.Image Cache<br />
5.Dependency Injection<br />
6.Sqlite ORM<br />
7.Router<br />
8.Utils<br />

SAFApp
===
SAFApp其实不能算是一个完整的模块，SAFApp继承了Application。增加了一个可作为缓存存放app全局变量的session，一个ImageLoader，一个记录Activity的List。

Event Bus
===
事件总线框架，类似于google guava、square otto的event bus。它是一种消息发布-订阅模式,它的工作机制类似于观察者模式，通过通知者去注册观察者，最后由通知者向观察者发布消息。

Event Bus解耦了asyncTask、handler、thread、broadcast等组件。使用Event bus可以轻松地跨多个Fragment进行通讯。

它用法很简单，在Activity或者Fragment中使用，其中event是一个简单的POJO<br />
// 退出系统的事件<br />
eventBus.post(new LogoutEvent());

回调事件，同样在Activity或者Fragment中定义好。回调方法名可以随便定义，参数须要和event一一对应。并且在方法名前加上注解Subscribe

         /**
          * 退出整个app
          * @param event
          */
          @Subscribe
          public void onLogoutEvent(LogoutEvent event) {
          }
@Subscribe可以使用枚举
         /**
          * 使用ThreadMode.BackgroundThread枚举，表示在后台线程运行，不在主线程中运行。
          * @param event
          */
          @Subscribe(ThreadMode.BackgroundThread)
          public void onBackendFresh(BackendFreshEvent event) {
          
          }
使用枚举BackgroundThread时，如果在回调方法中需要更新ui，则必须要配合handler使用。 在不使用枚举的情况下，@Subscribe会默认使用PostThread，表示回调方法会在主线程中运行。 如果在一个Activity中存在多个Fragment，并且在Activity或者在Fragment中存在订阅同一event的回调方法。如果发出event的请求时，这些回调方法都会起作用。
