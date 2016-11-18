package com.test.saf.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.test.saf.R;
import com.test.saf.app.BaseActivity;

public class MainActivity extends BaseActivity {

	private DrawerLayout drawerLayout;
	private View content;
	private RecyclerView recyclerView;
	private NavigationView navigationView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initFab();
		initToolbar();
		setupDrawerLayout();

		content = findViewById(R.id.content);
	}

	private void initFab() {
		findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				Snackbar.make(content, "FAB Clicked", Snackbar.LENGTH_SHORT).show();
			}
		});
	}

	private void initToolbar() {
		final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		final ActionBar actionBar = getSupportActionBar();

		if (actionBar != null) {
			actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	private void setupDrawerLayout() {
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		navigationView = (NavigationView) findViewById(R.id.navigation_view);
		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override public boolean onNavigationItemSelected(MenuItem menuItem) {
				Snackbar.make(content, menuItem.getTitle() + " pressed", Snackbar.LENGTH_LONG).show();
				menuItem.setChecked(true);
				drawerLayout.closeDrawers();
				return true;
			}
		});
	}

}
