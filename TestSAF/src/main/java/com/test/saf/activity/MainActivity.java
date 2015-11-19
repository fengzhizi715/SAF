package com.test.saf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.test.saf.R;
import com.test.saf.Test2Event;
import com.test.saf.TestEvent;
import com.test.saf.app.BaseActivity;

import cn.salesuite.saf.rxjava.eventbus.RxEventBus;
import cn.salesuite.saf.rxjava.eventbus.RxEventBusAnnotationManager;
import cn.salesuite.saf.rxjava.eventbus.Subscribe;
import cn.salesuite.saf.rxjava.eventbus.ThreadMode;

public class MainActivity extends BaseActivity {

	private TextView text;
    private RxEventBusAnnotationManager manager;
    private static final String TAG = MainActivity.class.getName();
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		text = (TextView)findViewById(R.id.text);
		manager = new RxEventBusAnnotationManager(this);
		RxEventBus.get().post(new TestEvent());

		text.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
//				RxEventBus.get().post(new Test2Event());
				Intent i = new Intent(MainActivity.this,SecondActivity.class);
				startActivity(i);
			}
		});
	}
	
	@Subscribe
	void onTest(TestEvent event) {
		Log.i(TAG, "onTestEvent");
		Toast.makeText(MainActivity.this, "onTestEvent", Toast.LENGTH_SHORT).show();
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
}
