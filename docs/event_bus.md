事件总线框架，类似于google guava、square otto的event bus。它是一种消息发布-订阅模式,它的工作机制类似于观察者模式，通过通知者去注册观察者，最后由通知者向观察者发布消息。

Event Bus解耦了asyncTask、handler、thread、broadcast等组件。使用Event bus可以轻松地跨多个Fragment进行通讯。

它用法很简单，在Activity或者Fragment中使用，其中event是一个简单的POJO<br>
```Java
// 退出系统的事件
eventBus.post(new LogoutEvent());
```

回调事件，同样在Activity或者Fragment中定义好。回调方法名可以随便定义，参数须要和event一一对应。并且在方法名前加上注解Subscribe

```Java
         /**
          * 退出整个app
          * @param event
          */
          @Subscribe
          public void onLogoutEvent(LogoutEvent event) {
          }
```

@Subscribe可以使用枚举

```Java
         /**
          * 使用ThreadMode.BackgroundThread枚举，表示在后台线程运行，不在主线程中运行。
          * @param event
          */
          @Subscribe(ThreadMode.BackgroundThread)
          public void onBackendFresh(BackendFreshEvent event) {
          }
```

使用枚举BackgroundThread时，如果在回调方法中需要更新ui，则必须要配合handler使用。 在不使用枚举的情况下，@Subscribe会默认使用PostThread，表示回调方法会在主线程中运行。 如果在一个Activity中存在多个Fragment，并且在Activity或者在Fragment中存在订阅同一event的回调方法。如果发出event的请求时，这些回调方法都会起作用。
