通用注解，也是区别于[Dependency Injection](docs/general_annotation.md)新的注解，基于aspectj的AOP，无需使用耗费性能的反射.不过,需要在build.gradle中配置一下aspectj


| 注解名称        | 作用          | 备注          |
| ------------- |:-------------:| :-------------:|
| @Async        |借助rxjava,异步执行app中的方法|       |
| @Cacheable    |Spring Cache风格的Cache注解,将结果放于缓存中|只适用于android4.0以后|
| @LogMethod    |将方法的入参和出参都打印出来,可以用于调试|       |
| @HookMethod   |可以在调用某个方法之前、以及之后进行hook|比较适合埋点的场景，可以单独使用也可以跟任何自定义注解配合使用|
| @Prefs        |将方法返回的结果放入AppPrefs中|只适用于android4.0以后|
| @Safe         |可以安全地执行方法,而无需考虑是否会抛出运行时异常|       |
| @Trace        |用于追踪某个方法花费的时间,可以用于性能调优的评判|       |


@Async的使用方法:
---
```Java
	@Async
	private void useAsync() {
		Log.e(TAG, " thread=" + Thread.currentThread().getId());
		Log.e(TAG, "ui thread=" + Looper.getMainLooper().getThread().getId());
	}
```

@Cacheable的使用方法:
---
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
---
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
将@Trace和@Async两个注解结合使用,可以看到调用loadUser()方法花费的时间.
```Java
05-18 14:31:31.229 21190-21190/app.magicwindow.cn.testsaf I/MainActivity: MainActivity=loadUser() take [1ms]
05-18 14:31:31.231 21190-22033/app.magicwindow.cn.testsaf E/com.test.saf.activity.MainActivity:  thread=14876
05-18 14:31:31.231 21190-22033/app.magicwindow.cn.testsaf E/com.test.saf.activity.MainActivity: ui thread=1
```


@HookMethod的使用方法:
---
不写beforeMethod和afterMethod，则相当于没有使用@HookMethod<br>
beforeMethod和afterMethod对应的都是方法名，分别表示在调用doSomething()之前执行和之后执行。目前还不支持在beforeMethod和afterMethod中传递参数。
```Java
   @HookMethod(beforeMethod="dosthbeforeMethod",afterMethod="dosthafterMethod")
   void doSomething() {

   }
```
