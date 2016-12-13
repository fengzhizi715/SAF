图片缓存模块包括2级缓存，内存中的cache和sd卡上存放在文件中的cache。

图片缓存模块通过ImageLoader进行图片加载。 如果app中使用了SAFApp，则无须创建新的ImageLoader就可以使用。     
```Java
          // 第一个参数是图片的url，第二个参数是ImageView对象，第三个参数是默认图片
          imageLoader.displayImage(url, imageView ,R.drawable.defalut_icon);
```
