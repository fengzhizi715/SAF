Rest Client模块提供了http的get、post、put、delete方法。这个模块还不是很完善，只是适应自身项目需要，未来会不断增加新的功能。
这个模块完全使用jdk中的HttpURLConnection。

同步调用get方法：

```Java
          RestClient client = RestClient.get(url);
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
