SAF
===
SAF(Simple Android Framework)是一个简单的android框架，它为开发Android app开发提供了基础性组件。  
SAF曾经在多个项目中使用，包括今夜酒店特价app、锦江之星app、京东内部的多个app等等。这个项目第一次提交到google code是2012年的3月26号，我已经断断续续做了4年多了。  
目前google code上的工程早已暂停维护，全部迁移到github上。它到了1.1.19版本，下一个版本肯定会是1.2.0。
目前已经增加了Rxjava作为依赖库,新增RxEventBus和RxImageLoader这两个模块,不过还不太成熟,不推荐生产中使用,新版本会进行改进。
遇到任何问题欢迎跟我联系，qq: 63067756/wechat: fengzhizi715, 玩得开心:)

我也觉得SAF越来越大了,怎么办呢? 好在SAF通过gradle进行构建项目,在命令行中输入gradle,即可一键生成多种jar包.

除了SAF自带的demo app外，[魔窗的android demo app](https://github.com/magicwindow/mw-androidsdk-example)也使用了SAF，而且也是开源的

![](logo.png)

主要功能
-----------------------------------
* [General Annotation(use aspectj)](docs/general_annotation.md) 通用的注解
* [RxAsyncTask(use rxjava)](docs/rxasynctask.md) 区别于系统自带的AsyncTask
* [Permissions](https://github.com/fengzhizi715/SAF#permissions) android6.0 以后的权限操作
* [Event Bus](docs/event_bus.md) 事件总线
* [Rest Client](docs/rest_client.md) 网络框架
* [Image Cache(use rxjava)](docs/image_cache.md) 图片加载框架
* [Dependency Injection(use apt)](docs/dependency_injection.md) 依赖注入
* [Sqlite ORM](docs/sqlite_orm.md) 数据库操作框架
* [Router](docs/router.md) Activity、Fragment的路由框架
* [Cache](docs/cache.md) 缓存框架
* [L](docs/l.md) 日志框架
* [Utils](docs/utils.md) 各种工具类


Permissions
===

ChangeLog
===
[版本更新记录](CHANGELOG.md)

Contributors
===
1. [frankswu](https://github.com/frankswu) 完善老版本的Inject框架和Router框架
2. [ymcao](https://github.com/ymcao) 增加AsyncTask替换android系统的AsyncTask
3. [aaron](https://github.com/snailflying) 修改build.gradle，支持多个module合并到一个jar包。
