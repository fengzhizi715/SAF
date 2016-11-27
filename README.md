SAF
===
SAF(Simple Android Framework)是一个简单的android框架，它为开发Android app提供了基础性组件。  
SAF曾经在多个项目中使用，包括今夜酒店特价app、锦江之星app、京东内部的多个app等等。这个项目第一次提交到google code是2012年的3月26号，我已经断断续续做了4年多了。2014年9月开始[frankswu](https://github.com/frankswu)加入跟我一起开发SAF。  
目前google code上的工程暂停维护，迁移到github上。它到了1.1.19版本，下一个版本肯定会是1.2.0。
目前已经增加了Rxjava作为依赖库,做了RxEventBus和RxImageLoader这两个模块,不过还不太成熟,不推荐生产中使用,下一个版本会进行改进。
遇到任何问题欢迎跟我联系，qq: 63067756/wechat: fengzhizi715, 玩得开心:)

我也觉得SAF越来越大了,怎么办呢? 好在SAF通过gradle进行构建项目,在命令行中输入gradle,即可一键生成多种jar包.

除了SAF自带的demo外，[魔窗的android demo app](https://github.com/magicwindow/mw-androidsdk-example)也使用了SAF，而且也是开源的

![](logo.png)

主要功能
-----------------------------------
* [New Annotation](https://github.com/fengzhizi715/SAF#new-annotation-without-reflection)
* [RxAsyncTask](https://github.com/fengzhizi715/SAF#rxasynctask)
* [Event Bus](https://github.com/fengzhizi715/SAF#event-bus)
* [Rest Client](https://github.com/fengzhizi715/SAF#rest-client)
* [Image Cache](https://github.com/fengzhizi715/SAF#image-cache)
* [Dependency Injection](https://github.com/fengzhizi715/SAF#dependency-injection)
* [Sqlite ORM](https://github.com/fengzhizi715/SAF#sqlite-orm)
* [Router](https://github.com/fengzhizi715/SAF#router)
* [Cache](https://github.com/fengzhizi715/SAF#cache)
* [L](https://github.com/fengzhizi715/SAF#L)
* [Utils](https://github.com/fengzhizi715/SAF#utils)


New annotation without reflection
===
新的注解，基于aspectj的AOP，而无需再使用耗费性能的反射.
不过,需要在build.gradle中配置一下aspectj


| 注解名称        | 作用          | 备注          |
| ------------- |:-------------:| :-------------:|
| @Async        |借助rxjava,异步执行app中的方法|       |
| @Cacheable    |Spring Cache风格的Cache注解,将结果放于缓存中|只适用于android4.0以后|
| @LogMethod    |将方法的入参和出参都打印出来,可以用于调试|       |
| @Prefs        |将方法的结果放入AppPrefs中|只适用于android4.0以后|
| @Safe         |可以安全地执行方法,而无需考虑是否会抛出运行时异常|       |
| @Trace        |用于追踪某个方法花费的时间,可以用于性能调优的评判|       |

@Async的使用方法:
```Java
	@Async
	private void useAsync() {
		Log.e(TAG, " thread=" + Thread.currentThread().getId());
		Log.e(TAG, "ui thread=" + Looper.getMainLooper().getThread().getId());
	}
```


@Cacheable的使用方法:
```Java
	@Cacheable(key = "user")
	private User initData() {
		User user = new User();
		user.userName = "tony";
		user.password = "123456";
		return user;
	}
```

这里的@Cacheable,实际上用到[Cache](https://github.com/fengzhizi715/SAF#cache),要获取Cache也很简单.

@Trace的使用方法:
```Java
	@Trace
	@Async
	private void loadUser() {
		Log.e(TAG, " thread=" + Thread.currentThread().getId());
		Log.e(TAG, "ui thread=" + Looper.getMainLooper().getThread().getId());
		Cache cache = Cache.get(this);
		User user = (User) cache.getObject("user");
		Toast.makeText(MainActivity.this, SAFUtils.printObject(user), Toast.LENGTH_SHORT).show();
	}
```
将@Trace和@Async两个注解结合使用,可以看到loadUser()方法花费的时间.
```Java
05-18 14:31:31.229 21190-21190/app.magicwindow.cn.testsaf I/MainActivity: MainActivity=loadUser() take [1ms]<br>
05-18 14:31:31.231 21190-22033/app.magicwindow.cn.testsaf E/com.test.saf.activity.MainActivity:  thread=14876<br>
05-18 14:31:31.231 21190-22033/app.magicwindow.cn.testsaf E/com.test.saf.activity.MainActivity: ui thread=1<br>
```


RxAsyncTask
===
可以替换android自带的AsyncTask，底层使用rxjava，开发者只需实现onExecute()即可。
支持链式调用。success()方法是必须的。
```Java
     new RxAsyncTask<String>(){
            @Override
            public String onExecute() {
                return RestClient.get("https://api.github.com/users/fengzhizi715").body();
            }
        }.success(new RxAsyncTask.SuccessHandler<String>() {
            @Override
            public void onSuccess(String content) {
                L.json(content);
            }
        }).failed(new RxAsyncTask.FailedHandler() {
            @Override
            public void onFail(Throwable e) {
                L.e("error="+e.getMessage());
            }
        });
```


Event Bus
===
事件总线框架，类似于google guava、square otto的event bus。它是一种消息发布-订阅模式,它的工作机制类似于观察者模式，通过通知者去注册观察者，最后由通知者向观察者发布消息。

Event Bus解耦了asyncTask、handler、thread、broadcast等组件。使用Event bus可以轻松地跨多个Fragment进行通讯。

它用法很简单，在Activity或者Fragment中使用，其中event是一个简单的POJO<br />
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


Rest Client
===
Rest Client模块提供了http的get、post、put、delete方法。这个模块还不是很完善，只是适应自身项目需要，未来会不断增加新的功能。 这个模块没有基于apache httpclient，完全基于jdk中的HttpURLConnection。

同步调用get方法：

```Java
          RestClient client = RestClient.get(url);<p>
          String body = client.body();
```

异步调用get方法：

```Java
          RestClient.get(url,new HttpResponseHandler(){

              public void onSuccess(String content) {
                // content为http请求成功后返回的response
              }
              
             @Override
			 public void onFail(RestException exception){
						
			  }
          });
 ```


同步调用post方法：post body内容为json

```Java
          RestClient client = RestClient.post(url);
          client.acceptJson().contentType("application/json", null);
          client.send(jsonString); // jsonString是已经由json对象转换成string类型
          String body = client.body();
```

异步调用post方法：post body内容为json

```Java
          RestClient.post(url,json,new HttpResponseHandler(){ // json对应的是fastjson的JSONObject对象
        
             public void onSuccess(String content) {
             }
                
             @Override
			 public void onFail(RestException exception){
						
			 }
        
           });
```

异步调用post方法：以form形式传递数据

```Java
          RestClient.post(urlString, map, new HttpResponseHandler(){

              @Override
              public void onSuccess(String content) {

              }
                
              @Override
			   public void onFail(RestException exception){
			    }
                                        
          });
```


Image Cache
===
图片缓存模块包括2级缓存，内存中的cache和sd卡上存放在文件中的cache。

图片缓存模块通过ImageLoader进行图片加载。 如果app中使用了SAFApp，则无须创建新的ImageLoader就可以使用。     
```Java
          // 第一个参数是图片的url，第二个参数是ImageView对象，第三个参数是默认图片
          imageLoader.displayImage(url, imageView ,R.drawable.defalut_icon);
```


Dependency Injection
===
Dependency Injection是依赖注入的意思，简称DI。

SAF中的DI包括以下几个方面：
* Inject View ：简化组件的查找注册，目前支持约定大于配置，如果代码中的组件名称跟layout中要注入的组件id相同，则无需写(id=R.id.xxxx)
* Inject Views：支持多个相同类型组件的注入
* Inject Service ：简化系统服务的注册，目前只支持android的系统服务
* Inject Extra ：简化2个Activity之间Extra传递
* InflateLayout ：简化布局填充时，组件的查找注册
* OnClick：简化各种组件的Click事件写法，其属性after可以指定一个方法在OnClick之后执行(一般用于埋点或者其共性操作)。
* OnItemClick：简化ListView的ItemView事件写法

Inject View
---
Inject View可以简化组件的查找注册，包括android自带的组件和自定义组件。在使用Inject View之前，我们会这样写代码

```Java
          public class MainActivity extends Activity {
                
                private ImageView imageView;
                
                @Override
                protected void onCreate(Bundle savedInstanceState) {
                  super.onCreate(savedInstanceState);
                  
                  setContentView(R.layout.activity_main);
                  imageView = (ImageView) findViewById(R.id.imageview);
                }
           }
 ```


在使用Inject View之后，会这样写代码

```Java
          public class MainActivity extends Activity {
                    
                @InjectView(id= R.id.imageview)
                private ImageView imageView;
                    
                @Override
                protected void onCreate(Bundle savedInstanceState) {
                   super.onCreate(savedInstanceState);
                      
                   setContentView(R.layout.activity_main);
                   Injector.injectInto(this);
                }
          }
```

约定大于配置的写法，如果Layout中id的名字imageview跟Activity中控件的名字imageview完全一样，那么无需写(id= R.id.imageview)

```Java
          public class MainActivity extends Activity {
                    
                @InjectView
                private ImageView imageview;
                    
                @Override
                protected void onCreate(Bundle savedInstanceState) {
                   super.onCreate(savedInstanceState);
                      
                   setContentView(R.layout.activity_main);
                   Injector.injectInto(this);
                }
          }
```

目前，@InjectView可用于Activity、Dialog、Fragment,Adapter(ViewHolder)中。在Activity和Dialog用法相似，在Fragment和Adapter中用法有一点区别。

```Java
          public class DemoFragment extends Fragment {

                   @InjectView(id=R.id.title)
                   private TextView titleView;

                   @InjectView(id=R.id.imageview)
                   private ImageView imageView;

                   @Override
                   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                          View v = inflater.inflate(R.layout.fragment_demo, container, false);

                          Injector.injectInto(this,v); // 和Activity使用的区别之处在这里
          
                          initViews();
                          initData();
          
                          return v;
                   }
          
                  ......
           }
```

Inject Views
---

```Java
          public class MainActivity extends Activity {
                    
                @InjectViews(ids={R.id.imageView1,R.id.imageView2})
                private List<ImageView> imageviews;
                    
                @Override
                protected void onCreate(Bundle savedInstanceState) {
                   super.onCreate(savedInstanceState);
                      
                   setContentView(R.layout.activity_main);
                   Injector.injectInto(this);
                }
          }
 ```


Inject Extra
---

```Java
         /**
          * MainActivity传递数据给SecondActivity
          * Intent i = new Intent(MainActivity.this,SecondActivity.class);                                               
          * i.putExtra("test", "saf");
          * i.putExtra("test_object", hello);
          * startActivity(i);
          * 在SecondActivity可以使用@InjectExtra注解
          *
          * @author Tony Shen
          *
          */
         public class SecondActivity extends Activity{

               @InjectExtra(key="test")
               private String testStr;
        
               @InjectExtra(key="test_object")
               private Hello hello;
        
               protected void onCreate(Bundle savedInstanceState) {
                   super.onCreate(savedInstanceState);
                
                   Injector.injectInto(this);
                   Log.i("++++++++++++","testStr="+testStr);
                   Log.i("++++++++++++","hello="+SAFUtil.printObject(hello)); // 该方法用于打印对象
              }
          }
```

InflateLayout
---

```Java
        /**
         * @author Tony Shen
         *
         */
         @InflateLayout(id=R.layout.my_view)
         public class MyView extends LinearLayout {

              @InjectView(id = R.id.textview1)
	          public TextView view1;
    
              @InjectView(id = R.id.textview2)
	          public TextView view2;
	
	         public MyView(Context context) {
		         super(context);
	         }
        }
```


在Activity、Fragment中的写法:
```Java	
         MyView myView = Injector.build(mContext, MyView.class);
```


OnClick
---
@OnClick 可以在Activity、Fragment、Dialog、View中使用，支持多个组件绑定同一个方法。

```Java
     public class AddCommentFragment extends BaseFragment {
    
         @Override
         public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

             View v = inflater.inflate(R.layout.fragment_add_comment, container, false);

             Injector.injectInto(this, v);

             initView();

             return v;
        }
    
	    @OnClick(id={R.id.left_menu,R.id.btn_comment_cancel}, after = "pointClickView")
	    void clickLeftMenu() {
		    popBackStack();
	    }

      public void pointClickView(Method method, View view) {
        L.d("pointClickView");
        ....
      }
	
	    @OnClick(id=R.id.btn_comment_send)
	    void clickCommentSend() {
            if (StringHelper.isBlank(commentEdit.getText().toString())) {
               ToastUtil.showShort(mContext, R.string.the_comment_need_more_character);
            } else {
               AsyncTaskExecutor.executeAsyncTask(new AddCommentTask(showDialog(mContext)));
            }
	    }
	    
	    ....
    }
```

OnItemClick
---
```Java 
	@OnItemClick(id=R.id.listview)
	void itemClickListView(AdapterView<?> parent, View view,
			int position, long id) {
		IncomeVO vo= (IncomeVO) listview.getItemAtPosition(position);				
		Intent i = new Intent(mContext,MyIncomeDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("month", vo.month);
		bundle.putString("allIncome", Double.toString(vo.allIncome));
		i.putExtras(bundle);
		startActivity(i);
	}
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


Cache
===
这是一个通用的Cache,可以保存字符串、对象、JSON等等,操作起来十分简单,还可以设置缓存的过期时间.

保持缓存数据：
```Java
      Cache cache = Cache.get(this);
      cache.put("key1", "test value");
      cache.put("key2", "test value", 10);//保存10秒钟，10秒后会过期
```

获取缓存数据：
```Java
      Cache cache = Cache.get(this);
      String value = cache.getString("key1");
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

json方法打印的日志风格如下：
![](L_json.png)

Utils
===
包含了很多常用的工具类，比如日期操作、字符串操作、SAFUtil里包含各种乱七八糟的常用类等等。


