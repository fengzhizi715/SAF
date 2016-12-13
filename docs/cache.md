这是一个通用的Cache,可以保存String、对象、JSON等等,操作起来十分简单,支持设置缓存的过期时间。保存Cache的过程也可以使用@Cacheable

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
