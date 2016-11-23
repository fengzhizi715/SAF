package com.test.saf.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.test.saf.R;
import com.test.saf.app.BaseActivity;
import com.test.saf.fragment.HomeFragment;
import com.test.saf.menu.MenuManager;
import com.test.saf.utils.DoubleClickExitUtils;

import cn.salesuite.saf.inject.annotation.InjectView;

public class MainActivity extends BaseActivity {

	@InjectView(id=R.id.drawer_layout)
	DrawerLayout drawerLayout;

	@InjectView
	View content;

	@InjectView(id = R.id.navigation_view)
	NavigationView navigationView;

	@InjectView
	FloatingActionButton fab;

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
		initData(savedInstanceState);
	}

	private void initViews() {
		initFab();
		initToolbar();
		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(MenuItem menuItem) {

				showMenu(menuItem);
				toolbar.setTitle(menuItem.getTitle());
				Snackbar.make(content, menuItem.getTitle() + " pressed", Snackbar.LENGTH_LONG).show();
				menuItem.setChecked(true);
				drawerLayout.closeDrawers();
				return true;
			}
		});
	}

	private void initData(Bundle savedInstanceState) {

		doubleClickExitHelper = new DoubleClickExitUtils(this);

		if (savedInstanceState != null) {
			mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
		}

		if (mContent == null) {
			menuManager = MenuManager.getInstance(getSupportFragmentManager());
			mContent = new HomeFragment();
		}

		getSupportFragmentManager().beginTransaction().add(R.id.content_frame,mContent, MenuManager.MenuType.HOME.getTitle()).commit();
	}

	private void initFab() {
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MenuManager.MenuType curType = menuManager.getCurType();
				Snackbar.make(content, curType.getTitle()+" Clicked", Snackbar.LENGTH_SHORT).show();
			}
		});
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
				drawerLayout.openDrawer(Gravity.LEFT,true);
			}
		});
	}

	public void showMenu(MenuItem menuItem) {

		if (menuItem.getTitle().equals(MenuManager.MenuType.HOME.getTitle())) {
			menuManager.show(MenuManager.MenuType.HOME);
		} else if (menuItem.getTitle().equals(MenuManager.MenuType.ANNOTATION.getTitle())) {
			menuManager.show(MenuManager.MenuType.ANNOTATION);
		} else if (menuItem.getTitle().equals(MenuManager.MenuType.EVENTBUS.getTitle())) {
			menuManager.show(MenuManager.MenuType.EVENTBUS);
		} else if (menuItem.getTitle().equals(MenuManager.MenuType.IMAGELOADER.getTitle())) {
			menuManager.show(MenuManager.MenuType.IMAGELOADER);
		} else if (menuItem.getTitle().equals(MenuManager.MenuType.SQLITE.getTitle())) {
			menuManager.show(MenuManager.MenuType.SQLITE);
		} else if (menuItem.getTitle().equals(MenuManager.MenuType.ROUTER.getTitle())) {
			menuManager.show(MenuManager.MenuType.ROUTER);
		} else if (menuItem.getTitle().equals(MenuManager.MenuType.CACHE.getTitle())) {
			menuManager.show(MenuManager.MenuType.CACHE);
		} else if (menuItem.getTitle().equals(MenuManager.MenuType.LOG.getTitle())) {
			menuManager.show(MenuManager.MenuType.LOG);
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
