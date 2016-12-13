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
