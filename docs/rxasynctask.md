
它完全可以替代android sdk中自带的AsyncTask使用，底层使用rxjava从而无需关心线程池的问题，开发者只需实现onExecute()即可。
它支持链式调用，支持重试机制retry()。<br>
默认情况下它是这样的，只需实现onExecute()和success()
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
        }).start();
```


使用重试机制retry()时，还需要实现failed()，以便重试失败后调用failed()。
```Java
		new RxAsyncTask<String>() {
			@Override
			public String onExecute() {
				return RestClient.get("https://api.github.com/users/fengzhizi715").body();
			}
		}.retry(3)
		 .success(new RxAsyncTask.SuccessHandler<String>() {
			@Override
			public void onSuccess(String content) {
				L.json(content);
			}
		}).failed(new RxAsyncTask.FailedHandler() {
			@Override
			public void onFail(Throwable e) {
				L.e("error=" + e.getMessage());
			}
		}).start();
```
