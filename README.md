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
* [Sqlite ORM](https://github.com/fengzhizi715/SAF#sqlite-orm)
* [Router](https://github.com/fengzhizi715/SAF#router)
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

Sqlite ORM
===
顾名思义就是sqlite的orm框架，采用oop的方式简化对sqlite的操作。 </br>
<strong>注意：在android studio2.0以后如果使用InstantRun功能，并且在android 5.0以上手机上调试会报错。强烈建议在调试时关闭InstantRun功能，release版本的app不会受到任何影响。</strong>（参考：http://stackoverflow.com/questions/36572515/dexfile-in-2-0-versions-of-android-studio-and-gradle）</br>
首先需要在AndroidManifest.xml中配上一些参数

```Java
        <!-- 表示在com.example.testsaf.db这个package下的类都是db的domain，一个类对应db里的一张表-->
        <meta-data
            android:name="DOMAIN_PACKAGE"
            android:value="com.example.testsaf.db" />

        <!-- 表示db的名称-->
        <meta-data
            android:name="DB_NAME"
            android:value="testsaf.db" />

         <!-- 表示db的版本号-->
         <meta-data
            android:name="DB_VERSION"
            android:value="1" />
```


使用orm框架需要初始化DBManager，需要在Applicaion中完成。SAF中的SAFApp，没有初始化DBManager，如果需要使用SAFApp可以重写一个Application继承SAFApp，并初始化DBManager。

```Java
          /**
           * @author Tony Shen
           *
           */
           public class TestApp extends Application{

                @Override
                public void onCreate() {
                   super.onCreate();
                   DBManager.initialize(this);
                }

           }
```

db的domain使用是也是基于注解

```Java
          /**
           *
           * 表示sqlite中autocomplete表的属性
           * @author Tony Shen
           *
           */
          @Table(name="autocomplete")
          public class Autocomplete extends DBDomain{

              @Column(name="key_words",length=20,notNull=true)
              public String KEY_WORDS;

              @Column(name="key_type",length=20,notNull=true)
              public String KEY_TYPE;

              @Column(name="key_reference",length=80)
              public String KEY_REFERENCE;
          }
```


db的操作很简单

```Java
          Autocomplete auto = new Autocomplete();
          auto.KEY_TYPE = "1";
          auto.KEY_WORDS = "testtest";
          auto.save(); // 插入第一条记录

          Autocomplete auto2 = new Autocomplete();
          auto2.KEY_TYPE = "0";
          auto2.KEY_WORDS = "haha";
          auto2.save(); // 插入第二条记录

          Autocomplete auto3 = new Autocomplete().get(1); // 获取Autocomplete的第一条记录
          if (auto3!=null) {
               Log.i("+++++++++++++++","auto3.KEY_WORDS="+auto3.KEY_WORDS);
          } else {
               Log.i("+++++++++++++++","auto3 is null!");
          }
```

查询结果集

```Java
List list = new Autocomplete().executeQuery("select * from autocomplete where KEY_WORDS = 'testtest'");
Log.i("+++++++++++++++","list.size()="+list.size());  // 根据sql条件查询

List list2 = new Autocomplete().executeQuery("select * from autocomplete where KEY_WORDS = ? and Id = ?","testtest","1");
Log.i("+++++++++++++++","list2.size()="+list2.size()); // 表示查询select * from autocomplete where KEY_WORDS = 'testtest' and Id = '1'
```


Router
===
类似于rails的router功能，可以实现app的应用内跳转,包括Activity之间、Fragment之间可以轻易实现相互跳转，并传递参数。
使用Activity跳转必须在Application中做好router的映射。 我们会做这样的映射，表示从某个Activity跳转到另一个Activity需要传递user、password2个参数
```Java
          Router.getInstance().setContext(getApplicationContext()); // 这一步是必须的，用于初始化Router
          Router.getInstance().map("user/:user/password/:password", SecondActivity.class);
```

有时候，activity跳转还会有动画效果，那么我们可以这么做

```Java
          RouterOptions options = new RouterOptions();
          options.enterAnim = R.anim.slide_right_in;
          options.exitAnim = R.anim.slide_left_out;
          Router.getInstance().map("user/:user/password/:password", SecondActivity.class, options);
```


在Application中定义好映射，activity之间跳转只需在activity中写下如下的代码，即可跳转到相应的Activity，并传递参数
```Java
           Router.getInstance().open("user/fengzhizi715/password/715");
```

如果在跳转前需要先做判断，看看是否满足跳转的条件,doCheck()返回false表示不跳转，true表示进行跳转到下一个activity

```Java
          Router.getInstance().open("user/fengzhizi715/password/715",new RouterChecker(){

                 public boolean doCheck() {
                     return true;
                 }
          });
```


单独跳转到某个网页，调用系统电话，调用手机上的地图app打开地图等无须在Application中定义跳转映射。

```Java
          Router.getInstance().openURI("http://www.g.cn");

          Router.getInstance().openURI("tel://18662430000");

          Router.getInstance().openURI("geo:0,0?q=31,121");
```

Fragment之间的跳转也无须在Application中定义跳转映射。直接在某个Fragment写下如下的代码
```Java
Router.getInstance().openFragment(new FragmentOptions(getFragmentManager(),new Fragment2()), R.id.content_frame);
```

当然在Fragment之间跳转可以传递参数
```Java
Router.getInstance().openFragment("user/fengzhizi715/password/715",new FragmentOptions(getFragmentManager(),new Fragment2()), R.id.content_frame);
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


Contributors
===
1. [frankswu](https://github.com/frankswu) 完善老版本的Inject框架和Router框架
2. [ymcao](https://github.com/ymcao) 增加AsyncTask替换android系统的AsyncTask
3. [aaron](https://github.com/snailflying) 修改build.gradle，支持多个module合并到一个jar包。
