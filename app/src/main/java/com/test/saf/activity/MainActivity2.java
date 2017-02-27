package com.test.saf.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.safframework.injectview.Injector;
import com.safframework.injectview.annotations.InjectViews;
import com.safframework.log.L;
import com.safframework.permissions.Permission;
import com.safframework.permissions.PermissionGuard;
import com.safframework.permissions.PermissionGuardAware;
import com.safframework.saf.rxjava.eventbus.RxEventBus;
import com.safframework.saf.rxjava.eventbus.RxEventBusAnnotationManager;
import com.safframework.saf.rxjava.eventbus.Subscribe;
import com.safframework.saf.rxjava.eventbus.ThreadMode;
import com.test.saf.R;
import com.test.saf.Test2Event;
import com.test.saf.TestEvent;
import com.test.saf.app.BaseActivity;

/**
 * Created by Tony Shen on 2016/11/18.
 */

public class MainActivity2 extends BaseActivity implements PermissionGuardAware {

    @InjectViews(ids={R.id.text})
    TextView[] text;
    private RxEventBusAnnotationManager manager;
    private static final String TAG = MainActivity.class.getName();
    private PermissionGuard permissionGuard;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Injector.injectInto(this);
        manager = new RxEventBusAnnotationManager(this);
        RxEventBus.getInstance().post(new TestEvent());
        permissionGuard = new PermissionGuard(mContext,this);

        text[0].setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
//				RxEventBus.getInstance().post(new Test2Event());
//		        Intent i = new Intent(MainActivity2.this,SecondActivity.class);
//		        startActivity(i);
//                loadUser();

                openCamera(v);
            }
        });

//        initData();
    }

    @Permission(Manifest.permission.CALL_PHONE)
    private void openCamera(View v) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + "10086");
        intent.setData(data);
        startActivity(intent);
    }

//    @Cacheable(key = "user")
//    private User initData() {
//
//        User user = new User();
//        user.userName = "tony";
//        user.password = "123456";
//        return user;
//    }

//    @Trace
//    @Async
//    private void loadUser() {
//        L.e(" thread=" + Thread.currentThread().getId());
//        L.e("ui thread=" + Looper.getMainLooper().getThread().getId());
//        Cache cache = Cache.get(this);
//        User user = (User) cache.getObject("user");
//        Toast.makeText(MainActivity2.this, SAFUtils.printObject(user), Toast.LENGTH_SHORT).show();
//    }

    @Subscribe
    void onTest(TestEvent event) {
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

    @Override
    public PermissionGuard getPermissionGuard() {
        return permissionGuard;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionGuard.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
