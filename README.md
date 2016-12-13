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
* [General Annotation(use aspectj)](docs/general_annotation.md)
* [RxAsyncTask(use rxjava)](docs/rxasynctask.md)
* [Permissions](https://github.com/fengzhizi715/SAF#permissions)
* [Event Bus](docs/event_bus.md)
* [Rest Client](docs/rest_client.md)
* [Image Cache(use rxjava)](https://github.com/fengzhizi715/SAF#image-cache)
* [Dependency Injection(use apt)](docs/dependency_injection.md)
* [Sqlite ORM](docs/sqlite_orm.md)
* [Router](docs/router.md)
* [Cache](docs/cache.md)
* [L](https://github.com/fengzhizi715/SAF#l)
* [Utils](https://github.com/fengzhizi715/SAF#utils)


Permissions
===


Image Cache
===
图片缓存模块包括2级缓存，内存中的cache和sd卡上存放在文件中的cache。

图片缓存模块通过ImageLoader进行图片加载。 如果app中使用了SAFApp，则无须创建新的ImageLoader就可以使用。     
```Java
          // 第一个参数是图片的url，第二个参数是ImageView对象，第三个参数是默认图片
          imageLoader.displayImage(url, imageView ,R.drawable.defalut_icon);
```


L
===
SAF的日志框架，极简的日志风格

| 方法名        | 作用          | 备注          |
| ------------- |:-------------:| :-------------:|
| e()       |Error级别打印日志|       |
| w()        |Warn级别打印日志|       |
| i()        |Info级别打印日志|       |
| d()        |Debug级别打印日志|       |
|json()      |将日志以json格式打印出来|       |

以e、w、i、d打印的日志风格如下：
第一行显示线程名<br>
第二行显示类中打印的行数<br>
第三行显示打印的具体内容


```Java
╔════════════════════════════════════════════════════════════════════════════════════════
║ Thread: main
╟────────────────────────────────────────────────────────────────────────────────────────
║ cn.salesuite.saf.aspects.TraceAspect.traceMethod  (TraceAspect.java:35)
╟────────────────────────────────────────────────────────────────────────────────────────
║ loadUser() take [14ms]
╚════════════════════════════════════════════════════════════════════════════════════════
```

json方法可以将String、Map、对象打印成json风格，具体可以参照下图：
![](L_json.png)

Utils
===
包含了很多常用的工具类，比如日期操作、字符串操作、SAFUtil里包含各种乱七八糟的常用类等等。

ChangeLog
==
[ChangeLog](CHANGELOG.md)


Contributors
===
1. [frankswu](https://github.com/frankswu) 完善老版本的Inject框架和Router框架
2. [ymcao](https://github.com/ymcao) 增加AsyncTask替换android系统的AsyncTask
3. [aaron](https://github.com/snailflying) 修改build.gradle，支持多个module合并到一个jar包。
