package com.test.saf.activity;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.test.saf.R;
import com.test.saf.Test2Event;
import com.test.saf.TestEvent;
import com.test.saf.app.BaseActivity;

import cn.salesuite.saf.aspects.annotation.Async;
import cn.salesuite.saf.aspects.annotation.Cacheable;
import cn.salesuite.saf.aspects.annotation.Trace;
import cn.salesuite.saf.cache.Cache;
import cn.salesuite.saf.http.rest.RestClient;
import cn.salesuite.saf.log.L;
import cn.salesuite.saf.rxjava.RxAsyncTask;
import cn.salesuite.saf.rxjava.eventbus.RxEventBus;
import cn.salesuite.saf.rxjava.eventbus.RxEventBusAnnotationManager;
import cn.salesuite.saf.rxjava.eventbus.Subscribe;
import cn.salesuite.saf.rxjava.eventbus.ThreadMode;
import cn.salesuite.saf.utils.SAFUtils;

/**
 * Created by Tony Shen on 2016/11/18.
 */

public class MainActivity2 extends BaseActivity {

    private TextView text;
    private RxEventBusAnnotationManager manager;
    private static final String TAG = MainActivity.class.getName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = (TextView)findViewById(R.id.text);
        manager = new RxEventBusAnnotationManager(this);
        RxEventBus.getInstance().post(new TestEvent());

        text.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
//				RxEventBus.getInstance().post(new Test2Event());
//		        Intent i = new Intent(MainActivity.this,SecondActivity.class);
//		        startActivity(i);
                loadUser();
            }
        });

        initData();

//		try {
//			ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript",this);
////			String result  = (String) engine.eval("2+2");
////			Toast.makeText(this,"result="+result,Toast.LENGTH_SHORT).show();
//			String json = "{\n" +
//					"    \"className\": \"com.test.saf.activity.User\",\n" +
//					"    \"method\": \"\",\n" +
//					"    \"field\": \"userName\",\n" +
//					"    \"fieldValue\": \"arron\"\n" +
//					"}";
//			Object result = engine.executeJava(json);
//			Toast.makeText(this,"result="+SAFUtils.printObject(result),Toast.LENGTH_SHORT).show();
//		} catch (Exception e){
//			e.printStackTrace();
//		}
    }

    @Cacheable(key = "user")
    private User initData() {

        User user = new User();
        user.userName = "tony";
        user.password = "123456";
        L.json(user);
        return user;
    }

//	private void initData() {
//		TestTask task = new TestTask();
//		task.success(new RxAsyncTask.SuccessHandler<String>() {
//			@Override
//			public void onSuccess(String content) {
//				L.i(content);
//			}
//		}).failed(new RxAsyncTask.FailedHandler() {
//			@Override
//			public void onFail(Throwable e) {
//				L.i("error="+e.getMessage());
//			}
//		});
//	}

    @Trace
    @Async
    private void loadUser() {
        L.e(" thread=" + Thread.currentThread().getId());
        L.e("ui thread=" + Looper.getMainLooper().getThread().getId());
        Cache cache = Cache.get(this);
        User user = (User) cache.getObject("user");
        Toast.makeText(MainActivity2.this, SAFUtils.printObject(user), Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    void onTest(TestEvent event) {
//		Log.i(TAG, "onTestEvent");
        L.i("onTestEvent");
        Toast.makeText(MainActivity2.this, "onTestEvent", Toast.LENGTH_SHORT).show();
    }

    @Subscribe(value= ThreadMode.BackgroundThread)
    void onTest2(Test2Event event) {
        Log.i(TAG, "onTest2Event");
        Toast.makeText(getApplication(), "onTest2Event", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (manager!=null) {
            manager.clear();
        }
    }

    class TestTask extends RxAsyncTask<String> {

        public String onExecute() {

            return RestClient.get("http://open.tuhaoliuliang.cn/getUrl?pkg=com.meinv.app&appVersion=1.0&apiVersion=1.0&platform=android&channel=wechat").body();
        }
    }
}
