package com.test.saf.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.test.saf.R;
import com.test.saf.app.BaseActivity;
import com.test.saf.fragment.HomeFragment;
import com.test.saf.menu.MenuManager;
import com.test.saf.utils.DoubleClickExitUtils;

import cn.salesuite.saf.inject.annotation.InjectView;
import cn.salesuite.saf.utils.Preconditions;

public class MainActivity extends BaseActivity {

	@InjectView(id=R.id.drawer_layout)
	DrawerLayout drawerLayout;

	@InjectView(id = R.id.navigation_view)
	NavigationView navigationView;

	@InjectView
	Toolbar toolbar;

	private MenuManager menuManager;
	private Fragment mContent;
	private DoubleClickExitUtils doubleClickExitHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initViews();
		initData();
	}

	private void initViews() {
		initToolbar();
		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(MenuItem menuItem) {

				if (Preconditions.isNotBlank(menuItem.getTitle())) {
					toolbar.setTitle(menuItem.getTitle());
				}
				showMenu(menuItem);
				menuItem.setChecked(true);

				if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
					drawerLayout.closeDrawer(GravityCompat.START);
				}
				return true;
			}
		});
	}

	private void initData() {

		doubleClickExitHelper = new DoubleClickExitUtils(this);

		if (mContent == null) {
			menuManager = MenuManager.getInstance(getSupportFragmentManager());
			mContent = new HomeFragment();
		}

		getSupportFragmentManager().beginTransaction().add(R.id.content_frame,mContent, MenuManager.MenuType.HOME.getTitle()).commit();
	}

	private void initToolbar() {
		setSupportActionBar(toolbar);
		final ActionBar actionBar = getSupportActionBar();

		if (actionBar != null) {
			actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		toolbar.setNavigationOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				drawerLayout.openDrawer(GravityCompat.START);
			}
		});
	}

	private void showMenu(MenuItem menuItem) {

		switch (menuItem.getItemId()) {
			case R.id.drawer_saf:
				menuManager.show(MenuManager.MenuType.HOME);
				break;
			case R.id.drawer_anno:
				menuManager.show(MenuManager.MenuType.ANNOTATION);
				break;
			case R.id.drawer_eventbus:
				menuManager.show(MenuManager.MenuType.EVENTBUS);
				break;
			case R.id.drawer_imageloader:
				menuManager.show(MenuManager.MenuType.IMAGELOADER);
				break;
			case R.id.drawer_sqlite:
				menuManager.show(MenuManager.MenuType.SQLITE);
				break;
			case R.id.drawer_router:
				menuManager.show(MenuManager.MenuType.ROUTER);
				break;
			case R.id.drawer_cache:
				menuManager.show(MenuManager.MenuType.CACHE);
				break;
			case R.id.drawer_log:
				menuManager.show(MenuManager.MenuType.LOG);
				break;
			default:
				break;
		}
	}

	//重写物理按键的返回逻辑(实现返回键跳转到上一页)
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//用户触摸返回键
		if(keyCode == KeyEvent.KEYCODE_BACK){
			doubleClickExitHelper.onKeyDown(keyCode, event);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
