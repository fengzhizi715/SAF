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
