package com.test.saf.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.test.saf.R;
import com.test.saf.app.BaseActivity;
import com.test.saf.fragment.HomeFragment;
import com.test.saf.menu.MenuManager;

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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initViews();
		initData();
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

	private void initData() {

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
				Snackbar.make(content, "FAB Clicked", Snackbar.LENGTH_SHORT).show();
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
	}

	public void showMenu(MenuItem menuItem) {

		if (menuItem.getTitle().equals(MenuManager.MenuType.HOME.getTitle())) {
			menuManager.show(MenuManager.MenuType.HOME);
		} else if (menuItem.getTitle().equals(MenuManager.MenuType.ANNOTATION.getTitle())) {
			menuManager.show(MenuManager.MenuType.ANNOTATION);
		}
	}

}
