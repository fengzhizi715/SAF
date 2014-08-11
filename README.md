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