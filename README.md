SAF
===
SAF已经在多个项目中使用，包括今夜酒店特价app、锦江之星app、京东内部的一个app等等。目前它刚刚到1.1版本，肯定会存在各种各样的问题。 
这个项目第一次提交到google code是2012年的3月26号，我已经断断续续做了2年了。目前google code上的工程暂停维护，迁移到github上。遇到任何问题欢迎跟我的qq联系，qq：63067756


主要功能
-----------------------------------
* SAFApp<br />
* Event Bus<br />
* Rest Client<br />
* Image Cache<br />
* Dependency Injection<br />
* Sqlite ORM<br />
* Router<br />
* Utils<br />

SAFApp
===
SAFApp其实不能算是一个完整的模块，SAFApp继承了Application。增加了一个可作为缓存存放app全局变量的session，一个ImageLoader，一个记录Activity的List。

Event Bus
===
事件总线框架，类似于google guava、square otto的event bus。它是一种消息发布-订阅模式,它的工作机制类似于观察者模式，通过通知者去注册观察者，最后由通知者向观察者发布消息。

Event Bus解耦了asyncTask、handler、thread、broadcast等组件。使用Event bus可以轻松地跨多个Fragment进行通讯。

它用法很简单，在Activity或者Fragment中使用，其中event是一个简单的POJO<br />
<pre><code>
// 退出系统的事件
eventBus.post(new LogoutEvent());
</pre></code>

回调事件，同样在Activity或者Fragment中定义好。回调方法名可以随便定义，参数须要和event一一对应。并且在方法名前加上注解Subscribe

         /**
          * 退出整个app
          * @param event
          */
          @Subscribe
          public void onLogoutEvent(LogoutEvent event) {
          }
          
          
@Subscribe可以使用枚举<br />

         /**
          * 使用ThreadMode.BackgroundThread枚举，表示在后台线程运行，不在主线程中运行。
          * @param event
          */
          @Subscribe(ThreadMode.BackgroundThread)
          public void onBackendFresh(BackendFreshEvent event) {
          
          }
使用枚举BackgroundThread时，如果在回调方法中需要更新ui，则必须要配合handler使用。 在不使用枚举的情况下，@Subscribe会默认使用PostThread，表示回调方法会在主线程中运行。 如果在一个Activity中存在多个Fragment，并且在Activity或者在Fragment中存在订阅同一event的回调方法。如果发出event的请求时，这些回调方法都会起作用。


Rest Client
===
Rest Client模块提供了http的get、post、put、delete方法。这个模块还不是很完善，只是适应自身项目需要，未来会不断增加新的功能。 这个模块没有基于apache httpclient，完全基于jdk中的HttpURLConnection。

同步调用get方法：
<pre><code>
          RestClient client = RestClient.get(url);<p>
          String body = client.body();
</pre></code>

异步调用get方法：
<pre><code>
          RestClient.get(url,new HttpResponseHandler(){
              
              public void onSuccess(String content) {
                // content为http请求成功后返回的response
              }
          });
</pre></code>

同步调用post方法：post body内容为json
<pre><code>
          RestClient client = RestClient.post(url);
          client.acceptJson().contentType("application/json", null);
          client.send(jsonString); // jsonString是已经由json对象转换成string类型
          String body = client.body();
</pre></code>

异步调用post方法：post body内容为json
<pre><code>
          RestClient.post(url,json,new HttpResponseHandler(){ // json对应的是fastjson的JSONObject对象
        
                public void onSuccess(String content) {
                }
        
           });
</pre></code>

异步调用post方法：以form形式传递数据
<pre><code>
          RestClient.post(urlString, map, new HttpResponseHandler(){

                @Override
                public void onSuccess(String content) {

                }
                                        
          });
</pre></code>


Image Cache
===
图片缓存模块包括2级缓存，内存中的cache和sd卡上存放在文件中的cache。

图片缓存模块通过ImageLoader进行图片加载。 如果app中使用了SAFApp，则无须创建新的ImageLoader就可以使用。     
<pre><code>
          // 第一个参数是图片的url，第二个参数是ImageView对象，第三个参数是默认图片
          imageLoader.displayImage(url, imageView ,R.drawable.defalut_icon);
</pre></code>


Dependency Injection
===
Dependency Injection是依赖注入的意思，简称DI。

SAF中的DI包括以下几个方面：
* Inject View ：简化组件的查找注册
* Inject Service ：简化系统服务的注册，目前只支持android的系统服务
* Inject Extra ：简化2个Activity之间Extra传递

Inject View
---
Inject View可以简化组件的查找注册，包括android自带的组件和自定义组件。在使用Inject View之前，我们会这样写代码
<pre><code>
          public class MainActivity extends Activity {
                
                private ImageView imageView;
                
                @Override
                protected void onCreate(Bundle savedInstanceState) {
                  super.onCreate(savedInstanceState);
                  
                  setContentView(R.layout.activity_main);
                  imageView = (ImageView) findViewById(R.id.imageview);
                }
           }
</pre></code>

在使用Inject View之后，会这样写代码
<pre><code>
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
</pre></code>

目前，@InjectView可用于Activity、Dialog、Fragment中。在Activity和Dialog用法相似，在Fragment中用法有一点区别。
<pre><code>
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
</pre></code>

Inject Extra
---
<pre><code>
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
</pre></code>


Sqlite ORM
===
顾名思义就是sqlite的orm框架，采用oop的方式简化对sqlite的操作。 首先需要在AndroidManifest.xml中配上一些参数
<pre><code>
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
</pre></code>

使用orm框架需要初始化DBManager，需要在Applicaion中完成。SAF中的SAFApp，没有初始化DBManager，如果需要使用SAFApp可以重写一个Application继承SAFApp，并初始化DBManager。
<pre><code>
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
</pre></code>     

db的domain使用是也是基于注解
<pre><code>
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
</pre></code> 

db的操作很简单
<pre><code>
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
</pre></code> 

查询结果集
<pre><code>
List list = new Autocomplete().executeQuery("select * from autocomplete where KEY_WORDS = 'testtest'");
Log.i("+++++++++++++++","list.size()="+list.size());  // 根据sql条件查询
                
List list2 = new Autocomplete().executeQuery("select * from autocomplete where KEY_WORDS = ? and Id = ?","testtest","1");
Log.i("+++++++++++++++","list2.size()="+list2.size()); // 表示查询select * from autocomplete where KEY_WORDS = 'testtest' and Id = '1'
</pre></code> 


Router
===

Utils
===